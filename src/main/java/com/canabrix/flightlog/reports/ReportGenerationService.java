package com.canabrix.flightlog.reports;

import com.canabrix.flightlog.Flightlog;
import com.canabrix.flightlog.FlightlogEntry;
import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Service to run a flight log across all the report generators.
 *
 */
@Service
public class ReportGenerationService {

    private static final Logger logger = LoggerFactory.getLogger(ReportGenerationService.class);

    /**
     * @param flightlog Flight Log. Assumed to be in entry date desc order.
     * @return The generated reports
     */
    public List<Report> generateReports(Flightlog flightlog) {

        // Add new reports to this list here
        List<Report> reports = new ArrayList<>();
        reports.add(new LandingReport());
        reports.add(new ApproachReport());
        reports.add(new FlightTimeReport());

        // Get the latest day's flights
        List<FlightlogEntry> latestDayEntries = new ArrayList<>();
        int i = 0;
        FlightlogEntry entry = flightlog.getEntries().get(i);
        Date latestDate = entry.getEntryDate();
        while (DateUtils.isSameDay(entry.getEntryDate(), latestDate)) {
            if (logger.isDebugEnabled()) {
                logger.debug("Putting into latest day entries entry#: " + i);
            }
            latestDayEntries.add(entry);
            entry = flightlog.getEntries().get(++i);
        }

        // Send the latest day flightlog entries to each of the reports
        for (Report report : reports) {
            report.consumeLatestDayEntries(latestDayEntries);
        }

        // Get the remaining entries and send to each of the reports
        List<FlightlogEntry> pastEntries = new ArrayList<>();
        while (i < flightlog.getNumEntries()) {
            if (logger.isDebugEnabled()) {
                logger.debug("Sending to reports past entry#: " + i);
            }
            for (Report report : reports) {
                report.consumePastEntry(flightlog.getEntries().get(i));
            }
            i++;
        }

        return reports;
    }

}
