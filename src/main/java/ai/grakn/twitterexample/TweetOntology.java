package ai.grakn.twitterexample;

import ai.grakn.GraknGraph;
import ai.grakn.concept.*;

public class TweetOntology {
  public static void createOntology(GraknGraph graphWriter) {
    // resources
    ResourceType idType = graphWriter.putResourceType("identifier", ResourceType.DataType.STRING);
    ResourceType textType = graphWriter.putResourceType("text", ResourceType.DataType.STRING);
    ResourceType screenNameType = graphWriter.putResourceType("screen_name", ResourceType.DataType.STRING);

    // entities
    EntityType tweetType = graphWriter.putEntityType("tweet");
    EntityType userType = graphWriter.putEntityType("user");

    // roles
    RoleType writesType = graphWriter.putRoleType("writes");
    RoleType writtenType = graphWriter.putRoleType("written");

    // relations
    RelationType tweetedType = graphWriter.putRelationType("tweeted").relates(writesType).relates(writtenType);

    // resource and relation assignments
    tweetType.resource(idType);
    tweetType.resource(textType);
    userType.resource(screenNameType);
    userType.plays(writesType);
    tweetType.plays(writtenType);

  }

  public static Entity insertTweet(GraknGraph graknGraph, String tweet) {
    EntityType tweetEntityType = graknGraph.getEntityType("tweet");
    ResourceType tweetResouceType = graknGraph.getResourceType("text");

    Entity tweetEntity = tweetEntityType.addEntity();
    Resource tweetResouce = tweetResouceType.putResource(tweet);

    return tweetEntity.resource(tweetResouce);
  }
}


//    GraknSession session = Grakn.session(Grakn.IN_MEMORY, "MyGraph");
//
//    // ------------------------ write ------------------------
//    GraknGraph graphWriter = session.open(GraknTxType.WRITE);
//
//    // resources
//    ResourceType idType = graphWriter.putResourceType("identifier", ResourceType.DataType.STRING);
//    ResourceType textType = graphWriter.putResourceType("text", ResourceType.DataType.STRING);
//    ResourceType handleType = graphWriter.putResourceType("handle", ResourceType.DataType.STRING);
//
//    // entities
//    EntityType tweetType = graphWriter.putEntityType("tweet");
//    EntityType userType = graphWriter.putEntityType("user");
//
//    // roles
//    RoleType writesType = graphWriter.putRoleType("writes");
//    RoleType writtenType = graphWriter.putRoleType("written");
//
//    // relations
//    RelationType tweetedType = graphWriter.putRelationType("tweeted").relates(writesType).relates(writtenType);
//
//    // resource and relation assignments
//    tweetType.resource(idType);
//    tweetType.resource(textType);
//    userType.resource(handleType);
//    userType.plays(writesType);
//    tweetType.plays(writtenType);
//
//    // ------------------------ insert -----------------------
//    Entity user1 = userType.addEntity();
//    Resource user1Handle = handleType.putResource("User 1");
//    user1.resource(user1Handle);
//
//    graphWriter.commit();
//
//    // ------------------------ match ------------------------
//
//    GraknGraph graphReader = session.open(GraknTxType.READ);
//
//    QueryBuilder qb = graphReader.graql();
//
//    MatchQuery query = qb.match(var("x").isa("user")).limit(50);
//
//    ResourceType handleType2 = graphReader.getResourceType("handle");
//
//    for (Map<String, Concept> result : query) {
//      Entity resultUser = result.get("x").asEntity();
//      Resource<?> resultUserResources = resultUser.resources(handleType2).iterator().next();
//      System.out.println(resultUserResources.getValue());
//    }
//
//    graphReader.close();
