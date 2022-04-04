package com.isitshaking;

import com.isitshaking.model.Earthquake;
import com.isitshaking.model.USGSConnection;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterFactory;

import java.util.concurrent.TimeUnit;

public class DaemonBot {

    public final static float LOWER_LIMIT_EARTHQUAKE_DETECTION = 1.7f;

    public DaemonBot() {
    }

    /**
     * Deamon of the bot it self, once this is called the bot will be reading for earthquakes constantly
     */
    public void run() {
        Earthquake prevEarthquake = null;
        while(true) {
            try {
                System.out.println("Nothing new sleeping for 1 minute...");
                TimeUnit.MINUTES.sleep(1);
                System.out.println("Getting last earthquake information...");
                USGSConnection usgsConnection = new USGSConnection();
                Earthquake lastEarthquake = usgsConnection.getLastEarthquake();
                if (prevEarthquake != null && lastEarthquake.getId().equals(prevEarthquake.getId())
                        || lastEarthquake.getMagnitude() < LOWER_LIMIT_EARTHQUAKE_DETECTION) continue;
                System.out.println("Building tweet...");
                String tweet = lastEarthquake.buildEarthquakeTweet();
                Twitter twitter = new TwitterFactory().getInstance();
                System.out.println("Tweeting...");
                Status status = twitter.updateStatus(tweet);
                System.out.println("Tweeted: " + status.getText());
                prevEarthquake = lastEarthquake;
            } catch (Exception error) {
                error.printStackTrace();
            }
        }
    }
}
