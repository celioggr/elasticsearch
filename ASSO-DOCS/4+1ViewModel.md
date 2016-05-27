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
  
  
  ##Process View
  

The following diagram describes the execution flow of elasticsearch, its interactions and its dynamic functionalities that enable (major or minor) tasks that take place in the cluster.
Following the flow chart from the starting point we can see that after a node is initialized, major tasks such as discover other nodes in your cluster to talk to, or elect a master node take place. Then, this master node, will be responsable for delivery changes to the cluster. After these changes the master node should refresh the actions taken, update the cluster and wait for new events to come.
These mecanics are supported by the cluster.action environment represented at the left corner of the diagram.  

![alt tag](http://i.imgur.com/Rg4WAwm.png)

  
  ##Development View
  
  
  
  ##Physical View
  
  
  
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


