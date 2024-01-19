package com.cobra.tracker.db.repo;

import com.cobra.tracker.db.model.ForensicData;
import com.cobra.tracker.forensics.DiffSummary;
import com.cobra.tracker.util.CobraException;
import com.cobra.tracker.util.LogUtil;

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

    public DiffSummary diff(InventoryStore oldInventory, InventoryStore newInventory) throws CobraException {
        try {
            DiffSummary diffSummary = new DiffSummary();
            for (ForensicData forensicData : oldInventory.getAllData()) {
                ForensicData object = newInventory.remove(forensicData.key());
                if (object == null) {
                    writeDeletedFiledEntry(forensicData);
                    diffSummary.incrementDeletedItems();
                } else {
                    if (object.equals(forensicData)) {
                        writeUnchangedFiledEntry(forensicData);
                        diffSummary.incrementUnchangedItems();
                    } else {
                        writeModifiedFileEntry(forensicData);
                        diffSummary.incrementChangedItems();
                    }
                }
            }
            for (ForensicData forensicData : newInventory.getAllData()) {
                writeNewFileEntry(forensicData);
                diffSummary.incrementNewItems();
            }
            return diffSummary;
        } finally {
            close();
        }
    }

    public String getDiffResultsDir() {
        return diffResultsDir;
    }

    public void writeNewFileEntry(ForensicData forensicData) throws CobraException {
        newFiles.write(forensicData.toString());
        System.out.println("new entry: " + forensicData.toString());
    }

    public void writeModifiedFileEntry(ForensicData forensicData) throws CobraException {
        modifiedFiles.write(forensicData.toString());
        System.out.println("modified entry: " + forensicData.toString());

    }

    public void writeDeletedFiledEntry(ForensicData forensicData) throws CobraException {
        deletedFiles.write(forensicData.toString());
        System.out.println("deleted entry: " + forensicData.toString());

    }

    public void writeUnchangedFiledEntry(ForensicData forensicData) throws CobraException {
        unchangedFiles.write(forensicData.toString());
        System.out.println("unchanged entry: " + forensicData.toString());

    }

    public void close() {
        super.close();
        newFiles.close();
        modifiedFiles.close();
        deletedFiles.close();
        unchangedFiles.close();
    }
}
