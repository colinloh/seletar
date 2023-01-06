package com.canabrix.wordle;

public class Possibility {

    public Possibility(String word) {
        this.word = word;
    }

    public Possibility(String word, Double divisiveFactor) {
        this(word);
        setDivisiveFactor(divisiveFactor);
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public Double getDivisiveFactor() {
        return divisiveFactor;
    }

    public void setDivisiveFactor(Double divisiveFactor) {
        this.divisiveFactor = divisiveFactor;
    }

    private String word;
    private Double divisiveFactor;

}
