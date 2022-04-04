package com.isitshaking;

import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;

public class IsItShakingApp {

    public static void main(String[] args) {
        Twitter twitter = new TwitterFactory().getInstance();
        try {
            twitter.updateStatus("Test 1");
        } catch (TwitterException twitterException) {
            twitterException.printStackTrace();
        }
    }
}
