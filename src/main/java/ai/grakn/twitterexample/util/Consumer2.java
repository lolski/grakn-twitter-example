package ai.grakn.twitterexample.util;

@FunctionalInterface
public interface Consumer2<A, B> {
  public void apply(A a, B b);
}
