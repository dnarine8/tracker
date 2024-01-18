package com.cobra.tracker.forensics;

import java.util.Set;

public class InventorySummary {
    private String inventoryDirName;
    private Set<String> keys;

    public String getInventoryDirName() {
        return inventoryDirName;
    }

    public void setInventoryDirName(String inventoryDirName) {
        this.inventoryDirName = inventoryDirName;
    }

    public Set<String> getKeys() {
        return keys;
    }

    public void setKeys(Set<String> keys) {
        this.keys = keys;
    }
}
