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

##Extending the TPF Server

```
git clone https://github.com/blazegraph/BlazegraphBasedTPFServer.git
cd BlazegraphBasedTPFServer
mvn package
```

## Configuration
To run an instance of the Blazegraph-Based TPF Server you need a configuration file. This file, usually called `config.json`, is a JSON document in which you specify the dataset(s) to which your server instance provides a TPF interface. The Blazegraph-Based TPF Server comes with an example of such a file, called `config-example.json`. Hence, you may copy this file to `config.json` and adjust it to your needs.

The configuration file contains a section for _"datasources"_ to which you add every dataset as a separate entry. The _"settings"_ section of such an entry for Blazegraph-based data sources contains the typical Blazegraph configuration options (which, in a standard Blazegraph setup, would be given in a properties file). In particular, for persistent, disk-based datasets (Blazegraph buffer mode "DiskRW" or "DiskWORM") the entry _"com.bigdata.journal.AbstractJournal.file"_ has to refer to the corresponding Blazegraph journal file; for in-memory datasets (buffer mode "MemStore") the entry _"file"_ has to refer to an RDF document. In the latter case the data from the document will be imported when you start your Blazegraph-Based TPF Server; for big datasets this import may take a bit of time (hence, your server is not available immediately) and it may require a significant amount of main memory.

## Running
The server can be started as follows:

```
java -server -Xmx4g -jar target/BlazegraphBasedTPFServer.jar config.json
```

You may have to increase the `-Xmx` parameter if you use an in-memory dataset.

## License
The Blazegraph-Based TPF Server is written by [Olaf Hartig](http://olafhartig.de/) and released under the [Apache License, Version 2.0](http://www.apache.org/licenses/).
