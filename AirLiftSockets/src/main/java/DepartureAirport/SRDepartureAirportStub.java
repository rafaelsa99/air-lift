package DepartureAirport;

import Communication.ClientCom;
import Communication.Message;
import Communication.MessageTypes;


public class SRDepartureAirportStub implements IDepartureAirport_Hostess, 
                                               IDepartureAirport_Passenger, 
                                               IDepartureAirport_Pilot{
    /**
    * DepartureAirport server host name.
    */
    private final String serverHostName;
    /**
    * DepartureAirport server port.
    */
    private final int serverPort;

    private final ClientCom clientCom;
        
    public SRDepartureAirportStub(String serverHostName, int serverPort) {
        this.serverHostName = serverHostName;
        this.serverPort = serverPort;
        this.clientCom = new ClientCom(serverHostName, serverPort);
    }

    @Override
    public void checkDocuments() {
        Message outMessage = new Message(MessageTypes.H_CHKD);
        Message inMessage = sendMessageAndWaitForReply(outMessage);
        if(inMessage.getMessageType() != MessageTypes.RSP_OK){
            System.out.println("Error on the reply received from the shared region!");
            System.exit (1);
        }
    }

    @Override
    public int waitForNextPassenger() {
        Message outMessage = new Message(MessageTypes.H_WTPS);
        Message inMessage = sendMessageAndWaitForReply(outMessage);
        if(inMessage.getMessageType() != MessageTypes.RSP_OK){
            System.out.println("Error on the reply received from the shared region!");
            System.exit (1);
        }
        return inMessage.getiParam1();
    }

    @Override
    public boolean waitForNextFlight() {
        Message outMessage = new Message(MessageTypes.H_WTFL);
        Message inMessage = sendMessageAndWaitForReply(outMessage);
        if(inMessage.getMessageType() != MessageTypes.RSP_OK){
            System.out.println("Error on the reply received from the shared region!");
            System.exit (1);
        }
        return inMessage.getbParam();
    }

    @Override
    public void prepareForPassBoarding() {
        Message outMessage = new Message(MessageTypes.H_PBRD);
        Message inMessage = sendMessageAndWaitForReply(outMessage);
        if(inMessage.getMessageType() != MessageTypes.RSP_OK){
            System.out.println("Error on the reply received from the shared region!");
            System.exit (1);
        }
    }

    @Override
    public void waitInQueue(int passengerID) {
        Message outMessage = new Message(MessageTypes.PS_WIQ, passengerID);
        Message inMessage = sendMessageAndWaitForReply(outMessage);
        if(inMessage.getMessageType() != MessageTypes.RSP_OK){
            System.out.println("Error on the reply received from the shared region!");
            System.exit (1);
        }
    }

    @Override
    public void showDocuments(int passengerID) {
        Message outMessage = new Message(MessageTypes.PS_SHD, passengerID);
        Message inMessage = sendMessageAndWaitForReply(outMessage);
        if(inMessage.getMessageType() != MessageTypes.RSP_OK){
            System.out.println("Error on the reply received from the shared region!");
            System.exit (1);
        }
    }

    @Override
    public boolean informPlaneReadyForBoarding() {
        Message outMessage = new Message(MessageTypes.P_PRFB);
        Message inMessage = sendMessageAndWaitForReply(outMessage);
        if(inMessage.getMessageType() != MessageTypes.RSP_OK){
            System.out.println("Error on the reply received from the shared region!");
            System.exit (1);
        }
        return inMessage.getbParam();
    }
    
    public void end(){
        Message outMessage = new Message(MessageTypes.END);
        Message inMessage = sendMessageAndWaitForReply(outMessage);
        if(inMessage.getMessageType() != MessageTypes.RSP_OK){
            System.out.println("Error on the reply received from the shared region!");
            System.exit (1);
        }
    }
    
    private Message sendMessageAndWaitForReply(Message outMessage){
        clientCom.open();
        clientCom.writeObject(outMessage);
        Message inMessage = (Message) clientCom.readObject();
        clientCom.close();
        return inMessage;
    }
}
