package com.canabrix.flightlog;

import com.google.common.collect.ImmutableList;

import java.util.ArrayList;
import java.util.List;

public class Flightlog {

    private List<FlightlogEntry> entries = new ArrayList<>();

    /** Create using a Flightlog Factory **/
    Flightlog() {
        super();
    }

    public List<FlightlogEntry> getEntries() {
        return ImmutableList.copyOf(entries);
    }

    public void addEntry(FlightlogEntry entry) {
        this.entries.add(entry);
    }

    public int getNumEntries() {
        return entries.size();
    }

}
