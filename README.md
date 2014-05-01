# visactor

visactor is a tool to visualize graph properties through time. 
There are great tools to visualize graphs and their properties such as [d3][1].
visactor focues to provide simple ways to visualize for instance the following properties:
- The flow/traffic of messages in a an actor model. Actors interact with one another using messages. The communicated messages among actor can actually build graphs in which the vertices are the actors and the edges are the messages. It an interesting problem to be able to visualize such communications through time as the actor model is busy.
- The financial transactions in a baking system build a graph. Each transaction is related to a spender, and a receiver, the amount and the time of transaction. It is interesting to be able to visualize such a graph and its properties through as the transactions happen through time.
 
## Demo

To run the demo, you need to have Java 1.8 and Apache Maven 3+. Simply, clone the source and then:
```bash
$ git clone http://github.com/nobeh/visactor.git 
$ cd visactor
visactor$ ./run-demo.sh 
```
then open your browser to [http://localhost:8080/index.html]

**Note** your browser must support [Server-sent Events][2]

[1]: http://d3js.org 
[2]: http://en.wikipedia.org/wiki/Server-sent_events

