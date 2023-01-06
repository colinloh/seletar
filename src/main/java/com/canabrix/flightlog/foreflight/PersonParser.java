package com.canabrix.flightlog.foreflight;

import com.canabrix.flightlog.FlightlogParseException;
import com.canabrix.flightlog.Person;
import com.canabrix.flightlog.PersonName;
import com.canabrix.flightlog.PersonRole;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Parse foreflight-style name/person expressions into our name/person structure.
 */
public abstract class PersonParser {

    private static final Logger logger = LoggerFactory.getLogger(PersonParser.class);

    public static PersonName parseAsPersonName(String expression) {

        String[] parts = StringUtils.split(expression, " ");
        if (ArrayUtils.isEmpty(parts)) {
            return null;
        }
        PersonName pn;
        if (parts.length >= 2) {
            pn = new PersonName(
                    parts[1],
                    parts[0],
                    null
            );
        } else {
            // Just set the single word as first name
            pn = new PersonName(null, parts[0], null);
        }
        return pn;
    }

    public static Person parseAsPerson(String expression) throws FlightlogParseException {
        // e.g. Foreflight expressions:
        // "Frances Langdon;Flight Attendant;test@yahoo.com"
        // "Firstname Lastname;Passenger;"
        String[] parts = StringUtils.split(expression, ";");
        if (ArrayUtils.isEmpty(parts)) {
            return null;
        }
        Person person = new Person();
        if (parts.length >= 3) {
            // We have an email address
            person.setEmail(parts[2]);
        }
        if (parts.length >= 2) {
            // We have a role
            person.setRole(parseAsPersonRole(parts[1]));
        }
        if (parts.length >= 1) {
            // Name comes first
            person.setName(parseAsPersonName(parts[0]));
        }
        return person;
    }

    public static PersonRole parseAsPersonRole(String expression) throws FlightlogParseException {
        if (StringUtils.isEmpty(expression)) {
            return null;
        }
        switch (StringUtils.toRootLowerCase(expression)) {
            case "instructor":
                return PersonRole.INSTRUCTOR;
            case "student":
                return PersonRole.STUDENT;
            case "passenger":
                return PersonRole.PASSENGER;
            case "pic":
                return PersonRole.PIC;
            case "sic":
                return PersonRole.SIC;
            case "safety pilot":
                return PersonRole.SAFETY_PILOT;
            case "flight attendant":
                return PersonRole.FLIGHT_ATTENDANT;
            case "flight engineer":
                return PersonRole.FLIGHT_ENGINEER;
            case "first officer":
                return PersonRole.FIRST_OFFICER;
            case "second officer":
                return PersonRole.SECOND_OFFICER;
            case "examiner":
                return PersonRole.EXAMINER;
            default:
                throw new FlightlogParseException(
                        expression,
                        "Person Role"
                );
        }
    }
}
