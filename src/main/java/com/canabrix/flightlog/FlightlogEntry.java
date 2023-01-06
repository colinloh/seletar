package com.canabrix.flightlog;

import java.util.*;

/**
 * POJO for a single entry in a flight logbook.
 * Used for JSON (de)serialization to/from Elastic Search etc.
 */
public class FlightlogEntry {

    public FlightlogEntry() {
    }

    private Date entryDate;
    private String aircraftID;
    private String origin;
    private String destination;
    private String route;

    // These are known as "OOOI" time.
    // See https://aspm.faa.gov/aspmhelp/index/OOOI_Data.html#:~:text=OOOI%20Data%20refers%20to%20times,carriers%20on%20a%20monthly%20basis.
    private String timeOut; // Gates closed. Plane leaves gates.
    private String timeOff; // Take off time.
    private String timeOn; // Landing time.
    private String timeIn; // Plane shutdown at gates.

    private String OnDuty;
    private String OffDuty;
    private Double totalTimeHours;
    private Double picHours;
    private Double sicHours;
    private Double nightHours;
    private Double soloHours;
    private Double crossCountryHours;
    private Double nightVisionGoggleHours;
    private Integer numNightVisionGoggleOps;
    private Double distanceNm;
    private Integer numDayTakeoffs;
    private Integer numDayLandingsFullStop;
    private Integer numNightTakeoffs;
    private Integer numNightLandingsFullStop;
    private Integer totalAllLandings;
    private Double actualInstrumentHours;
    private Double simulatedInstrumentHours;
    private Double hobbsStart;
    private Double hobbsEnd;
    private Double tachStart;
    private Double tachEnd;
    private Integer numHolds;
    private List<Approach> approaches;
    private Double dualGivenHours;
    private Double dualReceivedHours;
    private Double simulatedFlightHours;
    private Double groundTrainingHours;
    private PersonName instructorName;
    private String instructorComments;
    private Set<Person> persons;
    private Boolean isFlightReview;
    private Boolean isInstrumentProficiencyCheck;
    private Boolean isCheckride;
    private Boolean isFaa6158ProficiencyCheck;
    private Boolean isNvgProficiency;
    private String pilotComments;

    public Date getEntryDate() {
        return entryDate;
    }

    public void setEntryDate(Date entryDate) {
        this.entryDate = entryDate;
    }

    public String getAircraftID() {
        return aircraftID;
    }

