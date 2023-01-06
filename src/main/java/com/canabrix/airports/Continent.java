package com.canabrix.airports;

import org.apache.commons.lang3.StringUtils;

import javax.xml.parsers.ParserConfigurationException;

public enum Continent {
    NORTH_AMERICA,
    SOUTH_AMERICA,
    ASIA,
    AFRICA,
    ANTARTICA,
    OCEANIA,
    EUROPE
    ;

    public static Continent from(String label) throws AirportParseException {
        switch (StringUtils.toRootLowerCase(label)) {
            case "na":
                return NORTH_AMERICA;
            case "sa":
                return SOUTH_AMERICA;
            case "as":
                return ASIA;
            case "af":
                return AFRICA;
            case "an":
                return ANTARTICA;
            case "oc":
                return OCEANIA;
            case "eu":
                return EUROPE;
            default:
                throw new AirportParseException(label, "parsing into continent enum");
        }
    }

}
