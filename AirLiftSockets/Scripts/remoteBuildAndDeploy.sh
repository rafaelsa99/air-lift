i=1

for machine in "$@"
do
    echo "Machine chosen - $machine"
    i=$((i+1));
    #if [$i -lt 10]
    #then
    #    $i=0+$i
    #fi
done


#echo "Compiling source code"
# javac Project/ActiveEntity/*java Project/Common/*java Project/Communication/*java Project/DepartureAirport/*java Project/DestinationAirport/*java Project/Main/*java Project/Plane/*java Project/Repository/*java
#echo "Compilation succesfull"
export SSHPASS='qwerty'

echo "Deleting previous project"
for machine in "$@"
do
    sshpass -e ssh -o StrictHostKeyChecking=no sd205@l040101-ws0$machine.ua.pt << !
    echo "$machine"
    rm -rf Project
!
    i=$((i+1));
done
echo "Moving project to machines"

for machine in "$@"
do
    sshpass -e sftp -o StrictHostKeyChecking=no sd205@l040101-ws0$machine.ua.pt << !
    put -r Project/
    bye
!
    i=$((i+1));
done
echo "Compiling source code"
for machine in "$@"
do
    sshpass -e ssh -o StrictHostKeyChecking=no sd205@l040101-ws0$machine.ua.pt << !
#     javac Project/ActiveEntity/*java Project/Common/*java Project/Communication/*java Project/DepartureAirport/*java Project/DestinationAirport/*java Project/Main/*java Project/Plane/*java Project/Repository/*java
        javac */*/*.java
      i=$((i+1));
!
done

echo "Executing code"
echo "Executing Repository"
sshpass -e ssh -t -o StrictHostKeyChecking=no sd205@l040101-ws0$1.ua.pt << EOF
    cd Project/Main/
    java Main.RepositoryMain -sp 22040 > /dev/null 2 >&1 & 
EOF
    
    
sleep 5

echo "Executing DepartureAirport"
sshpass -e ssh -t -o StrictHostKeyChecking=no sd205@l040101-ws0$2.ua.pt << EOF
    cd Project/Main/
    java Main.DepartureAirportMain -sp 22041 -rh  l040101-ws0$1 -rp 22040 > /dev/null 2 >&1 & 
EOF
sleep 1    
echo "Executing Plane"
sshpass -e ssh -t -o StrictHostKeyChecking=no sd205@l040101-ws0$3.ua.pt << EOF
    cd Project/Main/
    java Main.PlaneMain -sp 22042 -rh  l040101-ws0$1 -rp 22040 > /dev/null 2 >&1 & 
EOF
sleep 1
echo "Executing DestinationAirport"
sshpass -e ssh -t -o StrictHostKeyChecking=no sd205@l040101-ws0$4.ua.pt << EOF
    cd Project/Main/
    java Main.DestinationAirportMain -sp 22043 -rh  l040101-ws0$1 -rp 22040 > /dev/null 2 >&1 & 
EOF

sleep 1
echo "Executing Passengers"
sshpass -e ssh -t -o StrictHostKeyChecking=no sd205@l040101-ws0$5.ua.pt << EOF
    cd Project/Main/
    java Main.PassengerMain -dh  l040101-ws0$2 -dp 22041 -dsh  l040101-ws0$4 -dsp 22043 -ph l040101-ws0$3 -pp 22042 > /dev/null 2 >&1 & 
EOF
sleep 2
echo "Executing Pilot"
sshpass -e ssh -t -o StrictHostKeyChecking=no sd205@l040101-ws0$6.ua.pt << EOF
    cd Project/Main/
    java Main.PilotMain -dh  l040101-ws0$2 -dp 22041 -ph l040101-ws0$3 -pp 22042 > /dev/null 2> /dev/null 2 >&1 & 
EOF
sleep 1
echo "Executing Hostess"
sshpass -e ssh -t -o StrictHostKeyChecking=no sd205@l040101-ws0$7.ua.pt << EOF
    cd Project/Main/
    java Main.HostessMain -dh  l040101-ws0$2 -dp 22041 -ph l040101-ws0$3 -pp 22042 > /dev/null 2 >&1 & 
EOF
sleep 5
echo "Getting logfile"
sshpass -e sftp -o StrictHostKeyChecking=no sd205@l040101-ws0$1.ua.pt << !
    get -r Project/log.txt
    bye
!
