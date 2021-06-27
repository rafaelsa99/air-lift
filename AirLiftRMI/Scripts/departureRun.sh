CODEBASE="file:///home/"$1"/Airlift/RMIProject/AirliftRMI"
java -Djava.rmi.server.codebase=$CODEBASE\
     -Djava.rmi.server.useCodebaseOnly=false\
     -Djava.security.policy=java.policy\
     Main.DepartureAirportMain -lp 22042 -rh l040101-ws08.ua.pt -rp 22047
 
