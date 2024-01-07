package com.cobra.tracker.app;

import com.cobra.tracker.FileInfo;
import com.cobra.tracker.ProcessInfo;
import com.cobra.tracker.util.CobraException;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Tracker {

    private final Inventory inventory;

    public Tracker(String dbFilename) throws CobraException {
        this.inventory = new Inventory(dbFilename);
        inventory.readTable();

    }

    public void diff(String sourceDirName) throws CobraException {
        inventory.diff(sourceDirName);
/*        File source = new File(sourceDirName);

          processDir(source);
          inventory.removeDeleteEntries();
          inventory.writeTable(dbFilename);
          System.out.println("Save to file: " + dbFilename);
     //       ProcessInfo.getProcessInfo();*/

    }

    public void dump(){
        inventory.dumpTable();
    }
}
