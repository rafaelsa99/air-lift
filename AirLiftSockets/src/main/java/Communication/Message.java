/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Communication;

import java.io.Serializable;

/**
 *
 * @author rafael
 */
public class Message implements Serializable{
    
    private static final long serialVersionUID = 20210515L;
    private int messageType;
    private boolean bParam;
    private int iParam1;
    private int iParam2;

    public Message() {}
    
    public Message(int messageType) {
        this.messageType = messageType;
    }

    public Message(int messageType, boolean bParam) {
        this.messageType = messageType;
        this.bParam = bParam;
    }

    public Message(int messageType, int iParam) {
        this.messageType = messageType;
        this.iParam1 = iParam;
    }

    public Message(int messageType, int iParam1, int iParam2) {
        this.messageType = messageType;
        this.iParam1 = iParam1;
        this.iParam2 = iParam2;
    }

    public int getMessageType() {
        return messageType;
    }

    public boolean getbParam() {
        return bParam;
    }

    public int getiParam1() {
        return iParam1;
    } 
    
    public int getiParam2() {
        return iParam2;
    } 
}
