echo "Compiling source code"
javac */*/*.java

echo "Executing code"
echo "Executing Repository"
cd Project
xterm -e -hold java  Main.RepositoryMain -sp 22040 
    
    
sleep 5

echo "Executing DepartureAirport"
xterm -e -hold java Main.DepartureAirportMain -sp 22041 -rh  l040101-ws0$1 -rp 22040
sleep 1    
echo "Executing Plane"
   xterm -e -hold java Main.PlaneMain -sp 22042 -rh  l040101-ws0$1 -rp 22040 
sleep 1
echo "Executing DestinationAirport"
xterm -e -hold java Main.DestinationAirportMain -sp 22043 -rh  l040101-ws0$1 -rp 22040

sleep 1
echo "Executing Passengers"
xterm -e -hold java Main.PassengerMain -dh  l040101-ws0$2 -dp 22041 -dsh  l040101-ws0$4 -dsp 22043 -ph l040101-ws0$3 -pp 22042
sleep 2
echo "Executing Pilot"
xterm -e -hold java Main.PilotMain -dh  l040101-ws0$2 -dp 22041 -ph l040101-ws0$3 -pp 22042
sleep 1
echo "Executing Hostess"
xterm -e -hold java Main.HostessMain -dh  l040101-ws0$2 -dp 22041 -ph l040101-ws0$3 -pp 22042 
wait
echo "Execution ended"
