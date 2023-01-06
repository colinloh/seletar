package com.canabrix.flightlog.reports;

import com.canabrix.flightlog.Approach;
import com.canabrix.flightlog.FlightlogEntry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;

public class ApproachReport extends Report {

    private static final Logger logger = LoggerFactory.getLogger(ApproachReport.class);

    protected ApproachReport() {
        super();
    }

    @Override
    public boolean consumeLatestDayEntries(List<FlightlogEntry> entries) {
        int i = 1;
        for (FlightlogEntry entry : entries) {
            List<Approach> approaches = entry.getApproaches();
            if (approaches != null && approaches.size() > 0 && logger.isDebugEnabled()) {
                logger.debug("# approaches found in entry: " + approaches.size());
            }
        }
        return true;
    }

    @Override
    public boolean consumePastEntry(FlightlogEntry entry) {
        return true;
    }

    @Override
    public String getTitle() {
        return "Approaches";
    }

    @Override
    public Map<String, Object> getDetails() {
        return null;
    }
}
