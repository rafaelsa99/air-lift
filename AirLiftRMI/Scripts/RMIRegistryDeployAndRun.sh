echo "Transfering data to the RMIregistry node."
sshpass -f password ssh sd205@l040101-ws08.ua.pt 'mkdir -p Airlift/RMIProj/'
sshpass -f password ssh sd205@l040101-ws08.ua.pt 'rm -rf Airlift/RMIProj/*'
sshpass -f password ssh sd205@l040101-ws08.ua.pt 'mkdir -p Public/classes/RMI'
sshpass -f password ssh sd205@l040101-ws08.ua.pt 'rm -rf Public/classes/RMI/*'
sshpass -f password ssh sd205@l040101-ws08.ua.pt 'mkdir -p Public/classes/Repository'
sshpass -f password ssh sd205@l040101-ws08.ua.pt 'rm -rf Public/classes/Repository/*'
sshpass -f password ssh sd205@l040101-ws08.ua.pt 'mkdir -p Public/classes/Plane'
sshpass -f password ssh sd205@l040101-ws08.ua.pt 'rm -rf Public/classes/Plane/*'
sshpass -f password ssh sd205@l040101-ws08.ua.pt 'mkdir -p Public/classes/DepartureAirport'
sshpass -f password ssh sd205@l040101-ws08.ua.pt 'rm -rf Public/classes/DepartureAirport/*'
sshpass -f password ssh sd205@l040101-ws08.ua.pt 'mkdir -p Public/classes/DestinationAirport'
sshpass -f password ssh sd205@l040101-ws08.ua.pt 'rm -rf Public/classes/DestinationAirport/*'
sshpass -f password scp RMIRegistry.zip sd205@l040101-ws08.ua.pt:Airlift/RMIProj/
echo "Decompressing data sent to the RMIregistry node."
sshpass -f password ssh sd205@l040101-ws08.ua.pt 'cd Airlift/RMIProj ; unzip -uq RMIRegistry.zip'
echo "Copying files to their proper folders"
sshpass -f password ssh sd205@l040101-ws08.ua.pt 'cd Airlift/RMIProj/ ; cp RMI/*.class /home/sd205/Public/classes/RMI ; cp Repository/*.class /home/sd205/Public/classes/Repository ; cp Plane/*.class /home/sd205/Public/classes/Plane ; cp DepartureAirport/*.class /home/sd205/Public/classes/DepartureAirport ; cp DestinationAirport/*.class /home/sd205/Public/classes/DestinationAirport ; cp set_rmiregistry_d.sh /home/sd205'
echo "Executing program at the RMIregistry node."
sshpass -f password ssh sd205@l040101-ws08.ua.pt './set_rmiregistry_d.sh sd205 22047'
