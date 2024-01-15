package com.cobra.tracker.forensics;

import com.cobra.tracker.db.FileInfo;
import com.cobra.tracker.db.DataStoreFactory;
import com.cobra.tracker.db.InventoryStore;
import com.cobra.tracker.util.CobraException;
import com.cobra.tracker.util.LogUtil;

import java.io.*;
import java.util.HashMap;

public class FileSystemForensics implements Forensics {
    private final HashMap<String, FileInfo> table = new HashMap<>();

    @Override
    public void buildInventory(String sourceDir) {
        InventoryStore inventoryStore = null;
        try {
            LogUtil.info("FileSystemForensics", String.format("Building inventory for %s.", sourceDir));
            inventoryStore = DataStoreFactory.createInventoryStore();
            processDir(new File(sourceDir),inventoryStore);
            inventoryStore.write(table);
            LogUtil.info("FileSystemForensics", String.format("Built inventory for %s.", sourceDir));

        } finally {
            if (inventoryStore != null) {
                inventoryStore.close();
            }
        }
    }

    @Override
    public void diff(Forensics forensics) {

    }


    private void processDir(File file,InventoryStore inventoryStore ) {
        LogUtil.info(String.format("Processing directory %s.", file.getPath()));
        File[] contents = file.listFiles();
        if (contents != null) {
            for (int i = 0; i < contents.length; i++) {
                processFile(contents[i].getAbsolutePath(),inventoryStore);
                processDir(contents[i],inventoryStore);
            }
        }
    }

    public void processFile(String filename,InventoryStore inventoryStore) {
        try {
            FileInfo fileInfo = new FileInfo(filename);
            fileInfo.setNew();
            table.put(fileInfo.key(), fileInfo);
        } catch (CobraException e) {
            try {
                inventoryStore.writeError(String.format("Error: Failed to process %s. %s.", filename, e.getMessage()));
            } catch (CobraException ex) {
                LogUtil.error(String.format("Error: Failed to process %s. %s.", filename, e.getMessage()), e);
            }
        }
    }

}


