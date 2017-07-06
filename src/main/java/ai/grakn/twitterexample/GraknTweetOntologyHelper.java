package ai.grakn.twitterexample;

import ai.grakn.GraknGraph;
import ai.grakn.GraknSession;
import ai.grakn.GraknTxType;
import ai.grakn.concept.*;
import ai.grakn.graql.AggregateQuery;
import ai.grakn.graql.MatchQuery;
import ai.grakn.graql.QueryBuilder;
import ai.grakn.graql.admin.Answer;
import ai.grakn.graql.analytics.CountQuery;

import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Stream;

import static ai.grakn.graql.Graql.count;
import static ai.grakn.graql.Graql.group;
import static ai.grakn.graql.Graql.var;

public class GraknTweetOntologyHelper {
  public static void initTweetOntology(GraknGraph graknGraph) {
    // resources
    ResourceType idType = graknGraph.putResourceType("identifier", ResourceType.DataType.STRING);
    ResourceType textType = graknGraph.putResourceType("text", ResourceType.DataType.STRING);
    ResourceType screenNameType = graknGraph.putResourceType("screen_name", ResourceType.DataType.STRING);

    // entities
    EntityType tweetType = graknGraph.putEntityType("tweet");
    EntityType userType = graknGraph.putEntityType("user");

    // roles
    RoleType writesType = graknGraph.putRoleType("writes");
    RoleType writtenByType = graknGraph.putRoleType("written_by");

    // relations
    RelationType tweetedType = graknGraph.putRelationType("tweeted").relates(writesType).relates(writtenByType);

    // resource and relation assignments
    tweetType.resource(idType);
    tweetType.resource(textType);
    userType.resource(screenNameType);
    userType.plays(writesType);
    tweetType.plays(writtenByType);
  }

public static Relation insertUserTweet(GraknGraph graknGraph, String screenName, String tweet) {
  Entity tweetEntity = insertTweet(graknGraph, tweet);
  Entity userEntity = insertUserIfNotExist(graknGraph, screenName);
  return insertTweetedRelation(graknGraph, userEntity, tweetEntity);
}

  public static Optional<Entity> findUser(QueryBuilder queryBuilder, String user) {
    MatchQuery findUser = queryBuilder.match(var("x").isa("user").has("screen_name", user)).limit(1);
    Iterator<Concept> concepts = findUser.get("x").iterator();
    if (concepts.hasNext()) {
      Entity entity = concepts.next().asEntity();
      return Optional.of(entity);
    }
    else return Optional.empty();
  }

  public static Entity insertUser(GraknGraph graknGraph, String user) {
    EntityType userEntityType = graknGraph.getEntityType("user");
    ResourceType userResourceType = graknGraph.getResourceType("screen_name");
    Entity userEntity = userEntityType.addEntity();
    Resource userResource = userResourceType.putResource(user);
    return userEntity.resource(userResource);
  }

  public static Entity insertUserIfNotExist(GraknGraph graknGraph, String screenName) {
    QueryBuilder qb = graknGraph.graql();
    return findUser(qb, screenName).orElse(insertUser(graknGraph, screenName));
  }

  public static Entity insertTweet(GraknGraph graknGraph, String tweet) {
    EntityType tweetEntityType = graknGraph.getEntityType("tweet");
    ResourceType tweetResouceType = graknGraph.getResourceType("text");

    Entity tweetEntity = tweetEntityType.addEntity();
    Resource tweetResource = tweetResouceType.putResource(tweet);

    return tweetEntity.resource(tweetResource);
  }

  public static Relation insertTweetedRelation(GraknGraph graknGraph, Entity user, Entity tweet) {
    RelationType tweetedType = graknGraph.getRelationType("tweeted");
    RoleType writesType = graknGraph.getRoleType("writes");
    RoleType writtenByType = graknGraph.getRoleType("written_by");

    Relation tweetedRelation = tweetedType.addRelation()
        .addRolePlayer(writesType, user)
        .addRolePlayer(writtenByType, tweet);

    return tweetedRelation;
  }

  public static Stream<Map.Entry<String, Long>> calculateTweetCountPerUser(GraknGraph graknGraph) {
    // build query
    QueryBuilder qb = graknGraph.graql();
    AggregateQuery q = qb.match(
        var("user").isa("user"),
        var("tweet").isa("tweet"),
        var().rel("writes", "user").rel("written_by", "tweet").isa("tweeted")
        ).aggregate(group("user", count()));

    // execute query
    Map<Concept, Long> result = ((Map<Concept, Long>) q.execute());

    // map Map<Concept, Long> into Stream<Map.Entry<String, Long>> before returning
    ResourceType screenNameResourceType = graknGraph.getResourceType("screen_name");

    Stream<Map.Entry<String, Long>> mapped = result.entrySet().stream().map(entry -> {
      Concept key = entry.getKey();
      Long value = entry.getValue();
      String screenName = (String) key.asEntity().resources(screenNameResourceType).iterator().next().getValue();
      return new HashMap.SimpleImmutableEntry<>(screenName, value);
    });

    return mapped;
  }

  // TODO: properly handle exception
public static void withGraknGraph(GraknSession session, Consumer<GraknGraph> fn) {
  GraknGraph graphWriter = session.open(GraknTxType.WRITE);
  fn.accept(graphWriter);
  graphWriter.commit();
}
}