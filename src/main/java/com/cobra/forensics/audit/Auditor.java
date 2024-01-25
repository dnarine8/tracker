package com.cobra.forensics.audit;

import com.cobra.forensics.db.repo.DiffResultsStore;
import com.cobra.forensics.db.repo.InventoryStore;
import com.cobra.forensics.util.CobraException;

public interface Auditor {
    InventorySummary buildInventory(InventoryStore inventoryStore, String sourceDir) throws CobraException;
    boolean supportInventory();
    DiffSummary diff(String oldInventoryDir, String newInventoryDir, DiffResultsStore resultsStore ) throws CobraException;
    String getType();
}
