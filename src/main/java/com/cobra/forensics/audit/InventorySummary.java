package com.cobra.forensics.audit;

import java.util.Set;

public class InventorySummary {

    private String inventoryDirName;
    private Set<String> keys;
    private String timeStamp;

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

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

}
