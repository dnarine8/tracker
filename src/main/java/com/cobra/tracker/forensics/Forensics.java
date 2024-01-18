package com.cobra.tracker.forensics;

import com.cobra.tracker.util.CobraException;

public interface Forensics {
    InventorySummary buildInventory(String outputDir) throws CobraException;
    DiffSummary diff(String oldInventoryDir,String newInventoryDir) throws CobraException;
}
