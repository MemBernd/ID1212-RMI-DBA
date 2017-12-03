/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client.startup;

import client.view.Interpreter;
import common.Server;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

/**
 *
 * @author Bernardo
 */
public class ClientStartup {
    public static void main(String[] args) {
        try {
            Server server = (Server) Naming.lookup(Server.SERVER_NAME);
            new Interpreter(server);
        } catch (NotBoundException | MalformedURLException | RemoteException e) {
            System.out.println("Client problem starting.");
        }
    }
}
