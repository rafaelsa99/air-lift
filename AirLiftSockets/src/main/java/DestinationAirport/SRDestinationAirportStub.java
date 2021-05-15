
package DestinationAirport;

import Communication.ClientCom;
import Communication.Message;
import Communication.MessageTypes;

/**
 *
 * @author jcpbr
 */
public class SRDestinationAirportStub implements IDestinationAirport_Passenger{
    /**
    * Destination Airport server host name.
    */
    private String serverHostName;
    /**
    * Destination Airport server port.
    */
    private int serverPort;

    private ClientCom clientCom;
            
    public SRDestinationAirportStub(String serverHostName, int serverPort) {
        this.serverHostName = serverHostName;
        this.serverPort = serverPort;
        this.clientCom = new ClientCom(serverHostName, serverPort);
    }
    
    @Override
    public void leaveThePlane(int passengerID) {
        Message outMessage = new Message(MessageTypes.PS_LTP, passengerID);
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
