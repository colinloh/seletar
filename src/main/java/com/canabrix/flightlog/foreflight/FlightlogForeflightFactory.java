package com.canabrix.flightlog.foreflight;

import com.canabrix.flightlog.*;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.List;

public abstract class FlightlogForeflightFactory extends FlightlogFactory {

    private static final Logger logger = LoggerFactory.getLogger(FlightlogForeflightFactory.class);

    // If the following header changes, also make sure to update parseLine() to match.
    private static final String FLIGHT_TABLE_HEADER_LINE =
            "Date,AircraftID,From,To,Route,TimeOut,TimeOff,TimeOn,TimeIn," +
            "OnDuty,OffDuty,TotalTime,PIC,SIC,Night,Solo,CrossCountry,NVG,NVG Ops," +
            "Distance,DayTakeoffs,DayLandingsFullStop,NightTakeoffs,NightLandingsFullStop,AllLandings," +
            "ActualInstrument,SimulatedInstrument,HobbsStart,HobbsEnd,TachStart,TachEnd,Holds," +
            "Approach1,Approach2,Approach3,Approach4,Approach5,Approach6," +
            "DualGiven,DualReceived,SimulatedFlight,GroundTraining,InstructorName,InstructorComments," +
            "Person1,Person2,Person3,Person4,Person5,Person6," +
            "FlightReview,Checkride,IPC,NVG Proficiency,FAA6158,PilotComments";

    public static Flightlog create(
        List<String> lines
    ) throws FlightlogParseException {

        logger.info("Parsing foreflight logbook from lines");

        Flightlog flightlog = instantiateFlightlog();

        int i = 0;
        try {
            // Find the line for "Flights Table"
            while (!StringUtils.startsWithIgnoreCase(
                    lines.get(i),
                    "Flights Table,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,"
            )) {
                i++;
            }

            i++;
            // Now we're at the top of the CSV block for flights.
            // Check to make sure the header is exactly what we want
            if (!StringUtils.equalsIgnoreCase(
                    lines.get(i),
                    FLIGHT_TABLE_HEADER_LINE
            )) {
                throw new FlightlogParseException(
                        lines.get(i),
                        "Flight Table CSV Header Row Check",
                        null
                );
            }
        } catch (IndexOutOfBoundsException e) {
            throw new FlightlogParseException(
                    "log file lines",
                    "Finding the Flights Table",
                    e
            );
        }
        if (logger.isDebugEnabled()) {
            logger.debug("Found flight log header at line " + i);
        }

        // Advance to the first actual entry line
        i++;

        // Now we're at the first flight log entry. Parse each of these lines until the EOF.
        while (i < lines.size()) {
            String line = lines.get(i);
            if (logger.isDebugEnabled()) {
                logger.debug("Parsing:" + line);
            }
            try {
                flightlog.addEntry(parseLine(line));
            } catch (FlightlogParseException e) {
                logger.info("Error parsing line #" + i + ":" + line + " : " + e.getMessage());
            }
            i++;
        }

        return flightlog;
    }

    private static String nullIfEmpty(String s) {
        return StringUtils.isEmpty(s) ? null : s;
    }

    private static String parseAsTime(String s) {
        try {
            // We return a string because JSON doesn't yet have an easy way to deal with LocalTime
            return LocalTime.parse(s).toString();
        } catch (DateTimeParseException e) {
            return null;
        }
    }

    private static Double zeroDoubleIfNull(String s) {
        try {
            return Double.parseDouble(s);
        } catch (NumberFormatException e) {
            return 0.0;
        }
    }

