package com.cobra.tracker.forensics;

import com.cobra.tracker.db.model.FileInfo;
import com.cobra.tracker.db.model.ForensicData;
import com.cobra.tracker.db.repo.DataStoreFactory;
import com.cobra.tracker.db.repo.DiffResultsStore;
import com.cobra.tracker.db.repo.InventoryStore;
import com.cobra.tracker.util.CobraException;
import com.cobra.tracker.util.LogUtil;

import java.io.*;
import java.util.HashMap;

public class FileSystemAuditor extends GenericAuditor {
    private final HashMap<String, ForensicData> table = new HashMap<>();
    private InventoryStore inventoryStore = null;
    private static final String TYPE = "FILES";

    @Override
    public InventorySummary buildInventory(InventoryStore inventoryStore, String sourceDir){
        try {
            LogUtil.info("FileSystemForensics", String.format("Building inventory for %s.", sourceDir));
            this.inventoryStore = inventoryStore;
//            inventoryStore = DataStoreFactory.createInventoryStore();
            processDir(new File(sourceDir));
            inventoryStore.write();
            LogUtil.info("FileSystemForensics", String.format("Built inventory for %s.", sourceDir));
            InventorySummary summary = new InventorySummary();
            summary.setInventoryDirName(inventoryStore.getInventoryDir());
            summary.setKeys(inventoryStore.getKeys());
            return  summary;
        } finally {
            if (inventoryStore != null) {
                inventoryStore.close();
            }
        }

    }

    @Override
    public InventorySummary buildInventory(String sourceDir) {
        try {
            LogUtil.info("FileSystemForensics", String.format("Building inventory for %s.", sourceDir));
            inventoryStore = DataStoreFactory.createInventoryStore();
            processDir(new File(sourceDir));
            inventoryStore.write();
            LogUtil.info("FileSystemForensics", String.format("Built inventory for %s.", sourceDir));
            InventorySummary summary = new InventorySummary();
            summary.setInventoryDirName(inventoryStore.getInventoryDir());
            summary.setKeys(inventoryStore.getKeys());
            return  summary;
        } finally {
            if (inventoryStore != null) {
                inventoryStore.close();
            }
        }
    }

    public String getType(){
        return TYPE;
    }

    @Override
    public DiffSummary diff(String oldInventoryDir,String newInventoryDir) throws CobraException {
        InventoryStore oldInventoryStore = DataStoreFactory.loadInventoryStore(oldInventoryDir);
        InventoryStore newInventoryStore = DataStoreFactory.loadInventoryStore(newInventoryDir);
        DiffResultsStore resultsStore = DataStoreFactory.createDiffResultsStore();
        return resultsStore.diff(oldInventoryStore,newInventoryStore);
    }



    private void processDir(File file ) {
        LogUtil.info(String.format("Processing directory %s.", file.getPath()));
        File[] contents = file.listFiles();
        if (contents != null) {
            for (int i = 0; i < contents.length; i++) {
                processFile(contents[i].getAbsolutePath());
                if (contents[i].isDirectory()) {
                    processDir(contents[i]);
                }
            }
        }
    }

    public void processFile(String filename) {
        try {
            FileInfo fileInfo = new FileInfo(filename);
            fileInfo.setNew();
            inventoryStore.add(fileInfo);
        } catch (CobraException e) {
            try {
                inventoryStore.writeError(String.format("Error: Failed to process %s. %s.", filename, e.getMessage()));
            } catch (CobraException ex) {
                LogUtil.error(String.format("Error: Failed to process %s. %s.", filename, e.getMessage()), e);
            }
        }
    }

}


