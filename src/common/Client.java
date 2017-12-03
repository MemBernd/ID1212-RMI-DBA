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
public interface Client extends Remote {
    
    void printMessage(String output) throws RemoteException;
    
    void printWithoutPrompt(String output) throws RemoteException;
}
