package com.cobra.tracker.db;

import com.cobra.tracker.app.DataStore;
import com.cobra.tracker.util.CobraException;
import com.cobra.tracker.util.LogUtil;

import java.io.File;

public class StatusFileStore {
    private final String ERRORS = "errors.txt";
    private final DataStore errorFile;

    public StatusFileStore(String outputDir){
        String errorFileName = outputDir + ERRORS;
        errorFile = new DataStore(errorFileName);
        LogUtil.info("Status", String.format("Error Filename is %s.", errorFileName));
    }

    public void writeError(String errorMsg) throws CobraException {
        errorFile.write(errorMsg);
    }

    public void close(){
        errorFile.close();
    }

}
