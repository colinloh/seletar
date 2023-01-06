package com.canabrix.wordle;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class WordleTests {

    private static final Logger logger = LoggerFactory.getLogger(WordleTests.class);

    @Autowired
    private WordleService wordleService;

    @Test
    void readStaticWordsFile() throws IOException {
        assertThat(
                wordleService.wordleWords().getWords()
        )
            .withFailMessage("Static Word List failed to be loaded/is too small")
            .hasSizeGreaterThan(100)
        ;
    }

    @Test
    void wordsThatStartWithC() {
        Set<String> cWords = wordleService.wordleWords()
                .intersection(wordleService.wordsWith('c', 1))
                .getWords();

        assertThat(cWords)
            .withFailMessage("Words beginning with c is empty")
            .hasSizeGreaterThan(5)
        ;

        cWords.forEach(
                w -> {
                    // logger.debug("Checking " + w);
                    assertThat(w.charAt(0))
                            .withFailMessage("Word " + w + " does not begin with c")
                            .isEqualTo('c')
                    ;
                }
        );
    }

    @Test
    void wordsThatHaveEInThirdPosition() {
        Set<String> cWords = wordleService.wordleWords()
                .intersection(wordleService.wordsWith('e', 3))
                .getWords();

        assertThat(cWords)
                .withFailMessage("Words that have 'e' in third position is empty")
                .hasSizeGreaterThan(5)
        ;

        cWords.forEach(
                w -> {
                    assertThat(w.charAt(2))
                            .withFailMessage("Word " + w + " does not have e in third position")
                            .isEqualTo('e')
                    ;
                }
        );
    }

    @Test
    void wordsStartingWithFAndEndingWithD() {
        Set<String> cWords = wordleService.wordleWords()
                .intersection(wordleService.wordsWith('f', 1))
                .intersection(wordleService.wordsWith('d', 5))
                .getWords();

        assertThat(cWords)
                .withFailMessage("Words of 'f...d' is empty")
                .hasSizeGreaterThan(5)
        ;

        cWords.forEach(
                w -> {
                    assertThat(w.charAt(0))
                            .withFailMessage("Word " + w + " does not start with f")
                            .isEqualTo('f')
                    ;
                    assertThat(w.charAt(4))
                            .withFailMessage("Word " + w + " does not end with d")
                            .isEqualTo('d')
                    ;
                }
        );
    }

    @Test
    void unionOfQWordsWithZWords() {

        WordBucket QWords = wordleService.wordsWith('q', 1);
        WordBucket ZWords = wordleService.wordsWith('z', 1);

        Set<String> words = QWords
                .union(ZWords)
                .getWords();

        logger.debug("Q words: " + QWords.getWords().size());
        logger.debug("Z words: " + ZWords.getWords().size());
        logger.debug("union'ed words: " + words.size());

        words.forEach(
                w -> {
                    assertThat(w.charAt(0) == 'q' || w.charAt(0) == 'z')
                            .withFailMessage("Word " + w + " does not start with q or z")
                            .isTrue()
                    ;
                }
        );

        assertThat(words.size())
                .withFailMessage("Union'ed size is not size of the sum of unions")
                .isEqualTo(QWords.getWords().size() + ZWords.getWords().size())
                ;
    }

    @Test
    void edify() {
        WordBucket bucket = wordleService.wordleWords()
                .withoutLetter('r')
                .withoutLetter('a')
                .withoutLetter('t')
                .withoutLetter('s')
                .withoutLetter('b')
                .withoutLetter('n')
                .withoutLetter('g')
                .withoutLetter('m')
                .withoutLetter('c')
                .withoutLetter('o')
                .withoutLetter('q')
                .withLetterButNotIn('e', 4)
                .withLetterButNotIn('e', 5)
                .withLetterButNotIn('e', 2)
                .withLetterButNotIn('d', 3)
                .withLetterButNotIn('i', 2)
                ;

        assertThat(bucket.getWords().size())
                .withFailMessage("Edify is not the only word left")
                .isEqualTo(1);

        assertThat(bucket.getWords().contains("edify"))
                .withFailMessage("Edify is not in the results")
                .isTrue();
    }

    @Test
    void exactMatchScoring() {
        Assertions.assertThat(
                Analysis.exactMatchScore("choir", "chasm")
                ).withFailMessage("Exact Scoring Algo is wrong")
                .isEqualTo(2);

        assertThat(
                Analysis.exactMatchScore("whisk", "frisk")
                ).withFailMessage("Exact Scoring Algo is wrong")
                .isEqualTo(3);

        assertThat(
                Analysis.exactMatchScore("rates", "tares")
                ).withFailMessage("Exact Scoring Algo is wrong")
                .isEqualTo(3);
    }

    @Test
    void offsetMatchScoring() {
        assertThat(
                Analysis.offsetMatchScore("choir", "chasm")
        ).withFailMessage("Offset Scoring Algo is wrong")
                .isEqualTo(0);

        assertThat(
                Analysis.offsetMatchScore("rates", "tares")
        ).withFailMessage("Offset Scoring Algo is wrong")
                .isEqualTo(2);

        assertThat(
                Analysis.offsetMatchScore("plant", "train")
        ).withFailMessage("Offset Scoring Algo is wrong")
                .isEqualTo(2);
    }


}
