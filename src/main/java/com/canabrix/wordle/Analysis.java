package com.canabrix.wordle;

import org.apache.commons.lang3.tuple.MutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public abstract class Analysis {

    private static final int MAX_RESULTS = 50;

    private static final Logger logger = LoggerFactory.getLogger(Analysis.class);

    public static List<Possibility> analyze(WordBucket bucket) {
        String[] words = bucket.getWords().toArray(new String[bucket.getWords().size()]);

        Pair<String, Double>[] wordScores = new Pair[words.length];
        for (int i = 0; i < words.length; i++) {
            wordScores[i] = new MutablePair<>(
                words[i],
                exactMatchScoreVariance(words, i)
            );
        }

        Arrays.sort(wordScores,
                (x,y) -> {
                    double d = x.getRight() - y.getRight();
                    if (d > 0)
                        return 1;
                    else if (d < 0)
                        return -1;
                    else
                        return 0;
                }
        );

        List<Possibility> possibilities = new ArrayList<>(words.length);
        for (int i = 0; i < words.length && i < MAX_RESULTS; i++) {
            logger.info("Variance: " + wordScores[i].getLeft() + " : " + wordScores[i].getRight());
            possibilities.add(new Possibility(
                    wordScores[i].getLeft(), wordScores[i].getRight()
            ));
        }
        return possibilities;
    }

    public static int exactMatchScore(String w1, String w2) {
        int score = 0;
        for (int i = 0; i < WordleService.WORDLE_WORD_LENGTH; i++) {
            if (w1.charAt(i) == w2.charAt(i)) {
                score++;
            }
        }
        return score;
    }

    public static int offsetMatchScore(String w1, String w2) {
        int score = 0;
        for (int i = 0; i < WordleService.WORDLE_WORD_LENGTH; i++) {
            for (int j = 0; j < WordleService.WORDLE_WORD_LENGTH; j++) {
                if (j == i) continue; // We won't score exact matches
                if (w1.charAt(i) == w2.charAt(j)) {
                    score++;
                }
            }
        }
        return score;
    }

    public static double exactMatchScoreVariance(String[] words, int idxTargetWord) {
        double varianceScore = 0.0;

        // Histogram of scores for the target word against the others.
        int[] count = new int[5];
        for (int i = 0; i < 5; i++) {
            count[i] = 0;
        }

        String targetWord = words[idxTargetWord];
        for (int i = 0; i < words.length; i++) {
            if (i == idxTargetWord) continue;
            int score = exactMatchScore(targetWord, words[i]);
            count[score]++;
        }

        // calc the variance. This is our variance score and what we want.
        double mean = 0.0;
        for (int i = 0; i < 5; i++) {
            mean += count[i];
        }
        mean /= 5;

        for (int i = 0; i < 5; i++) {
            varianceScore = Math.max(varianceScore, Math.pow(count[i] - mean, 2));
        }

        return varianceScore;
    }


}
