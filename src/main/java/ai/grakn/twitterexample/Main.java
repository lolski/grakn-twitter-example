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
// - are objects retrieved via a session rendered invalid when we close the session?

import ai.grakn.twitterexample.util.Consumer2;


public class Main {
  public static void main(String[] args) {
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

    // twitter credentials
    String consumerKey = "s81rBRQWHvGE1llHPYry7zSOm";
    String consumerSecret = "weQ8oZhBDZq9PjADlZJ897MAlxkXlNsUEH04jsqYPaLX4QCTKB";
    String accessToken = "1425775171-bnAiy4iF6y2SH1WMXPQmuwDm40zLTkBLk62qjxS";
    String accessTokenSecret = "XZ1SYSBOOFIH2jP6IKT3Je10tGtGKxstdPhuy2X4dHCUC";

    // callback
    Consumer2<String, String> onTweetReceived =
        (screenName, text) -> System.out.println("user: " + screenName + ", text: " + text);

    TweetStreamProcessor tweetStreamProcessor = new TweetStreamProcessor(consumerKey, consumerSecret, accessToken, accessTokenSecret, onTweetReceived);

    tweetStreamProcessor.run();
  }
}