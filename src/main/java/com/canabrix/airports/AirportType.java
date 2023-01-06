package com.canabrix.airports;

/**
 * This corresponds to the airport types on ourairports.com
 *
 */
public enum AirportType {
    SMALL_AIRPORT,
    MEDIUM_AIRPORT,
    LARGE_AIRPORT,
    SEAPLANE_BASE,
    BALLOON_PORT,
    HELIPORT,
    CLOSED
    ;

    public static AirportType from(String label) throws AirportParseException {
        switch (label) {
        case "small_airport":
            return SMALL_AIRPORT;
        case "medium_airport":
            return MEDIUM_AIRPORT;
        case "large_airport":
            return LARGE_AIRPORT;
        case "heliport":
            return HELIPORT;
        case "closed":
            return CLOSED;
        case "seaplane_base":
            return SEAPLANE_BASE;
        case "balloonport":
            return BALLOON_PORT;
        default:
            throw new AirportParseException(label, "Converting to AirportType enum");
        }
    }

}
