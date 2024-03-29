package com.cobra.forensics.db.repo;

import com.cobra.forensics.db.model.ForensicData;
import com.cobra.forensics.util.CobraException;
import com.cobra.forensics.util.LogUtil;

import java.io.*;
import java.util.Collection;
import java.util.HashMap;
import java.util.Set;

/**
 * Stores data for an inventory.
 */
public class InventoryStore extends ErrorFileStore {

    private static final String TAG = "INVENTORY";
    private final String FILENAME_SYNTAX = "%sdb.bin";

    private final String dbName;
    private final String inventoryDir;

    private HashMap<String, ForensicData> table = new HashMap<>();

    public InventoryStore(String inventoryDir) {
        super(inventoryDir);
        this.inventoryDir = inventoryDir;
        dbName = String.format(FILENAME_SYNTAX, inventoryDir);
        LogUtil.info(TAG, String.format("Inventory database name is %s.", dbName));
    }

    public void add(ForensicData forensicData) {
        table.put(forensicData.key(), forensicData);
    }

    public ForensicData remove(ForensicData forensicData) {
        return table.remove(forensicData.key());
    }

    public Collection<ForensicData> getAllData() {
        return table.values();
    }

    public Set<String> getKeys() {
        return table.keySet();
    }

    /**
     * Loads the inventory from file.
     *
     * @return the inventory
     * @throws CobraException in case of errors
     */
    public void read() throws CobraException {
        try {
            File file = new File(dbName);
            LogUtil.info(TAG, String.format("Loading inventory from %s.", dbName));

            if (file.exists()) {
                try (FileInputStream fileInputStream = new FileInputStream(file);
                     ObjectInputStream output = new ObjectInputStream(fileInputStream)) {
                    Object obj = output.readObject();
                    if (obj instanceof HashMap) {
                        table = (HashMap<String, ForensicData>) obj;
                        LogUtil.info(TAG,String.format("Successfully loaded inventory from %s.", dbName));
                    } else {
                        throw new CobraException(String.format("Failed to load inventory from %s. Invalid format.", dbName));
                    }
                }
            } else {
                throw new CobraException(String.format("Failed to load inventory. %s does not exist.", dbName));
            }
        } catch (IOException | ClassNotFoundException e) {
            LogUtil.error(String.format("Failed to load inventory from %s.", dbName), e);
            throw new CobraException(String.format("Failed to load inventory from %s.", dbName));
        }
    }

    public void write() throws CobraException {
        try {
            File file = new File(dbName);
            try (FileOutputStream f = new FileOutputStream(file);
                 ObjectOutputStream s = new ObjectOutputStream(f)) {
                s.writeObject(table);
            }
        } catch (IOException e) {
            LogUtil.error(String.format("Failed to write inventory to %s.", dbName), e);
            throw new CobraException(String.format("Failed to write inventory to %s.", dbName));
        }
    }

    public String getInventoryDir() {
        return inventoryDir;
    }

    public void dumpTable() throws CobraException {
        dumpTable(table);
    }

    public void dumpTable(HashMap<String, ForensicData> db) {
        Collection<ForensicData> values = db.values();
        for (ForensicData fileInfo : values) {
            System.out.println(fileInfo);
        }
    }

}
