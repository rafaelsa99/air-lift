
package Plane;

import Common.ClientCom;
import Common.Message;
import Common.MessageTypes;

/**
 *
 * @author jcpbr
 */
public class SRPlaneStub implements IPlane_Pilot, 
                                    IPlane_Hostess, 
                                    IPlane_Passenger{
   /**
    * Plane server host name.
    */
    private String serverHostName;
    /**
    * Plane server port.
    */
    private int serverPort;

    private ClientCom clientCom;

    public SRPlaneStub(String serverHostName, int serverPort) {
        this.serverHostName = serverHostName;
        this.serverPort = serverPort;
        this.clientCom = new ClientCom(serverHostName, serverPort);
    }

    @Override
    public void waitForAllInBoard() {
        Message outMessage = new Message(MessageTypes.P_WTFB);
        Message inMessage = sendMessageAndWaitForReply(outMessage);
        if(inMessage.getMessageType() != MessageTypes.RSP_OK){
            System.out.println("Error on the reply received from the shared region!");
            System.exit (1);
        }
    }

    @Override
    public void flyToDestinationPoint() {
        Message outMessage = new Message(MessageTypes.P_FDES);
        Message inMessage = sendMessageAndWaitForReply(outMessage);
        if(inMessage.getMessageType() != MessageTypes.RSP_OK){
            System.out.println("Error on the reply received from the shared region!");
            System.exit (1);
        }
    }

    @Override
    public void announceArrival() {
        Message outMessage = new Message(MessageTypes.P_ANAR);
        Message inMessage = sendMessageAndWaitForReply(outMessage);
        if(inMessage.getMessageType() != MessageTypes.RSP_OK){
            System.out.println("Error on the reply received from the shared region!");
            System.exit (1);
        }
    }

    @Override
    public void flyToDeparturePoint() {
        Message outMessage = new Message(MessageTypes.P_FDEP);
        Message inMessage = sendMessageAndWaitForReply(outMessage);
        if(inMessage.getMessageType() != MessageTypes.RSP_OK){
            System.out.println("Error on the reply received from the shared region!");
            System.exit (1);
        }    }

    @Override
    public void parkAtTransferGate() {
        Message outMessage = new Message(MessageTypes.P_PATG);
        Message inMessage = sendMessageAndWaitForReply(outMessage);
        if(inMessage.getMessageType() != MessageTypes.RSP_OK){
            System.out.println("Error on the reply received from the shared region!");
            System.exit (1);
        }
    }

    @Override
    public void informPlaneReadyToTakeOff(int numPassengersOnPlane) {
        Message outMessage = new Message(MessageTypes.H_PRTO, numPassengersOnPlane);
        Message inMessage = sendMessageAndWaitForReply(outMessage);
        if(inMessage.getMessageType() != MessageTypes.RSP_OK){
            System.out.println("Error on the reply received from the shared region!");
            System.exit (1);
        }
    }

    @Override
    public void boardThePlane(int passengerID) {
        Message outMessage = new Message(MessageTypes.PS_BTP, passengerID);
        Message inMessage = sendMessageAndWaitForReply(outMessage);
        if(inMessage.getMessageType() != MessageTypes.RSP_OK){
            System.out.println("Error on the reply received from the shared region!");
            System.exit (1);
        }
    }

    @Override
    public void waitForEndOfFlight(int passengerID) {
        Message outMessage = new Message(MessageTypes.PS_EOF, passengerID);
        Message inMessage = sendMessageAndWaitForReply(outMessage);
        if(inMessage.getMessageType() != MessageTypes.RSP_OK){
            System.out.println("Error on the reply received from the shared region!");
            System.exit (1);
        }
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
