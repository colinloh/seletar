package com.canabrix.wordle;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class AdhocTest {

    @Autowired
    private WordleService wordleService;

    @Test
    void adhoc() {
        WordBucket bucket = wordleService.wordleWords()
                .withLetterAt('c',1)
                .withLetterAt('a',2)
                .withoutLetter('r')
                .withoutLetter('e')
                .withLetterAt('s',5)
                .withLetterButNotIn('m',3)
        ;

        Analysis.analyze(bucket);
    }

}
