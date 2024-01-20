package com.cobra.tracker.forensics;

import com.cobra.tracker.db.repo.DiffResultsStore;
import com.cobra.tracker.db.repo.InventoryStore;
import com.cobra.tracker.util.CobraException;

public interface Auditor {
    InventorySummary buildInventory(InventoryStore inventoryStore, String sourceDir) throws CobraException;
    DiffSummary diff(String oldInventoryDir, String newInventoryDir, DiffResultsStore resultsStore ) throws CobraException;
    String getType();
}
