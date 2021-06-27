echo "Transfering data to the general repository node."
echo "Rearranging folders"
sshpass -f password ssh sd205@l040101-ws$1.ua.pt 'mkdir -p AirLift/RMIProj'
sshpass -f password ssh sd205@l040101-ws$1.ua.pt 'rm -rf AirLift/RMIProj/*'
echo "Sending files"
sshpass -f password scp -r AirliftRMI sd205@l040101-ws$1.ua.pt:AirLift/RMIProj
sshpass -f password scp reposRun.sh sd205@l040101-ws$1.ua.pt:AirLift/RMIProj/AirliftRMI
sshpass -f password scp java.policy sd205@l040101-ws$1.ua.pt:AirLift/RMIProj/AirliftRMI
#echo "Decompressing data sent to the general repository node."
echo "Executing program at the general repository node."
sshpass -f password ssh sd205@l040101-ws$1.ua.pt 'cd AirLift/RMIProj/AirliftRMI ; ./reposRun.sh sd205' &
wait
echo "Server shutdown."
sshpass -f password ssh sd205@l040101-ws$1.ua.pt 'cd AirLift/RMIProj/AirliftRMI; less log.txt'
sshpass -f password sftp sd205@l040101-ws$1.ua.pt << !
    get -r AirLift/RMIProj/AirliftRMI/log.txt
    bye
!

 
