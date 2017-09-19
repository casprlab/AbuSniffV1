package com.fiu_CaSPR.Sajib.Constants;

import android.graphics.Color;

import java.util.ArrayList;

/**
 * Created by frankhu on 6/17/15.
 * This class is meant to hold all the words that we consider "cyber bullying".
 * Change the words in the mildlybaddictionary and the bad dictionary to fit your rubric.
 */
public class Dictionary {
    public static String[] mildlyBadDictionary = {"court", "poop", "poor","bad", "ugly", "retarded","stupid", "evil"};
    public static String[] badDctionary = {"bad", "words", "very bad words"};
    public static ArrayList<String> privateInformation = new ArrayList<>();

    // > 5
    // > 3 yellow
    // > 0 Green

    /**
     * Computes the score after given the number of mildly badwords and bad words that were found in the post.
     * It will return the Colors red, yellow, and green accordingly. It returns it in the form of an int however, so please be wary of that.
     * @param mildlyBadWords
     * @param badWords
     * @return int
     */
    public static int computeScore(int mildlyBadWords, int badWords){
        int score = mildlyBadWords*2 + badWords *4;
        if(score > 25){
            return Color.RED;
        }else if(score > 5){
            return Color.YELLOW;
        }else{
            return Color.GREEN;
        }
    }
}
