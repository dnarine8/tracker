package com.cobra.tracker.forensics.app;

import com.cobra.tracker.forensics.FileSystemForensics;
import com.cobra.tracker.util.CobraException;
import org.junit.Test;

import java.io.File;


public class FileSystemForensicsTest {

    @Test
    public void createInventory()  {
        String sourceDir = "C:\\Users\\dev\\Desktop\\repo\\cobra";
        FileSystemForensics forensics = new FileSystemForensics();
        forensics.buildInventory(sourceDir);
    }

    @Test
    public void diff() throws CobraException {
        String sourceDir = "C:\\Users\\dev\\Desktop\\repo\\cobra";

        FileSystemForensics forensics = new FileSystemForensics();
        String inventoryDir = forensics.buildInventory(sourceDir);

        FileSystemForensics forensics2 = new FileSystemForensics();
        String inventoryDir2 = forensics2.buildInventory(sourceDir);

        FileSystemForensics analyze = new FileSystemForensics();
        analyze.diff(extractDirName(inventoryDir),extractDirName(inventoryDir2));
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