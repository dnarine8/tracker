package com.cobra.tracker.app;

import com.cobra.tracker.FileInfo;
import com.cobra.tracker.util.CobraException;

import java.io.*;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;

public class Inventory {
    private HashMap<String, FileInfo> table = null;
    private final String inventoryFilename;

    public Inventory(String inventoryFilename){
        this.inventoryFilename = inventoryFilename;
    }
    public void readTable() throws CobraException {
        try {
            File file = new File(inventoryFilename);

            if (file.exists()) {
                try (FileInputStream f = new FileInputStream(file);
                     ObjectInputStream s = new ObjectInputStream(f)) {
                    table = (HashMap<String, FileInfo>) s.readObject();
                    System.out.println("Loaded inventory.");
                }
            } else {
                table = new HashMap<>();
                System.out.println("Created an empty inventory.");
            }
        } catch (IOException | ClassNotFoundException e) {
            throw new CobraException("Failed to load inventory.");
        }
    }

    public void diff(String sourceDirName) throws CobraException {
        File source = new File(sourceDirName);
        HashMap<String, FileInfo> oldTable = table;
        table = new HashMap<>();

        processDir(source,oldTable);
        System.out.println("Deleted entries: ");
        dumpTable(oldTable);
        writeTable();
    }

    private void processDir(File file, HashMap<String, FileInfo> oldTable) throws CobraException {
        File [] contents = file.listFiles();
        if (contents != null) {
            for (int i = 0; i < contents.length; i++) {
                processFile(contents[i].getAbsolutePath(),oldTable);
                processDir(contents[i], oldTable);
            }
        }

    }

    public void processFile(String filename, HashMap<String, FileInfo> oldTable) throws CobraException {
        FileInfo fileInfo = new FileInfo(filename);

        // 1 - Remove file entry from old table if it exists
        FileInfo fileInfoInInventory = oldTable.remove(fileInfo.key());
        if (fileInfoInInventory != null){
            // file exist, now update the status accordingly
            if (fileInfoInInventory.equals(fileInfo)){
                fileInfo.setNoChange();
            }
            else {
                fileInfo.setModified();
            }
        } else {
            // new file, just add to inventory
            fileInfo.setNew();
        }
        table.put(fileInfo.key(), fileInfo);
        System.out.println(fileInfo);
    }

    public void dumpTable() {
        dumpTable(table);
    }

    public void dumpTable(HashMap<String, FileInfo> db) {
        Collection<FileInfo> values = db.values();
        Iterator itr = values.iterator();
        while (itr.hasNext()) {
            System.out.println(itr.next());
        }
    }


    public void writeTable() throws CobraException {
        try {
            File file = new File(inventoryFilename);
            try (FileOutputStream f = new FileOutputStream(file);
                 ObjectOutputStream s = new ObjectOutputStream(f)) {
                s.writeObject(table);
                s.close();
            }
        } catch (IOException e) {
            throw new CobraException("Failed to write inventory.");
        }
    }

}
