package com.cobra.forensics.db.model;

import com.cobra.forensics.util.*;

import java.io.File;
import java.util.Arrays;

/**
 * Represents a File or Directory.
 */
public class FileInfo extends ForensicData {
    private String path;
    private boolean isDir;
    private boolean isHidden;
    private boolean readOnly;
    private boolean readWrite;
    private boolean isExecutable;
    private long lastModifiedDate;
    private long length;
    private byte[] hash = new byte[]{0};

    /**
     * Build the inventory for the given file.
     *
     * @param path the filepath
     */
    public FileInfo(String path) throws CobraException {
        this(new File(path));
    }

    public FileInfo(File f) throws CobraException {
        path = f.getPath();
        isDir = f.isDirectory();
        isHidden = f.isHidden();
        readOnly = f.canRead() && !f.canWrite();
        readWrite = f.canRead() && f.canWrite();
        isExecutable = f.canExecute();
        lastModifiedDate = f.lastModified();
        length = f.length();
        if (!isDir) {
            hash = CryptoUtil.hash(Constants.MD_SHA256, path, length);
        }
    }


    @Override
    public String toString() {
        StringBuilder buffer = new StringBuilder();
        buffer.append(path).append(',');
        buffer.append(super.toString());
        buffer.append(',');
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
            buffer.append("RW");
        } else if (readOnly) {
            buffer.append('R');
        }
        if (isExecutable) {
            buffer.append('X');
        }
        buffer.append(',');
        buffer.append("Date=");
        buffer.append(ForensicsUtil.dateToString(lastModifiedDate));
        buffer.append(", Length=");
        buffer.append(length);
        buffer.append(", Hash=");
        buffer.append(DataConverter.binaryToHex(hash));
        return buffer.toString();
    }

    @Override
    public boolean equals(Object obj) {
        boolean isEqual = false;
        if (obj != null) {
            if (obj instanceof FileInfo) {
                FileInfo fileInfo = (FileInfo) obj;
                isEqual = fileInfo.path.equals(this.path) &&
                        Arrays.equals(hash, fileInfo.hash) &&
                        this.lastModifiedDate == fileInfo.lastModifiedDate &&
                        this.length == fileInfo.length;
            }
        }
        return isEqual;
    }


    public int hashCode() {
        return path.hashCode();
    }

    /**
     * The full path uniquely identifies a file/dir.
     * @return the filename
     */
    public String key() {
        return this.path;
    }

}
