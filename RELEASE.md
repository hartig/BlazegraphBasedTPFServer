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
 * Setup your environment for Sonatype [See here](http://central.sonatype.org/pages/apache-maven.html#other-prerequisites).
 * Make sure to set the blazegraph version to the latest release in Maven Central.
 * Add and commit all changes.   
 * `mvn -Pmaven-central release:clean release:prepare` [See here](http://central.sonatype.org/pages/apache-maven.html#performing-a-release-deployment-with-the-maven-release-plugin).  You will be prompted to enter the next version number, which should be in the form X.Y.X, i.e. 0.1.1.  It's OK to accept the defaults.
 * `mvn -Pmaven-central release:perform` [See here](http://central.sonatype.org/pages/apache-maven.html#performing-a-release-deployment-with-the-maven-release-plugin).
 * Checkout the release tag, `git checkout BlazegraphBasedTPFServer-0.1.0`, and publish the javadocs:  `./bin/publishDocs.sh`.
 * Reverse merge into master and commit the changes:  `git checkout master`, `git merge BlazegraphBasedTPFServer-0.1.0`; `git push origin master`
 * Got to [Github](https://github.com/blazegraph/BlazegraphBasedTPFServer/releases) and update the release tag.

