package com.cobra.tracker.app;

import com.cobra.tracker.db.FileInfo;
import com.cobra.tracker.util.CobraException;

import java.io.*;
import java.util.Collection;
import java.util.HashMap;

public class Inventory {
    private HashMap<String, FileInfo> table = null;
    private final String inventoryFilename;
    private final DataStore newFiles = new DataStore("results_new.txt");
    private final DataStore modifiedFiles = new DataStore("results_modified.txt");
    private final DataStore deletedFiles = new DataStore("results_deleted.txt");
    private final DataStore unchangedFiles = new DataStore("results_nochange.txt");
    private final DataStore errorFile = new DataStore("results_errors.txt");
    private final DataStore statusFile = new DataStore("results_status.txt");

    public Inventory(String inventoryFilename){
        this.inventoryFilename = inventoryFilename;
    }

    public void readTable() throws CobraException {
        try {
            File file = new File(inventoryFilename);
            statusFile.write(String.format("Loading inventory from %s.", inventoryFilename));

            if (file.exists()) {
                try (FileInputStream f = new FileInputStream(file);
                     ObjectInputStream s = new ObjectInputStream(f)) {
                    table = (HashMap<String, FileInfo>) s.readObject();
                    statusFile.write(String.format("Loaded inventory from %s.", inventoryFilename));
                }
            } else {
                table = new HashMap<>();
                statusFile.write(String.format("Created a empty inventory from %s.", inventoryFilename));
            }
        } catch (IOException | ClassNotFoundException e) {
            statusFile.write(String.format("Failed to load inventory from %s.",inventoryFilename));
        }
    }

    public void diff(String sourceDirName) throws CobraException {
        long startTime = System.currentTimeMillis();
        try {
            File source = new File(sourceDirName);
            HashMap<String, FileInfo> oldTable = table;
            table = new HashMap<>();

            processDir(source, oldTable);
//            System.out.println("Deleted entries: ");
//            dumpTable(oldTable);
            writeTable();
        }finally {
            long elapsedTime = System.currentTimeMillis() - startTime;
            long seconds = elapsedTime / 1000;
            long minutes = seconds / 60;
            seconds = seconds % 60;
            statusFile.write(String.format("Processing time: %d mins %d secs",minutes,seconds));
            newFiles.close();
            modifiedFiles.close();
            unchangedFiles.close();
            deletedFiles.close();
            errorFile.close();
            statusFile.close();
        }
    }

    private void processDir(File file, HashMap<String, FileInfo> oldTable) throws CobraException {
        statusFile.write(String.format("Processing directory %s.",file.getPath()));

        File [] contents = file.listFiles();
        if (contents != null) {
            for (int i = 0; i < contents.length; i++) {
                processFile(contents[i].getAbsolutePath(),oldTable);
                processDir(contents[i], oldTable);
            }
        }

    }

    public void processFile(String filename, HashMap<String, FileInfo> oldTable)  {
        try {
            FileInfo fileInfo = new FileInfo(filename);

            // 1 - Remove file entry from old table if it exists
            FileInfo fileInfoInInventory = oldTable.remove(fileInfo.key());
            if (fileInfoInInventory != null) {
                // file exist, now update the status accordingly
                if (fileInfoInInventory.equals(fileInfo)) {
                    fileInfo.setNoChange();
                    unchangedFiles.write(fileInfo.toString());
                } else {
                    fileInfo.setModified();
                    modifiedFiles.write(fileInfo.toString());
                }
            } else {
                // new file, just add to inventory
                fileInfo.setNew();
                newFiles.write(fileInfo.toString());
            }
            table.put(fileInfo.key(), fileInfo);
        }catch (CobraException e){
            try {
                errorFile.write(String.format("Error: Failed to process %s. %s.",filename, e.getMessage()));
            } catch (CobraException ex) {
                System.out.println(String.format("Error: Failed to process %s. %s.",filename, e.getMessage()));
            }
        }
    }

    public void dumpTable() throws CobraException {
        dumpTable(table);
    }

    public void dumpTable(HashMap<String, FileInfo> db) throws CobraException {
        Collection<FileInfo> values = db.values();
        for (FileInfo fileInfo: values){
            deletedFiles.write(fileInfo.toString());
        }
    }


    public void writeTable() throws CobraException {
        try {
            File file = new File(inventoryFilename);
            try (FileOutputStream f = new FileOutputStream(file);
                 ObjectOutputStream s = new ObjectOutputStream(f)) {
                s.writeObject(table);
            }
        } catch (IOException e) {
            statusFile.write(String.format("Failed to write inventory to %s.", inventoryFilename));
        }
    }

}