    private static Integer zeroIntegerIfNull(String s) {
        try {
            return Integer.parseInt(s);
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    private static Approach parseApproachIfNotEmpty(String s) throws FlightlogParseException {
        if (!StringUtils.isEmpty(s)) {
            return ApproachForeflightFactory.create(s);
        } else {
            return null;
        }
    }

    public static FlightlogEntry parseLine(String line) throws FlightlogParseException {

        if (logger.isDebugEnabled()) {
            logger.debug("Parsing: " + line);
        }

        CSVRecord csvRecord;
        try (CSVParser parser = CSVParser.parse(line, CSVFormat.DEFAULT)) {

            List<CSVRecord> records = parser.getRecords();

            if (records.size() != 1) {
                logger.error("Should only have one CSV record for a single line, but found: " + records.size());
                throw new FlightlogParseException(
                        line,
                        "Parsing line into CSV"
                );
            }

            csvRecord = records.get(0);
        } catch (IOException e) {
            throw new FlightlogParseException(
                    line,
                    "CSV Parsing",
                    e
            );
        }

        if (logger.isDebugEnabled()) {
            logger.debug("number of fields found in line: " + csvRecord.size());
        }
        FlightlogEntry entry = new FlightlogEntry();

        int i = 0;
        // Date
        entry.setEntryDate(DateParser.parse(csvRecord.get(i++)));
        // AircraftID
        entry.setAircraftID(nullIfEmpty(csvRecord.get(i++)));
        // From
        entry.setOrigin(nullIfEmpty(csvRecord.get(i++)));
        // To
        entry.setDestination(nullIfEmpty(csvRecord.get(i++)));
        // Route
        entry.setRoute(nullIfEmpty(csvRecord.get(i++)));
        // TimeOut
        entry.setTimeOut(parseAsTime(csvRecord.get(i++)));
        // TimeOff
        entry.setTimeOff(parseAsTime(csvRecord.get(i++)));
        // TimeOn
        entry.setTimeOn(parseAsTime(csvRecord.get(i++)));
        // TimeIn
        entry.setTimeIn(parseAsTime(csvRecord.get(i++)));
        // OnDuty
        entry.setOnDuty(parseAsTime(csvRecord.get(i++)));

        // Under flightlog context, null values would be treated as zero.

        // OffDuty
        entry.setOffDuty(parseAsTime(csvRecord.get(i++)));
        // TotalTime
        entry.setTotalTimeHours(zeroDoubleIfNull(csvRecord.get(i++)));
        // PIC
        entry.setPicHours(zeroDoubleIfNull(csvRecord.get(i++)));
        // SIC
        entry.setSicHours(zeroDoubleIfNull(csvRecord.get(i++)));
        // Night
        entry.setNightHours(zeroDoubleIfNull(csvRecord.get(i++)));
        // Solo
        entry.setSoloHours(zeroDoubleIfNull(csvRecord.get(i++)));
        // CrossCountry
        entry.setCrossCountryHours(zeroDoubleIfNull(csvRecord.get(i++)));
        // NVG
        entry.setNightVisionGoggleHours(zeroDoubleIfNull(csvRecord.get(i++)));
        // NVG Ops
        entry.setNumNightVisionGoggleOps(zeroIntegerIfNull(csvRecord.get(i++)));
        // Distance
        entry.setDistanceNm(zeroDoubleIfNull(csvRecord.get(i++)));


        // DayTakeoffs
        entry.setNumDayTakeoffs(zeroIntegerIfNull(csvRecord.get(i++)));
        // DayLandingsFullStop
        entry.setNumDayLandingsFullStop(zeroIntegerIfNull(csvRecord.get(i++)));
        // NightTakeoffs
        entry.setNumNightTakeoffs(zeroIntegerIfNull(csvRecord.get(i++)));
        // NightLandingsFullStop
        entry.setNumNightLandingsFullStop(zeroIntegerIfNull(csvRecord.get(i++)));
        // AllLandings
        entry.setTotalAllLandings(zeroIntegerIfNull(csvRecord.get(i++)));
        // ActualInstrument
        entry.setActualInstrumentHours(zeroDoubleIfNull(csvRecord.get(i++)));
        // SimulatedInstrument
        entry.setSimulatedInstrumentHours(zeroDoubleIfNull(csvRecord.get(i++)));
        // HobbsStart
        entry.setHobbsStart(zeroDoubleIfNull(csvRecord.get(i++)));
        // HobbsEnd
        entry.setHobbsEnd(zeroDoubleIfNull(csvRecord.get(i++)));
        // TachStart
        entry.setTachStart(zeroDoubleIfNull(csvRecord.get(i++)));


        // TachEnd
        entry.setTachEnd(zeroDoubleIfNull(csvRecord.get(i++)));
        // Holds
        entry.setNumHolds(zeroIntegerIfNull(csvRecord.get(i++)));
        // Approach1	Approach2	Approach3	Approach4	Approach5	Approach6
        entry.addApproach(parseApproachIfNotEmpty(csvRecord.get(i++)));
        entry.addApproach(parseApproachIfNotEmpty(csvRecord.get(i++)));
        entry.addApproach(parseApproachIfNotEmpty(csvRecord.get(i++)));
        entry.addApproach(parseApproachIfNotEmpty(csvRecord.get(i++)));
        entry.addApproach(parseApproachIfNotEmpty(csvRecord.get(i++)));
        entry.addApproach(parseApproachIfNotEmpty(csvRecord.get(i++)));
        // DualGiven
        entry.setDualGivenHours(zeroDoubleIfNull(csvRecord.get(i++)));
        // DualReceived
        entry.setDualReceivedHours(zeroDoubleIfNull(csvRecord.get(i++)));


        // SimulatedFlight
        entry.setSimulatedFlightHours(zeroDoubleIfNull(csvRecord.get(i++)));
        // GroundTraining
        entry.setGroundTrainingHours(zeroDoubleIfNull(csvRecord.get(i++)));
        // InstructorName
        entry.setInstructorName(PersonParser.parseAsPersonName(csvRecord.get(i++)));
        // InstructorComments
        entry.setInstructorComments(nullIfEmpty(csvRecord.get(i++)));
        // Person1	Person2	Person3	Person4	Person5	Person6
        entry.addPerson(PersonParser.parseAsPerson(csvRecord.get(i++)));
        entry.addPerson(PersonParser.parseAsPerson(csvRecord.get(i++)));
        entry.addPerson(PersonParser.parseAsPerson(csvRecord.get(i++)));
        entry.addPerson(PersonParser.parseAsPerson(csvRecord.get(i++)));
        entry.addPerson(PersonParser.parseAsPerson(csvRecord.get(i++)));
        entry.addPerson(PersonParser.parseAsPerson(csvRecord.get(i++)));


        // FlightReview
        entry.setFlightReview(Boolean.parseBoolean(csvRecord.get(i++)));
        // Checkride
        entry.setCheckride(Boolean.parseBoolean(csvRecord.get(i++)));
        // IPC
        entry.setInstrumentProficiencyCheck(Boolean.parseBoolean(csvRecord.get(i++)));
        // NVG Proficiency
        entry.setNvgProficiency(Boolean.parseBoolean(csvRecord.get(i++)));
        // FAA6158
        entry.setFaa6158ProficiencyCheck(Boolean.parseBoolean(csvRecord.get(i++)));
        // PilotComments
        entry.setPilotComments(nullIfEmpty(csvRecord.get(i)));

        return entry;
    }
}
