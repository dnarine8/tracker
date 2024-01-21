package com.cobra.forensics.audit;

import com.cobra.forensics.db.model.FileInfo;
import com.cobra.forensics.db.model.ForensicData;
import com.cobra.forensics.db.repo.InventoryStore;
import com.cobra.forensics.util.CobraException;
import com.cobra.forensics.util.LogUtil;

import java.io.*;
import java.util.HashMap;

public class FileSystemAuditor extends GenericAuditor {
    private final HashMap<String, ForensicData> table = new HashMap<>();
    private static final String TYPE = "FILES";

    @Override
    public InventorySummary buildInventory(InventoryStore inventoryStore, String sourceDir) throws CobraException {
        try {
            processDir(inventoryStore, new File(sourceDir));
            inventoryStore.write();
            InventorySummary summary = new InventorySummary();
            summary.setInventoryDirName(inventoryStore.getInventoryDir());
            summary.setKeys(inventoryStore.getKeys());
            return summary;
        } finally {
            if (inventoryStore != null) {
                inventoryStore.close();
            }
        }
    }

    public String getType(){
        return TYPE;
    }


    private void processDir(InventoryStore inventoryStore, File file ) {
        LogUtil.info(String.format("Processing directory %s.", file.getPath()));
        File[] contents = file.listFiles();
        if (contents != null) {
            for (int i = 0; i < contents.length; i++) {
                processFile(inventoryStore,contents[i].getAbsolutePath());
                if (contents[i].isDirectory()) {
                    processDir(inventoryStore,contents[i]);
                }
            }
        }
    }

    public void processFile(InventoryStore inventoryStore,String filename) {
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

