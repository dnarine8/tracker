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

    private final String dbFilename;
    private final Inventory inventory = new Inventory();

    public Tracker(String dbFilename) throws CobraException {
        this.dbFilename = dbFilename;
        inventory.readTable(dbFilename);


    }

    public void diff(String sourceDirName) throws CobraException {
        File source = new File(sourceDirName);

          processDir(source);
          inventory.removeDeleteEntries();
          inventory.writeTable(dbFilename);
          System.out.println("Save to file: " + dbFilename);
     //       ProcessInfo.getProcessInfo();

    }

    public void dump(){
        inventory.dumpTable();
    }
    private void processDir(File file) throws CobraException {
        File [] contents = file.listFiles();
        if (contents != null) {
            for (int i = 0; i < contents.length; i++) {
                inventory.processFile(contents[i].getAbsolutePath());
                processDir(contents[i]);
            }
        }

    }
}
