# Streaming Public Tweets
In this tutorial we will look at how to stream public tweet into Grakn's knowledge graph. The tutorial aims to demonstrate key concepts such as receiving, inserting and querying data. Upon the completion of this tutorial, we hope you will have learnt about the following:

- Defining a simple Grakn.ai ontology using the Java API
- Streaming public tweets into the application with the [Twitter4J](http://twitter4j.org/ "Twitter4J") library
- Inserting tweets into the knowledge graph using Grakn's Graph API
- And finally, performing simple queries using Grakn's Query Language (i.e., GraQL)

## Registering Your Own Twitter Application
As of today, you will need an API key in order to call practicaly every endpoint in the Twitter API. Therefore, you must own a Twitter application (or, register a new one) before proceeding further.

## Bootstraping A Skeleton Project
Let's bootstrap a new maven project by running this command on the command line:

```sh
echo 'hello'
```

Now that you have basic project structure and `pom.xml`in place, let's start customising them to our needs. As we will be using a few Java 8 feature (e.g., lambda function), make sure to add the following configuration:

```xml
<plugin>
    <groupId>org.apache.maven.plugins</groupId>
    <artifactId>maven-compiler-plugin</artifactId>
    <configuration>
        <source>1.8</source>
        <target>1.8</target>
    </configuration>
</plugin>
```

Then make sure you have all the required dependencies, i.e., `grakn-graph`, `grakn-graql`, `twitter4j-core`, and `twitter4j-stream`:

```xml
<dependencies>
    <dependency>
        <groupId>ai.grakn</groupId>
        <artifactId>grakn-graph</artifactId>
        <version>0.13.0</version>
    </dependency>
    <dependency>
        <groupId>ai.grakn</groupId>
        <artifactId>grakn-graql</artifactId>
        <version>0.13.0</version>
    </dependency>

    <dependency>
        <groupId>org.twitter4j</groupId>
        <artifactId>twitter4j-core</artifactId>
        <version>4.0.6</version>
    </dependency>

    <dependency>
        <groupId>org.twitter4j</groupId>
        <artifactId>twitter4j-stream</artifactId>
        <version>4.0.6</version>
    </dependency>
</dependencies>
```

For reference purpose, you always look at the complete `pom.xml` definition [here](google.com).

## A Few Important Concepts

GraknSession

GraknGraph

keyspace

## Defining The Ontology

*For those unfamiliar with the term, an ontology can be thought of as the "database schema" of knowledge graphs.* 

Now let's think about the ontology. As we are mainly interested in both the **tweet** itself as well as **who posted the tweet**, let us capture these concepts by defining two entities: **user** and **tweet**.

The user entity will hold the user's actual username in a resource called **screen_name** while the tweet entity will contain the tweet content in another resource called **text**.

After we're done defining We will also define a relation between a **user** a **tweet**. 

## Streaming Data From Twitter

Now that we're done with the ontology setup code, let's move on by coding the twitter listener. Create a class `AsyncTweetStreamProcessorHelper`

## Inserting Tweets Into The Knowledge Graph
### Insert Tweet
### Upsert User
### Relating The Tweet To The User
## Performing Simple Queries
### Defining The Query
### Performing The Query
## Running The Application
## Conclusion