package com.fiu_CaSPR.Sajib.DataStructures;

/**
 * Created by frankhu on 6/15/15.
 * This is the basic data structure for a friend request. We utilize this in order to store our data in a malleable way. Also it implements the Comparable interface so
 * that we can use the Collections.sort method in order to sort a list of friend requests easily. This class already implements the compareTo method, so all you will have to call
 * is Collections.sort(...);
 */
public class FriendRequest implements Comparable<FriendRequest>{
    private int score = 0;
    private String name = "";
    private String id = "";
    private double percent = 0.0;
    private String div = "";
    private String mobileDiv = "";
    private int numberInPage = 0;
    private boolean injected = false;

    /**
     * This returns the danger score in the form of a string in order to determine if a friend request is red, yellow, or green
     * It is utilized in the the thread that performs the Javascript injection and makes switches on each case.
     * @return String dangerScore
     */
    public String getDangerScore(){
        if(score >15){
            return "red";
        }else if (score > 8){
            return "yellow";
        }else return "green";
    }

    /**
     *  This method was originally meant for the user to see the "danger score" in a percent style. Because
     *  giving a user a number of danger didn't make sense, however now it is deprecated because we don't seem to need to use it.
     * @return int PercentScore
     */
    @Deprecated
    public int getPerecentScore(){
       return score/21;
    }


    /**
     * setter for how many photoNumbers there are. It doesn't actually set the number of photos, it simply adds to the score since that
     * information was not necessary at the time.
     * @param photos
     */
    public void setPhotoNumber(int photos){
        if(photos == 4){
            score += 0;
        }else if (photos > 3){
            score += 3;
        }else{
            score += 5;
        }
    }

    /**
     * This method is meant for multiple items, if a friend request profile has successfully completed a portion of the rubric (or failed), this
     * method will add or subtract five accordingly.
     * @param isTrue
     */
    public void hasInfo(boolean isTrue){
        if(!isTrue)
            score+=5;
    }

    /**
     * This method is to set the number of mutual friends a user has. Again this only adds the score.
     * @param num
     */
    public void numberOfMutualFriends(int num){
        if(num<3){
            score+=5;
        }else if(num < 5){
            score+=3;
        }else{
            // Alot of mutual friends, seems good.
        }
    }

    /**
     * This method is to set the number of life events a friend request profile has.
     * @param num
     */
    public void numberOfLifeEvents(int num){
        if(num >5){

        }else if ( num > 3){
            score += num;
        }else{
            score +=6;
        }
    }

    // Below are all the getters and setters that return and set information accordingly.
    public String getName(){
        return name;
    }
    public String getId(){
        return id;
    }
    @Deprecated
    public String getDiv(){return div;}
    @Deprecated
    public String getMobileDiv(){return mobileDiv;}
    public boolean getInjected(){return injected;}
    public int getNumberInPage(){return numberInPage;}
    public void setId(String id){
        this.id = id;
    }
    public void setName(String name){
        this.name = name;
    }
    public void setDiv(String div){this.div = div;}
    @Deprecated
    public void setMobileDiv(String mobileDiv){this.mobileDiv = mobileDiv;}
    public void setNumberInPage(int num){numberInPage = num;}
    public void setInjected(boolean injection){injected=injection;}


    /**
     * Overriden method of the interface Comparable<T>, we use this to give a way to determine if one friend request is better than other
     * This is for sorting the friendrequests and then putting them at the top and bottom accordingly. We can use javascript injections
     * in order to change where a div is; It was originally made for the first mockup and to sort them and show the green ones at the top.
     * @param f
     * @return
     */
    @Override
    public int compareTo(FriendRequest f){
        if(this.getDangerScore().equalsIgnoreCase("green")){
            return -1;
        }else if(this.getDangerScore().equalsIgnoreCase("yellow")){
            if(f.getDangerScore().equalsIgnoreCase("red")){
                return 1;
            }else return -1;
        }else{
            return 1;
        }
    }
}
