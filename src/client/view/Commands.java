/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client.view;

/**
 *
 * @author Bernardo
 */
public enum Commands {
    //connect to the server
    CONNECT,
    
    //quit the client
    QUIT,
    
    //invalid command
    INVALID,
    
    //login
    LOGIN,
    
    //logout
    LOGOUT,
    
    //register
    REGISTER,
    
    //unregister
    UNREGISTER,
    
    //list files
    LIST,
    
    //upload file
    UPLOAD,
    
    //download file
    DOWNLOAD,
    
    //notify for change in file
    NOTIFY,
    
    //delete file
    DELETE,
    
    //show help
    HELP
}
