
package Communication;

import java.io.Serializable;

/**
 * Serializable class representing the message to be sent.
 * @author Rafael Sá (104552), José Brás (74029)
 */
public class Message implements Serializable{
    
    /**
     * Serial Version UID.
     */
    private static final long serialVersionUID = 20210515L;
    /**
     * Indication of the message type.
     */
    private int messageType;
    /**
     * Boolean parameter.
     */
    private boolean bParam;
    /**
     * First integer parameter.
     */
    private int iParam1;
    /**
     * Second integer parameter.
     */
    private int iParam2;

    /**
     * Message instantiation without parameters.
     */
    public Message() {}
    
    /**
     * Message instantiation with the message type.
     * @param messageType indication of the message type
     */
    public Message(int messageType) {
        this.messageType = messageType;
    }
    
    /**
     * Message instantiation with the message type and a boolean parameter.
     * @param messageType indication of the message type
     * @param bParam  boolean parameter
     */
    public Message(int messageType, boolean bParam) {
        this.messageType = messageType;
        this.bParam = bParam;
    }

    /**
     * Message instantiation with the message type and one integer parameter.
     * @param messageType indication of the message type
     * @param iParam integer parameter
     */
    public Message(int messageType, int iParam) {
        this.messageType = messageType;
        this.iParam1 = iParam;
    }

    /**
     * Message instantiation with the message type and two integer parameters.
     * @param messageType indication of the message type
     * @param iParam1 first integer parameter
     * @param iParam2 second integer parameter
     */
    public Message(int messageType, int iParam1, int iParam2) {
        this.messageType = messageType;
        this.iParam1 = iParam1;
        this.iParam2 = iParam2;
    }

    /**
     * Get the type of the message
     * @return message type
     */
    public int getMessageType() {
        return messageType;
    }

    /**
     * Get the value of the boolean parameter.
     * @return the value of the boolean parameter
     */
    public boolean getbParam() {
        return bParam;
    }

    /**
     * Get the value of the first integer parameter.
     * @return the value of the first integer parameter
     */
    public int getiParam1() {
        return iParam1;
    } 
    
    /**
     * Get the value of the second integer parameter.
     * @return the value of the second integer parameter
     */
    public int getiParam2() {
        return iParam2;
    } 
}
