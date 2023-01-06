package com.canabrix.wordle;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashSet;
import java.util.Set;

public class WordBucket {

    private static final Logger logger = LoggerFactory.getLogger(WordBucket.class);

    private Set<String> words;
    public WordBucket() {
        words = new HashSet<>();
    }
    public void addWord(String word) {
        words.add(word);
    }
    public void removeWord(String word) {
        words.remove(word);
    }

    public Set<String> getWords() {
        return this.words;
    }

    // Chainable methods to help update buckets
    public WordBucket clone() {
        WordBucket result = new WordBucket();

        this.words.forEach(
                w -> result.addWord(w)
        );
        if (logger.isDebugEnabled()) {
            logger.debug(result.getWords().size() + " words cloned.");
        }

        return result;
    }

    public WordBucket intersection(WordBucket target) {
        WordBucket result = new WordBucket();

        words.forEach(
                w -> {
                    if (target.getWords().contains(w)) {
                        result.addWord(w);
                    }
                }
        );
        if (logger.isDebugEnabled()) {
            logger.debug(result.getWords().size() + " words in intersection");
        }

        return result;
    }

    public WordBucket union(WordBucket target) {
        WordBucket result = new WordBucket();

        words.forEach(
            w -> result.addWord(w)
        );
        target.getWords().forEach(
            w -> result.addWord(w)
        );
        if (logger.isDebugEnabled()) {
            logger.debug(result.getWords().size() + " words in union.");
        }

        return result;
    }

    public WordBucket exclude(WordBucket target) {
        WordBucket result = new WordBucket();

        this.getWords().forEach(
                w -> {
                    if (!target.getWords().contains(w)) {
                        result.addWord(w);
                    }
                }
        );

        return result;
    }

    public WordBucket withoutLetter(char letter) {
        WordBucket result = this;
        for (int i = 1; i <= WordleService.WORDLE_WORD_LENGTH; i++) {
                result = result.exclude(WordleService.wordsWith(letter, i));
        }
        return result;
    }

    /**
     * @param index 1-based index
     */
    public WordBucket withLetterAt(char letter, int index) {
        return this.intersection(WordleService.wordsWith(letter, index));
    }

    /**
     * @param notInIndex 1-based index
     */
    public WordBucket withLetterButNotIn(char letter, int notInIndex) {

        WordBucket candidates = new WordBucket();
        if (logger.isDebugEnabled()) {
            logger.debug("With " + letter + " but not in " + notInIndex);
        }
        for (int i = 1; i <= WordleService.WORDLE_WORD_LENGTH; i++) {
            if (i != notInIndex) {
                candidates = candidates.union(WordleService.wordsWith(letter, i));
            }
        }

        return this.intersection(candidates).exclude(WordleService.wordsWith(letter, notInIndex));
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        words.forEach(
                w -> sb.append(w).append("\n")
        );

        return sb.toString();
    }
}

