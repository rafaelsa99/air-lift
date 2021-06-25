# Air Lift Remote Method Invocation Implementation
The operations that were assigned to activities previously carried out in the information sharing regions (for the already implemented concurrent version), must now be assigned to independent requests performed on the servers, seen as remote objects, through the remote method of invocation.

One aims for a solution to be written in Java, to be run in Linux under Java RMI, either in a concentrated manner (on a single platform), or in a distributed fashion (up to 9 different platforms) and to terminate (it must contemplate service shutdown).
A logging file, that describes the evolution of the internal state of the problem in a clear and precise way, must be included.

### Parameters
Optional arguments: 
- -p <NUM_PASSENGERS>: Number of passengers (Default = 21)
- -s <MAX_SLEEP>: Maximum sleeping time in milliseconds (Default = 1000)
- -l <LOG_FILENAME>: Filename of the logging file (Default = "log.txt")
- -i <MIN_PASSENGER>: Minimum number of passengers on a flight (Default = 5)
- -a <MAX_PASSENGER>: Maximum number of passengers on a flight (Default = 10)
- -lp <LISTENING_PORT>: Port number for listening to service requests (Default depends on entity: Registry = 9000, Departure Airport = 9001, Destination Airport = 9002, Plane = 9003, Repository = 9004)
- -rh <REGISTRY_HOSTNAME>: Name of the platform where is located the RMI registering service (Default = "localhost")
- -rp <REGISTRY_PORT>: Port number where the registering service is listening to service requests (Default = 9000)

#### Run with shell scripts (local and remote)
Example with local build:
```
//TODO
```
