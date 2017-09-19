package com.fiu_CaSPR.Sajib.DataStructures;

import com.fiu_CaSPR.Sajib.Constants.Dictionary;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by frankhu on 6/17/15.
 * This data structure is created for the Post. The post data structure holds all the information to determine when to change or not change the edittext color
 */
public class Post {
    private int score = 0;
    private int mildlyBadWordCount = 0;
    private int badWordCount = 0;
    String text = "";
    String mildlyBadRegexToFilter = "";
    String badRegexToFilter = "";
    Pattern mildlyBadPattern;
    Pattern badPattern;
    String privateInfoFilter = "";
    Pattern privateInfoPattern;

    /**
     * Constructor for the Post data structure. It generates a string of the bad dictionary words and mildly bad dictionary words
     * and puts a | in between each word. This is so that we can use the string for regex. We can simply use all the words with |s in between
     * and run a regex matcher function on the string given in the function getScoreColor. This is for simplicity and faster results.
     */
    public Post(){
        StringBuilder build = new StringBuilder();
        for(int i =0 ;i< Dictionary.badDctionary.length-1; ++i)
            build.append(Dictionary.badDctionary[i] + "|");
        build.append(Dictionary.badDctionary[Dictionary.badDctionary.length-1]);
        badRegexToFilter = build.toString();
        build = new StringBuilder();
        for(int i =0; i< Dictionary.mildlyBadDictionary.length; ++i){
            build.append(Dictionary.mildlyBadDictionary[i] + "|");
        }
        build.append(Dictionary.mildlyBadDictionary[Dictionary.mildlyBadDictionary.length-1]);
        mildlyBadRegexToFilter = build.toString();
        build = new StringBuilder();
        for(int i =0; i< Dictionary.privateInformation.size(); ++i){
            build.append(Dictionary.privateInformation.get(i) + "|");
        }
        build.append(Dictionary.privateInformation.get(Dictionary.privateInformation.size()-1));
        privateInfoFilter = build.toString();

        mildlyBadPattern = Pattern.compile(mildlyBadRegexToFilter);
        badPattern = Pattern.compile(badRegexToFilter);
        privateInfoPattern = Pattern.compile(privateInfoFilter);

    }

    @Deprecated
    public int getScore(){
        return score;
    }

    /**
     * This function takes in some text and runs the regex string created in the constructor. essentially it uses the words from the dictionary
     * and looks to find how many words there are in the post from the dictionary. From there we return the computed score.
     * @param text
     * @return
     */
    public int getScoreColor(String text) {
        this.text = text;
        Matcher matcher = mildlyBadPattern.matcher(text);
        int count = 0;
        while (matcher.find()) count++;
        mildlyBadWordCount = count;
        count = 0;
        Matcher matcher2 = badPattern.matcher(text);
        while(matcher2.find()) count++;
        badWordCount = count;
        count = 0;
        Matcher matcher3 = privateInfoPattern.matcher(text);
        while(matcher3.find()) count++;
        badWordCount += count;
        return Dictionary.computeScore(mildlyBadWordCount, badWordCount);
    }
    @Deprecated
    public void setMildlyBadWordCount(int count) {
        mildlyBadWordCount = count;
    }

    @Deprecated
    public void setBadWordCount(int count) {
        badWordCount = count;
    }


    public void setText(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }

    @Deprecated
    public void filterMildlyBadWords() {
        text.replaceAll(text, mildlyBadRegexToFilter);
    }
    @Deprecated
    public void filterBadWords(){
        text.replaceAll(text,badRegexToFilter);
    }
}
