package com.canabrix.airports;

import com.canabrix.aws.CanabrixAwsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.function.Consumer;

@Service
public class AirportS3ReaderService {

    private static final Logger logger = LoggerFactory.getLogger(AirportS3ReaderService.class);

    public static final String AIRPORTS_BUCKET_PATH = "airports";
    public static final String AIRPORTS_FILENAME = "airports.csv";
    public static final String RUNWAYS_FILENAME = "runways.csv";

    public int loadAirports(
            Consumer<String> consumer
    ) throws IOException {
        logger.info("Consuming airports file from S3.");
        return canabrixAwsService.consumeS3Lines(
                AIRPORTS_BUCKET_PATH + "/" + AIRPORTS_FILENAME,
                consumer
        );
    }

    public int loadRunways(
            Consumer<String> consumer
    ) throws IOException {
        logger.info("Consuming runways file from S3.");
        return canabrixAwsService.consumeS3Lines(
                AIRPORTS_BUCKET_PATH + "/" + RUNWAYS_FILENAME,
                consumer
        );
    }

    public AirportS3ReaderService() { super(); }

    @Autowired
    private CanabrixAwsService canabrixAwsService;
}
