package com.cobra.tracker.db;

import java.io.Serializable;

public abstract class ForensicData implements Serializable {
    public enum FileStatus {UNKNOWN, NEW, MODIFIED, NO_CHANGE, DELETED};
    private FileStatus status = FileStatus.UNKNOWN;

    public void setNew() {
        status = FileStatus.NEW;
    }

    public void setDeleted() {
        status = FileStatus.DELETED;
    }

    public void setModified() {
        status = FileStatus.MODIFIED;
    }

    public void setNoChange() {
        status = FileStatus.NO_CHANGE;
    }

    public abstract String key();

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
