package com.canabrix.seletar;

import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.canabrix.aws.CanabrixAwsService;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class SeletarApplicationTests {

	private static final Logger logger = LoggerFactory.getLogger(SeletarApplicationTests.class);

	@Autowired
	private CanabrixAwsService awsService;

	@Test
	void awsCredentials() {
		String key = AmazonS3ClientBuilder.standard()
				.getCredentials().getCredentials().getAWSAccessKeyId();
		logger.info("Access key: " + key);
		assertThat(key).withFailMessage("AWS Access Key not found").isNotBlank();
	}

	@Test
	void contextLoads() {
		assertThat(awsService.listObjectSummaries("words").size())
				.withFailMessage("Blank S3 Bucket List")
				.isGreaterThan(0)
				;
	}
}
