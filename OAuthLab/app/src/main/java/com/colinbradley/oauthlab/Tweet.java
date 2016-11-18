package com.colinbradley.oauthlab;

/**
 * Created by colinbradley on 11/17/16.
 */

public class Tweet {
    private String tweet, timeOf;

    public Tweet (String tweet, String timeOf){
        this.tweet = tweet;
        this.timeOf = timeOf;
    }

    public String getTweet() {
        return tweet;
    }

    public String getTimeOf() {
        return timeOf;
    }

    public void setTweet(String tweet) {
        this.tweet = tweet;
    }

    public void setTimeOf(String timeOf) {
        this.timeOf = timeOf;
    }
}
