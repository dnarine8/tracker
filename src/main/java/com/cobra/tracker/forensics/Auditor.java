package com.cobra.tracker.forensics;

import com.cobra.tracker.db.repo.InventoryStore;
import com.cobra.tracker.util.CobraException;

public interface Auditor {
    InventorySummary buildInventory(String sourceDir) throws CobraException;
    InventorySummary buildInventory(InventoryStore inventoryStore, String sourceDir) throws CobraException;
    DiffSummary diff(String oldInventoryDir,String newInventoryDir) throws CobraException;
    String getType();
}
