package com.canabrix.elasticsearch;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
public class ElasticSearchConnectionTests {

    private static final Logger logger = LoggerFactory.getLogger(ElasticSearchConnectionTests.class);

    @Autowired
    private CanabrixElasticSearchService searchService;

    @Test
    public void connectionTest() throws IOException {
        assertThat(searchService)
                .withFailMessage("Search Service instantiation failure")
                .isNotNull()
        ;
        List<String> keys = searchService.getIndicesInfo();
        assertThat(keys)
                .withFailMessage("Unable to get index information")
                .asList().isNotNull()
        ;
        keys.forEach(
                k -> logger.debug(k)
        )
        ;
    }

}
