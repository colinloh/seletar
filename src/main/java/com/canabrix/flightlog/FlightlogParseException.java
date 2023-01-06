package com.canabrix.flightlog;

public class FlightlogParseException extends Exception {

    private static String expressionParsed(String expressionParsed, String parsingObjective) {
        return "Parsing failure: " + parsingObjective + ": " + expressionParsed;
    }

    private FlightlogParseException() {
        super();
    }

    public FlightlogParseException(
            String expressionParsed,
            String parsingObjective,
            Throwable cause
    ) {
        super(expressionParsed(parsingObjective, expressionParsed), cause);
    }

    public FlightlogParseException(
            String expressionParsed,
            String parsingObjective
    ) {
        this(parsingObjective, expressionParsed, null);
    }
}