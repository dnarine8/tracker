package com.cobra.tracker;

import com.cobra.tracker.util.CobraException;
import org.junit.Test;

import static org.junit.Assert.*;

public class FileInfoTest {

    @Test
    public void create() throws CobraException {
        FileInfo fileInfo = new FileInfo("junk.txt");
        System.out.println(fileInfo);

    }
}