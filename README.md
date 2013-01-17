MDB-Two-Servers-Example
=======================

An application example that deploys a message driven bean on two servers. There are two main modules:

1. An application client that is deployed on the local server. It uses two connection factories, one ordinary one and one that is configured to communicate with the remote server, to create two publishers and two subscribers and to publish and to consume messages.

2. A message-driven bean that is deployed twice: once on the local server, and once on the remote one. It processes the messages and sends replies.

In our code, the term 'local server' or 'earth' means the application client and the bean class are deployed. On the other hand, the term 'remote server' or 'jupiter' means the server on which only the bean class is deployed.


The basic steps of the modules are as follows:
1. You start two Java EE servers, one on each system.
2. On the local server (earth), you create two connection factories: one local and one that communicates with the remote server (jupiter). On the remote server, you create a connection factory that has the same name.
3. The application client looks up the two connection factories (the local one and the one that communicates with the remote server) to create two connections, sessions, publishers, and subscribers. The subscribers use a message listener.
4. Each publisher publishes five messages.
5. Each of the local and the remote message-driven beans receives five messages and sends replies.
6. The client’s message listener consumes the replies.

Use NetBeans, Eclipse or ant to build an App Client module for the local server and a EJB module for both the local and remote servers.
