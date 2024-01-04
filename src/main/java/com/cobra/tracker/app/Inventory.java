package com.cobra.tracker.app;

import com.cobra.tracker.FileInfo;
import com.cobra.tracker.util.CobraException;

import java.io.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;

public class Inventory {
    private HashMap<String, FileInfo> table = null;

    public void readTable(String inventoryFilename) throws CobraException {
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

    public void processFile(String filename) throws CobraException {
        FileInfo fileInfo = new FileInfo(filename);

        // 1 - Check to see if file exists
        FileInfo fileInfoInInventory = table.get(fileInfo.key());
        if (fileInfoInInventory != null){
            // file exist, now update the status accordingly
            fileInfoInInventory.checkAndSetFileChange(fileInfo);
            table.put(fileInfo.key(),fileInfo);
        }else {
            // new file, just add to inventory
            fileInfo.setNew();
            table.put(fileInfo.key(), fileInfo);
        }

    }

    public void dumpTable() {
        Collection<FileInfo> values = table.values();
        Iterator itr = values.iterator();
        while (itr.hasNext()) {
            System.out.println(itr.next());
        }
    }

    public void removeDeleteEntries(){
        Collection<FileInfo> values = table.values();
        ArrayList<String> deleteList = new ArrayList<String>();
        Iterator<FileInfo> itr = values.iterator();
        System.out.println("removing deleted entries");
        while (itr.hasNext()) {
            FileInfo fi = itr.next();
            if (fi.isDeleted()) {
                System.out.println(fi);
                deleteList.add(fi.key());
            }
        }

        Iterator<String> itrDelete = deleteList.iterator();
        while (itrDelete.hasNext()) {
            table.remove(itrDelete.next());
        }
        System.out.println("removed deleted entries");

    }

    public void writeTable(String inventoryFileName) throws CobraException {
        try {
            File file = new File(inventoryFileName);
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
