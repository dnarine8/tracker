package com.tracker;


import java.io.File;
import java.io.Serializable;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 * Goal 1 - Search through user account
 * Step 1 Start from root dir, get list of dir, for each file/dir
 *    create an an instance of FileInfo, add the following
 *     - the file/name
 *     - if file or dir
 *     - hidden
 *     - read only, write, executable
 *     - last date/time modified
 *    if directory, call step 1
 *
 *
 */


/**
 *
 */
public class FileInfo implements Serializable {
    public enum FileStatus {created,modified, nochange,deleted};
    private String filename;
    private boolean isDir;
    private boolean isHidden;
    private boolean readOnly;
    private boolean readWrite;
    private boolean executable;
    private long lastModifiedDate;
    private long length;
    private transient FileStatus status = FileStatus.deleted;


    public FileInfo(File f){
        filename = f.getPath();
        isDir = f.isDirectory();
        isHidden = f.isHidden();
        readOnly = f.canRead() && !f.canWrite();
        readWrite = f.canRead() && f.canWrite();
        lastModifiedDate = f.lastModified();
        length = f.length();
        status = FileStatus.deleted;
    }

    public String toString(){
       StringBuilder buffer = new StringBuilder();
       buffer.append(filename);
       buffer.append(" status=");
       buffer.append(status);
       buffer.append(" dir=");
       buffer.append(isDir);
       buffer.append(" hidden=");
       buffer.append(isHidden);
       buffer.append(" readonly=");
       buffer.append(readOnly);
       buffer.append(" readwrite=");
       buffer.append(readWrite);       
       buffer.append(" date=");
       buffer.append(lastModifiedDate);
       buffer.append(" length=");
       buffer.append(length);
       
       return buffer.toString();
    }
    public void setNew(){
        status = FileStatus.created;
    }

    public void setDeleted(){
        status = FileStatus.deleted;
    }

    public void setModified(){
        status = FileStatus.modified;
    }
    public void setNoChange() {
        status = FileStatus.nochange;
    }


    public boolean equals(Object o){
        boolean isEqual = false;
        if (o != null) {
            if (o instanceof FileInfo) {
                FileInfo f = (FileInfo)o;
                if (f == this || f.filename.equals(this.filename)){
                    isEqual = true;
                }
            }
        }
        return isEqual;
    }

    public int hashCode(){
        return this.filename.hashCode();
    }

    public String key(){
        return this.filename;
    }

    public boolean isDeleted() {
        return (FileStatus.created != status) &&
                (FileStatus.modified != status) &&
                (FileStatus.nochange != status);
    }

    public void checkAndSetFileChange(FileInfo newFile){
        if((this.lastModifiedDate != newFile.lastModifiedDate ||
                this.length != newFile.length)){
           status =  FileStatus.modified;
           System.out.println("Change: " + this.toString());
        }
        else {
            status = FileStatus.nochange;
        }
    }

}
