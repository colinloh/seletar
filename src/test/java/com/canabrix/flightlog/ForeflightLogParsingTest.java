package com.canabrix.flightlog;

import com.canabrix.flightlog.foreflight.ApproachForeflightFactory;
import com.canabrix.flightlog.foreflight.FlightlogForeflightFactory;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.ResourceUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class ForeflightLogParsingTest {

    private static final String LOCAL_TEST_LOGFILE = "classpath:static/logbook_2022-12-26_20_19_06.csv";
    private static final Logger logger = LoggerFactory.getLogger(ForeflightLogParsingTest.class);

    @Test
    public void testParser() throws Exception {
        logger.info("Test parsing of local logfile : " + LOCAL_TEST_LOGFILE);
        File f = ResourceUtils.getFile(LOCAL_TEST_LOGFILE);
        List<String> lines;
        try (
                BufferedReader reader =
                        new BufferedReader(
                                new InputStreamReader(
                                        new FileInputStream(f)
                                )
                        )
        ) {
            lines = reader.lines().collect(Collectors.toList());
        }

        Flightlog flightLog = FlightlogForeflightFactory.create(lines);

        assertThat(flightLog.getNumEntries())
                .withFailMessage("Parsed flight log has wrong number of entries")
                .isEqualTo(217) // 217 entries in the test log file. Change if needed after verification.
        ;
    }

    private static Stream<Arguments> approachTestParams() {
        return Stream.of(
                // LOC, no ILS
                Arguments.of("1;LOC;10;KLAF;", "KLAF", ApproachType.ILS_LOC, "10", Boolean.TRUE, null),
                // ILS and LOC
                Arguments.of("1;ILS OR LOC RWY 10;10;KLAF;", "KLAF", ApproachType.ILS_LOC, "10", Boolean.FALSE, null),
                // RNAV. Has comments.
                Arguments.of("1;RNAV (GPS) RWY 12;12;KKLS;Cloud layer between FAF and MAP",
                        "KKLS", ApproachType.RNAV, "12", Boolean.FALSE, "Cloud layer between FAF and MAP"),
                // Circling
                Arguments.of("1;LOC;20;KPWT;", "KPWT", ApproachType.ILS_LOC, "20", Boolean.TRUE, null),
                // Circling from runway R to L
                Arguments.of("1;ILS Y OR LOC Y RWY 16R;16L;KPAE;", "KPAE", ApproachType.ILS_LOC, "16L", Boolean.TRUE, null),
                // Circling back to the same runway, with keyword "circle"
                Arguments.of("1;ILS Y OR LOC Y RWY 16R;16R;KPAE;circled around during landing", "KPAE",
                        ApproachType.ILS_LOC, "16R", Boolean.TRUE, "circled around during landing"),
                // Circling back to the same runway, with keyword "circling"
                Arguments.of("1;ILS Y OR LOC Y RWY 16R;16R;KPAE;did some circling because tower said so", "KPAE",
                        ApproachType.ILS_LOC, "16R", Boolean.TRUE, "did some circling because tower said so"),
                // VOR, with some comments
                Arguments.of("1;VOR-A;;KPAE;", "KPAE", ApproachType.VOR, null, Boolean.FALSE, null),
                // NDB
                Arguments.of("1;NDB RWY 34;34;KAWO;", "KAWO", ApproachType.NDB, "34", Boolean.FALSE, null),
                // LDA
                Arguments.of("1;LDA/DME RWY 28R;28R;KSFO;", "KSFO", ApproachType.LDA, "28R", Boolean.FALSE, null),
                // SDF
                Arguments.of("1;SDF RWY 5;5;KMOR;", "KMOR", ApproachType.SDF, "5", Boolean.FALSE, null),
                // VISUAL
                Arguments.of("1;RACEWAY VISUAL Rwy 28L;28L;KMRY;", "KMRY", ApproachType.VISUAL, "28L", Boolean.FALSE, null)
        );
    };

    @ParameterizedTest
    @MethodSource("approachTestParams")
    public void testApproachParsing(
            String expression,
            String expectedAirport,
            ApproachType expectedApproachType,
            String expectedRunway,
            boolean expectedIsCircling,
            String expectedComments
    ) throws Exception {

        logger.debug("Testing : "+ expression);
        Approach approach = ApproachForeflightFactory.create(expression);
        assertThat(approach.getAirport())
                .withFailMessage("Wrong airport:" + expression + ":" + approach.getAirport())
                .isEqualTo(expectedAirport);
        assertThat(approach.getApproachType())
                .withFailMessage("Wrong approach type:" + expression + ":" + approach.getApproachType())
                .isEqualTo(expectedApproachType);

        // Note: Seems like the assert functions fail to equate null vs. blank and boolean vs Boolean.
        if (expectedRunway != null) {
            assertThat(approach.getRunway())
                    .withFailMessage("Wrong runway:" + expression + ":" + approach.getRunway())
                    .isEqualTo(expectedRunway);
        } else {
            assertThat(approach.getRunway())
                    .withFailMessage("Runway given when there should be none:" + expression)
                    .isBlank();
        }

        if (expectedIsCircling) {
            assertThat(approach.isCircleToLand())
                    .withFailMessage("Wrongly marked as isCircle:" + expression)
                    .isTrue();
        } else {
            assertThat(approach.isCircleToLand())
                    .withFailMessage("Wrongly marked as non-circling:" + expression + ":" + approach.isCircleToLand())
                    .isFalse();
        }
        if (expectedComments == null) {
            assertThat(approach.getComments())
                    .withFailMessage("Wrongly added comments:" + expression + ":" + approach.getComments())
                    .isBlank();
        } else {
            assertThat(approach.getComments())
                    .withFailMessage("Wrong comments:" + expression + ":" + approach.getComments())
                    .isEqualTo(expectedComments);
        }


    }

}
