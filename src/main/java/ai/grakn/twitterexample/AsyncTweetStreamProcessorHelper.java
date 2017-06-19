package ai.grakn.twitterexample;

import twitter4j.*;
import twitter4j.conf.Configuration;
import twitter4j.conf.ConfigurationBuilder;

import java.util.function.BiConsumer;

public class AsyncTweetStreamProcessorHelper {
  public static TwitterStream listenToTwitterStreamAsync(String consumerKey, String consumerSecret, String accessToken, String accessTokenSecret, BiConsumer<String, String> onTweetReceived) {
    final String DEFAULT_LANGUAGE = "en";

    Configuration conf = createTwitterConfiguration(consumerKey, consumerSecret, accessToken, accessTokenSecret);
    TweetListener tweetListener = new TweetListener(onTweetReceived);

    TwitterStreamFactory twitterStreamFactory = new TwitterStreamFactory(conf);
    TwitterStream twitterStreamSingleton = twitterStreamFactory.getInstance();
    twitterStreamSingleton.addListener(tweetListener);

    twitterStreamSingleton.sample(DEFAULT_LANGUAGE);

    return twitterStreamSingleton;
  }

  private static Configuration createTwitterConfiguration(String consumerKey, String consumerSecret, String accessToken, String accessTokenSecret) {
    return new ConfigurationBuilder()
        .setDebugEnabled(false)
        .setOAuthConsumerKey(consumerKey)
        .setOAuthConsumerSecret(consumerSecret)
        .setOAuthAccessToken(accessToken)
        .setOAuthAccessTokenSecret(accessTokenSecret)
        .build();
  }
}

// An implementation which implements twitter4j's StatusListener
class TweetListener implements StatusListener {
  public TweetListener(BiConsumer<String, String> onStatusReceived) {
    this.onStatusReceived = onStatusReceived;
  }

  public void onStatus(Status status) {
    onStatusReceived.accept(status.getUser().getScreenName(), status.getText());
  }
  public void onException(Exception ex) {
    ex.printStackTrace();
  }

  // a bunch of empty event handler implementations, we're not using them
  public void onDeletionNotice(StatusDeletionNotice statusDeletionNotice) {}
  public void onTrackLimitationNotice(int numberOfLimitedStatuses) {}
  public void onScrubGeo(long lat, long long_) {}
  public void onStallWarning(StallWarning stallWarning) {}

  private BiConsumer<String, String> onStatusReceived;
}