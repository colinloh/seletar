package com.canabrix.flightlog.reports;

import com.canabrix.flightlog.FlightlogEntry;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class LandingReport extends Report {

    protected LandingReport() {
        super();
    }

    @Override
    public boolean consumeLatestDayEntries(List<FlightlogEntry> entries) {

        Set<String> airports = new HashSet<>();
        int numDayLandings = 0, numNightLandings = 0;

        for (FlightlogEntry e : entries) {
            numDayLandings += e.getNumDayLandingsFullStop();
            numNightLandings += e.getNumNightLandingsFullStop();
            if ((e.getNumDayLandingsFullStop() + e.getNumNightLandingsFullStop()) > 0) {
                airports.add(e.getDestination());
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
        return "Landings";
    }

    @Override
    public Map<String, Object> getDetails() {
        return null;
    }

}
