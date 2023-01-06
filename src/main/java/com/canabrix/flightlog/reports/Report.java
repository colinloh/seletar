package com.canabrix.flightlog.reports;

import com.canabrix.flightlog.FlightlogEntry;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A Flightlog report is a set of facts/information that is based on an analysis of a flight log,
 * and can be given to a frontend to compile an interesting view of the flight log data.
 * These objects should be maintained as a JSON-friendly POJO.
 */
public abstract class Report {

    protected Report() {
        super();
        this.details = new HashMap<>();
    }

    protected Map<String, Object> details;

    public abstract String getTitle();

    public Map<String, Object> getDetails() {
        return details;
    }

    /**
     * Signal to expect to receive flight entries in descending order of entry date.
     * @param latestEntries The entries of the most recent day of flying.
     * @return True if further entries are desired. False would mean that analyze() would not be called for this iteration.
     */
    protected abstract boolean consumeLatestDayEntries(
            List<FlightlogEntry> latestEntries
    );

    /**
     * Called for each Flightlog Entry in descending order of entry date.
     * @param entry The entry to analyze.
     * @return true if further entries are desired. False would make the caller stop calling this for this iteration.
     */
    protected abstract boolean consumePastEntry(FlightlogEntry entry);
}
