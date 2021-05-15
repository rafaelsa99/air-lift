/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DepartureAirport;

/**
 *
 * @author jcpbr
 */
public class DepartureAirportProxy extends Thread{
    
    SRDepartureAirport sRDepartureAirport;

    public DepartureAirportProxy(SRDepartureAirport sRDepartureAirport) {
        this.sRDepartureAirport = sRDepartureAirport;
    }
}
