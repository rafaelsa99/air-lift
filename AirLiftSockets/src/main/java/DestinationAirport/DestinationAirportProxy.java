/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DestinationAirport;

/**
 *
 * @author jcpbr
 */
public class DestinationAirportProxy extends Thread{
    
    SRDestinationAirport sRDestinationAirport;

    public DestinationAirportProxy(SRDestinationAirport sRDestinationAirport) {
        this.sRDestinationAirport = sRDestinationAirport;
    }
}
