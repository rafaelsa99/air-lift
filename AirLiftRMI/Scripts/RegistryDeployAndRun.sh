echo "Transfering data to the registry node."
#sshpass -f password ssh sd205@l040101-ws08.ua.pt 'mkdir -p Airlift/RMIProj/'
sshpass -f password scp Registry.zip sd205@l040101-ws08.ua.pt:Airlift/RMIProj/
echo "Decompressing data sent to the registry node."
#sshpass -f password ssh sd205@l040101-ws08.ua.pt 'cd Airlift/RMIProj/ ; unzip -uq Registry.zip ; cp RMI/* /home/sd205/Public/classes/RMI'
sshpass -f password ssh sd205@l040101-ws08.ua.pt 'cd Airlift/RMIProj/ ; unzip -uq Registry.zip ;'

echo "Executing program at the registry node."
sshpass -f password ssh sd205@l040101-ws08.ua.pt 'cd Airlift/RMIProj/; ./registry_com_d.sh sd205'
