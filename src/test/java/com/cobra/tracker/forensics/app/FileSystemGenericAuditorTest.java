package com.cobra.tracker.forensics.app;

import com.cobra.tracker.forensics.FileSystemAuditor;
import com.cobra.tracker.util.CobraException;
import org.junit.Test;

import java.io.File;


public class FileSystemGenericAuditorTest {

    @Test
    public void createInventory()  {
        String sourceDir = "C:\\Users\\dev\\Desktop\\repo\\cobra";
        FileSystemAuditor forensics = new FileSystemAuditor();
        forensics.buildInventory(sourceDir);
    }

    @Test
    public void diff() throws CobraException {
        String sourceDir = "C:\\Users\\dev\\Desktop\\repo\\cobra";

        FileSystemAuditor forensics = new FileSystemAuditor();
        String inventoryDir = forensics.buildInventory(sourceDir).getInventoryDirName();

        FileSystemAuditor forensics2 = new FileSystemAuditor();
        String inventoryDir2 = forensics2.buildInventory(sourceDir).getInventoryDirName();

        FileSystemAuditor analyze = new FileSystemAuditor();
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