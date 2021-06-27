#xterm  -T "RMI Registry" -hold -e "./RMIRegistryDeployAndRun.sh" & 
#sleep 15
xterm  -T "Registry" -hold -e "./RegistryDeployAndRun.sh" &
sleep 10
xterm  -T "General Repository" -hold -e "./RepoDeployAndRun.sh $1" & 
sleep 12
xterm  -T "Departure Airport" -hold -e "./DepartureAirportDeployAndRun.sh $2" &
sleep 5
xterm  -T "Plane" -hold -e "./PlaneDeployAndRun.sh $3" & 
sleep 5
xterm  -T "Destination Airport" -hold -e "./DestinationAirportDeployAndRun.sh $4" &
sleep 5
xterm  -T "Pilot" -hold -e "./PilotDeployAndRun.sh $5"  & 
xterm  -T "Hostess" -hold -e "./HostessDeployAndRun.sh $6" &
xterm  -T "Passenger" -hold -e "./PassengerDeployAndRun.sh $7" &
wait
