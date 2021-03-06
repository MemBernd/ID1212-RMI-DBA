/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package common;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 *
 * @author Bernardo
 */
public interface Server extends Remote {
    
    public static final String SERVER_NAME = "server";
    
    void register(String username, String password) throws RemoteException, AccessException;
    
    void unregister(String username, String password) throws RemoteException, AccessException;
    
    long login(Client client, String username, String password) throws RemoteException, AccessException;
    
    void logout(long id) throws RemoteException;
    
    void list(long id) throws RemoteException, AccessException;
    
    void upload(long id, String name, long size, boolean pub, boolean read, boolean write) throws RemoteException, AccessException;
    
    FileHolder download(long id, String name) throws RemoteException, AccessException;
    
    void delete(long id, String name) throws RemoteException, AccessException;
    
    void addNotification(long id, String name) throws RemoteException, AccessException;
    
    
}
