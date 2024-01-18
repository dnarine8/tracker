package com.cobra.tracker;

import com.cobra.tracker.db.repo.DataStoreFactory;

import java.io.File;

public class BaseTest {
    private static final String BASE_DIR = "testdir";
    private static final String DATA_DIR = "data";

    private final String baseDir;
    public BaseTest(){
        ClassLoader classLoader = getClass().getClassLoader();
        File file = new File(classLoader.getResource(BASE_DIR).getFile());
        DataStoreFactory.setBaseDir(file.getAbsolutePath() + File.separator + DATA_DIR);
        baseDir = file.getAbsolutePath() + File.separator ;

    }

    protected String createSourceDir(String filename){
        String sourceDir = baseDir + filename;
        File f = new File(sourceDir);
        if (f.exists()){
            f.delete();
        }
        f.mkdir();
        return sourceDir;
    }
}
