package com.cobra.tracker.db;

import java.io.Serializable;

/**
 * Base class for all forensic data. Contains the status, useful for tracking diffs.
 */
public abstract class ForensicData implements Serializable {
    /**
     * The status is set during the comparison of two forensic object. Status of an object can be
     * determined as follows:
     * NEW : if only present in the newer object (or the second object in a diff command)
     * MODIFIED: if present in both object, but differ in hash value or an identifiable value
     * NO_CHANGE: the objects are identical, i.e. isEquals return true.
     * UNKNOWN: Usually indicates the object no longer exists, equivalent to being deleted.
     */
    public enum FileStatus {UNKNOWN, NEW, MODIFIED, NO_CHANGE};

    /**
     * The status is set during the comparison of two forensic object.
     */
    private FileStatus status = FileStatus.UNKNOWN;

    /**
     * To be implemented by subclasses to return a value that uniquely identifies an object.
     * @return unique value to be used as a key (e.g. in a hashtable, database record etc.)
     */
    public abstract String key();

    public void setNew() {
        status = FileStatus.NEW;
    }
    public void setModified() {
        status = FileStatus.MODIFIED;
    }
    public void setNoChange() {
        status = FileStatus.NO_CHANGE;
    }


    public boolean isDeleted() {
        return (FileStatus.NEW != status) &&
                (FileStatus.MODIFIED != status) &&
                (FileStatus.NO_CHANGE != status);
    }

    @Override
    public String toString() {
        return String.format("Status=%s", status);
    }
}
