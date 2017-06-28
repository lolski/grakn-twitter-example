package ai.grakn.twitterexample;

import ai.grakn.Grakn;
import ai.grakn.GraknSession;

import java.util.Map;
import java.util.stream.Stream;

import static ai.grakn.twitterexample.GraknTweetOntologyHelper.*;
import static ai.grakn.twitterexample.AsyncTweetStreamProcessorHelper.*;

public class Main {
  // Twitter credentials
  private static final String consumerKey = "s81rBRQWHvGE1llHPYry7zSOm";
  private static final String consumerSecret = "weQ8oZhBDZq9PjADlZJ897MAlxkXlNsUEH04jsqYPaLX4QCTKB";
  private static final String accessToken = "1425775171-bnAiy4iF6y2SH1WMXPQmuwDm40zLTkBLk62qjxS";
  private static final String accessTokenSecret = "XZ1SYSBOOFIH2jP6IKT3Je10tGtGKxstdPhuy2X4dHCUC";

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