package com.canabrix.flightlog;

public class Approach {

    private String airport;
    private String title;
    private ApproachType approachType;
    private String runway;
    private boolean isCircleToLand = false;
    private String comments;

    Approach() {
    }

    public String getAirport() {
        return airport;
    }

    public void setAirport(String airport) {
        this.airport = airport;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public ApproachType getApproachType() {
        return approachType;
    }

    public void setApproachType(ApproachType approachType) {
        this.approachType = approachType;
    }

    public String getRunway() {
        return runway;
    }

    public void setRunway(String runway) {
        this.runway = runway;
    }

    public boolean isCircleToLand() {
        return isCircleToLand;
    }

    public void setCircleToLand(boolean circleToLand) {
        isCircleToLand = circleToLand;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }
}
