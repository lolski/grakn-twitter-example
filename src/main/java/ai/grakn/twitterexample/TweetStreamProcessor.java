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
    twitterStream.sample(DEFAULT_LANGUAGE);
  }

  private final String DEFAULT_LANGUAGE = "en";
  private TwitterStreamFactory twitterStreamFactory;
  private TwitterStream twitterStream;
}

class TwitterHelper {
  public static Configuration createConfiguration(String consumerKey, String consumerSecret, String accessToken, String accessTokenSecret) {
    return new ConfigurationBuilder()
        .setDebugEnabled(false)
        .setOAuthConsumerKey(consumerKey)
        .setOAuthConsumerSecret(consumerSecret)
        .setOAuthAccessToken(accessToken)
        .setOAuthAccessTokenSecret(accessTokenSecret)
        .build();
  }
}

class TweetListener implements StatusListener {
  public TweetListener(Consumer2<String, String> onStatusReceived) {
    this.onStatusReceived = onStatusReceived;
  }

  public void onStatus(Status status) {
    onStatusReceived.apply(status.getUser().getScreenName(), status.getText());
  }

  public void onDeletionNotice(StatusDeletionNotice statusDeletionNotice) {}
  public void onTrackLimitationNotice(int numberOfLimitedStatuses) {}
  public void onException(Exception ex) {
    ex.printStackTrace();
  }
  public void onScrubGeo(long lat, long long_) {}
  public void onStallWarning(StallWarning stallWarning) {}

  private Consumer2<String, String> onStatusReceived;
}