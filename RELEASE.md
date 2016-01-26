# Blazegraph-Based TPF Server
The Blazegraph-Based TPF Server is a [Linked Data Fragment (LDF)](http://linkeddatafragments.org/) server that provides a [Triple Pattern Fragment (TPF)](http://linkeddatafragments.org/in-depth/#tpf) interface using the [Blazegraph](https://www.blazegraph.com/) graph database as backend.


##Maven Dependency
The Blazegraph-based TPF Server is available on Maven Central.

```
   <dependency>
     <groupId>com.blazegraph</groupId>
     <artifactId>BlazegraphBasedTPFServer</artifactId>
     <version>0.1.0-SNAPSHOT</version>
   </dependency>
```

##Javadocs
[Javadocs](https://blazegraph.github.io/BlazegraphBasedTPFServer/apidocs/)

##Building a release

 * Make sure the pom is at the latest version-SNAPSHOT, i.e. 0.1.0-SNAPSHOT
 * Make sure to set the blazegraph version to the latest release in Maven Central.
 * Add and commit all changes.   
 * `mvn release:clean release:prepare` [See](http://central.sonatype.org/pages/apache-maven.html#performing-a-release-deployment-with-the-maven-release-plugin).  You will be prompted to enter the next version number, which should be in the form X.Y.X, i.e. 0.2.0.
 * `mvn release:perform` [See](http://central.sonatype.org/pages/apache-maven.html#performing-a-release-deployment-with-the-maven-release-plugin)
 * Checkout the release tag, `git checkout 0.1.0`, and publish the javadocs:  `./publishJavadoc.sh`.

