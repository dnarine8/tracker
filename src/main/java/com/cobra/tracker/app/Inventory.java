package com.cobra.tracker.app;

import com.cobra.tracker.FileInfo;
import com.cobra.tracker.util.CobraException;

import java.io.*;
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
                }
            } else {
                table = new HashMap<>();
            }
        } catch (IOException | ClassNotFoundException e) {
            throw new CobraException("Failed to load inventory.");
        }
    }

    public void dumpTable() {
        Collection<FileInfo> values = table.values();
        Iterator itr = values.iterator();
        while (itr.hasNext()) {
            System.out.println(itr.next());
        }
    }

    public void writeTable(String inventoryFileName) throws CobraException {
        try {
            File file = new File(inventoryFileName);
            try (FileOutputStream f = new FileOutputStream(file);
                 ObjectOutputStream s = new ObjectOutputStream(f)) {
                s.writeObject(table);
            }
        } catch (IOException e) {
            throw new CobraException("Failed to write inventory.");
        }
    }

}
