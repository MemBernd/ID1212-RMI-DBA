/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server.model;

import common.AccessException;
import common.Client;
import common.FileHolder;
import java.rmi.RemoteException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.StringJoiner;
import server.storage.DBStorage;

/**
 *
 * @author Bernardo
 */
public class ClientHandler {
    private Map<Long, Client> clients = new HashMap();
    private Map<Long, String> clientNames = new HashMap();
    private Map<String, Client> clientsToNotify = new HashMap();
    private final Random randomId = new Random();
    private final DBStorage storage;
    
    public ClientHandler() {
        storage = new DBStorage();
    }
    
    public synchronized boolean login(String username, String password) throws SQLException {
        return storage.login(username, password);
    }

    public synchronized long addClient(String username, Client client) {
        long id = randomId.nextLong();
        clients.put(id, client);
        clientNames.put(id, username);
        return id;
    }
    
    public boolean uploadFile(long id, String name, long size, 
            boolean pub, boolean read, boolean write) throws SQLException {
        FileHolder file = new FileHolder(name, size, pub, read, write, clientNames.get(id));
        return storage.upload(file, clientNames.get(id));
    }
    
    public boolean notificationIsValid(long id, String fileName) {
        //check if owner matches and if file is public
        return false;
    }
    
    public void notify(String fileName, String user, String action) throws RemoteException {
        Client client;
        if((client = clientsToNotify.get(fileName)) != null) {
            client.printMessage(fileName + " was " + action + " by " + user);
        }
    }
    
    public void addNotify(String fileName, long id) throws AccessException {
        if(notificationIsValid(id, fileName)) {
            clientsToNotify.put(fileName, getClient(id));
        } else {
            throw new AccessException("Not the owner");
        }
    }
    
    public boolean canRead(long id, String fileName) throws SQLException, AccessException {
        FileHolder file = storage.getFile(fileName);
        if (file == null)
            throw new AccessException("File not available");
        if (file.getOwner().equals(clientNames.get(id)))
            return true;
        if (file.isPub() && file.isRead())
            return true;
        return false;
    }
    
    public boolean canWrite(long id, String fileName) throws SQLException {
        FileHolder file = storage.getFile(fileName);
        if (file == null)
            return true;
        if (file.getOwner().equals(clientNames.get(id)))
            return true;
        if (file.isPub() && file.isWrite())
            return true;
        return false;
    }
    
    public String getFiles(String username) throws SQLException {
        StringJoiner joiner = new StringJoiner("\n");
        for (FileHolder file : storage.getFiles(username)){
            joiner.add(file.toString());
        }
        return joiner.toString();
    }

    public void printAtClient(long id, String txt) throws RemoteException {
        getClient(id).printMessage(txt);
    }

    public String getUsername(long id) {
        return clientNames.get(id);
    }

    public Client getClient(long id) {
        return clients.get(id);
    }

    public void removeClient(long id) {
        clients.remove(id);
        clientNames.remove(id);
    }


}
