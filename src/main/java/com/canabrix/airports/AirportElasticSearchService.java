package com.canabrix.airports;


import com.canabrix.elasticsearch.CanabrixElasticSearchService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class AirportElasticSearchService {

    private static final Logger logger = LoggerFactory.getLogger(AirportElasticSearchService.class);
    public static final String INDEX_NAME = "airports";

    public void index(Airport airport) throws IOException {
        // logger.info("Inserting Airport: " + airport.getAirportCode());
        esService.insertDocument(
                INDEX_NAME,
                airport.getAirportCode(),
                airport
        );
    }

    @Autowired
    private CanabrixElasticSearchService esService;

}