    public void setAircraftID(String aircraftID) {
        this.aircraftID = aircraftID;
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getRoute() {
        return route;
    }

    public void setRoute(String route) {
        this.route = route;
    }

    public String getTimeOut() {
        return timeOut;
    }

    public void setTimeOut(String timeOut) {
        this.timeOut = timeOut;
    }

    public String getTimeOff() {
        return timeOff;
    }

    public void setTimeOff(String timeOff) {
        this.timeOff = timeOff;
    }

    public String getTimeOn() {
        return timeOn;
    }

    public void setTimeOn(String timeOn) {
        this.timeOn = timeOn;
    }

    public String getTimeIn() {
        return timeIn;
    }

    public void setTimeIn(String timeIn) {
        this.timeIn = timeIn;
    }

    public String getOnDuty() {
        return OnDuty;
    }

    public void setOnDuty(String onDuty) {
        OnDuty = onDuty;
    }

    public String getOffDuty() {
        return OffDuty;
    }

    public void setOffDuty(String offDuty) {
        OffDuty = offDuty;
    }

    public Double getTotalTimeHours() {
        return totalTimeHours;
    }

    public void setTotalTimeHours(Double totalTimeHours) {
        this.totalTimeHours = totalTimeHours;
    }

    public Double getPicHours() {
        return picHours;
    }

    public void setPicHours(Double picHours) {
        this.picHours = picHours;
    }

    public Double getSicHours() {
        return sicHours;
    }

    public void setSicHours(Double sicHours) {
        this.sicHours = sicHours;
    }

    public Double getNightHours() {
        return nightHours;
    }

    public void setNightHours(Double nightHours) {
        this.nightHours = nightHours;
    }

    public Double getSoloHours() {
        return soloHours;
    }

    public void setSoloHours(Double soloHours) {
        this.soloHours = soloHours;
    }

    public Double getCrossCountryHours() {
        return crossCountryHours;
    }

    public void setCrossCountryHours(Double crossCountryHours) {
        this.crossCountryHours = crossCountryHours;
    }

    public Double getNightVisionGoggleHours() {
        return nightVisionGoggleHours;
    }

    public void setNightVisionGoggleHours(Double nightVisionGoggleHours) {
        this.nightVisionGoggleHours = nightVisionGoggleHours;
    }

    public Integer getNumNightVisionGoggleOps() {
        return numNightVisionGoggleOps;
    }

    public void setNumNightVisionGoggleOps(Integer numNightVisionGoggleOps) {
        this.numNightVisionGoggleOps = numNightVisionGoggleOps;
    }

    public Double getDistanceNm() {
        return distanceNm;
    }

    public void setDistanceNm(Double distanceNm) {
        this.distanceNm = distanceNm;
    }

    public Integer getNumDayTakeoffs() {
        return numDayTakeoffs;
    }

    public void setNumDayTakeoffs(Integer numDayTakeoffs) {
        this.numDayTakeoffs = numDayTakeoffs;
    }

    public Integer getNumDayLandingsFullStop() {
        return numDayLandingsFullStop;
    }

    public void setNumDayLandingsFullStop(Integer numDayLandingsFullStop) {
        this.numDayLandingsFullStop = numDayLandingsFullStop;
    }

    public Integer getNumNightTakeoffs() {
        return numNightTakeoffs;
    }

    public void setNumNightTakeoffs(Integer numNightTakeoffs) {
        this.numNightTakeoffs = numNightTakeoffs;
    }

    public Integer getNumNightLandingsFullStop() {
        return numNightLandingsFullStop;
    }

    public void setNumNightLandingsFullStop(Integer numNightLandingsFullStop) {
        this.numNightLandingsFullStop = numNightLandingsFullStop;
    }

    public Integer getTotalAllLandings() {
        return totalAllLandings;
    }

    public void setTotalAllLandings(Integer totalAllLandings) {
        this.totalAllLandings = totalAllLandings;
    }

    public Double getActualInstrumentHours() {
        return actualInstrumentHours;
    }

    public void setActualInstrumentHours(Double actualInstrumentHours) {
        this.actualInstrumentHours = actualInstrumentHours;
    }

    public Double getSimulatedInstrumentHours() {
        return simulatedInstrumentHours;
    }

    public void setSimulatedInstrumentHours(Double simulatedInstrumentHours) {
        this.simulatedInstrumentHours = simulatedInstrumentHours;
    }

    public Double getHobbsStart() {
        return hobbsStart;
    }

    public void setHobbsStart(Double hobbsStart) {
        this.hobbsStart = hobbsStart;
    }

    public Double getHobbsEnd() {
        return hobbsEnd;
    }

    public void setHobbsEnd(Double hobbsEnd) {
        this.hobbsEnd = hobbsEnd;
    }

    public Double getTachStart() {
        return tachStart;
    }

    public void setTachStart(Double tachStart) {
        this.tachStart = tachStart;
    }

    public Double getTachEnd() {
        return tachEnd;
    }

    public void setTachEnd(Double tachEnd) {
        this.tachEnd = tachEnd;
    }

    public Integer getNumHolds() {
        return numHolds;
    }

    public void setNumHolds(Integer numHolds) {
        this.numHolds = numHolds;
    }

    public List<Approach> getApproaches() {
        return approaches;
    }

    public void addApproach(Approach approach) {
        if (this.approaches == null) {
            this.approaches = new ArrayList<>();
        }
        if (approach != null) {
            this.approaches.add(approach);
        }
    }

    public Double getDualGivenHours() {
        return dualGivenHours;
    }

    public void setDualGivenHours(Double dualGivenHours) {
        this.dualGivenHours = dualGivenHours;
    }

    public Double getDualReceivedHours() {
        return dualReceivedHours;
    }

    public void setDualReceivedHours(Double dualReceivedHours) {
        this.dualReceivedHours = dualReceivedHours;
    }

    public Double getSimulatedFlightHours() {
        return simulatedFlightHours;
    }

    public void setSimulatedFlightHours(Double simulatedFlightHours) {
        this.simulatedFlightHours = simulatedFlightHours;
    }

    public Double getGroundTrainingHours() {
        return groundTrainingHours;
    }

    public void setGroundTrainingHours(Double groundTrainingHours) {
        this.groundTrainingHours = groundTrainingHours;
    }

    public PersonName getInstructorName() {
        return instructorName;
    }

    public void setInstructorName(PersonName instructorName) {
        this.instructorName = instructorName;
    }

    public String getInstructorComments() {
        return instructorComments;
    }

    public void setInstructorComments(String instructorComments) {
        this.instructorComments = instructorComments;
    }

    public Set<Person> getPersons() {
        return persons;
    }

    public void addPerson(Person person) {
        if (this.persons == null) {
            this.persons = new HashSet<>();
        }
        persons.add(person);
    }

    public Boolean isFlightReview() {
        return isFlightReview;
    }

    public void setFlightReview(Boolean flightReview) {
        isFlightReview = flightReview;
    }

    public Boolean isInstrumentProficiencyCheck() {
        return isInstrumentProficiencyCheck;
    }

    public void setInstrumentProficiencyCheck(Boolean instrumentProficiencyCheck) {
        isInstrumentProficiencyCheck = instrumentProficiencyCheck;
    }

    public Boolean isCheckride() {
        return isCheckride;
    }

    public void setCheckride(Boolean checkride) {
        isCheckride = checkride;
    }

    public Boolean isFaa6158ProficiencyCheck() {
        return isFaa6158ProficiencyCheck;
    }

    public void setFaa6158ProficiencyCheck(Boolean faa6158ProficiencyCheck) {
        isFaa6158ProficiencyCheck = faa6158ProficiencyCheck;
    }

    public Boolean isNvgProficiency() {
        return isNvgProficiency;
    }

    public void setNvgProficiency(Boolean nvgProficiency) {
        isNvgProficiency = nvgProficiency;
    }

    public String getPilotComments() {
        return pilotComments;
    }

    public void setPilotComments(String pilotComments) {
        this.pilotComments = pilotComments;
    }
}
