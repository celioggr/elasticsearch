#Table of contents

1. [Introduction](https://github.com/celioggr/elasticsearch/blob/master/ASSO-DOCS/4%2B1ViewModel.md#introduction)
2. [Logical View](https://github.com/celioggr/elasticsearch/blob/master/ASSO-DOCS/4%2B1ViewModel.md#logical-view)
3. [Process View](https://github.com/celioggr/elasticsearch/blob/master/ASSO-DOCS/4%2B1ViewModel.md#process-view)
4. [Development View](https://github.com/celioggr/elasticsearch/blob/master/ASSO-DOCS/4%2B1ViewModel.md#development-view) 
5. [Physical View](https://github.com/celioggr/elasticsearch/blob/master/ASSO-DOCS/4%2B1ViewModel.md#physical-view)

##Introduction

This document aims to represent the aspects related with the software architecture of elasticsearch project, according to the 4+1 Architecture view model. 
The views are used to describe the system from the viewpoint of different stakeholders and should provide a fundamental organization perspective of the system, its components and relationships between them.
We will be presenting four components:

  * [Logical View](https://github.com/celioggr/elasticsearch/blob/master/ASSO-DOCS/4%2B1ViewModel.md#logical-view)

  * [Process View](https://github.com/celioggr/elasticsearch/blob/master/ASSO-DOCS/4%2B1ViewModel.md#process-view)

  * [Development View](https://github.com/celioggr/elasticsearch/blob/master/ASSO-DOCS/4%2B1ViewModel.md#development-view)

  * [Physical View](https://github.com/celioggr/elasticsearch/blob/master/ASSO-DOCS/4%2B1ViewModel.md#physical-view)
  
  
##Logical View
The following diagram describes the packages and dependencies of the logical system.
Here we present the main features that elastic search provides to its user (Search Data,Index Data, Analyse Data) which are 
implemented by those three packages at the right side of the diagram. These main features take its actions when the user 
communicates throw the REST API that elasticsearch provides which is a simple way for users to make HTTP REQUESTS to interact with cluster. 
The packages are described as follows:

 * action: 
  * Deals with all the actions that take place in the cluster such as index, get or seach for data.

 * plugins: 
  * Allows you to manage the plugins available for elasticsearch such as kibana, marvel or sense.

 * discovery: 
  * starts the event listeners in order to discover and state other nodes and electing a master of the cluster that raises cluster state change events. 
  * Publish the changes to the cluster from the master. 




![alt tag](http://i.imgur.com/K8ndXiL.jpg)
  
##Process View
  

The following diagram describes the execution flow of elasticsearch, its interactions and its dynamic functionalities that enable (major or minor) tasks that take place in the cluster.
Following the flow chart from the starting point we can see that after a node is initialized, major tasks such as discover other nodes in your cluster to talk to, or elect a master node take place. Then, this master node, will be responsable for delivery changes to the cluster. After these changes the master node should refresh the actions taken, update the cluster and wait for new events to come.
These mecanics are supported by the cluster.action environment represented at the left corner of the diagram.  

![alt tag](http://i.imgur.com/Rg4WAwm.png)

  
##Development View
  
The development view illustrates the system from a programmer's perspective. Also known as implementation view, the following diagram represents the implementation views. The ElasticSearch is an alternative to traditional databases, as it stores information for each client, and then each client has multiple copies of data. The fact that it is decentralized and that it stores information by indexing is one of the reasons why the number of followers of the technology is constantly increasing. The node.js waits for the customer information to be able to perform operations on the data. Then it just contacts a host in order to do the search from the indexes.

![alt tag](http://i.imgur.com/zovqy7F.png)  
  
## Physical View
  
  
  
The physical view aims to consider aspects like topology and connections between hardware and Software. This view is also known as Deployment View.
After downloading and extracting the package from elasticsearch website running it should be pretty straightforward if you already have a Java Runtime Environment installed (Java 7 or later). This is the only requirement that you need (for playing with Elasticsearch on your PC or on a small cluster of machines) if you just want execute it, or a JDK (which includes the JRE). When it comes to deploy Elasticsearch to production a few recommendations needs to be consider, such as: you should garantee at least 16 to 32 GB of memory (most common cases), as many cores as you can fit in the CPU and some SSDs would be a very nice deal too.
To run elasticsearch just type the following commands:

On linux or OS X


```
cd elasticsearch-<version>
./bin/elasticsearch
```


On Windows


```
cd elasticsearch-<version>
bin\elasticsearch.bat
```

This commands will trigger a foreground elasticsearch process in the console you're ready to go.


![alt tag](http://i.imgur.com/lpnVJsG.png)


