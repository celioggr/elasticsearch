#Overview of Elasticsearch

##Abstract

Elasticsearch is an open-source, search engine designed for horizontal scalability, reliability, and easy management which can power extremely fast searches in your data. Basically Elasticsearch is a database server based on Lucene, and written in Java, that takes data in and stores it in a sophisticated format. Its main protocol is implemented with HTTP/JSON which is a lightweight format that is used for data interchanging.

This chapter hopefully will provide a concise well-structured overview of ElasticSearch Software Architecture. We hope that the insights presented here will make it easier for people to joint efforts and contribute to the project providing the core team of ElasticSearch an outsider’s feedback.

The project has been structured from multiple viewpoints having regard to the 4+1 view model.

##Table of Contents

1. [Introduction](https://github.com/celioggr/elasticsearch/blob/master/ASSO-DOCS/overview.md#introduction)

2. [What is ElasticSearch](https://github.com/celioggr/elasticsearch/blob/master/ASSO-DOCS/overview.md#what-is-elasticsearch)

3. [What for](https://github.com/celioggr/elasticsearch/blob/master/ASSO-DOCS/overview.md#what-for)

4. [Basic Concepts](https://github.com/celioggr/elasticsearch/blob/master/ASSO-DOCS/overview.md#basic-concepts)

	* Node

	* Cluster

	* Index

	* Shards

5. [A top-down approach on ElasticSearch](https://github.com/celioggr/elasticsearch/blob/master/ASSO-DOCS/overview.md#a-top-down-approach-on-elasticsearch)
	* Why ElasticSearch?
	
	* From the Top...
	
	* ...To the bottom

6. [Further tasks](https://github.com/celioggr/elasticsearch/blob/master/ASSO-DOCS/overview.md#further-tasks)

7. [Feedback](https://github.com/celioggr/elasticsearch/blob/master/ASSO-DOCS/overview.md#feedback)

8. [Contact Us!](https://github.com/celioggr/elasticsearch/blob/master/ASSO-DOCS/overview.md#contact-us)

##Introduction

Elasticsearch is an open-source, search engine which can power extremely fast searches. It is currently a very active open source project, which handles pull requests daily. This makes Elasticsearch an excellent candidate for a Software Architecture analysis.
As a group of students from the [Faculty of Sciences of Porto, Portugal (FCUP)](https://sigarra.up.pt/fcup/en/WEB_PAGE.INICIAL) attending the Software Architecture course we hope that this document allow you better understanding the concept behind ElasticSearch. During a period of 5 months we'll try to understand and describe ElasticSearch software architecture and hopefully make some worth contributions to the project.

We will first describe more detailed what Elasticsearch is, how and for what it can be used. Then we will look at the architecture itself, in terms of the logical, process, development and physical views.
Finally, we bring all the views together to see how robust the architecture of ElasticSearch is, taking in great consideration the System consistency and validity.


##What is ElasticSearch?

 Elasticsearch is an independent database server, written in Java, that stores data in a sophisticated format optimized for language based searches. Working with it is convenient as its main protocol is implemented with HTTP/JSON. Elasticsearch is also easily scalable, supporting clustering and leader election out of the box.


##What for?
 
 Elasticsearch is a tool for querying written words. It can perform some other smart tasks, but at its core it’s made for wading through text, returning text similar to a given query and/or statistical analyses of a corpus of text. Some of its purposes are:

* Find a product database, by the description;
* Find similar text in a number of websites;
* Search through posts in a blog;
* Phrase suggester or autocomplete ("Did you mean" in Google was implemented using ElasticSearch);
* Among others...


##Basic Concepts

* Cluster


A cluster is a group of nodes (servers) that together holds your entire data and provides indexing and search capabilities across all nodes.  A cluster is identified by a unique name.

* Node


A node is a single server that is part of your cluster, stores your data, and participates in the cluster’s indexing and search capabilities. A node is identified by a name just like clusters.Node names are important if you want to identify which servers in your network correspond to which nodes in your cluster, for example.

* Index 


An index is a collection of documents that have similar characteristics. An index is identified by a name too.

* Shards


Shards allow you to "split" the weight on your shoulders. Suppose that you have a tremendous amount of data, you might want to split the weight with others. Well shards allows this! When you create an index, you can simply define the number of shards that you want. Each shard is in itself a fully-functional and independent "index" that can be hosted on any node (server) in the cluster.


##A top-down approach on ElasticSearch

######Why ElasticSearch?
* ElasticSearch is an alternative to most common DataBases management systems like Mongo DB, Raven DB among others. 

* It's incredibly fast when it comes to searching and highly escalable.

* It uses Lucene's stable, proven technology.

* Denormalized data which improves response time of searches.



######From the Top...


Elasticsearch index is an abstraction on top of a collection of Lucene indexes.


To look at how things get together it's easier to start from the top, the user's perspective in other words.
What really happens when a user send a search request to Elasticsearch? 


Suppose a cluster topology... When you send a request to a node in Elasticsearch, that node becomes the coordinator of that request. From this moment on this node will decide which routes requests to the shards will be taken, how to merge different nodes responses and eventually return the search to the user when the request is done. 


######...To the bottom

Here we’ll look at what happens at the shard level, how a search is converted in Lucene's equivalents and when a search is sucessfull.

As soon as we get a coordinator node it consults the mappings to determine which shard the request must go. The request is sent to the primary of that shard. The selected shard writes the operation to its translog, and relays the request to the replicas(slaves). When a sufficient number of replicas have acknowledged, the primary returns success.



##Further tasks


- Complete and submit more information to this document as we learn more about the Software Architecture behind ElasticSearch;


##Feedback

 We value your feedback on any of the material here publish by us. 
 For your feedback, you can:

 * Contact up201303171@fc.up.pt


##Contact Us

Here are the people who take some time writting this:

* Célio Rodrigues @github/celioggr up201303171@fc.up.pt
* Cláudia Correia (...)
* Fábio Teixeira (...)
* Patricia Silva (...)


