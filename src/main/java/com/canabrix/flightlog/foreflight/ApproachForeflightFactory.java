package com.canabrix.flightlog.foreflight;

import com.canabrix.flightlog.Approach;
import com.canabrix.flightlog.ApproachFactory;
import com.canabrix.flightlog.ApproachType;
import com.canabrix.flightlog.FlightlogParseException;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class ApproachForeflightFactory extends ApproachFactory {

    private static final Logger logger = LoggerFactory.getLogger(ApproachForeflightFactory.class);
    private static final int NUM_PARTS_BEFORE_COMMENTS = 4;

    public static Approach create(String expression) throws FlightlogParseException {

        /*
         * Examples from foreflight:
         * 1;RNAV (GPS) RWY 2;20;KPWT;
         * 1;VOR-A;;KPAE;
         * 1;RNAV (GPS) Y RWY 16;16;KRNT;
         * 1;ILS OR LOC RWY 17;17;KTIW;
         */
        Approach approach = instantiateApproach();

        try (CSVParser parser = CSVParser.parse(expression, CSVFormat.newFormat(';'))) {
            List<CSVRecord> records = parser.getRecords();
            if (records.size() > 1) {
                logger.warn("Encountered more than one line for approach expression.");
            }
            CSVRecord record = records.get(0);
            if (record.size() < NUM_PARTS_BEFORE_COMMENTS) {
                throw new FlightlogParseException(
                        expression,
                        "Approach expression doesn't have at least 4 parts per foreflight convention."
                );
            }

            // First part always seems to be '1'.
            approach.setTitle(record.get(1));
            approach.setRunway(record.get(2));
            approach.setAirport(record.get(3));

            // Parse any comments.
            // They come after the 3rd ';', and may contain ';' so split would have split them, if any.
            StringBuilder sb = new StringBuilder();
            for (int i = NUM_PARTS_BEFORE_COMMENTS; i < record.size(); i++) {
                if (i > NUM_PARTS_BEFORE_COMMENTS) {
                    sb.append(';');
                }
                sb.append(record.get(i));
            }
            approach.setComments(sb.toString());

            // Derive the rest of the approach properties from the parsed fields.
            approach.setApproachType(deriveApproachType(approach));
            approach.setCircleToLand(deriveIsCircle(approach));
        } catch (IOException e) {
            throw new FlightlogParseException(
                    expression,
                    "Parsing approach",
                    e);
        }

        return approach;
    }

    private static final Map<String, ApproachType> keywordToTypeMap = new HashMap<>();
    static {
        keywordToTypeMap.put("ILS", ApproachType.ILS_LOC);
        keywordToTypeMap.put("LOC", ApproachType.ILS_LOC);
        keywordToTypeMap.put("RNAV", ApproachType.RNAV);
        keywordToTypeMap.put("VOR", ApproachType.VOR);
        keywordToTypeMap.put("NDB", ApproachType.NDB);
        keywordToTypeMap.put("PAR", ApproachType.PAR);
        keywordToTypeMap.put("LDA", ApproachType.LDA);
        keywordToTypeMap.put("SDF", ApproachType.SDF);
        keywordToTypeMap.put("ASR", ApproachType.ASR);
        keywordToTypeMap.put("VISUAL", ApproachType.VISUAL);
    }

    private static ApproachType deriveApproachType(Approach approach) {
        String approachTitle = StringUtils.toRootUpperCase(approach.getTitle());
        for (String key : keywordToTypeMap.keySet()) {
            if (StringUtils.contains(approachTitle, key)) {
                return keywordToTypeMap.get(key);
            }
        }
        logger.warn("Failed to derive approach type from approach title: " + approachTitle);
        return null;
    }

    private static boolean deriveIsCircle(Approach approach) {
        // It's a circle if the runway used is not found in the title.
        boolean isCircle = false;

        if (!StringUtils.containsIgnoreCase(
                approach.getTitle(),
                approach.getRunway()
        )) {
            logger.debug("Flagging as circling because runway is not found in approach title.");
            isCircle = true;
        } else {
            logger.debug("Not circling: Title: " + approach.getTitle() + " contains " + approach.getRunway());
        }

        // The above logic isn't sufficient.
        // It's possible to circle onto the same runway as the approach chart.
        // e.g. If existing local VFR traffic requires the inbound instrument approach aircraft to
        // break off from final to circle and join downwind.
        // To allow for logging this via an explicit comment, if the keyword circle/circling is found in comments,
        // treat as a circling approach.
        if (StringUtils.containsIgnoreCase(approach.getComments(), "circl")) {
            logger.debug("Flagging as circling because of comments.");
            isCircle = true;
        }

        return isCircle;
    }
}
