package ai.grakn.twitterexample;

/*

Goal: demonstrate streaming data into Grakn, introduce interesting Grakn concepts to the user
- source candidate: public user tweets
- data volume: small (small enough that we can runAsync the program in a single node with a mid level compute power)
  - streaming api vs rest api?
- questions:
  - two users who replies each other are close connections

 */

// TODO:
// - are objects retrieved via a session rendered invalid when we close the session?


/*
 * tutorial

- pre-requisites
  - need to have twitter account

- tweet streaming

- define ontology

- insert data

- query

- advanced: moving away from grakn in memory

- advanced: bulk streaming
 */

import ai.grakn.Grakn;
import ai.grakn.GraknSession;
import ai.grakn.concept.Entity;

import java.util.function.BiConsumer;

public class Main {
  // twitter credentials
  private static final String consumerKey = "s81rBRQWHvGE1llHPYry7zSOm";
  private static final String consumerSecret = "weQ8oZhBDZq9PjADlZJ897MAlxkXlNsUEH04jsqYPaLX4QCTKB";
  private static final String accessToken = "1425775171-bnAiy4iF6y2SH1WMXPQmuwDm40zLTkBLk62qjxS";
  private static final String accessTokenSecret = "XZ1SYSBOOFIH2jP6IKT3Je10tGtGKxstdPhuy2X4dHCUC";

  // grakn settings
  private static final String graphImplementation = Grakn.IN_MEMORY;
  private static final String keyspace = "twitter-example";

  public static void main(String[] args) {
    try (GraknSession session = Grakn.session(graphImplementation, keyspace)) {
      BiConsumer<String, String> onTweetReceived = (screenName, tweet) -> {
        System.out.println("user: " + screenName + ", text: " + tweet);

        GraknTweetOntologyHelper.withAutoCommit(session, graknGraph -> {
          Entity tweetEntity = GraknTweetOntologyHelper.insertTweet(graknGraph, tweet);
          Entity userEntity = GraknTweetOntologyHelper.insertUserIfNotExist(graknGraph, screenName);
          GraknTweetOntologyHelper.insertTweetedRelation(graknGraph, userEntity, tweetEntity);
        });
      };

      AsyncTweetStreamProcessor tweetStreamProcessor = new AsyncTweetStreamProcessor(
          consumerKey, consumerSecret, accessToken, accessTokenSecret, onTweetReceived);

      GraknTweetOntologyHelper.withAutoCommit(session, GraknTweetOntologyHelper::initTweetOntology); // create ontology

      tweetStreamProcessor.runAsync(); // runs on a separate thread
    }
  }
}