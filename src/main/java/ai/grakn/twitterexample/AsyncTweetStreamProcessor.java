package ai.grakn.twitterexample;

import twitter4j.*;
import twitter4j.conf.Configuration;
import twitter4j.conf.ConfigurationBuilder;

import java.util.function.BiConsumer;


public class AsyncTweetStreamProcessor {
  public AsyncTweetStreamProcessor(String consumerKey, String consumerSecret, String accessToken, String accessTokenSecret, BiConsumer<String, String> onTweetReceived) {
    Configuration conf = createConfiguration(consumerKey, consumerSecret, accessToken, accessTokenSecret);
    TweetListener tweetListener = new TweetListener(onTweetReceived);

    this.twitterStreamFactory = new TwitterStreamFactory(conf);
    this.twitterStream = twitterStreamFactory.getInstance();
    this.twitterStream.addListener(tweetListener);
  }

  public void runAsync() {
    twitterStream.sample(DEFAULT_LANGUAGE);
  }

  private static Configuration createConfiguration(String consumerKey, String consumerSecret, String accessToken, String accessTokenSecret) {
    return new ConfigurationBuilder()
        .setDebugEnabled(false)
        .setOAuthConsumerKey(consumerKey)
        .setOAuthConsumerSecret(consumerSecret)
        .setOAuthAccessToken(accessToken)
        .setOAuthAccessTokenSecret(accessTokenSecret)
        .build();
  }
  private final String DEFAULT_LANGUAGE = "en";

  private TwitterStreamFactory twitterStreamFactory;
  private TwitterStream twitterStream;
}

class TweetListener implements StatusListener {
  public TweetListener(BiConsumer<String, String> onStatusReceived) {
    this.onStatusReceived = onStatusReceived;
  }

  public void onStatus(Status status) {
    onStatusReceived.accept(status.getUser().getScreenName(), status.getText());
  }

  public void onDeletionNotice(StatusDeletionNotice statusDeletionNotice) {}
  public void onTrackLimitationNotice(int numberOfLimitedStatuses) {}
  public void onException(Exception ex) {
    ex.printStackTrace();
  }
  public void onScrubGeo(long lat, long long_) {}
  public void onStallWarning(StallWarning stallWarning) {}

  private BiConsumer<String, String> onStatusReceived;
}