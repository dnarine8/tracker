package com.cobra.forensics.db.repo;

import com.cobra.forensics.util.CobraException;
import com.cobra.forensics.util.LogUtil;

/**
 * Based class for writing objects to storage. Implements storage for all errors encountered during the assessment.
 */
public class ErrorFileStore {

    /**
     * Name of error file.
     */
    private final String FILENAME_SYNTAX = "%serrors.txt";

    /**
     * Writes the output.
     */
    private final DataStore dataStore;

    public ErrorFileStore(String outputDir){
        String errorFileName = String.format(FILENAME_SYNTAX, outputDir);
        dataStore = new DataStore(errorFileName);
        LogUtil.info("ERROR", String.format("Error Filename is %s.", errorFileName));
    }

    public void writeError(String errorMsg) throws CobraException {
        dataStore.write(errorMsg);
    }

    public void close(){
        dataStore.close();
    }

}
