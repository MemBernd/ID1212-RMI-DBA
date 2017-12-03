/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server.startup;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import server.controller.Controller;

/**
 *
 * @author Bernardo
 */
public class ServerStartup {
    public static void main(String[] args) {
        try {
            new ServerStartup().register();
            Naming.rebind(Controller.SERVER_NAME, new Controller());
            System.out.println("System started.");
        } catch(MalformedURLException | RemoteException ex) {
            System.out.println("Problem starting server.");
        }
    }

    public void register() throws RemoteException {
        try {
            LocateRegistry.getRegistry().list();
        } catch (RemoteException e) {
            LocateRegistry.createRegistry(Registry.REGISTRY_PORT);
        }
    }
}
