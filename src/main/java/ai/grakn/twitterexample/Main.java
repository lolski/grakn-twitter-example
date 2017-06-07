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

import ai.grakn.Grakn;
import ai.grakn.GraknGraph;
import ai.grakn.GraknSession;
import ai.grakn.GraknTxType;
import ai.grakn.concept.Concept;
import ai.grakn.graql.MatchQuery;
import ai.grakn.graql.QueryBuilder;

import java.util.Map;

import static ai.grakn.graql.Graql.*;


public class Main {
  public static void main(String[] args) {
    GraknSession session = Grakn.session(Grakn.IN_MEMORY, "MyGraph");

    // write

    // select
    GraknGraph graphReader = session.open(GraknTxType.READ);

    QueryBuilder qb = graphReader.graql();

    MatchQuery query = qb.match(var("x").isa("person").has("firstname", "Bob")).limit(50);

    for (Map<String, Concept> result : query) {
      System.out.println(result.get("x").getId());
    }

    graphReader.close();
    System.out.println("hello");
  }
}
