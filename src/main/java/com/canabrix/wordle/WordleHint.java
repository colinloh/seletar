package com.canabrix.wordle;

import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.Map;

public class WordleHint {

    public WordleHint() {
    }

    public WordleHint(char letter, int[] atPositions, int[] somewhereOtherThan, boolean noMore) {
        this.letter = letter;
        this.atPositions = atPositions;
        this.somewhereOtherThan = somewhereOtherThan;
        this.noMore = noMore;
    }

    private int[] fromObjListToIntArray(List<Object> objList) {
        int[] result = new int[objList.size()];
        for (int i = 0; i < objList.size(); i++) {
            result[i] = (int)objList.get(i);
        }
        return result;
    }

    /**
     * Constructor for wiring from a graphQL-provided map
     */
    public WordleHint(Map<String, Object> map) {
        this();
        this.setLetter((String)(map.get("letter")));
        this.setAtPositions(fromObjListToIntArray((List)map.get("atPositions")));
        this.setSomewhereOtherThan(fromObjListToIntArray((List)map.get("somewhereOtherThan")));
        this.setNoMore((Boolean)map.get("isNoMore"));
    }

    private char letter;
    private int[] atPositions;
    private int[] somewhereOtherThan;
    private boolean noMore;

    public char getLetter() {
        return letter;
    }

    public void setLetter(String letter) {
        this.letter = StringUtils.lowerCase(letter).charAt(0);
    }

    public int[] getAtPositions() {
        return atPositions;
    }

    public void setAtPositions(int[] atPositions) {
        this.atPositions = atPositions;
    }

    public int[] getSomewhereOtherThan() {
        return somewhereOtherThan;
    }

    public void setSomewhereOtherThan(int[] somewhereOtherThan) {
        this.somewhereOtherThan = somewhereOtherThan;
    }

    public boolean isNoMore() {
        return noMore;
    }

    public void setNoMore(boolean noMore) {
        this.noMore = noMore;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append(letter).append(": ");
        if (getAtPositions() != null) {
            sb.append(" is at: ");
            for (int i : getAtPositions()) {
                sb.append(i).append(" ");
            }
            sb.append(",");
        }

        if (getSomewhereOtherThan() != null) {
            sb.append(" is somewhere other than: ");
            for (int i : getSomewhereOtherThan()) {
                sb.append(i).append(" ");
            }
            sb.append(",");
        }

        if (isNoMore()) {
            sb.append(" is nowhere (else).");
        }

        return sb.toString();
    }
}
