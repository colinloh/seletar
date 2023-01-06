package com.canabrix.wordle;

import graphql.schema.DataFetcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
public class WordleDataFetchers {

    private static final Logger logger = LoggerFactory.getLogger(WordleDataFetchers.class);

    @Autowired
    private WordleService wordleService;

    public DataFetcher<List<Possibility>> getWordlePossibilities() {
        return dataFetchingEnvironment -> {
            List<Map> hintsIn = dataFetchingEnvironment.getArgument("hints");
            List<WordleHint> hints = new ArrayList<>(hintsIn.size());
            if (logger.isDebugEnabled()) {
                logger.debug("Fetching wordle possibilities");
                for (Map hintIn : hintsIn) {
                    WordleHint hint = new WordleHint(hintIn);
                    hints.add(hint);
                    logger.debug("Created hint: " + hint);
                }
            }

            List<Possibility> possibilities = wordleService.getPossibilites(hints);

            logger.info("Returning " + possibilities.size() + " possibilities.");
            return possibilities;
        };
    }
}