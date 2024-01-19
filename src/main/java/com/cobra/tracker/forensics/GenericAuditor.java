package com.cobra.tracker.forensics;

import com.cobra.tracker.db.repo.DataStoreFactory;
import com.cobra.tracker.db.repo.DiffResultsStore;
import com.cobra.tracker.db.repo.InventoryStore;
import com.cobra.tracker.util.CobraException;

abstract public class GenericAuditor implements Auditor {


    /**
     * Compares two inventory and logs the differences.
     * @param oldInventoryDir the old inventory directory
     * @param newInventoryDir the new inventory directory
     * @return a summary of the differences.
     * @throws CobraException for any errors
     */
    protected DiffSummary diffInventory(String oldInventoryDir, String newInventoryDir) throws CobraException {
        InventoryStore oldInventoryStore = DataStoreFactory.loadInventoryStore(oldInventoryDir);
        InventoryStore newInventoryStore = DataStoreFactory.loadInventoryStore(newInventoryDir);
        DiffResultsStore resultsStore = DataStoreFactory.createDiffResultsStore();
        return resultsStore.diff(oldInventoryStore,newInventoryStore);
    }
}
