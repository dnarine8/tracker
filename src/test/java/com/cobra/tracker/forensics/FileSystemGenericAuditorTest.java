package com.cobra.tracker.forensics;

import com.cobra.tracker.BaseTest;
import com.cobra.tracker.util.CobraException;
import org.junit.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;


public class FileSystemGenericAuditorTest extends BaseTest {

    @Test
    public void createInventory() throws FileNotFoundException, CobraException {
        String sourceDir = createSourceDir("source");

        // add file1.txt, file2.txt
        createFile(sourceDir + File.separator + "file1.txt", "this is a test");
        createFile(sourceDir + File.separator + "file2.txt", "this is a test test");

        // add new dir with file1.txt and file2.txt
        String newDir = sourceDir + File.separator + "dir1";
        (new File(newDir)).mkdir();
        createFile(newDir + File.separator + "file1.txt", "this is a test");
        createFile(newDir + File.separator + "file2.txt", "this is a test test");

        FileSystemAuditor forensics = new FileSystemAuditor();
        InventorySummary summary = forensics.buildInventory(sourceDir);
        String inventoryDir = summary.getInventoryDirName();

        Set<String> keys = summary.getKeys();

        assertEquals(keys.size(),5);

        // verify inventory directory created
        File f = new File(inventoryDir);
        assertTrue(f.exists());
        (new File(sourceDir)).delete();
    }

    @Test
    public void diff() throws CobraException, FileNotFoundException, InterruptedException {
        String sourceDir = createSourceDir("source");

        // add file1.txt, file2.txt
        createFile(sourceDir + File.separator + "file1.txt", "this is a test");
        createFile(sourceDir + File.separator + "file2.txt", "this is a test test");

        // add new dir with file1.txt and file2.txt
        String newDir = sourceDir + File.separator + "dir1";
        (new File(newDir)).mkdir();
        createFile(newDir + File.separator + "file1.txt", "this is a test");
        createFile(newDir + File.separator + "file2.txt", "this is a test test");

        // create first inventory
        FileSystemAuditor forensics = new FileSystemAuditor();
        String inventory1Dir = forensics.buildInventory(sourceDir).getInventoryDirName();

        Thread.sleep(2000);

        // change file2
        createFile(sourceDir + File.separator + "file2.txt", "this is a test");

        // add new file to dir1
        createFile(newDir + File.separator + "file3.txt", "this is a test test");

        // delete file1 from dir1
        (new File(newDir + File.separator + "file1.txt")).delete();

        // create second inventory
        FileSystemAuditor forensics2 = new FileSystemAuditor();
        String inventory2Dir = forensics2.buildInventory(sourceDir).getInventoryDirName();


        System.out.println("Diffing " + inventory1Dir + " and " + inventory2Dir);

        FileSystemAuditor analyze = new FileSystemAuditor();
        DiffSummary diffSummary = analyze.diff(extractDirName(inventory1Dir),extractDirName(inventory2Dir));

        System.out.println(diffSummary.getChangeItems());
        System.out.println(diffSummary.getDeletedItems());
        System.out.println(diffSummary.getNewItems());
        System.out.println(diffSummary.getUnchangeItems());
        (new File(sourceDir)).delete();

    }


    private void createFile(String name, String data) throws FileNotFoundException {
        try (PrintWriter out = new PrintWriter(name)) {
            out.println(data);
        }
    }

    private String extractDirName(String inventoryDir){
        int index = inventoryDir.lastIndexOf(File.separator);
        int startIndex;
        int endIndex;
        if (index == inventoryDir.length() - 1){
            endIndex = index;
            startIndex = inventoryDir.lastIndexOf(File.separator, endIndex - 1);
        } else {
            startIndex = index;
            endIndex = inventoryDir.length() - 1;
        }
        return inventoryDir.substring(++startIndex,endIndex);

    }
}