package ai.grakn.twitterexample;

import ai.grakn.GraknGraph;
import ai.grakn.GraknSession;
import ai.grakn.GraknTxType;
import ai.grakn.concept.*;

import java.util.function.Consumer;

public class GraknTweetOntologyHelper {
  public static void initTweetOntology(GraknGraph graphWriter) {
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

  public static Entity insertTweet(GraknGraph graphWriter, String tweet) {
    EntityType tweetEntityType = graphWriter.getEntityType("tweet");
    ResourceType tweetResouceType = graphWriter.getResourceType("text");

    Entity tweetEntity = tweetEntityType.addEntity();
    Resource tweetResouce = tweetResouceType.putResource(tweet);

    return tweetEntity.resource(tweetResouce);
  }

  public static void withAutoCommit(GraknSession session, Consumer<GraknGraph> fn) {
    GraknGraph graphWriter = session.open(GraknTxType.WRITE);
    fn.accept(graphWriter);
    graphWriter.commit();
  }
}