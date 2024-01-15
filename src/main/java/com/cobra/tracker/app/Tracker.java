package com.cobra.tracker.app;

import com.cobra.tracker.util.CobraException;

public class Tracker {

    private final Inventory inventory;

    public Tracker(String dbFilename) throws CobraException {
        this.inventory = new Inventory(dbFilename);
        inventory.readTable();

    }

    public void buildInventory(String filename){
        Inventory currentInventory = new Inventory(filename);

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

    public void dump() throws CobraException {
        inventory.dumpTable();
    }
}
