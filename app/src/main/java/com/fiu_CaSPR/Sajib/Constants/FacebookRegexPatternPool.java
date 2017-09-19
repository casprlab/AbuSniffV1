package com.fiu_CaSPR.Sajib.Constants;

import android.util.Log;

import com.fiu_CaSPR.Sajib.DataStructures.FriendRequest;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by fhu004 on 6/15/2015.
 * This class holds all the regex patterns, as well as items that require global access. It is a singleton class that holds
 * the accesstoken, cookies, username, and other singleton variables.
 */
public class FacebookRegexPatternPool {


    public static String accessToken = "";
    public static String cookie = "";
    public static String userName = "";
    public static String div = "";
    public static int friendsDivCount=0;
    // this boolean variables is for when the user logs in and then the regex to find
    // the username has been run. It is set to true when the username has been found which is in the facebook page.
    public static boolean processed = false;

    // Once we get the id, we put it here for easy access.
    public static String id = "";
    // Elements is a JSoup Object, we put our elements here so that we can use them between classes and threads.
    public static Elements elements = null;

    // Mobile HTML source
    public static String mobileHTML = "";

    // Find the style of the page
    public static Pattern style = Pattern.compile("<html[^*]*<body[^>]*[>]");

    // find id pattern in the friend div
    public static Pattern findIdPattern = Pattern.compile("hovercard=\"[^*].+?</a></div>");

    // Find user name, use on friend div
    public static Pattern findFriendRequestName = Pattern.compile("user.php.+?</a>");

    // find the location pattern in Overview
    public static Pattern findUniversityPattern = Pattern.compile("<div class=\"_c[^*].+?</a>");

    // In overview Pattern find lives in
    public static Pattern livesInPattern = Pattern.compile(("Lives in[^*].+?</a>"));

    // Finds the friend request Divs
    public static  Pattern r = Pattern.compile("<h2 class=\"_34e\">Respond to Your *[^*].+?</div></div></div></div></div></div>");

    // Finds friend Requests
    public static  Pattern friendRequestPattern = Pattern.compile("<div class=\"clearfix ruUserBox _3-z\".+?</div></div></div></div>");

    // Finds profile Picture Div:
    public static Pattern profilePicDivs = Pattern.compile("<div><a class=\"a[^*].+?Pr");

    // Finds Number of profile pics in the div stream
    public static Pattern numOfProfilePicsPat = Pattern.compile("<div class=\"_46-h _53f2\"");

    // Finds the lifeEvent Div
    public static Pattern lifeEventDiv = Pattern.compile("<ul class=\"uiList[^*]*Born");

    // Finds the number of LifeEvents in the lifeeventDiv
    public static Pattern lifeEvents = Pattern.compile("class=\"_3fo8");

    /*******************************/

    // Finds the Friends DIV
    public static Pattern friendsDiv = Pattern.compile("<a href=\"https:.+?<[/]a>");

    // Finds the ID Level 2 Div
    public static Pattern idLevel2Div = Pattern.compile("=\\d*&amp;e");

    // Finds the USER ID
    public static Pattern idDiv = Pattern.compile("[\\d]*[\\d]");

    // Finds the Name Level 2 Div
    public static Pattern nameLevel2Div = Pattern.compile(">.*");

    // Finds the Name
    public static Pattern nameDiv = Pattern.compile("[a-zA-Z][^<]*[a-zA-Z]");

    public static Map<String, String> friendsMap = new HashMap<String, String>();
    public static String[ ][ ] friendsArray = new String[20][2];
    public static  String user_id;
    public  static  String user_full_name;

/************************************/

    public static ArrayList<String> friendNames = new ArrayList<>();
    public static HashMap<String,String> friendsHashmap = new HashMap<>();

    public static ArrayList<FriendRequest> friendRequestList = new ArrayList<>();
    public static FriendRequest currentFriendRequest = new FriendRequest();
    public static String styleDiv = "";
    public static String Name;


    // Finds the names of the friend requests

    /**
     * This takes in the HTML Source of the desktop version of facebook's friend request page, and then runs regex functions in order to find the
     * id. This id is used later in order to reach their about page and other pages that have their information.
     * @param friendRequestsHtmlSource
     */
    public static void findNames(String friendRequestsHtmlSource){

        // We pass in the Friend Request HTML Source, and then use the regex findIdPattern to look for the id in the friendrequest html source
        final Matcher findName = findFriendRequestName.matcher(friendRequestsHtmlSource);
        final Matcher findIdMatcher = FacebookRegexPatternPool.findIdPattern.matcher(friendRequestsHtmlSource);
        StringBuilder builder = new StringBuilder();
        // Please look at the Matcher API in order to understand how it works
        while(findName.find()){
            String[] friendSourceSplit = findName.group().split(">");
            // We have to parse the string that the regex finds, because the regex can only find a part of the html source that contains
            // the div,
            for(int i =0;i < friendSourceSplit[1].length(); ++i){
                if(friendSourceSplit[1].toCharArray()[i] == '<'){
                    break;
                }
                builder.append(friendSourceSplit[1].toCharArray()[i]);
            }
            // From there we use the StringBuilder API in order to generate the names and add them to the friendNames ArrayList
            /*if(!friendNames.contains(builder.toString())) {
                friendNames.add(builder.toString());
                currentFriendRequest.setName(builder.toString());
            }else break;*/
            builder = new StringBuilder();
            // This parses through the string a final time to get the current friend request
            if(findIdMatcher.find()){
                char[] sCut = findIdMatcher.group().substring(39,findIdMatcher.group().length()-7).toCharArray();
                for(int i =0;i< sCut.length; ++i){
                    if(sCut[i] == '\"'){
                        break;
                    }
                    builder.append(sCut[i]);
                }
                //friendsHashmap.put(friendName,id);
                currentFriendRequest.setId(builder.toString());
            }
        }
    }


