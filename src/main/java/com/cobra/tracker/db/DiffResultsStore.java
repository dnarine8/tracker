package com.cobra.tracker.db;

import com.cobra.tracker.app.DataStore;
import com.cobra.tracker.util.CobraException;
import com.cobra.tracker.util.LogUtil;

import java.io.File;
import java.util.Collection;

public class DiffResultsStore extends StatusFileStore {
    private final String NEW_FILES = "new.txt";
    private final String MODIFIED_FILES = "modified.txt";
    private final String DELETED_FILES = "deleted.txt";
    private final String UNCHANGED_FILES = "nochange.txt";


    private final DataStore newFiles;
    private final DataStore modifiedFiles;
    private final DataStore deletedFiles;
    private final DataStore unchangedFiles;
    private final String diffResultsDir;

    public DiffResultsStore(String outputDir) {
        super(outputDir);
        this.diffResultsDir = outputDir;
        String newFilesEntryFilename = outputDir + NEW_FILES;
        String modifiedFilesEntryFilename = outputDir + MODIFIED_FILES;
        String deletedFilesEntryFilename = outputDir + DELETED_FILES;
        String unchangedFilesEntryFilename = outputDir + UNCHANGED_FILES;

        newFiles = new DataStore(newFilesEntryFilename);
        modifiedFiles = new DataStore(modifiedFilesEntryFilename);
        deletedFiles = new DataStore(deletedFilesEntryFilename);
        unchangedFiles = new DataStore(unchangedFilesEntryFilename);
        LogUtil.info("Results", String.format("Filename with new entries is %s.", newFilesEntryFilename));
        LogUtil.info("Results", String.format("Filename with modified entries is %s.", modifiedFilesEntryFilename));
        LogUtil.info("Results", String.format("Filename with deleted entries is %s.", deletedFilesEntryFilename));
        LogUtil.info("Results", String.format("Filename with unchanged entries is %s.", unchangedFilesEntryFilename));
    }

    public void diff(InventoryStore oldInventory, InventoryStore newInventory) throws CobraException {
        for (ForensicData forensicData : oldInventory.getAllData()) {
            ForensicData object = newInventory.remove(forensicData.key());
            if (object == null) {
                writeDeletedFiledEntry(forensicData);
            } else {
                if (object.equals(forensicData)) {
                    writeUnchangedFiledEntry(forensicData);
                } else {
                    writeModifiedFileEntry(forensicData);
                }
            }
        }
        for (ForensicData forensicData : newInventory.getAllData()) {
            writeNewFileEntry(forensicData);
        }
    }

    public String getDiffResultsDir() {
        return diffResultsDir;
    }

    public void writeNewFileEntry(ForensicData forensicData) throws CobraException {
        newFiles.write(forensicData.toString());
    }

    public void writeModifiedFileEntry(ForensicData forensicData) throws CobraException {
        modifiedFiles.write(forensicData.toString());
    }

    public void writeDeletedFiledEntry(ForensicData forensicData) throws CobraException {
        deletedFiles.write(forensicData.toString());
    }

    public void writeUnchangedFiledEntry(ForensicData forensicData) throws CobraException {
        unchangedFiles.write(forensicData.toString());
    }

    public void close() {
        super.close();
        newFiles.close();
        modifiedFiles.close();
        deletedFiles.close();
        unchangedFiles.close();
    }
}
