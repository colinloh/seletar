package com.canabrix.airports;

public class AirportParseException extends Exception {

    private static String expressionParsed(String expressionParsed, String parsingObjective) {
        return "Parsing failure: " + parsingObjective + ": " + expressionParsed;
    }

    private AirportParseException() {
        super();
    }

    public AirportParseException(
            String expressionParsed,
            String parsingObjective,
            Throwable cause
    ) {
        super(expressionParsed(parsingObjective, expressionParsed), cause);
    }

    public AirportParseException(
            String expressionParsed,
            String parsingObjective
    ) {
        this(parsingObjective, expressionParsed, null);
    }
}
