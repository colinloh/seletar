package com.canabrix.aws;

import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.function.Consumer;

@Service
public class CanabrixAwsService {
    private static final Logger logger = LoggerFactory.getLogger(CanabrixAwsService.class);

    public static final String S3_BUCKET = "angmokio";

    private final AmazonS3 client;
    public CanabrixAwsService() {
        AmazonS3ClientBuilder builder = AmazonS3ClientBuilder.standard();
        AWSCredentialsProvider acp = builder.getCredentials();
        logger.info("Access key: " + acp.getCredentials().getAWSAccessKeyId());
        this.client = builder.withRegion("us-west-2").build();
    }

    public List<S3ObjectSummary> listObjectSummaries(String path) {
        return client.listObjects(S3_BUCKET, path).getObjectSummaries();
    }

    /**
     * @param key The key of the S3 object to consume lines from
     * @param consumer A consumer for each line of the file
     */
    public int consumeS3Lines(
            String key,
            Consumer<String> consumer
    ) throws IOException {
        int numLinesConsumed = 0;
        try (final S3Object s3Object = this.client.getObject(
                S3_BUCKET,
                key
            );
            final InputStreamReader streamReader = new InputStreamReader(s3Object.getObjectContent(), StandardCharsets.UTF_8);
            final BufferedReader reader = new BufferedReader(streamReader);
        ) {
            String l = reader.readLine();
            while (l != null) {
                consumer.accept(l);
                numLinesConsumed++;
                l = reader.readLine();
            }
        }
        return numLinesConsumed;
    }
}
