#Overview of Elasticsearch

##Abstract

Elasticsearch is an open-source, search engine designed for horizontal scalability, reliability, and easy management which can power extremely fast searches in your data. Basically Elasticsearch is a database server based on Lucene, and written in Java, that takes data in and stores it in a sophisticated format. Its main protocol is implemented with HTTP/JSON which is a lightweight format that is used for data interchanging.

This chapter hopefully will provide a concise well-structured overview of ElasticSearch Software Architecture. We hope that the insights presented here will make it easier for people to joint efforts and contribute to the project providing the core team of ElasticSearch an outsider’s feedback.

The project has been structured from multiple viewpoints having regard to the 4+1 view model.

##Table of Contents

1. [Introduction](https://github.com/celioggr/elasticsearch/blob/master/ASSO-DOCS/overview.md#introduction)

2. [What is ElasticSearch](https://github.com/celioggr/elasticsearch/blob/master/ASSO-DOCS/overview.md#what-is-elasticsearch)

3. [What for](https://github.com/celioggr/elasticsearch/blob/master/ASSO-DOCS/overview.md#what-for)

4. [Supported Operating Systems](https://github.com/celioggr/elasticsearch/blob/master/ASSO-DOCS/overview.md#supported-operating-systems)

5. [Basic Concepts](https://github.com/celioggr/elasticsearch/blob/master/ASSO-DOCS/overview.md#basic-concepts)

	* Field
	
	* Document
	
	* Cluster

	* Node

	* Index

	* Shards

6. [A top-down approach on ElasticSearch](https://github.com/celioggr/elasticsearch/blob/master/ASSO-DOCS/overview.md#a-top-down-approach-on-elasticsearch)
	* Why ElasticSearch?
	
	* From the Top...
	
	* ...To the bottom

7. [Lucene's paper on ElasticSearch](https://github.com/celioggr/elasticsearch/blob/master/ASSO-DOCS/overview.md#lucenes-paper-on-elasticsearch)

8. [Used technology](https://github.com/celioggr/elasticsearch/blob/master/ASSO-DOCS/overview.md#used-technology)

9. [Further tasks](https://github.com/celioggr/elasticsearch/blob/master/ASSO-DOCS/overview.md#further-tasks)

10. [Feedback](https://github.com/celioggr/elasticsearch/blob/master/ASSO-DOCS/overview.md#feedback)

11. [Contact Us!](https://github.com/celioggr/elasticsearch/blob/master/ASSO-DOCS/overview.md#contact-us)

##Introduction

Elasticsearch is an open-source, search engine which can power extremely fast searches. It is currently a very active open source project, which handles pull requests daily. This makes Elasticsearch an excellent candidate for a Software Architecture analysis.
As a group of students from the [Faculty of Sciences of Porto, Portugal (FCUP)](https://sigarra.up.pt/fcup/en/WEB_PAGE.INICIAL) attending the Software Architecture course we hope that this document allow you better understanding the concept behind ElasticSearch. During a period of 5 months we'll try to understand and describe ElasticSearch software architecture and hopefully make some worth contributions to the project.

We will first describe more detailed what Elasticsearch is, how and for what it can be used. Then we will look at the architecture itself, in terms of the logical, process, development and physical views.
Finally, we bring all the views together to see how robust the architecture of ElasticSearch is, taking in great consideration the System consistency and validity.


##What is ElasticSearch?

Elasticsearch is an independent database server, written in Java, that stores data in a sophisticated format optimized for language based searches. Working with it is convenient as its main protocol is implemented with HTTP/JSON. Elasticsearch is also easily scalable, supporting clustering and leader election out of the box.


##What for?
 
Elasticsearch is a tool for querying written words. It can perform some other smart tasks, but at its core it’s made for wading through text, returning text similar to a given query and/or statistical analyses of a corpus of text. Some of its purposes are:

* Finding a product database, by the description;
* Finding similar text in a number of websites;
* Searching through posts in a blog;
* Auto-completing a search box based on partially typed words ("Did you mean" in Google was implemented using ElasticSearch).

However, while Elasticsearch is great at solving the aforementioned problems, it’s not the best choice for others. It’s especially bad at solving problems for which relational databases are optimized. Problems such as:

* Calculating how many items are left in the inventory; 
* Figuring out the sum of all line-items on all the invoices sent out in a given month;
* Executing two operations transactionally with rollback support;
* Creating records that are guaranteed to be unique across multiple given terms, for instance a phone number and extension.

Elasticsearch is generally fantastic at providing approximate answers from data, such as scoring the results by quality. While Elasticsearch can perform exact matching and statistical calculations, its primary task of search is an inherently approximate task. Finding approximate answers is a property that separates Elasticsearch from more traditional databases. That being said, traditional relational databases excel at precision and data integrity, for which elasticsearch and Lucene have few provisions.


##Supported Operating Systems

Elasticsearch is a standalone Java app, and can be easily started from the command line. The platform of ElasticSearch supports UNIX/Linux, Mac OSX and Microsoft Windows, but the OS must have Java 1.7 or a newer version.


##Basic Concepts


* Field


The smallest individual unit of data in elasticsearch is a field, which has a defined type and has one or many values of that type. A field contains a single piece of data, like the number 42 or the string "Hello, World!", or a single list of data of the same type, such as the array [5, 6, 7, 8].

* Document


Documents are collections of fields, and comprise the base unit of storage in elasticsearch; something like a row in a traditional RDBMS. The reason a document is considered the base unit of storage is because, peculiar to Lucene, all field updates fully rewrite a given document to storage (while preserving unmodified fields). So, while from an API perspective the field is the smallest single unit, the document is the smallest unit from a storage perspective.

* Cluster


A cluster is a group of nodes (servers) that together holds your entire data and provides indexing and search capabilities across all nodes.  A cluster is identified by a unique name.

* Node


A node is a single server that is part of your cluster, stores your data, and participates in the cluster’s indexing and search capabilities. A node is identified by a name just like clusters. Node names are important if you want to identify which servers in your network correspond to which nodes in your cluster, for example.

* Index 


An index is a collection of documents that have similar characteristics (identified by a name). It is a place to store data. In reality, an index is just a logical namespace that points to one or more physical shards and groups them. Applications talk to the index.

* Shards


We can think about shards as containers for data.
   
Shards allow you to "split" the weight on your shoulders. Suppose that you have a tremendous amount of data, you might want to split the weight with others. When you create an index, you can simply define the number of shards that you want. Each shard is in itself a fully-functional and independent "index" that can be hosted on any node (server) in the cluster. A Shard is a low-level worker unit that hold a slice of all the data in the index.

Your documents are stored and indexed in shards and these shards are allocated to nodes in your cluster. As your cluster grows or shrinks, Elasticsearch will automatically migrate shards between nodes so that the cluster remains balanced.

A shard can be either a primary shard or a replica shard. When you index a document, it is indexed first on the primary shard, then on all replicas of the primary shard. So each document is stored in a single primary shard. On the other hand a replica shard is just a copy of a primary shard. This redundancy of data protects documents from hardware failure (i.e. a replica shard can be promoted to a primary shard if the primary fails) and increases performance (i.e. get and search requests can be handled by primary or replica shards).  

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


##Lucene's paper on ElasticSearch

Lucene is old in internet years, dating back to 1999. It’s also exceedingly popular and proven. Lucene is used by untold numbers of companies, running the gamut from huge corporations such as Twitter, to small startups. Lucene is proven, tested, and is widely considered best-of-breed in open-source search software.

The core of Elasticsearch’s intelligent search engine is largely another software project: Lucene. It is perhaps easiest to understand Elasticsearch as a piece of infrastructure built around Lucene’s Java libraries. Everything in Elasticsearch that pertains to the actual algorithms for matching text and storing optimized indexes of searchable terms is implemented by Lucene. Elasticsearch itself provides a more useable and concise API, scalability, and operational tools on top of Lucene’s search implementation.


##Used technology

The primary data-format elasticsearch uses is JSON. Given that, all documents must be valid JSON values. Each document in elasticsearch must conform to a user-defined type mapping, analogous to a database schema. A type’s mapping both defines the types of its fields (say integer, string, etc.) and the way in which those properties are indexed (this gets complex, and will be covered later). Types also scope IDs within an index. Multiple documents in an index may have identical IDs as long as they are of different types. Types are defined with the Mapping API, which associates type names to property definitions.


##Further tasks

- Complete and submit more information to this document as we learn more about the Software Architecture behind ElasticSearch;


##Feedback

 We value your feedback on any of the material here publish by us. 
 For your feedback, you can:

 * Contact up201303171@fc.up.pt
 * Contact up201305725@fc.up.pt


##Contact Us

Here are the people who take some time writting this:

* Célio Rodrigues @github/celioggr up201303171@fc.up.pt
* Cláudia Correia (...)
* Fábio Teixeira  @github/fabioteixeira2up up201305725@fc.up.pt
* Patricia Silva  @github/patriciareiasilva up201203528@fc.up.pt
