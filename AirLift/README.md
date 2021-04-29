# Air Lift Concurrent Implementation
The project simulates the life cycle of the passengers, the hostess and the pilot using a model for thread communication and synchronization: monitors and shared memory. 

One aims for a distributed solution with multiple information sharing regions, written in <b>Java</b>, run in Linux and which terminates. <br>
A logging file, which describes the evolution of the internal state of the problem in a clear and precise way, is included.

### Parameters
Optional arguments: 
- -p <NUM_PASSENGERS>: Number of passengers (Default = 21)
- -s <MAX_SLEEP>: Maximum sleeping time in milliseconds (Default = 1000)
- -l <LOG_FILENAME>: Filename of the logging file (Default = "log.txt")
- -i <MIN_PASSENGER>: Minimum number of passengers on a flight (Default = 5)
- -a <MAX_PASSENGER>: Maximum number of passengers on a flight (Default = 10)

#### Example
```
java -jar AirLift.jar -p 15 -s 500 -l "logFile.txt" -i 2 -a 7
```
