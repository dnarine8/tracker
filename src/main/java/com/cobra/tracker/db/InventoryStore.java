package com.cobra.tracker.db;

import com.cobra.tracker.util.CobraException;
import com.cobra.tracker.util.LogUtil;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

/**
 * Stores data for an inventory.
 */
public class InventoryStore extends StatusFileStore {
    private final String DATABASE = "db.bin";
    private final String dbName;
    private final String inventoryDir;

    public InventoryStore(String inventoryDir) {
        super(inventoryDir);
        this.inventoryDir = inventoryDir;
        dbName = inventoryDir + DATABASE;
        LogUtil.info("Inventory", dbName);
    }

    public Map<String, FileInfo> read() throws CobraException {
        try {
            File file = new File(dbName);
            LogUtil.info(String.format("Loading inventory from %s.", dbName));

            if (file.exists()) {
                try (FileInputStream f = new FileInputStream(file);
                     ObjectInputStream s = new ObjectInputStream(f)) {
                    Map<String, FileInfo> table = (HashMap<String, FileInfo>) s.readObject();
                    LogUtil.info(String.format("Loaded inventory from %s.", dbName));
                    return table;
                }
            } else {
                LogUtil.warn(String.format("Failed to load inventory. %s does not exist.", dbName));
                throw new CobraException(String.format("Failed to load inventory. %s does not exist.", dbName));
            }
        } catch (IOException | ClassNotFoundException e) {
            LogUtil.error(String.format("Failed to load inventory from %s.", dbName), e);
            throw new CobraException(String.format("Failed to load inventory from %s.", dbName));
        }
    }

    public void write(Map<String, FileInfo> table) {
        try {
            File file = new File(dbName);
            try (FileOutputStream f = new FileOutputStream(file);
                 ObjectOutputStream s = new ObjectOutputStream(f)) {
                s.writeObject(table);
            }
        } catch (IOException e) {
            LogUtil.error(String.format("Failed to write inventory to %s.", dbName), e);
        }
    }

    public String getInventoryDir() {
        return inventoryDir;
    }
}
