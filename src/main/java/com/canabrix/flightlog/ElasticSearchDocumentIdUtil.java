package com.canabrix.flightlog;

import java.text.SimpleDateFormat;

/**
 * Utility class to derive unique Document IDs used for Elastic Search, for flightlog domain objects
 */
public abstract class ElasticSearchDocumentIdUtil {

    private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

    public static String deriveDocId(FlightlogEntry entry) {
        StringBuilder sb = new StringBuilder();

        // For flight log entries, we will assume that it's sufficient to identify a flight entry by means of:
        // the date, time/gate out, takeoff time, origin airport.
        sb.append(sdf.format(entry.getEntryDate())).append(":");
        sb.append(entry.getTimeOut() != null ? entry.getTimeOut() : "");
        sb.append(":");
        sb.append(entry.getTimeOff() != null ? entry.getTimeOff() : "");
        sb.append(":");
        sb.append(entry.getOrigin() != null ? entry.getOrigin() : "");

        return sb.toString();
    }


}
