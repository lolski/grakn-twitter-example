package ai.grakn.twitterexample;

/*

Goal: demonstrate streaming data into Grakn, introduce interesting Grakn concepts to the user
- source candidate: public user tweets
- data volume: small (small enough that we can run the program in a single node with a mid level compute power)
  - streaming api vs rest api?
- questions:
  - two users who replies each other are close connections

 */

// TODO:
// - look at other example on project format
// - look at twitter streaming vs rest api

import ai.grakn.graql.QueryBuilder;

public class Main {
  public static void main(String[] args) {
    System.out.println("hello");
  }
}
