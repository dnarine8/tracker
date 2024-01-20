package com.cobra.tracker;

import com.cobra.tracker.db.repo.DataStoreFactory;
import com.cobra.tracker.forensics.Forensics;
import com.cobra.tracker.util.CobraException;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Comparator;

public class BaseTest {
    private static final String BASE_DIR = "testdir";
    private static final String DATA_DIR = "data";
    private static final String INVENTORY_DIR = "inventory";

    private final String baseDir;
    private final String inventoryBaseDir;

    public BaseTest(){
        Path resourceDirectory = Paths.get("src","test","resources", BASE_DIR);
        baseDir = resourceDirectory.toFile().getAbsolutePath() + File.separator;
        String dataDir = baseDir + DATA_DIR;
        Forensics.setBaseDir(dataDir);
        inventoryBaseDir = dataDir + File.separator + INVENTORY_DIR;

/*        ClassLoader classLoader = getClass().getClassLoader();
        URL baseDirName = classLoader.getResource(BASE_DIR);
        if (baseDirName == null){

        }

        File file = new File(classLoader.getResource(BASE_DIR).getFile());
        DataStoreFactory.setBaseDir(file.getAbsolutePath() + File.separator + DATA_DIR);
        baseDir = file.getAbsolutePath() + File.separator ;
        inventoryBaseDir = baseDir + File.separator + INVENTORY_DIR;
        */



    }

    protected boolean containBaseDir(String inventoryDir){
        return inventoryDir.contains(inventoryBaseDir);
    }

    protected String createSourceDir(String filename) throws CobraException {
        String sourceDir = baseDir + filename;
        File f = new File(sourceDir);
        if (f.exists()){
            throw new CobraException(String.format("Source dir %s already exists.", sourceDir));
        }
        f.mkdirs();
        return sourceDir;
    }

    protected void delete(File f) throws  FileNotFoundException {
        if (f.isDirectory()) {
            for (File c : f.listFiles())
                delete(c);
        }
        if (!f.delete())
            throw new FileNotFoundException("Failed to delete file: " + f);
    }
}
