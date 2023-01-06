package com.canabrix;

import com.canabrix.aws.CanabrixAwsService;
import com.canabrix.wordle.WordleService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import javax.servlet.http.HttpSession;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

@SpringBootApplication
@RestController
public class SeletarApplication {

	private static final Logger logger = LoggerFactory.getLogger(SeletarApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(SeletarApplication.class, args);
	}

	private static final String S3_WORDS_PATH = "words";

	private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss'Z'");

	@GetMapping("/s3list")
	public List<String> s3List() {
		List<String> keys = new ArrayList<>();
		awsService.listObjectSummaries(S3_WORDS_PATH).forEach(
				s -> keys.add(s.getKey())
		);
		return keys;
	}

	@GetMapping("/s3words")
	public List<String> s3Words(@RequestParam(name = "filename") String filename) throws IOException {
		logger.info("Fetching: " + filename);
		List<String> words = new ArrayList<>();
		awsService.consumeS3Lines(
				S3_WORDS_PATH + "/" + filename,
				words::add
		);

		return words;
	}

	@GetMapping("/init")
	public Set<String> wordleWords(HttpSession session) {
		logger.info("Initializing to Wordle words");
		Set<String> words = WordleService.wordleWords().getWords();
		session.setAttribute("words", words);
		return words;
	}

	private CanabrixAwsService awsService;
	@Autowired
	public void setAwsService(CanabrixAwsService awsService) {
		this.awsService = awsService;
	}

	private WordleService wordleService;
	@Autowired
	public void setWordListService(WordleService wordleService) {
		this.wordleService = wordleService;
	}

}
