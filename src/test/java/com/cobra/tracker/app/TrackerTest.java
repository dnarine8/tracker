package com.cobra.tracker.app;

import com.cobra.tracker.util.CobraException;
import org.junit.Test;

import java.io.File;

import static org.junit.Assert.*;

public class TrackerTest {

    @Test
    public void diff() throws CobraException {
        String dbFilename = "db.bin";
        String testDir = "testdir";
        ClassLoader classLoader = getClass().getClassLoader();
        File file = new File(classLoader.getResource(testDir).getFile());
        String absolutePath = file.getAbsolutePath();

        Tracker tracker = new Tracker(String.format("%s%s%s",absolutePath,File.separator,dbFilename));
        String sourceDir = String.format("%s%s%s",absolutePath,File.separator,"source");
        System.out.println(absolutePath);
        System.out.println(sourceDir);
        tracker.diff(sourceDir);
    }
}