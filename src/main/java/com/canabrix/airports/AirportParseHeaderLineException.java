package com.canabrix.airports;

public class AirportParseHeaderLineException extends Exception {

    private String headerLineEncountered;
    public AirportParseHeaderLineException(String headerLineEncountered) {
        super();
        this.headerLineEncountered = headerLineEncountered;
    }

    public String getHeaderLineEncountered() {
        return headerLineEncountered;
    }
}
