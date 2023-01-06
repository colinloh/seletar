package com.canabrix.flightlog;

import com.canabrix.flightlog.reports.ReportGenerationService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

/**
 * Test FlightlogService's integration with S3 and Elastic Search etc.
 */
@SpringBootTest
public class FlightlogIntegrationTest {

    private static final Logger logger = LoggerFactory.getLogger(FlightlogIntegrationTest.class);

    @Autowired
    private FlightlogElasticSearchService flightlogElasticSearchService;

    @Autowired
    private FlightlogS3ReaderService flightlogS3ReaderService;

    @Autowired
    private ReportGenerationService reportGenerationService;

    @Test
    public void testReportsFromElasticSearchFlightlog() throws IOException {

        logger.info("Testing fetch of data from elastic search");
        Flightlog flightlog = flightlogElasticSearchService.queryFlightlog();
        logger.info("Number of entries fetched from elastic search: " + flightlog.getNumEntries());
        assertThat(flightlog.getEntries())
                .withFailMessage("Couldn't get flightlog from elastic search")
                .isNotNull();
        // 10 results is the default. We want to ensure we're getting more than default.
        assertThat(flightlog.getNumEntries())
                .withFailMessage("Flight log has 10 or fewer entries. Check elastic search.")
                .isGreaterThan(10);

        logger.info("Testing the generation of reports from the flight log");
        assertThat(reportGenerationService.generateReports(flightlog))
                .withFailMessage("Failed to generate reports")
                .isNotNull()
        ;

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        for (FlightlogEntry entry : flightlog.getEntries()) {
            // Ensure that it's properly hydrated POJOs.
            String entryDate = sdf.format(entry.getEntryDate());
            assertThat(entry.getEntryDate())
                    .withFailMessage("Entry date is incorrect for : " + entryDate)
                    .isAfterOrEqualTo(FlightlogElasticSearchService.DEFAULT_START_DATE)
                    .isBeforeOrEqualTo(new Date())
                    ;
            assertThat(entry.getAircraftID())
                    .withFailMessage("Aircraft ID is missing for : " + entryDate)
                    .isNotBlank()
                    ;
            double totalAllHours =
                    entry.getTotalTimeHours() + entry.getSimulatedFlightHours() + entry.getSimulatedInstrumentHours();
            assertThat(totalAllHours)
                    .withFailMessage("No hours at all for : " + entryDate)
                    .isGreaterThan(0.0)
                    ;
        }
    }

    @Test
    public void testLatestS3Logbook() throws FlightlogParseException {

        logger.info("Looking for latest logbook file in S3");
        String latestLogbookFileName = flightlogS3ReaderService.findLatestFlightlogKey();
        logger.info("Found latest logbook file: " + latestLogbookFileName);

        Flightlog flightlog = flightlogS3ReaderService.createFlightlogFromS3File(latestLogbookFileName);

        Assertions.assertThat(flightlog)
                .withFailMessage("Failed to get flightlog from S3: " + latestLogbookFileName)
                .isNotNull();

        int numEntries = flightlog.getNumEntries();
        Assertions.assertThat(numEntries)
                .withFailMessage("Flight log object doesn't have entries.")
                .isGreaterThan(0)
        ;

        logger.info("Found " + numEntries + " entries in flightlog: " + latestLogbookFileName);

        logger.info("Testing the insertion into elastic search");
        assertThat(flightlogElasticSearchService.index(flightlog))
                .withFailMessage("Failed to insert all rows into elastic search")
                .isEqualTo(numEntries)
                ;
    }

}
