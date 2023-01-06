package com.canabrix.flightlog;

import com.amazonaws.services.s3.model.S3ObjectSummary;
import com.canabrix.aws.CanabrixAwsService;
import com.canabrix.flightlog.foreflight.FlightlogForeflightFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class FlightlogS3ReaderService {

    private static final Logger logger = LoggerFactory.getLogger(FlightlogS3ReaderService.class);

    public static final String LOGBOOKS_BUCKET_PATH = "logbooks";
    private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm");

    public FlightlogS3ReaderService() {
        super();
    }

    /**
     * @return Key of the latest flight log file
     */
    public String findLatestFlightlogKey() {
        Date latestSoFar = null;
        S3ObjectSummary latestObject = null;
        // get the latest file in this path according to S3.
        for (S3ObjectSummary s : canabrixAwsService.listObjectSummaries(LOGBOOKS_BUCKET_PATH)) {
            Date d = s.getLastModified();
            if (latestSoFar == null || d.after(latestSoFar)) {
                latestSoFar = d;
                latestObject = s;
            }
        }
        if (latestObject != null) {
            logger.info("Latest logbook file found is : "
                    + sdf.format(latestObject.getLastModified())
                    + " : "
                    + latestObject.getKey())
            ;
        } else {
            logger.warn("Unable to find a single logbook file in path: " + LOGBOOKS_BUCKET_PATH);
        }

        return latestObject.getKey();
    }

    public Flightlog createFlightlogFromS3File(String key) throws FlightlogParseException {

        List<String> lines = new ArrayList<>();
        try {
            canabrixAwsService.consumeS3Lines(
                    key,
                    l -> lines.add(l)
            );
        } catch (IOException e) {
            throw new FlightlogParseException(
                    key, "Reading lines from S3 object", e
            );
        }

        return FlightlogForeflightFactory.create(lines);

    }

    public Flightlog getLatestFlightlog() throws FlightlogParseException {
        return createFlightlogFromS3File(findLatestFlightlogKey());
    }

    @Autowired
    private CanabrixAwsService canabrixAwsService;
}
