package com.cobra.tracker.db.repo;

import com.cobra.tracker.util.CobraException;
import com.cobra.tracker.util.LogUtil;

/**
 * Based class for writing objects to storage. Implements storage for all errors encountered during the assessment.
 */
public class StatusFileStore {
    /**
     * Name of error file.
     */
    private final String ERRORS = "errors.txt";

    /**
     * Writes the output.
     */
    private final DataStore dataStore;

    public StatusFileStore(String outputDir){
        String errorFileName = outputDir + ERRORS;
        dataStore = new DataStore(errorFileName);
        LogUtil.info("Status", String.format("Error Filename is %s.", errorFileName));
    }

    public void writeError(String errorMsg) throws CobraException {
        dataStore.write(errorMsg);
    }

    public void close(){
        dataStore.close();
    }

}
