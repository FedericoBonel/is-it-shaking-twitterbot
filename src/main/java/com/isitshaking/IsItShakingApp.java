package com.isitshaking;

import com.isitshaking.model.Earthquake;
import com.isitshaking.model.USGSConnection;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterFactory;

import java.util.concurrent.TimeUnit;

public class IsItShakingApp {

    public static void main(String[] args) {
        Earthquake prevEarthquake = null;
        while(true) {
            try {
                TimeUnit.MINUTES.sleep(1);
                System.out.println("Getting last earthquake information...");
                USGSConnection usgsConnection = new USGSConnection();
                Earthquake lastEarthquake = usgsConnection.getLastEarthquake();
                System.out.println("Building tweet...");
                Twitter twitter = new TwitterFactory().getInstance();
                String tweet = buildEarthquakeTweet(lastEarthquake);
                System.out.println("Tweeting...");
                Status status = twitter.updateStatus(tweet);
                System.out.println("Tweeted: " + status.getText());
            } catch (Exception error) {
                error.printStackTrace();
            }

        }
    }

    private static String buildEarthquakeTweet(Earthquake lastEarthquake) {
        StringBuilder tweet = new StringBuilder();
        tweet.append("It sure did shake! Last earthquake was at: ").append(lastEarthquake.getTime())
                .append("\n in: ").append(lastEarthquake.getLocation())
                .append("\nMagnitude was: ").append(lastEarthquake.getMagnitude())
                .append("\nGet more information at: ").append(lastEarthquake.getUrl());
        return tweet.toString();
    }
}
