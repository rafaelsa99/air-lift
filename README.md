# Air Lift
Practical assignment of the Distributed Systems course of the Masters in Informatics Engineering of the University of Aveiro.

## The Problem
The described activities take place during passenger air lift from an origin town and a destination town somewhere in Portugal and aim to portray what happens in between. One assumes there are three relevant locations: the departure airport, where passengers arrive at random times to check in for the transfer flight; the plane, which takes them to their destination; and the destination airport, where they arrive after the travel.

Three types of entities will be considered: the passengers, who take the transfer flight between the two towns; the hostess, who controls the embarking procedure; and the pilot, who flies the plane. 

A single plane and an indeterminate number of transfer flights are assumed: passengers are only transported from the origin town to the destination town; on the way back, the plane flies empty. A fixed number of N passengers will transfer. The number of passengers that take each flight is variable: it ranges between MIN and MAX for all the flights, but the last; for the last, all the remaining passengers are transported, whatever its number. 

Activities are organized in the following way:
- upon arrival at the departure airport, passengers queue in at transfer gate waiting to be called by the hostess;
- when advised by the pilot that the plane is ready to board, the hostess signals the passenger at the head of the queue, if there is one, to join her and present the flight documentation; after checking it, she requests him/her to board the plane
- if the queue is empty and the number of passengers that have already boarded is between MIN and MAX, or there are no more passengers to transfer, the hostess advises the pilot that the boarding is complete and that the the flight may start;
- once inside the plane, the passengers take a seat and wait for the flight to take place; upon arrival, they exit the plane and leave the airport;
- if there are still passengers to transfer, the pilot parks the plane at the departure gate and advises the hostess that the boarding may start; upon being informed that the boarding is complete, she flies to the destination town;
- after landing and parking the plane at the arrival gate, the pilot announces the passengers that they may leave; when the last passenger has exited, she flies back to the origin town.
 
In the end of the day, a full report of the activities is issued.

It is assumed that there are twenty one passengers participating in the air lift, that the plane capacity is ten and that a transfer flight takes place if there is a minimum of five passengers.
