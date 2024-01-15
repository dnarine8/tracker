package com.cobra.tracker.app;

import com.cobra.tracker.util.CobraException;
import com.cobra.tracker.util.Constants;
import com.cobra.tracker.util.CryptoUtil;
import org.junit.Test;

import java.io.File;

import static org.junit.Assert.*;

public class TrackerTest {

    @Test
    public void diff() throws CobraException {
    //    String sourceDir = "C:\\Users\\dev\\Desktop\\repo\\cobra";
        String sourceDir = "C:\\Users\\dev";

        String dbFilename = "db.bin";
        String testDir = "testdir";
        ClassLoader classLoader = getClass().getClassLoader();
        File file = new File(classLoader.getResource(testDir).getFile());
        String absolutePath = file.getAbsolutePath();

//        Tracker tracker = new Tracker(String.format("%s%s%s",absolutePath,File.separator,dbFilename));
        Tracker tracker = new Tracker(dbFilename);

        //String sourceDir = String.format("%s%s%s",absolutePath,File.separator,"source");
        System.out.println(absolutePath);
        System.out.println(sourceDir);
        tracker.diff(sourceDir);
    }

    @Test
    public void testhash() throws CobraException {
        String filename = "C:\\Users\\dev\\AppData\\Local\\Comms\\UnistoreDB\\store.jfm";
        CryptoUtil.hash(Constants.MD_SHA256, filename, new File(filename).length());
    }
}