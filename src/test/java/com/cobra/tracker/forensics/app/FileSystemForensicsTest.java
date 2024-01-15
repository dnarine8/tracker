package com.cobra.tracker.forensics.app;

import com.cobra.tracker.forensics.FileSystemForensics;
import org.junit.Test;


public class FileSystemForensicsTest {

    @Test
    public void createInventory()  {
        String sourceDir = "C:\\Users\\dev\\Desktop\\repo\\cobra";
        FileSystemForensics forensics = new FileSystemForensics();
        forensics.buildInventory(sourceDir);
    }
}