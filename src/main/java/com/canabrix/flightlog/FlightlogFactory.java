package com.canabrix.flightlog;

public abstract class FlightlogFactory {
    protected static Flightlog instantiateFlightlog() {
        return new Flightlog();
    }
}
