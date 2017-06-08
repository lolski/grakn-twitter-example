package ai.grakn.twitterexample;

import ai.grakn.twitterexample.util.Consumer2;
import twitter4j.*;
import twitter4j.conf.Configuration;
import twitter4j.conf.ConfigurationBuilder;

import static ai.grakn.twitterexample.TwitterHelper.*;

public class TweetStreamProcessor {
  public TweetStreamProcessor(String consumerKey, String consumerSecret, String accessToken, String accessTokenSecret, Consumer2<String, String> onTweetReceived) {
    Configuration conf = createConfiguration(consumerKey, consumerSecret, accessToken, accessTokenSecret);
    TweetListener tweetListener = new TweetListener(onTweetReceived);

    this.twitterStreamFactory = new TwitterStreamFactory(conf);
    this.twitterStream = twitterStreamFactory.getInstance();
    this.twitterStream.addListener(tweetListener);
  }

  public void run() {
    twitterStream.sample();
  }

  private TwitterStreamFactory twitterStreamFactory;
  private TwitterStream twitterStream;
}

class TwitterHelper {
  public static Configuration createConfiguration(String consumerKey, String consumerSecret, String accessToken, String accessTokenSecret) {
    ConfigurationBuilder cb = new ConfigurationBuilder();
    Configuration configuration = cb.setDebugEnabled(true)
        .setOAuthConsumerKey("s81rBRQWHvGE1llHPYry7zSOm")
        .setOAuthConsumerSecret("weQ8oZhBDZq9PjADlZJ897MAlxkXlNsUEH04jsqYPaLX4QCTKB")
        .setOAuthAccessToken("1425775171-bnAiy4iF6y2SH1WMXPQmuwDm40zLTkBLk62qjxS")
        .setOAuthAccessTokenSecret("XZ1SYSBOOFIH2jP6IKT3Je10tGtGKxstdPhuy2X4dHCUC").build();

    return configuration;
  }
}

class TweetListener implements StatusListener {
  public TweetListener(Consumer2<String, String> onStatusReceived) {
    this.onStatusHandler = onStatusReceived;
  }

  public void onStatus(Status status) {
    onStatusHandler.apply(status.getUser().getScreenName(), status.getText());
  }

  public void onDeletionNotice(StatusDeletionNotice statusDeletionNotice) {}
  public void onTrackLimitationNotice(int numberOfLimitedStatuses) {}
  public void onException(Exception ex) {
    ex.printStackTrace();
  }
  public void onScrubGeo(long lat, long long_) {}
  public void onStallWarning(StallWarning stallWarning) {}

  private Consumer2<String, String> onStatusHandler;
}