
package Plane;

import Communication.Message;
import Communication.MessageTypes;

/**
 * Interface to the shared region of the plane.
 * Responsible to process a request and generate the reply message.
 * @author Rafael Sá (104552), José Brás (74029)
 */
public class SRPlaneInterface {
    
    /**
     * Shared region of the plane.
     */
    private final SRPlane srPlane;

    /**
     * Plane interface instantiation.
     * @param srPlane shared region of the plane
     */
    public SRPlaneInterface(SRPlane srPlane) {
        this.srPlane = srPlane;
    }
    /**
     * Process a request and generate the reply message.
     * @param inMessage message received with the request
     * @return the reply message
     */
    public Message processAndReply(Message inMessage){
        Message outMessage;
        switch(inMessage.getMessageType()){
                case MessageTypes.P_WTFB:
                    srPlane.waitForAllInBoard();
                    outMessage = new Message(MessageTypes.RSP_OK);
                    break;
                case MessageTypes.P_FDES:
                    srPlane.flyToDestinationPoint();
                    outMessage = new Message(MessageTypes.RSP_OK);
                    break;
                case MessageTypes.P_ANAR:
                    srPlane.announceArrival();
                    outMessage = new Message(MessageTypes.RSP_OK);
                    break;
                case MessageTypes.P_FDEP:
                    srPlane.flyToDeparturePoint();
                    outMessage = new Message(MessageTypes.RSP_OK);
                    break;
                case MessageTypes.P_PATG:
                    srPlane.parkAtTransferGate();
                    outMessage = new Message(MessageTypes.RSP_OK);
                    break;
                case MessageTypes.H_PRTO:
                    srPlane.informPlaneReadyToTakeOff(inMessage.getiParam1());
                    outMessage = new Message(MessageTypes.RSP_OK);
                    break;
                case MessageTypes.PS_BTP:
                    srPlane.boardThePlane(inMessage.getiParam1());
                    outMessage = new Message(MessageTypes.RSP_OK);
                    break;
                case MessageTypes.PS_EOF:
                    srPlane.waitForEndOfFlight(inMessage.getiParam1());
                    outMessage = new Message(MessageTypes.RSP_OK);
                    break;
                case MessageTypes.PS_LTP:
                    srPlane.leaveThePlane(inMessage.getiParam1());
                    outMessage = new Message(MessageTypes.RSP_OK);
                    break;
                default:
                    outMessage = new Message(MessageTypes.RSP_ER);
            }
        return outMessage;
    }
}
