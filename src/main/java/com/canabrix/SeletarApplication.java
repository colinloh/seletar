package com.canabrix;

import com.amazonaws.services.s3.model.S3ObjectSummary;
import com.canabrix.aws.CanabrixAwsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.util.List;

@SpringBootApplication
@RestController
public class SeletarApplication {

	private static final Logger logger = LoggerFactory.getLogger(SeletarApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(SeletarApplication.class, args);
	}

	@GetMapping("/s3")
	public List<S3ObjectSummary> s3(HttpSession session) {
		logger.info("Checking S3");
		return awsService.listObjectSummaries(null);
		// session.setAttribute("summaries", summaries);
	}

	private CanabrixAwsService awsService;
	@Autowired
	public void setAwsService(CanabrixAwsService awsService) {
		this.awsService = awsService;
	}

}
