package com.cobra.tracker.forensics;

import com.cobra.tracker.util.CobraException;

public interface Forensics {
    String buildInventory(String outputDir) throws CobraException;
    void diff(String oldInventoryDir,String newInventoryDir) throws CobraException;
}
