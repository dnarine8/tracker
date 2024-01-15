package com.cobra.tracker.db;

import com.cobra.tracker.util.CobraException;
import com.cobra.tracker.util.Constants;
import com.cobra.tracker.util.CryptoUtil;
import com.cobra.tracker.util.DataConverter;

import java.io.File;
import java.io.Serializable;
import java.util.Arrays;


/**
 * Goal 1 - Search through user account
 * Step 1 Start from root dir, get list of dir, for each file/dir
 * create an  instance of FileInfo, add the following
 * - the file/name
 * - if file or dir
 * - hidden
 * - read only, write, executable
 * - last date/time modified
 * if directory, call step 1
 */


/**
 * Represents a File or Directory.
 */
public class FileInfo extends ForensicData {
    public enum FileStatus {unknown, created, modified, nochange, deleted};
    private String filename;
    private boolean isDir;
    private boolean isHidden;
    private boolean readOnly;
    private boolean readWrite;
    private boolean isExecutable;
    private long lastModifiedDate;
    private long length;
    private byte[] hash = new byte[]{0};
    private transient FileStatus status = FileStatus.unknown;


    /**
     * Build the inventory for the given file.
     *
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
        if (!isDir) {
            hash = hashFile();
        }
        status = FileStatus.unknown;
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
        StringBuilder buffer = new StringBuilder();
        buffer.append(filename).append(',');
        if (status == null){
            buffer.append(FileStatus.unknown).append(',');
        }else {
            buffer.append(status).append(',');
        }
        if (isDir) {
            buffer.append('D');
        } else {
            buffer.append('F');
        }
        buffer.append(',');
        if (isHidden) {
            buffer.append("H,");
        }
        if (readWrite) {
            buffer.append("RW,");
        } else if (readOnly) {
            buffer.append("R,");
        }
        if (isExecutable) {
            buffer.append("X,");
        }

        buffer.append("Date=");
        buffer.append(lastModifiedDate);
        buffer.append(", Length=");
        buffer.append(length);
        buffer.append(", Hash=");
        buffer.append(DataConverter.binaryToHex(hash));

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
        return CryptoUtil.hash(Constants.MD_SHA256, filename, length);
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

    public boolean equals(FileInfo newFile) {
        if (this == newFile){
            return true;
        }
        return  this.lastModifiedDate == newFile.lastModifiedDate &&
                this.length == newFile.length &&
                Arrays.equals(hash,newFile.hash);
    }

    public void checkAndSetFileChange(FileInfo newFile) {
        if ((this.lastModifiedDate != newFile.lastModifiedDate ||
                this.length != newFile.length || !Arrays.equals(hash,newFile.hash))) {
            status = FileStatus.modified;
            System.out.println("Change: " + this.toString());
        } else {
            status = FileStatus.nochange;
        }
    }

}
