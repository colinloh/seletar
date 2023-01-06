package com.canabrix.controllers;

import com.canabrix.airports.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/airport")
public class AirportController {

    private static final Logger logger = LoggerFactory.getLogger(AirportController.class);

    @Autowired
    private AirportElasticSearchService airportElasticSearchService;

    @Autowired
    private AirportS3ReaderService airportS3ReaderService;

    @PostMapping("/indexAll")
    public Map<String, String> indexAll() throws IOException {
        // throw new UnsupportedOperationException("Expensive operation. Disabled for now");
        Map<String, String> results = new HashMap<>();

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm");

        Date start = new Date();

        int numAirportsIndexed = airportS3ReaderService.loadAirports(
            line -> {
                try {
                    Airport airport = OurAirportsParser.from(line);
                    airportElasticSearchService.index(airport);
                    // logger.debug("Indexed airport: " + airport.getAirportCode());
                } catch (AirportParseException | IOException e) {
                    logger.error("Failed to parse/index airport", e);
                } catch (AirportParseHeaderLineException e) {
                    // This is okay.
                    logger.debug("Header line encoutered and skipped during parsing.");
                }
            }
        );
        results.put("num indexed" , Integer.toString(numAirportsIndexed));

        Date end = new Date();
        results.put("Started", sdf.format(start));
        results.put("Ended", sdf.format(end));
        long minutes = (end.getTime() - start.getTime())/1000/60;
        results.put("Num min taken", Long.toString(minutes));

        return results;
    }
}
