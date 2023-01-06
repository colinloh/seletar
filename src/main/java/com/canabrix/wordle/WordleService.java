package com.canabrix.wordle;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import java.io.*;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class WordleService {

    private static final Logger logger = LoggerFactory.getLogger(WordleService.class);

    public WordleService() throws IOException {
        initWordleSets();
    }

    public static final int WORDLE_WORD_LENGTH = 5;
    public static final int NUMBER_OF_LETTERS_IN_ENGLISH_ALPHABET = 26;
    private static WordBucket allWordleWords;
    private static final WordBucket[][] wordBuckets = new WordBucket[WORDLE_WORD_LENGTH][NUMBER_OF_LETTERS_IN_ENGLISH_ALPHABET];

    private synchronized void initWordleSets() throws IOException {
        logger.info("Initializing Wordle word buckets");
        File f = ResourceUtils.getFile("classpath:static/all_words.txt");
        Set<String> wordleWords;
        try (
                BufferedReader reader =
                    new BufferedReader(
                        new InputStreamReader(
                            new FileInputStream(f)
                        )
                    )
        ) {
            wordleWords = reader.lines().filter(
                    w -> (w.length() == WORDLE_WORD_LENGTH)
            ).collect(Collectors.toSet());
        }

        for (int i = 0; i < WORDLE_WORD_LENGTH; i++) {
            for (int j = 0; j < NUMBER_OF_LETTERS_IN_ENGLISH_ALPHABET; j++) {
                wordBuckets[i][j] = new WordBucket();
            }
        }

        // Assign each of the words into the buckets
        allWordleWords = new WordBucket();
        wordleWords.forEach(
            w -> {
                allWordleWords.addWord(w);
                for (int pos = 0; pos < WORDLE_WORD_LENGTH; pos++) {
                    char letter = w.charAt(pos);
                    int letterOrd = letter - 'a';
                    // logger.debug("Adding " + w + " to " + pos + ":" + letter);
                    wordBuckets[pos][letterOrd].addWord(w);
                }
            }
        );
    }

    public static WordBucket wordleWords() {
        return allWordleWords.clone();
    }

    /**
     * @param letter lower-case letter
     * @param position 1-based index of the letter position
     * @return The words bucket matching the index and letter. e.g. "(3,'g') would be words where the 4th letter is 'g'.
     */
    public static WordBucket wordsWith(char letter, int position) {
        return wordBuckets[position - 1][letter - 'a'];
    }

    public List<Possibility> getPossibilites(List<WordleHint> hints) {

        WordBucket bucket = wordleWords();
        for (WordleHint hint : hints) {

            // We need to keep track of whether the letter has been accounted for already.
            boolean hasAppeared = false;

            char letter  = hint.getLetter();
            for (int p : hint.getAtPositions()) {
                bucket = bucket.withLetterAt(letter, p);
                hasAppeared = true;
            }
            for (int p : hint.getSomewhereOtherThan()) {
                bucket = bucket.withLetterButNotIn(letter, p);
                hasAppeared = true;
            }
            if (hint.isNoMore()) {
                if (!hasAppeared) {
                    for (int i = 0; i < WORDLE_WORD_LENGTH; i++) {
                        bucket = bucket.exclude(
                                bucket.withLetterAt(letter, i + 1)
                        );
                    }
                }
                // else it has appeared before. Don't exclude it because there are cases when we might over-exclude.
            }
        }
        return Analysis.analyze(bucket);
    }
}
