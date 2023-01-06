package com.canabrix.airports;

import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
public class AirportsS3IntegrationTest {

    private static final Logger logger = LoggerFactory.getLogger(AirportsS3IntegrationTest.class);

    @Autowired
    private AirportS3ReaderService airportS3ReaderService;

    @Test
    public void testReadAirportsFromS3() throws IOException {
        int numLinesRead = airportS3ReaderService.loadAirports(
                l -> {
                    assertThat(StringUtils.length(l) > 0)
                            .withFailMessage("Line has 0 length")
                            .isTrue();
                }
        );
        logger.info("Num lines read from S3 airport file: " + numLinesRead);
        assertThat(numLinesRead)
                .withFailMessage("Failed to read lines from the S3 Airport file")
                .isGreaterThan(70000) // There should be about 74k entries in the file
                ;
    }

    @Test
    public void testReadRunwaysFromS3() throws IOException {
        int numLinesRead = airportS3ReaderService.loadRunways(
                l -> {
                    assertThat(StringUtils.length(l) > 0)
                            .withFailMessage("Line has 0 length")
                            .isTrue();
                }
        );
        logger.info("Num lines read from S3 runways file: " + numLinesRead);
        assertThat(numLinesRead)
                .withFailMessage("Failed to read lines from the S3 runways file")
                .isGreaterThan(40000) // There should be about 44k entries in the file
        ;
    }
}
