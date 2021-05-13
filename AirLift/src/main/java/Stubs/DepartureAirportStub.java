package Stubs;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author jcpbr
 */
public class DepartureAirportStub {
    /**
	 * DepartureAirport server hostname
	 * @serialField serverHostName
	 */
	private String serverHostName;

	/**
	 * Lounge server port
	 * @serialField serverPort
	 */
	private int serverPort;

    /**
     * Lounge stub instantiation
     */
    public DepartureAirportStub() {
			serverHostName = "";
			serverPort = 1;
    }
}
