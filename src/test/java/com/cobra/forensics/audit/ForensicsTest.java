package com.cobra.forensics.audit;

import com.cobra.forensics.BaseTest;
import com.cobra.forensics.app.Forensics;
import com.cobra.forensics.util.CobraException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Set;

import static org.junit.Assert.*;

public class ForensicsTest extends BaseTest {

    private String sourceDir;
    private String newDir;
    @Before
    public void setup() throws FileNotFoundException, CobraException {
        sourceDir = createSourceDir("source");

        // add file1.txt, file2.txt
        createFile(sourceDir + File.separator + "file1.txt", "this is a test");
        createFile(sourceDir + File.separator + "file2.txt", "this is a test test");

        // add new dir with file1.txt and file2.txt
        newDir = sourceDir + File.separator + "dir1";
        (new File(newDir)).mkdir();
        createFile(newDir + File.separator + "dir1file1.txt", "this is a test");
        createFile(newDir + File.separator + "dir1file2.txt", "this is a test test");

    }

    @After
    public void tearDown() throws FileNotFoundException {
        delete(new File(sourceDir));
    }

    @Test
    public void buildInventory() {
        Forensics forensics = new Forensics();
        InventorySummary [] result = forensics.buildInventory(sourceDir);
        InventorySummary summary = result[0];
        String inventoryDir = summary.getInventoryDirName();
        assertTrue(containBaseDir(inventoryDir));

        Set<String> keys = summary.getKeys();

        assertEquals(keys.size(),5);

        // verify inventory directory created
        File f = new File(inventoryDir);
        assertTrue(f.exists());
    }


    @Test
    public void diff() throws FileNotFoundException, CobraException {
        Forensics forensics = new Forensics();
        InventorySummary [] result = forensics.buildInventory(sourceDir);
        InventorySummary summary = result[0];
        String inventory1Dir = summary.getTimeStamp();

        // change file2
        createFile(sourceDir + File.separator + "file2.txt", "this is a test");

        // add new file to dir1
        createFile(newDir + File.separator + "dir1file3.txt", "this is a test test");

        // delete file1 from dir1
        (new File(newDir + File.separator + "dir1file1.txt")).delete();

        Forensics forensics2 = new Forensics();
        result = forensics2.buildInventory(sourceDir);
        summary = result[0];
        String inventory2Dir = summary.getTimeStamp();

        Forensics forensics3 = new Forensics();
        DiffSummary []diffSummaries = forensics.diff(inventory1Dir,inventory2Dir);
        System.out.println("old inventory dir is " + inventory1Dir);
        System.out.println("new inventory dir is " + inventory2Dir);

        System.out.println(diffSummaries[0]);

    }

/*    public void diff() throws CobraException {
        String sourceDir = "C:\\Users\\dev\\Desktop\\repo\\cobra";

        FileSystemAuditor forensics = new FileSystemAuditor();
        String inventoryDir = forensics.buildInventory(sourceDir).getInventoryDirName();

        FileSystemAuditor forensics2 = new FileSystemAuditor();
        String inventoryDir2 = forensics2.buildInventory(sourceDir).getInventoryDirName();

        FileSystemAuditor analyze = new FileSystemAuditor();
        analyze.diff(extractDirName(inventoryDir),extractDirName(inventoryDir2));
    }*/
    private String extractDirName(String inventoryDir){
        int index = inventoryDir.indexOf("inventory");
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

    private void createFile(String name, String data) throws FileNotFoundException {
        try (PrintWriter out = new PrintWriter(name)) {
            out.println(data);
        }
    }

}