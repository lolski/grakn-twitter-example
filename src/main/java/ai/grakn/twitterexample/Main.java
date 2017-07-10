package ai.grakn.twitterexample;

import ai.grakn.Grakn;
import ai.grakn.GraknSession;

import java.util.Map;
import java.util.stream.Stream;

import static ai.grakn.twitterexample.GraknTweetOntologyHelper.*;
import static ai.grakn.twitterexample.AsyncTweetStreamProcessorHelper.*;

public class Main {
  // Twitter credentials
  private static final String consumerKey = "";
  private static final String consumerSecret = "";
  private static final String accessToken = "";
  private static final String accessTokenSecret = "";

  // Grakn settings
  private static final String graphImplementation = Grakn.IN_MEMORY;
  private static final String keyspace = "twitter-example";

  public static void main(String[] args) {
    try (GraknSession session = Grakn.session(graphImplementation, keyspace)) {
      withGraknGraph(session, graknGraph -> initTweetOntology(graknGraph)); // initialize ontology

      listenToTwitterStreamAsync(consumerKey, consumerSecret, accessToken, accessTokenSecret, (screenName, tweet) -> {
        withGraknGraph(session, graknGraph -> {
          insertUserTweet(graknGraph, screenName, tweet); // insert tweet
          Stream<Map.Entry<String, Long>> result = calculateTweetCountPerUser(graknGraph); // query
          prettyPrintQueryResult(result); // display
        });
      });
    }
  }

  public static void prettyPrintQueryResult(Stream<Map.Entry<String, Long>> result) {
    System.out.println("------");
    result.forEach(e -> System.out.println("-- user " + e.getKey() + " tweeted " + e.getValue() + " time(s)."));
    System.out.println("------");
  }
}