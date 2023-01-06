package com.canabrix.airports;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;

/**
 * Util class to encapsulate the parsing logic for ourairports.com files
 */
public abstract class OurAirportsParser {

    private static final Logger logger = LoggerFactory.getLogger(OurAirportsParser.class);
    public static Airport from(String csvLine) throws AirportParseException, AirportParseHeaderLineException {

        // We can't parse the header line.
        if (StringUtils.startsWith(csvLine, "\"id\",\"ident\",\"type\"")) {
            throw new AirportParseHeaderLineException(csvLine);
        }

        Airport airport = new Airport();
        try (CSVParser parser = CSVParser.parse(csvLine, CSVFormat.DEFAULT)) {
            List<CSVRecord> records = parser.getRecords();

            if (records.size() != 1) {
                logger.error("Unexpected number of CSV 'records' when parsing a single line: " + records.size());
                throw new AirportParseException(csvLine, "Parsing CSV into airport object");
            }
            CSVRecord csvRecord = records.get(0);

            // ourairports.com airports CSV header:
            // "id","ident","type","name","latitude_deg","longitude_deg","elevation_ft","continent","iso_country",
            // "iso_region","municipality","scheduled_service","gps_code","iata_code","local_code","home_link",
            // "wikipedia_link","keywords"
            int i = 0;
            airport.setOurAirportsId(nullIntegerIfEmpty(csvRecord.get(i++)));
            airport.setAirportCode(csvRecord.get(i++));
            airport.setAirportType(AirportType.from(csvRecord.get(i++)));
            airport.setName(csvRecord.get(i++));
            airport.setLatitudeDeg(nullDoubleIfEmpty(csvRecord.get(i++)));
            airport.setLongitudeDeg(nullDoubleIfEmpty(csvRecord.get(i++)));
            airport.setElevationFt(nullIntegerIfEmpty(csvRecord.get(i++)));
            airport.setContinent(Continent.from(csvRecord.get(i++)));
            airport.setCountryIso(csvRecord.get(i++));
            airport.setRegionIso(csvRecord.get(i++));
            airport.setMunicipality(csvRecord.get(i++));
            airport.setScheduledService(nullBooleanIfEmpty(csvRecord.get(i++)));
            airport.setGpsCode(csvRecord.get(i++));
            airport.setIataCode(csvRecord.get(i++));
            airport.setLocalCode(csvRecord.get(i++));
            airport.setHomeUrl(csvRecord.get(i++));
            airport.setWikipediaUrl(csvRecord.get(i++));
            airport.setKeywords(csvRecord.get(i));
        } catch (IOException | NumberFormatException e) {
            throw new AirportParseException(csvLine, "Parsing CSV into airport object", e);
        }
        return airport;
    }

    private static Double nullDoubleIfEmpty(String s) throws NumberFormatException {
        if (StringUtils.isEmpty(s)) {
            return null;
        } else {
            return Double.parseDouble(s);
        }
    }

    private static Integer nullIntegerIfEmpty(String s) throws NumberFormatException {
        if (StringUtils.isEmpty(s)) {
            return null;
        } else {
            return Integer.parseInt(s);
        }
    }

    private static Boolean nullBooleanIfEmpty(String s) throws NumberFormatException {
        if (StringUtils.isEmpty(s)) {
            return null;
        } else {
            return Boolean.parseBoolean(s);
        }
    }

}
