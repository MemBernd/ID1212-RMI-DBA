/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client.view;

import client.*;
import common.AccessException;
import common.Client;
import common.FileHolder;
import common.Server;
import java.io.File;
import java.io.UncheckedIOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Scanner;

/**
 *
 * @author Bernardo
 */
public class Interpreter extends Thread {
    private final Scanner consoleInput = new Scanner(System.in);
    private static final String PROMPT = "> ";
    private boolean exit = false;
    private final PrintUsingThread put = new PrintUsingThread();
    private final ConsoleOutput output;
    private Server server;
    private long id;
    
    private static final String help = "commands: " +
                "\nconnect: connect [host]" +
                "\nlogin: login [username] [password]" +
                "\nlogout: logout" +
                "\nregister: register [username] [password]" +
                "\nunregister: unregister [username] [password]" +
                "\nupload: upload [filename] [public] [read] [write] (true/false)" +
                "\ndownload: download [filename]" +
                "\ndelete: delete [filename]" +
                "\nnotify: notify [filename]";

    public Interpreter(Server server) throws RemoteException{
        output = new ConsoleOutput();
        this.server = server;
        new Thread(this).start();
    }

    @Override
    public void run() {
        put.println(help);
        while (!exit) {
            try {
                CmdHandling cmd = new CmdHandling(readLine());
                //System.out.println(cmd.getCmd());
                switch (cmd.getCmd()) {
                    case CONNECT:
                        findServer(cmd.getArgument(0));
                        break;
                    case REGISTER:
                        server.register(cmd.getArgument(0), cmd.getArgument(1));
                        System.out.println("Successful: register.");
                        break;
                    case UNREGISTER:
                        server.unregister(cmd.getArgument(0), cmd.getArgument(1));
                        System.out.println("Successful: unregister.");
                        break;
                    case LOGIN:
                        id = server.login(output, cmd.getArgument(0), cmd.getArgument(1));
                        System.out.println("Successful: login.");
                        break;
                    case LOGOUT:
                        server.logout(id);
                        System.out.println("Successful: logout.");
                        break;
                    case LIST:
                        server.list(id);
                        break;
                    case UPLOAD:
                        Path path = Paths.get("");
                        String file = cmd.getArgument(0);
                        String text = path.toAbsolutePath().resolve(file).toString();
                        long size = new File(text).length();
                        server.upload(id, cmd.getArgument(0),
                                size,
                                Boolean.parseBoolean(cmd.getArgument(1)),
                                Boolean.parseBoolean(cmd.getArgument(2)),
                                Boolean.parseBoolean(cmd.getArgument(3)));
                        System.out.println("Successful: upload.");
                        break;
                    case DOWNLOAD:
                        FileHolder temp = server.download(id, cmd.getArgument(0));
                        int tempSize = (int) temp.getSize();
                        byte[] data = new byte[tempSize];
                        Files.write(Paths.get(temp.getName()), data);
                        System.out.println("Successful: downloaded " + temp.getName() + ".");
                        break;
                    case NOTIFY:
                        server.addNotification(id, cmd.getArgument(0));
                        System.out.println("Successful: added notifications listener.");
                        break;
                    case DELETE:
                        server.delete(id, cmd.getArgument(0));
                        System.out.println("Successful: delete.");
                        break;
                    case QUIT:
                        exit = true;
                        //insert code to quit, essentially logout first and then quit
                        break;
                    case HELP:
                        System.out.println(help);
                        break;
                    default:
                        System.out.println("unknown command. try 'help' for help.");

                }
            }  catch (RemoteException e) {
                System.out.println(e.getMessage());
            } catch (AccessException e) {
                System.out.println(e.getMessage());
            } catch (Exception e) {
                put.println("Problem with operation");
                e.printStackTrace();
            } 
        }
    }

    private void findServer(String host) throws NotBoundException, MalformedURLException, RemoteException {
        server = (Server) Naming.lookup("//" + host + "/" + Server.SERVER_NAME);
    }
    private String readLine() {
        put.print(PROMPT);
        return consoleInput.nextLine();
    }

    private class ConsoleOutput extends UnicastRemoteObject implements Client {

        public ConsoleOutput() throws RemoteException {
        }

        @Override
        public void printMessage (String output) {
            put.println(output);
            put.print(PROMPT);
        }
        
        @Override
        public void printWithoutPrompt(String output) {
            put.println(output);
        }
    }
}
