package com.canabrix.flightlog.reports;

import com.canabrix.flightlog.FlightlogEntry;

import java.util.List;

public class FlightTimeReport extends Report {

    @Override
    public String getTitle() {
        return "Flight Time";
    }

    @Override
    protected boolean consumeLatestDayEntries(List<FlightlogEntry> latestEntries) {
        return false;
    }

    @Override
    protected boolean consumePastEntry(FlightlogEntry entry) {
        return false;
    }
}
