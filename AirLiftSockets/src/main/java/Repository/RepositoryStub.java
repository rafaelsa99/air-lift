
package Repository;

import Communication.ClientCom;
import Communication.Message;
import Communication.MessageTypes;

/**
 * Stub for the shared region of the general repository.
 * @author Rafael Sá (104552), José Brás (74029)
 */
public class RepositoryStub implements IRepository_DepartureAirport, 
                                   IRepository_Plane,
                                   IRepository_DestinationAirport{
    
    /**
    * Repository server host name.
    */
    private final String serverHostName;
    /**
    * Repository server port.
    */
    private final int serverPort;
    /**
     * Client communications manager.
     */
    private final ClientCom clientCom;
    
    /**
     * Stub of the shared region of the general repository instantiation.
     * @param serverHostName general repository server host name
     * @param serverPort general repository server port
     */
    public RepositoryStub(String serverHostName, int serverPort) {
        this.serverHostName = serverHostName;
        this.serverPort = serverPort;
        this.clientCom = new ClientCom(serverHostName, serverPort);
    }
    /**
     * Sets the pilot state.
     * @param stPilot new pilot state
     */
    @Override
    public void setPilotState(int stPilot) {
        Message outMessage = new Message(MessageTypes.R_PIST, stPilot);
        Message inMessage = sendMessageAndWaitForReply(outMessage);
        if(inMessage.getMessageType() != MessageTypes.RSP_OK){
            System.out.println("Error on the reply received from the shared region!");
            System.exit (1);
        }
    }
    /**
     * Sets the hostess state.
     * @param stHostess new hostess state1
     */
    @Override
    public void setHostessState(int stHostess) {
        Message outMessage = new Message(MessageTypes.R_HST1, stHostess);
        Message inMessage = sendMessageAndWaitForReply(outMessage);
        if(inMessage.getMessageType() != MessageTypes.RSP_OK){
            System.out.println("Error on the reply received from the shared region!");
            System.exit (1);
        }
    }
    /**
     * Sets the hostess state.
     * @param stHostess new hostess state
     * @param passengerID passenger id being checked
     */
    @Override
    public void setHostessState(int stHostess, int passengerID) {
        Message outMessage = new Message(MessageTypes.R_HST2, stHostess, passengerID);
        Message inMessage = sendMessageAndWaitForReply(outMessage);
        if(inMessage.getMessageType() != MessageTypes.RSP_OK){
            System.out.println("Error on the reply received from the shared region!");
            System.exit (1);
        }
    }
    /**
     * Sets the passenger state.
     * @param stPassenger new passenger state
     * @param passengerID passenger id
     */
    @Override
    public void setPassengerState(int stPassenger, int passengerID) {
        Message outMessage = new Message(MessageTypes.R_PSST, stPassenger, passengerID);
        Message inMessage = sendMessageAndWaitForReply(outMessage);
        if(inMessage.getMessageType() != MessageTypes.RSP_OK){
            System.out.println("Error on the reply received from the shared region!");
            System.exit (1);
        }
    }
    /**
     * Prints the final sum up with all flights that took place and the number of passenger in each one.
     */
    @Override
    public void printSumUp() {
        Message outMessage = new Message(MessageTypes.R_SUMP);
        Message inMessage = sendMessageAndWaitForReply(outMessage);
        if(inMessage.getMessageType() != MessageTypes.RSP_OK){
            System.out.println("Error on the reply received from the shared region!");
            System.exit (1);
        }
    }
    /**
     * End of the simulation.
     * Shutdown of the shared region of the general repository.
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
