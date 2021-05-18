
package Plane;

import Communication.ClientCom;
import Communication.Message;
import Communication.MessageTypes;

/**
 * Stub for the shared region of the plane.
 * @author Rafael Sá (104552), José Brás (74029)
 */
public class SRPlaneStub implements IPlane_Pilot, 
                                    IPlane_Hostess, 
                                    IPlane_Passenger{
   /**
    * Plane server host name.
    */
    private final String serverHostName;
    /**
    * Plane server port.
    */
    private final int serverPort;
    /**
     * Client communications manager.
     */
    private final ClientCom clientCom;

    /**
     * Stub of the shared region of the plane instantiation.
     * @param serverHostName plane server host name
     * @param serverPort plane server port
     */
    public SRPlaneStub(String serverHostName, int serverPort) {
        this.serverHostName = serverHostName;
        this.serverPort = serverPort;
        this.clientCom = new ClientCom(serverHostName, serverPort);
    }
    
    /**
     * Operation for the pilot to wait for the boarding process to end.
     */
    @Override
    public void waitForAllInBoard() {
        Message outMessage = new Message(MessageTypes.P_WTFB);
        Message inMessage = sendMessageAndWaitForReply(outMessage);
        if(inMessage.getMessageType() != MessageTypes.RSP_OK){
            System.out.println("Error on the reply received from the shared region!");
            System.exit (1);
        }
    }
    /**
     * Operation for the pilot to fly the plane to the Destination Point.
     */
    @Override
    public void flyToDestinationPoint() {
        Message outMessage = new Message(MessageTypes.P_FDES);
        Message inMessage = sendMessageAndWaitForReply(outMessage);
        if(inMessage.getMessageType() != MessageTypes.RSP_OK){
            System.out.println("Error on the reply received from the shared region!");
            System.exit (1);
        }
    }
    /**
     * Operation for the pilot to announce that the plane has arrived to the destination airport.
     */
    @Override
    public void announceArrival() {
        Message outMessage = new Message(MessageTypes.P_ANAR);
        Message inMessage = sendMessageAndWaitForReply(outMessage);
        if(inMessage.getMessageType() != MessageTypes.RSP_OK){
            System.out.println("Error on the reply received from the shared region!");
            System.exit (1);
        }
    }
    /**
     * Operation for the pilot to fly the plane to the Departure Point.
     */
    @Override
    public void flyToDeparturePoint() {
        Message outMessage = new Message(MessageTypes.P_FDEP);
        Message inMessage = sendMessageAndWaitForReply(outMessage);
        if(inMessage.getMessageType() != MessageTypes.RSP_OK){
            System.out.println("Error on the reply received from the shared region!");
            System.exit (1);
        }    }
    /**
     * Operation for the pilot to park the plane at the transfer gate.
     */
    @Override
    public void parkAtTransferGate() {
        Message outMessage = new Message(MessageTypes.P_PATG);
        Message inMessage = sendMessageAndWaitForReply(outMessage);
        if(inMessage.getMessageType() != MessageTypes.RSP_OK){
            System.out.println("Error on the reply received from the shared region!");
            System.exit (1);
        }
    }
    /**
     * Operation for the hostess to inform that the plane is ready to take off.
     * @param numPassengersOnPlane number of passengers that must be on plane before allow the take off
     */
    @Override
    public void informPlaneReadyToTakeOff(int numPassengersOnPlane) {
        Message outMessage = new Message(MessageTypes.H_PRTO, numPassengersOnPlane);
        Message inMessage = sendMessageAndWaitForReply(outMessage);
        if(inMessage.getMessageType() != MessageTypes.RSP_OK){
            System.out.println("Error on the reply received from the shared region!");
            System.exit (1);
        }
    }
    /**
     * Operation for the passengers to board the plane.
     * @param passengerID passenger id
     */
    @Override
    public void boardThePlane(int passengerID) {
        Message outMessage = new Message(MessageTypes.PS_BTP, passengerID);
        Message inMessage = sendMessageAndWaitForReply(outMessage);
        if(inMessage.getMessageType() != MessageTypes.RSP_OK){
            System.out.println("Error on the reply received from the shared region!");
            System.exit (1);
        }
    }
    /**
     * Operation for the passenger to wait for the end of the flight.
     * @param passengerID passenger id
     */
    @Override
    public void waitForEndOfFlight(int passengerID) {
        Message outMessage = new Message(MessageTypes.PS_EOF, passengerID);
        Message inMessage = sendMessageAndWaitForReply(outMessage);
        if(inMessage.getMessageType() != MessageTypes.RSP_OK){
            System.out.println("Error on the reply received from the shared region!");
            System.exit (1);
        }
    }
    /**
     * Operation for the passenger leave the plane.
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
     * Shutdown of the shared region of the plane.
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
        clientCom.open();
        clientCom.writeObject(outMessage);
        Message inMessage = (Message) clientCom.readObject();
        clientCom.close();
        return inMessage;
    }
}
