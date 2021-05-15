/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Repository;

/**
 *
 * @author rafael
 */
public class RepositoryProxy extends Thread{
    
    Repository repository;

    public RepositoryProxy(Repository repository) {
        this.repository = repository;
    }
}
