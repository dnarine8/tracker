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
import java.util.HashSet;
import java.util.Set;
import java.util.prefs.Preferences;

import static org.junit.Assert.*;


public class ForensicsTest extends BaseTest {

    private String sourceDir;
    private String newDir;
    @Before
    public void setup() throws FileNotFoundException, CobraException {
        deleteSourceDir("source");

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
    public void tearDown() throws  CobraException {
        deleteSourceDir("source");
    }

    @Test
    public void buildInventory() {
        Forensics forensics = new Forensics();
        InventorySummary [] result = forensics.buildInventory(sourceDir);
        int expectedOfTypes = 4;
        assertEquals(expectedOfTypes,result.length);
        Set<String> auditorTypes = new HashSet<>();
        auditorTypes.add(FileSystemAuditor.TYPE);
        auditorTypes.add(ProcessAuditor.TYPE);
        auditorTypes.add(StartupAuditor.TYPE);
        auditorTypes.add(ServiceAuditor.TYPE);

        for (InventorySummary summary: result) {
            String inventoryDir = summary.getInventoryDirName();
            assertTrue(containBaseDir(inventoryDir));

            // verify inventory directory created
            File f = new File(inventoryDir);
            assertTrue(f.exists());

            int startIndex = inventoryDir.lastIndexOf(File.separator, inventoryDir.length() - 2) + 1;
            String type = inventoryDir.substring(startIndex, inventoryDir.length() - 1);
            auditorTypes.remove(type);
        }
        assertEquals(0,auditorTypes.size());
    }


    public void diff() throws FileNotFoundException {
        Forensics forensics = new Forensics();
        InventorySummary [] result = forensics.buildInventory(sourceDir);
//        InventorySummary summary = result[0];
        String inventory1Dir = getFilesInventoryTimestampDir(result);

        // change file2
        createFile(sourceDir + File.separator + "file2.txt", "this is a test");

        // add new file to dir1
        createFile(newDir + File.separator + "dir1file3.txt", "this is a test test");

        // delete file1 from dir1
        (new File(newDir + File.separator + "dir1file1.txt")).delete();

        Forensics forensics2 = new Forensics();
        result = forensics2.buildInventory(sourceDir);
     //   summary = result[0];
        String inventory2Dir = getFilesInventoryTimestampDir(result);

        Forensics forensics3 = new Forensics();
        DiffSummary []diffSummaries = forensics.diff(inventory1Dir,inventory2Dir);
        System.out.println("old inventory dir is " + inventory1Dir);
        System.out.println("new inventory dir is " + inventory2Dir);

        for (DiffSummary summary: diffSummaries) {
            System.out.println(summary);
        }

    }

    private String getFilesInventoryTimestampDir(InventorySummary [] result) {

        for (InventorySummary summary: result) {
            String inventoryDir = summary.getInventoryDirName();
            int startIndex = inventoryDir.lastIndexOf(File.separator, inventoryDir.length() - 2) + 1;
            String type = inventoryDir.substring(startIndex, inventoryDir.length() - 1);
            if (type.equals(FileSystemAuditor.TYPE)){
                return summary.getTimeStamp();
            }
        }

        return null;
    }
    private void createFile(String name, String data) throws FileNotFoundException {
        try (PrintWriter out = new PrintWriter(name)) {
            out.println(data);
        }
    }

    @Test
    public void prototype(){
        Preferences preferences = Preferences.systemRoot();
        System.out.println("test");

    }
}