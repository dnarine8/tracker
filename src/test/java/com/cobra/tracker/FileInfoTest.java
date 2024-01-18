package com.cobra.tracker;

import com.cobra.tracker.db.model.FileInfo;
import com.cobra.tracker.util.CobraException;
import org.junit.Test;

public class FileInfoTest {

    @Test
    public void create() throws CobraException {
        FileInfo fileInfo = new FileInfo("junk.txt");
        System.out.println(fileInfo);

    }
}