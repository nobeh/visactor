# visactor

visactor is a tool to visualize graph properties through time. 
There are great tools to visualize graphs and their properties such as [D3][1].
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
then open your browser to `http://localhost:8080/index.html`

**Note** your browser must support [Server-sent Events][2]

## Use Visactor

visactor provides the following REST end points:

### Push events

Events can be put into visactor at `/events/sink`:
- The HTTP method is `PUT`.
- The content type should be `application/json`.
- Each event is defined as (id, timestamp, source, target). If ID is not provided, an instance of `java.util.UUID` is used. Time stamp should be the standard UNIX epoch seconds and if not provided, it defaults to now.
- The response is 201 if OK and contains the ID of the event.

An example cURL command would like:
```bash
curl -H "Content-Type: application/json" -X PUT -d '{"source": "a", "target": "b"}' http://localhost:8080/events/sink
```

### Receive events

visactor provides access to the "stream" of events using `/events/source`:
- The HTTP method is `GET`.
- The HTTP content type is `text/event-stream` as specified is [Server-sent Events][2]
- Each event is a JSON object that has the same properties as above.
- The stream can be utilizid using JavaScript [EventSource][3]

visactor provides an out-of-the-box `index.html` that uses the same JavaScript technique with [D3][1].
However, you may develop another layer to visualize the events that are published from visactor.

## Contribution

## License

visactor is licensed under [Apache License v2][4]:

```
Copyright 2014 Behrooz Nobakht

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```

[1]: http://d3js.org 
[2]: http://en.wikipedia.org/wiki/Server-sent_events
[3]: http://www.html5rocks.com/en/tutorials/eventsource/basics/
[4]: http://www.apache.org/licenses/LICENSE-2.0.html
