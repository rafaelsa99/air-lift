echo "Transfering data to the Departure Airport node."
echo "Rearranging folders"
sshpass -f password ssh sd205@l040101-ws$1.ua.pt 'mkdir -p AirLift/RMIProj'
sshpass -f password ssh sd205@l040101-ws$1.ua.pt 'rm -rf AirLift/RMIProj/*'
echo "Sending files"
sshpass -f password scp -r AirliftRMI sd205@l040101-ws$1.ua.pt:AirLift/RMIProj
sshpass -f password scp departureRun.sh sd205@l040101-ws$1.ua.pt:AirLift/RMIProj/AirliftRMI
sshpass -f password scp java.policy sd205@l040101-ws$1.ua.pt:AirLift/RMIProj/AirliftRMI
#echo "Decompressing data sent to the general repository node."
echo "Executing program at the Departure Airport node."
sshpass -f password ssh sd205@l040101-ws$1.ua.pt 'cd AirLift/RMIProj/AirliftRMI ; ./departureRun.sh sd205'
echo "Server shutdown."
 
