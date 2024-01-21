package com.cobra.forensics.db.repo;

import com.cobra.forensics.db.model.ForensicData;
import com.cobra.forensics.audit.DiffSummary;
import com.cobra.forensics.util.CobraException;
import com.cobra.forensics.util.LogUtil;

public class DiffResultsStore extends ErrorFileStore {
    private static final String TAG = "RESULTS";
    private final String NEW_FILES = "%snew.txt";
    private final String MODIFIED_FILES = "%smodified.txt";
    private final String DELETED_FILES = "%sdeleted.txt";
    private final String UNCHANGED_FILES = "%snochange.txt";

    private final DataStore newFiles;
    private final DataStore modifiedFiles;
    private final DataStore deletedFiles;
    private final DataStore unchangedFiles;
    private final String diffResultsDir;

    public DiffResultsStore(String outputDir) {
        super(outputDir);
        this.diffResultsDir = outputDir;

        String newFilesEntryFilename = String.format(NEW_FILES, outputDir);
        String modifiedFilesEntryFilename = String.format(MODIFIED_FILES, outputDir);
        String deletedFilesEntryFilename = String.format(DELETED_FILES, outputDir);
        String unchangedFilesEntryFilename = String.format(UNCHANGED_FILES, outputDir);

        newFiles = new DataStore(newFilesEntryFilename);
        modifiedFiles = new DataStore(modifiedFilesEntryFilename);
        deletedFiles = new DataStore(deletedFilesEntryFilename);
        unchangedFiles = new DataStore(unchangedFilesEntryFilename);

        LogUtil.info(TAG, String.format("Filename with new entries is %s.", newFilesEntryFilename));
        LogUtil.info(TAG, String.format("Filename with modified entries is %s.", modifiedFilesEntryFilename));
        LogUtil.info(TAG, String.format("Filename with deleted entries is %s.", deletedFilesEntryFilename));
        LogUtil.info(TAG, String.format("Filename with unchanged entries is %s.", unchangedFilesEntryFilename));
    }

    public DiffSummary diff(InventoryStore oldInventory, InventoryStore newInventory) throws CobraException {
        try {
            DiffSummary diffSummary = new DiffSummary();
            diffSummary.setDirName(diffResultsDir);
            for (ForensicData forensicData : oldInventory.getAllData()) {
                ForensicData object = newInventory.remove(forensicData);
                if (object == null) {
                    deletedFiles.write(forensicData.toString());
                    diffSummary.incrementDeletedItems();
                } else {
                    if (object.equals(forensicData)) {
                        unchangedFiles.write(forensicData.toString());
                        diffSummary.incrementUnchangedItems();
                    } else {
                        modifiedFiles.write(forensicData.toString());
                        diffSummary.incrementChangedItems();
                    }
                }
            }
            for (ForensicData forensicData : newInventory.getAllData()) {
                newFiles.write(forensicData.toString());
                diffSummary.incrementNewItems();
            }
            return diffSummary;
        } finally {
            close();
        }
    }

    public void close() {
        super.close();
        newFiles.close();
        modifiedFiles.close();
        deletedFiles.close();
        unchangedFiles.close();
    }
}
