package com.cobra.tracker;

import com.cobra.tracker.util.CobraException;
import com.cobra.tracker.util.Constants;
import com.cobra.tracker.util.CryptoUtil;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;


/**
 * Goal 1 - Search through user account
 * Step 1 Start from root dir, get list of dir, for each file/dir
 * create an an instance of FileInfo, add the following
 * - the file/name
 * - if file or dir
 * - hidden
 * - read only, write, executable
 * - last date/time modified
 * if directory, call step 1
 */


/**
 *
 */
public class FileInfo implements Serializable {
    public enum FileStatus {unknown, created, modified, nochange, deleted};
    private String filename;
    private boolean isDir;
    private boolean isHidden;
    private boolean readOnly;
    private boolean readWrite;
    private boolean isExecutable;
    private long lastModifiedDate;
    private long length;
    private byte [] hash;
    private transient FileStatus status = FileStatus.unknown;


    /**
     * Build the inventory for the given file.
     * @param filename the filepath
     */
    public FileInfo(String filename) throws CobraException {
        this.filename = filename;
        File f = new File(filename);
        isDir = f.isDirectory();
        isHidden = f.isHidden();
        readOnly = f.canRead() && !f.canWrite();
        readWrite = f.canRead() && f.canWrite();
        isExecutable = f.canExecute();
        lastModifiedDate = f.lastModified();
        length = f.length();
        hash = hashFile();
    }

    public FileInfo(String filename, boolean isDir, boolean isHidden, boolean readOnly,
                    boolean readWrite, boolean isExecutable, long lastModifiedDate,
                    long length, byte [] hash){
        this.filename = filename;
        this.isDir = isDir;
        this.isHidden = isHidden;
        this.readOnly = readOnly;
        this.readWrite = readWrite;
        this.isExecutable = isExecutable;
        this.lastModifiedDate = lastModifiedDate;
        this.length = length;
        this.hash = hash;
    }

    public FileInfo(File f) {
        filename = f.getPath();
        isDir = f.isDirectory();
        isHidden = f.isHidden();
        readOnly = f.canRead() && !f.canWrite();
        readWrite = f.canRead() && f.canWrite();
        lastModifiedDate = f.lastModified();
        isExecutable = f.canExecute();
        length = f.length();
    }


    public String toString() {

        String.format("filename=%s, isDir = %b, isHidden=%b, readOnly=%b, readWrite= %b,isExecutable= %s,lastModifiedDate = lastModifiedDate;\n" +
                "        this.length = length;\n" +
                "        this.hash = hash;")
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

    public void setNew() {
        status = FileStatus.created;
    }

    public void setDeleted() {
        status = FileStatus.deleted;
    }

    public void setModified() {
        status = FileStatus.modified;
    }

    public void setNoChange() {
        status = FileStatus.nochange;
    }


    public boolean equals(Object o) {
        boolean isEqual = false;
        if (o != null) {
            if (o instanceof FileInfo) {
                FileInfo f = (FileInfo) o;
                if (f == this || f.filename.equals(this.filename)) {
                    isEqual = true;
                }
            }
        }
        return isEqual;
    }

    public byte[] hashFile() throws CobraException {
        try {
            Path path = Paths.get(filename);

            byte[] data = Files.readAllBytes(path);
            byte[] fileHash = CryptoUtil.hash(Constants.SHA256, data);
            return fileHash;
        } catch (IOException e) {
            throw new CobraException(String.format("Failed to read file: %s.", filename));
        }
    }

    public int hashCode() {
        return this.filename.hashCode();
    }

    public String key() {
        return this.filename;
    }

    public boolean isDeleted() {
        return (FileStatus.created != status) &&
                (FileStatus.modified != status) &&
                (FileStatus.nochange != status);
    }

    public void checkAndSetFileChange(FileInfo newFile) {
        if ((this.lastModifiedDate != newFile.lastModifiedDate ||
                this.length != newFile.length)) {
            status = FileStatus.modified;
            System.out.println("Change: " + this.toString());
        } else {
            status = FileStatus.nochange;
        }
    }

}
