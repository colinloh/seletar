package com.canabrix.flightlog.foreflight;

import com.canabrix.flightlog.FlightlogParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public abstract class DateParser {

    private static final Logger logger = LoggerFactory.getLogger(DateParser.class);
    // We might want to add more date formatters in the future.
    public static final SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");

    public static Date parse(String exp) throws FlightlogParseException {
        Date result = null;
        try {
            result = sdf1.parse(exp);
        } catch (ParseException e) {
            logger.info("SDF1 failed to parse as a date: " + exp);
        }
        // We'll iterate through the other date formatters here, if any.
        // We'll only truly fail when they all don't work.

        if (result == null) {
            throw new FlightlogParseException(exp, "date");
        }
        return result;
    }

}
