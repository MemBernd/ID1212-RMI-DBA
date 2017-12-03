/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server.controller;

import common.AccessException;
import common.Client;
import common.FileHolder;
import common.Server;
import server.model.ClientHandler;
import server.storage.DBStorage;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Bernardo
 */
public class Controller extends UnicastRemoteObject implements Server {
    
    private final ClientHandler clientHandler;
    public Controller() throws RemoteException {
        super();
        clientHandler = new ClientHandler();
    }

    @Override
    public void register(String username, String password) throws RemoteException {
        try {
            clientHandler.register(username, password);
        } catch (SQLException ex) {
            throw new RemoteException("Couldn't register new user.");
        }
    }

    @Override
    public void unregister(String username, String password) throws RemoteException {
        try {
            clientHandler.unregister(username, password);
        } catch (SQLException ex) {
            throw new RemoteException("Couldn't unregister user.");
        }
    }

    @Override
    public long login(Client client, String username, String password) throws RemoteException, AccessException {
        try {
            if(clientHandler.login(username, password))
                return clientHandler.addClient(username, client);
            //client.printMessage("bad username/password");
            
        } catch (SQLException ex) {
            Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
        }
        throw new AccessException("Error logging in.");
    }

    @Override
    public void logout(long id) throws RemoteException {
            clientHandler.removeClient(id);
    }

    @Override
    public void list(long id) throws RemoteException, AccessException {
        String name = clientHandler.getUsername(id);
        if (name!= null) {
            try {
                clientHandler.printAtClient(id, clientHandler.getFiles(name));
            } catch (SQLException ex) {
                ex.printStackTrace();
                throw new AccessException("Error listing files");
            }
        } else {
            throw new AccessException("Not logged in");
        }
    }

    @Override
    public void upload(long id, String name, long size, boolean pub, boolean read, boolean write) throws RemoteException, AccessException {
        String userName = clientHandler.getUsername(id);
        if (userName != null) {
            try {
                if (clientHandler.uploadFile(id, name, size, pub, read, write))
                    clientHandler.notify(name, userName, "modified");
            } catch (SQLException ex) {
                ex.printStackTrace();
                throw new AccessException("Error uploading");
            }
        } else {
            throw new AccessException("Not logged in");
        }
    }

    @Override
    public FileHolder download(long id, String name) throws RemoteException, AccessException {
        String userName = clientHandler.getUsername(id);
        if (userName != null) {
            try {
                FileHolder file = null;
                if ((file = clientHandler.downloadFile(id, name)) != null)
                    clientHandler.notify(name, userName, "downloaded");
                return file;
            } catch (SQLException ex) {
                ex.printStackTrace();
                throw new AccessException("Error downloading");
            }
        } else {
            throw new AccessException("Not logged in");
        }
    }

    @Override
    public void delete(long id, String name) throws RemoteException, AccessException {
        String userName = clientHandler.getUsername(id);
        if (userName != null) {
            try {
                if (clientHandler.deleteFile(id, name))
                    clientHandler.notify(name, userName, "deleted");
            } catch (SQLException ex) {
                ex.printStackTrace();
                throw new AccessException("Error deleting file");
            }
        } else {
            throw new AccessException("Not logged in");
        }
    }

    @Override
    public void addNotification(long id, String name) throws RemoteException, AccessException {
        String userName = clientHandler.getUsername(id);
        if (userName != null) {
            try {
                clientHandler.addNotify(id, name);
            } catch (SQLException ex) {
                ex.printStackTrace();
                throw new AccessException("Error adding notifier");
            }
        } else {
            throw new AccessException("Not logged in");
        }
    }

}
