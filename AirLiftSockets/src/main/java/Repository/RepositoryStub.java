
package Repository;

import Communication.ClientCom;
import Communication.Message;
import Communication.MessageTypes;

/**
 *
 * @author rafael
 */
public class RepositoryStub implements IRepository_DepartureAirport, 
                                   IRepository_Plane,
                                   IRepository_DestinationAirport{
    
    /**
    * Repository server host name.
    */
    private String serverHostName;
    /**
    * Repository server port.
    */
    private int serverPort;
    
    private ClientCom clientCom;

    public RepositoryStub(String serverHostName, int serverPort) {
        this.serverHostName = serverHostName;
        this.serverPort = serverPort;
        this.clientCom = new ClientCom(serverHostName, serverPort);
    }
    
    @Override
    public void setPilotState(int stPilot) {
        Message outMessage = new Message(MessageTypes.R_PIST, stPilot);
        Message inMessage = sendMessageAndWaitForReply(outMessage);
        if(inMessage.getMessageType() != MessageTypes.RSP_OK){
            System.out.println("Error on the reply received from the shared region!");
            System.exit (1);
        }
    }

    @Override
    public void setHostessState(int stHostess) {
        Message outMessage = new Message(MessageTypes.R_HST1, stHostess);
        Message inMessage = sendMessageAndWaitForReply(outMessage);
        if(inMessage.getMessageType() != MessageTypes.RSP_OK){
            System.out.println("Error on the reply received from the shared region!");
            System.exit (1);
        }
    }

    @Override
    public void setHostessState(int stHostess, int passengerID) {
        Message outMessage = new Message(MessageTypes.R_HST2, stHostess, passengerID);
        Message inMessage = sendMessageAndWaitForReply(outMessage);
        if(inMessage.getMessageType() != MessageTypes.RSP_OK){
            System.out.println("Error on the reply received from the shared region!");
            System.exit (1);
        }
    }

    @Override
    public void setPassengerState(int stPassenger, int passengerID) {
        Message outMessage = new Message(MessageTypes.R_PSST, stPassenger, passengerID);
        Message inMessage = sendMessageAndWaitForReply(outMessage);
        if(inMessage.getMessageType() != MessageTypes.RSP_OK){
            System.out.println("Error on the reply received from the shared region!");
            System.exit (1);
        }
    }

    @Override
    public void printSumUp() {
        Message outMessage = new Message(MessageTypes.R_SUMP);
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
