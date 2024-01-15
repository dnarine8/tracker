package com.cobra.tracker.forensics;

import com.cobra.tracker.util.CobraException;

public interface Forensics {
    void buildInventory(String outputDir) throws CobraException;
    void diff(Forensics forensics);
}
