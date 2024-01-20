package com.cobra.tracker.forensics;

import com.cobra.tracker.BaseTest;
import com.cobra.tracker.util.CobraException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Set;

import static org.junit.Assert.*;

public class ForensicsTest extends BaseTest {

    private String sourceDir;

    @Before
    public void setup() throws FileNotFoundException, CobraException {
        sourceDir = createSourceDir("source");

        // add file1.txt, file2.txt
        createFile(sourceDir + File.separator + "file1.txt", "this is a test");
        createFile(sourceDir + File.separator + "file2.txt", "this is a test test");

        // add new dir with file1.txt and file2.txt
        String newDir = sourceDir + File.separator + "dir1";
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
    public void diff() {
    }

    private void createFile(String name, String data) throws FileNotFoundException {
        try (PrintWriter out = new PrintWriter(name)) {
            out.println(data);
        }
    }

}