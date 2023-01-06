package com.canabrix.flightlog;

public abstract class ApproachFactory {
    protected static Approach instantiateApproach() {
        return new Approach();
    }
}
