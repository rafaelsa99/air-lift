package DepartureAirport;

import Communication.ClientCom;
import Communication.Message;
import Communication.MessageTypes;

/**
 * Stub for the shared region of the departure airport.
 * @author Rafael Sá (104552), José Brás (74029)
 */
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
        
    /**
     * Stub of the shared region of the departure airport instantiation.
     * @param serverHostName departureAirport server host name
     * @param serverPort departureAirport server port
     */
    public SRDepartureAirportStub(String serverHostName, int serverPort) {
        this.serverHostName = serverHostName;
        this.serverPort = serverPort;
    }
    
    /**
     * Operation to check the documents of the next passenger in queue.
     */
    @Override
    public void checkDocuments() {
        Message outMessage = new Message(MessageTypes.H_CHKD);
        Message inMessage = sendMessageAndWaitForReply(outMessage);
        if(inMessage.getMessageType() != MessageTypes.RSP_OK){
            System.out.println("Error on the reply received from the shared region!");
            System.exit (1);
        }
    }

    /**
     * Operation for the hostess to wait for the next passenger.
     * @return the number of passengers on the plane.
     *         -1, if the boarding procedure is still ongoing.
     */
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

    /**
     * Operation for the hostess to wait for the next flight.
     * @return true, if there are still passengers to transport and the  simulation continues. 
     *         false, if there are no more passengers left to transport and the simulation ends.
     */
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

    /**
     * Operation for the Hostess to prepare for passenger boarding.
     */
    @Override
    public void prepareForPassBoarding() {
        Message outMessage = new Message(MessageTypes.H_PBRD);
        Message inMessage = sendMessageAndWaitForReply(outMessage);
        if(inMessage.getMessageType() != MessageTypes.RSP_OK){
            System.out.println("Error on the reply received from the shared region!");
            System.exit (1);
        }
    }
    
    /**
     * Operation for the passenger to wait in the queue.
     * @param passengerID passenger id 
     */
    @Override
    public void waitInQueue(int passengerID) {
        Message outMessage = new Message(MessageTypes.PS_WIQ, passengerID);
        Message inMessage = sendMessageAndWaitForReply(outMessage);
        if(inMessage.getMessageType() != MessageTypes.RSP_OK){
            System.out.println("Error on the reply received from the shared region!");
            System.exit (1);
        }
    }
    /**
     * Operation for the passenger show the documents.
     * @param passengerID passenger id
     */
    @Override
    public void showDocuments(int passengerID) {
        Message outMessage = new Message(MessageTypes.PS_SHD, passengerID);
        Message inMessage = sendMessageAndWaitForReply(outMessage);
        if(inMessage.getMessageType() != MessageTypes.RSP_OK){
            System.out.println("Error on the reply received from the shared region!");
            System.exit (1);
        }
    }
    /**
     * Operation to inform that the plane is ready for boarding.
     * @return true, if there are still passengers to transport and the  simulation continues. 
     *         false, if there are no more passengers left to transport and the simulation ends.
     */
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
    /**
     * End of the simulation.
     * Shutdown of the shared region of the departure airport.
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
