package com.cobra.forensics;

import com.cobra.forensics.db.model.FileInfo;
import com.cobra.forensics.util.CobraException;
import org.junit.Test;

public class FileInfoTest {

    @Test
    public void create() throws CobraException {
        FileInfo fileInfo = new FileInfo("junk.txt");
        System.out.println(fileInfo);

    }
}