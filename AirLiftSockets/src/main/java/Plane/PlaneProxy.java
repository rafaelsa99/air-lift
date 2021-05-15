/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Plane;

/**
 *
 * @author Rafael Sá (104552), José Brás (74029)
 */
public class PlaneProxy extends Thread{
    
    SRPlane srPlane;

    public PlaneProxy(SRPlane srPlane) {
        this.srPlane = srPlane;
    }
}
