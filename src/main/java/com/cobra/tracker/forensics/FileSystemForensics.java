package com.cobra.tracker.forensics;

import com.cobra.tracker.db.*;
import com.cobra.tracker.util.CobraException;
import com.cobra.tracker.util.LogUtil;

import java.io.*;
import java.util.HashMap;

public class FileSystemForensics implements Forensics {
    private final HashMap<String, ForensicData> table = new HashMap<>();
    private InventoryStore inventoryStore = null;

    @Override
    public String buildInventory(String sourceDir) {
        try {
            LogUtil.info("FileSystemForensics", String.format("Building inventory for %s.", sourceDir));
            inventoryStore = DataStoreFactory.createInventoryStore();
            processDir(new File(sourceDir));
            inventoryStore.write();
            LogUtil.info("FileSystemForensics", String.format("Built inventory for %s.", sourceDir));
            return inventoryStore.getInventoryDir();

        } finally {
            if (inventoryStore != null) {
                inventoryStore.close();
            }
        }
    }

    @Override
    public void diff(String oldInventoryDir,String newInventoryDir) throws CobraException {
        InventoryStore oldinventoryStore = DataStoreFactory.loadInventoryStore(oldInventoryDir);
        oldinventoryStore.read();
    //    oldinventoryStore.dumpTable();

        InventoryStore newinventoryStore = DataStoreFactory.loadInventoryStore(newInventoryDir);
        newinventoryStore.read();
    //    newinventoryStore.dumpTable();

        DiffResultsStore resultsStore = DataStoreFactory.createDiffResultsStore();
        resultsStore.diff(oldinventoryStore,newinventoryStore);
    }



    private void processDir(File file ) {
        LogUtil.info(String.format("Processing directory %s.", file.getPath()));
        File[] contents = file.listFiles();
        if (contents != null) {
            for (int i = 0; i < contents.length; i++) {
                processFile(contents[i].getAbsolutePath());
                processDir(contents[i]);
            }
        }
    }

    public void processFile(String filename) {
        try {
            FileInfo fileInfo = new FileInfo(filename);
            fileInfo.setNew();
            inventoryStore.add(fileInfo);
//            table.put(fileInfo.key(), fileInfo);
        } catch (CobraException e) {
            try {
                inventoryStore.writeError(String.format("Error: Failed to process %s. %s.", filename, e.getMessage()));
            } catch (CobraException ex) {
                LogUtil.error(String.format("Error: Failed to process %s. %s.", filename, e.getMessage()), e);
            }
        }
    }

}


