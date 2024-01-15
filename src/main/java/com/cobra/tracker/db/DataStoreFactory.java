package com.cobra.tracker.db;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DataStoreFactory {
    private static final String BASE_DIR = "data";
    private static final String INVENTORY_BASE_DIR = BASE_DIR + File.separator + "inventory" + File.separator;
    private static final String RESULTS_BASE_DIR = BASE_DIR + File.separator + "results" + File.separator;

    public static InventoryStore createInventoryStore() {
        String inventoryDirName = INVENTORY_BASE_DIR + getNewDirName() + File.separator;
        File f = new File(inventoryDirName);
        f.mkdirs();
        return new InventoryStore(inventoryDirName);
    }

    public static DiffResultsStore createDiffResultsStore() {
        String diffResultsDirName = RESULTS_BASE_DIR + getNewDirName() + File.separator;
        File f = new File(diffResultsDirName);
        f.mkdirs();
        return new DiffResultsStore(diffResultsDirName);
    }

    private static String getNewDirName() {
        long currentDateTime = System.currentTimeMillis();
        Date currentDate = new Date(currentDateTime);
        DateFormat df = new SimpleDateFormat("dd_MM_yy_HH_mm_ss");
        return df.format(currentDate);
    }
}
