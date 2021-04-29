# Air Lift Message Passing Implementation
The operations that were previously assigned to activities carried out in the information sharing regions (for the already implemented concurrent version), are now assigned to independent requests
performed on the servers through message passing. In each case, a connection has to be established, a request has to be made, waiting for the reply will follow and the connection has to be closed.

One aims for a solution to be written in <b>Java</b>, to be run in Linux under TCP sockets, either in a concentrated manner (on a single platform), or in a distributed fashion (up to 7 different platforms), and to
terminate (it must contemplate service shutdown). A logging file, that describes the evolution of the internal state of the problem in a clear and precise way, must be included.

### Parameters
// TODO

#### Example
```
java -jar AirLift.jar
```
