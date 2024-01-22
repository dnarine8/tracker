package com.cobra.forensics;

import com.cobra.forensics.app.Forensics;
import com.cobra.forensics.util.CobraException;

import java.io.File;
import java.io.FileNotFoundException;
import java.nio.file.Path;
import java.nio.file.Paths;

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
    protected void deleteSourceDir(String filename) throws CobraException {
        String sourceDir = baseDir + filename;
        File f = new File(sourceDir);
        if (f.exists()){
            try {
                delete(f);
            } catch (FileNotFoundException e) {
                throw new CobraException(e.getMessage());
            }
        }
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
