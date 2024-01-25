package com.cobra.forensics.audit;

import com.cobra.forensics.db.repo.DiffResultsStore;
import com.cobra.forensics.db.repo.InventoryStore;
import com.cobra.forensics.util.CobraException;

import java.io.File;

abstract public class GenericAuditor implements Auditor {


    /**
     * Compares two inventory and logs the differences.
     * @param oldInventoryDir the old inventory directory
     * @param newInventoryDir the new inventory directory
     * @return a summary of the differences.
     * @throws CobraException for any errors
     */
    @Override
    public DiffSummary diff(String oldInventoryDir,String newInventoryDir,DiffResultsStore resultsStore) throws CobraException {

        InventoryStore oldInventoryStore = loadInventory(oldInventoryDir);
        InventoryStore newInventoryStore = loadInventory(newInventoryDir);
        return resultsStore.diff(oldInventoryStore,newInventoryStore);
    }



    private InventoryStore loadInventory(String inventoryDirName) throws CobraException {
        File f = new File(inventoryDirName);
        if (!f.exists()){
            throw new CobraException(String.format("Invalid inventory directory %s. Directory does not exist.",inventoryDirName));
        }
        InventoryStore inventoryStore = new InventoryStore(inventoryDirName);
        inventoryStore.read();
        return  inventoryStore;

    }
}
