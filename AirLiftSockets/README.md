# Air Lift Message Passing Implementation
The operations that were previously assigned to activities carried out in the information sharing regions (for the already implemented concurrent version), are now assigned to independent requests
performed on the servers through message passing. In each case, a connection has to be established, a request has to be made, waiting for the reply will follow and the connection has to be closed.

One aims for a solution to be written in <b>Java</b>, to be run in Linux under TCP sockets, either in a concentrated manner (on a single platform), or in a distributed fashion (up to 7 different platforms), and to
terminate (it must contemplate service shutdown). A logging file, that describes the evolution of the internal state of the problem in a clear and precise way, must be included.

### Parameters
Optional Arguments:
- -p <NUM_PASSENGERS>: Number of passengers (Default = 21)
- -s <MAX_SLEEP>: Maximum sleeping time in milliseconds (Default = 1000)
- -l <LOG_FILENAME>: Filename of the logging file (Default = "log.txt")
- -i <MIN_PASSENGER>: Minimum number of passengers on a flight (Default = 5)
- -a <MAX_PASSENGER>: Maximum number of passengers on a flight (Default = 10)
- -sp <SERVER_PROXY_PORT>: Server Proxy Agent Port (Default depends of the server)
- -rh <REPOSITORY_SERVER_HOSTNAME>: General Repository Server Hostname (Default = "localhost")
- -rp <REPOSITORY_SERVER_PORT>: General Repository Server Port (Default = 9004)
- -dh <DEPARTURE_AIRPORT_SERVER_HOSTNAME>: Shared Region Departure Airport Server Hostname (Default = "localhost")
- -dp <DEPARTURE_AIRPORT_SERVER_PORT>: Shared Region Departure Airport Server Port (Default = 9001)
- -ph <PLANE_SERVER_HOSTNAME>: Shared Region Plane Server Hostname (Default = "localhost")
- -pp <PLANE_SERVER_PORT>: Shared Region Plane Server Port (Default = 9003)
- -dsh <DESTINATION_AIRPORT_SERVER_HOSTNAME>: Shared Region Destination Airport Server Hostname (Default = "localhost")
- -dsp <DESTINATION_AIRPORT_SERVER_PORT>: Shared Region Destination Airport Server Port (Default = 9002)

#### Run with shell scripts (local and remote)
Example with local build:
```
Scripts/localBuildAndDeploy.sh 
```
