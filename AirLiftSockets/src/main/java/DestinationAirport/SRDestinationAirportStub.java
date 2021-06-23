
package DestinationAirport;

import Communication.ClientCom;
import Communication.Message;
import Communication.MessageTypes;

/**
 * Stub for the shared region of the destination airport.
 * @author Rafael Sá (104552), José Brás (74029)
 */
public class SRDestinationAirportStub implements IDestinationAirport_Passenger{
    /**
    * Destination Airport server host name.
    */
    private final String serverHostName;
    /**
    * Destination Airport server port.
    */
    private final int serverPort;

    /**
     * Stub of the shared region of the destination airport instantiation.
     * @param serverHostName destination airport server host name
     * @param serverPort destination airport server port
     */    
    public SRDestinationAirportStub(String serverHostName, int serverPort) {
        this.serverHostName = serverHostName;
        this.serverPort = serverPort;
    }
    
    /**
     * Operation to indicate that the passenger left the plane and arrived at the destination airport.
     * @param passengerID passenger id
     */
    @Override
    public void leaveThePlane(int passengerID) {
        Message outMessage = new Message(MessageTypes.PS_LTP, passengerID);
        Message inMessage = sendMessageAndWaitForReply(outMessage);
        if(inMessage.getMessageType() != MessageTypes.RSP_OK){
            System.out.println("Error on the reply received from the shared region!");
            System.exit (1);
        }
    }
    
    /**
     * End of the simulation.
     * Shutdown of the shared region of the destination airport.
     */
    public void end(){
        Message outMessage = new Message(MessageTypes.END);
        Message inMessage = sendMessageAndWaitForReply(outMessage);
        if(inMessage.getMessageType() != MessageTypes.RSP_OK){
            System.out.println("Error on the reply received from the shared region!");
            System.exit (1);
        }
    }
    /**
     * Send a message to the shared region and wait for the reply.
     * @param outMessage message to be sent
     * @return the message replied by the shared region
     */
    private Message sendMessageAndWaitForReply(Message outMessage){
        ClientCom clientCom = new ClientCom(serverHostName, serverPort);
        clientCom.open();
        clientCom.writeObject(outMessage);
        Message inMessage = (Message) clientCom.readObject();
        clientCom.close();
        return inMessage;
    }
}
