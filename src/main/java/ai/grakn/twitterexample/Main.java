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
// - look at twitter streaming vs rest api

import ai.grakn.Grakn;
import ai.grakn.GraknGraph;
import ai.grakn.GraknSession;
import ai.grakn.GraknTxType;
import ai.grakn.concept.*;
import ai.grakn.graql.MatchQuery;
import ai.grakn.graql.QueryBuilder;

import java.util.Map;

import static ai.grakn.graql.Graql.*;


public class Main {
  public static void main(String[] args) {
    GraknSession session = Grakn.session(Grakn.IN_MEMORY, "MyGraph");

    createTwitterOntologies(session);

    // ------------------------ insert -----------------------

    // ------------------------ match ------------------------
    GraknGraph graphReader = session.open(GraknTxType.READ);

    QueryBuilder qb = graphReader.graql();

    MatchQuery query = qb.match(var("x").isa("user")).limit(50);

    for (Map<String, Concept> result : query) {
      System.out.println(result.get("x").getId());
    }

    graphReader.close();
    System.out.println("hello");
  }

  public static void createTwitterOntologies(GraknSession session) {
    // ------------------------ write ------------------------
    GraknGraph graphWriter = session.open(GraknTxType.WRITE);

    // resources
    ResourceType id = graphWriter.putResourceType("identifier", ResourceType.DataType.STRING);
    ResourceType text = graphWriter.putResourceType("text", ResourceType.DataType.STRING);
    ResourceType handle = graphWriter.putResourceType("handle", ResourceType.DataType.STRING);

    // entities
    EntityType tweet = graphWriter.putEntityType("tweet");
    EntityType user = graphWriter.putEntityType("user");

    // roles
    RoleType writes = graphWriter.putRoleType("writes");
    RoleType written = graphWriter.putRoleType("written");

    // relations
    RelationType tweeted = graphWriter.putRelationType("tweeted").relates(writes).relates(written);

    // resource and relation assignments
    tweet.resource(id);
    tweet.resource(text);
    user.resource(handle);
    user.plays(writes);
    tweet.plays(written);

    graphWriter.commit();
  }
}
