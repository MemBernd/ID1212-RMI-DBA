/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server.storage;

import common.Client;
import common.FileHolder;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Bernardo
 */
public class DBStorage {
    private static final String DB = "id1212";
    private static final String USER_TABLE = "user";
    private static final String FILE_TABLE = "file";
    private PreparedStatement registerUser;
    private PreparedStatement unregisterUser;
    private PreparedStatement loginUser;
    private PreparedStatement listFiles;
    private PreparedStatement uploadFile;
    private PreparedStatement getFile;
    private PreparedStatement deleteFile;
    


    public DBStorage() {
        try {
            prepareStatements(connect());
        } catch (ClassNotFoundException  | SQLException e) {
            e.printStackTrace();
        }
    }
    
    public boolean upload(FileHolder file, String owner) throws SQLException {
        uploadFile.setString(1, file.getName());
        uploadFile.setString(2, Long.toString(file.getSize()));
        uploadFile.setString(3, booleanToString(file.isPub()));
        uploadFile.setString(4, booleanToString(file.isRead()));
        uploadFile.setString(5, booleanToString(file.isWrite()));
        uploadFile.setString(6, owner);
        System.out.println(uploadFile.toString());
        return uploadFile.executeUpdate() == 1;
    }
    
    public FileHolder getFile(String fileName) throws SQLException {
        getFile.setString(1, fileName);
        try (ResultSet result = getFile.executeQuery()) {
            if(result.next())
                return new FileHolder(result.getString(1),
                                    Long.parseLong(result.getString(2)),
                                    toBoolean(result.getString(3)),
                                    toBoolean(result.getString(4)),
                                    toBoolean(result.getString(5)),
                                    result.getString(6));
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            return null;
        }
    }
    
    public boolean login(String username, String password) throws SQLException {
        loginUser.setString(1, username);
        loginUser.setString(2, password);
        //System.out.println(loginUser.toString());
        
        try (ResultSet result = loginUser.executeQuery()) {
            if(result.next())
                return true;
            return false;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public List<FileHolder> getFiles(String username) throws SQLException {
        List<FileHolder> files = new ArrayList();
        listFiles.setString(1, username);
        try (ResultSet result = listFiles.executeQuery()) {
            while(result.next()) {
                files.add(new FileHolder(result.getString(1),
                                    Long.parseLong(result.getString(2)),
                                    toBoolean(result.getString(3)),
                                    toBoolean(result.getString(4)),
                                    toBoolean(result.getString(5)),
                                    result.getString(6)));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return files;
    }
    
    private boolean toBoolean(String txt) {
        System.out.println(txt);
        if (txt.equals("1"))
            return true;
        else
            return false;
    }
    
    private String booleanToString(boolean acc) {
        if (acc)
            return "1";
        return "0";
    }


    private Connection connect() throws ClassNotFoundException, SQLException {
        Class.forName("com.mysql.jdbc.Driver");
        return DriverManager.getConnection("jdbc:mysql://localhost:3306/" + DB, DB, DB);
    }

    private void prepareStatements(Connection con) throws SQLException {
        registerUser = con.prepareStatement("Insert Into " + USER_TABLE +
                        " VALUES (?, ?)");
        unregisterUser = con.prepareStatement("Delete from " + USER_TABLE +
                        " Where name = ?");
        loginUser = con.prepareStatement("Select * from " + USER_TABLE +
                        " Where name = ? And password = ?");
        listFiles = con.prepareStatement("Select * from " + FILE_TABLE +
                        " Where `owner` = ? Or public = 1");
        uploadFile = con.prepareStatement("Insert into " + FILE_TABLE +
                        " (`name`, `size`, `public`, `read`, `write`, `owner`) Values " +
                        " (?, ?, ?, ?, ?, ?) On Duplicate key update " +
                        " `size` = Values(size), public = Values(public), " +
                        " `read` = Values(`read`), `write` = Values(`write`), `owner` = Values(`owner`)");
        getFile = con.prepareStatement("Select * from " + FILE_TABLE +
                        " Where name = ?");
        deleteFile = con.prepareStatement("Delete from " + FILE_TABLE +
                        " Where name = ?");
    }

}
