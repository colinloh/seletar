package com.canabrix.airports;

import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.ResourceUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class OurAirportsParserTest {

    private static final String LOCAL_TEST_AIRPORTS_FILE = "classpath:static/truncated_airports.csv";
    private static final Logger logger = LoggerFactory.getLogger(OurAirportsParserTest.class);

    @Test
    public void testLocalOurAirportsFile() throws Exception {
        logger.info("Test parsing of local logfile : " + LOCAL_TEST_AIRPORTS_FILE);
        File f = ResourceUtils.getFile(LOCAL_TEST_AIRPORTS_FILE);
        try (
                BufferedReader reader =
                        new BufferedReader(
                                new InputStreamReader(
                                        new FileInputStream(f)
                                )
                        )
        ) {
            int numParsed = 0;
            boolean encounteredOnce = false;
            for (String line : reader.lines().toList()) {
                try {
                    Airport airport = OurAirportsParser.from(line);

                    assertThat(airport.getAirportCode())
                        .withFailMessage("Airport code is missing")
                        .isNotBlank()
                        ;
                    assertThat(airport.getOurAirportsId())
                        .withFailMessage("ourairports.com id is missing")
                        .isNotNull()
                        ;
                    numParsed++;
                } catch (AirportParseHeaderLineException e) {
                    assertThat(e.getHeaderLineEncountered())
                            .isEqualTo(line);
                    assertThat(StringUtils.startsWith(line, "\"id\",\"ident\",\"type\""))
                            .isTrue();
                    assertThat(encounteredOnce)
                            .isFalse();
                    encounteredOnce = true;
                }
            }
            logger.info("Number of airport CSV lines parsed: " + numParsed);
            assertThat(numParsed)
                    .withFailMessage("Didn't parse the whole test file")
                    .isEqualTo(199) // 200 lines including the header line, which we'd skip.
                    ;
        }
    }
}
