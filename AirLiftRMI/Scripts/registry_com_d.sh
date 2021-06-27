CODEBASE="http://l040101-ws08.ua.pt/"$1"/Public/classes/"
java -Djava.rmi.server.codebase=$CODEBASE\
     -Djava.rmi.server.useCodebaseOnly=true\
     -Djava.security.policy=java.policy\
     Main.ServerRegisterRemoteObject 22048 l040101-ws08.ua.pt 22049
