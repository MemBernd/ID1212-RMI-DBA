/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package common;

import java.io.File;
import java.io.Serializable;

/**
 *
 * @author Bernardo
 */
public class FileHolder implements Serializable{
    
    public FileHolder(String name, long size, boolean pub, boolean read, boolean write,
            String owner) {
        this.name = name;
        this.size = size;
        this.pub = pub;
        this.read = read;
        this.write = write;
        this.owner = owner;
    }
    
    public FileHolder(File file, boolean pub, boolean read, boolean write) {
        name = file.getName();
        size = file.length();
        this.pub = pub;
        this.read = read;
        this.write = write;
    }
    
    public String toString() {
        String file = "Filename: " + name;
        file += "\tsize: " + size;
        file += "\tpublic: " + pub;
        file += "\tpublic read: " + read;
        file += "\tpublic write: " + write;
        file += "\towner: " + owner;
        return file;
    }
    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the size
     */
    public long getSize() {
        return size;
    }

    /**
     * @param size the size to set
     */
    public void setSize(long size) {
        this.size = size;
    }

    /**
     * @return the owner
     */
    public String getOwner() {
        return owner;
    }

    /**
     * @param owner the owner to set
     */
    public void setOwner(String owner) {
        this.owner = owner;
    }

    /**
     * @return the pub
     */
    public boolean isPub() {
        return pub;
    }

    /**
     * @param pub the pub to set
     */
    public void setPub(boolean pub) {
        this.pub = pub;
    }

    /**
     * @return the read
     */
    public boolean isRead() {
        return read;
    }

    /**
     * @param read the read to set
     */
    public void setRead(boolean read) {
        this.read = read;
    }

    /**
     * @return the write
     */
    public boolean isWrite() {
        return write;
    }

    /**
     * @param write the write to set
     */
    public void setWrite(boolean write) {
        this.write = write;
    }
    private String name;
    private long size;
    private String owner;
    private boolean pub;
    private boolean read;
    private boolean write;
    
}