    /**
     * Finds university and current location by utilizing regex patterns and JSoup.
     * @param cookie
     */
    public static void processCurrentFriendRequestOverview(String cookie){
        String url = "https://www.facebook.com/" +currentFriendRequest.getId() ;
        try {
            // Uses JSoup to download the source of the site. It uses the Desktop version since
            // the desktop version is less prone to change and can be accessed more easily through a computer
            Connection.Response response = Jsoup.connect(url).cookie(url, cookie).ignoreContentType(true).userAgent("Mozilla/5.0 (X11; U; Linux i686; en-US; rv:1.9.0.4) Gecko/20100101 Firefox/4.0")
                    .timeout(3000)
                    .execute();
            // add /about to the url to get to www.facebook.com/username/about page
            url = response.url() +"/about";
            response = Jsoup.connect(url).cookie(url, cookie).ignoreContentType(true).userAgent("Mozilla/5.0 (X11; U; Linux i686; en-US; rv:1.9.0.4) Gecko/20100101 Firefox/4.0")
                    .timeout(3000)
                    .execute();
            // Finds the university and lives in pattern
            final Matcher match1 = findUniversityPattern.matcher(response.body());
            final Matcher match2 = livesInPattern.matcher(response.body());
            // l stands for location
            if(match1.find()) {
                // runs the regex and finds the university pattern
                String[] l = match1.group().split(">");
                l[l.length - 1].substring(0, l[l.length - 1].length() - 3);
                Log.i("regexPattern: " , " livesIn" + l[l.length - 1].substring(0, l[l.length - 1].length() - 3));
            }else currentFriendRequest.hasInfo(false);


            // runes the lives in pattern to find where the person lives
            if(match2.find()){
                String[] l = match1.group().split(">");
                l[l.length - 1].substring(0, l[l.length - 1].length() - 3);
                Log.i("regexPattern: " , " findUniversity" + l[l.length - 1].substring(0, l[l.length - 1].length() - 3));
            }else currentFriendRequest.hasInfo(false);

            Log.i("After overview process", currentFriendRequest.getDangerScore());
        }catch(Exception e){

        }
    }


    /**
     * This process the current friend request's photos. It counts how many photos there are when you load the facebook.com/photos_albums.
     * @param cookie
     */
    public static void processCurrentFriendRequestPhotos(String cookie){
        // goes the www.facebook.com/currentFriendRequest.id
        // Facebook works like this :
        // Facebook.com/id -> facebook.com/username
        // Facebook redirects an id to the person's username, and you cannot reach the facebook.com/id/photos_albums.
        // So we have to connect to facebook first and then we have to add photos_albums to the url, because
        // facebook only works by loading facebook.com/username/photos_albums
        String url = "https://www.facebook.com/" +currentFriendRequest.getId();
        try {
            Connection.Response response = Jsoup.connect(url).cookie(url, cookie).ignoreContentType(true).userAgent("Mozilla/5.0 (X11; U; Linux i686; en-US; rv:1.9.0.4) Gecko/20100101 Firefox/4.0")
                    .timeout(3000)
                    .execute();
            url = response.url()  +"/photos_albums";
            response = Jsoup.connect(url).cookie(url, cookie).ignoreContentType(true).userAgent("Mozilla/5.0 (X11; U; Linux i686; en-US; rv:1.9.0.4) Gecko/20100101 Firefox/4.0")
                    .timeout(3000)
                    .execute();
            final Matcher match = profilePicDivs.matcher(response.body());
            int numOfPics = 0;
            if(match.find()){
                final Matcher profilePicMatcher = numOfProfilePicsPat.matcher(match.group());
                while(profilePicMatcher.find())
                    numOfPics += 1;
            }
            currentFriendRequest.setPhotoNumber(numOfPics);
            Log.i("After profile pics", currentFriendRequest.getDangerScore());
        }catch(Exception e){
            Log.e("Error: ", e.toString());
        }
    }

    /**
     * Process the current friend requestLifeEvents, we have a "currentfriendRequest" that we are processing, and from there we
     * find how many life events there are. It utilizes the regex patterns in the class, in order to find the number of life events.
     * @param cookie
     */
    public static void processCurrentFriendRequestLifeEvents(String cookie){
        String url = "https://www.facebook.com/" +currentFriendRequest.getId() ;
        try{
            Connection.Response response = Jsoup.connect(url).cookie(url, cookie).ignoreContentType(true).userAgent("Mozilla/5.0 (X11; U; Linux i686; en-US; rv:1.9.0.4) Gecko/20100101 Firefox/4.0")
                    .timeout(3000)
                    .execute();
            url = response.url()+"/about?section=year-overviews&pnref=about";
            response = Jsoup.connect(url).cookie(url, cookie).ignoreContentType(true).userAgent("Mozilla/5.0 (X11; U; Linux i686; en-US; rv:1.9.0.4) Gecko/20100101 Firefox/4.0")
                    .timeout(3000)
                    .execute();
            int numOfLifeEvents = 0;
            final Matcher match = lifeEventDiv.matcher(response.body());
            if(match.find()){
                final Matcher match2 = lifeEvents.matcher(match.group());
                while(match2.find()){
                    numOfLifeEvents+=1;
                }
            }
            currentFriendRequest.numberOfLifeEvents(numOfLifeEvents);
            Log.i("After life events", currentFriendRequest.getDangerScore());
        }catch(Exception e){
            Log.e("Error: ", e.toString());
        }
    }


}
