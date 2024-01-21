package com.cobra.forensics.db.repo;

import com.cobra.forensics.util.CobraException;
import com.cobra.forensics.util.LogUtil;

import java.io.*;

/**
 * Generic object for persisting data. For now a data is written to a file. We should migrate
 * to a database eventually and this class will most likely represent an interface to a record/relation.
 */
public class DataStore {

    /**
     * The output file.
     */
    private final String filename;

    /**
     * Buffers the output.
     */
    private BufferedWriter writer = null;

    public DataStore(String filename) {
        this.filename = filename;
    }

    /**
     * Writes the output. The file is opened the first time.
     *
     * @param outputString
     * @throws CobraException for failures encountered during write.
     */
    public void write(String outputString) throws CobraException {
        try {
            if (writer == null) {
                writer = new BufferedWriter(new FileWriter(filename));
            }
            writer.write(outputString);
            writer.newLine();
        } catch (IOException e) {
            throw new CobraException(String.format("Failed to write output to %s. " + filename));
        }
    }

    public void close() {
        try {
            if (writer != null) {
                writer.flush();
                writer.close();
            }
        } catch (Exception e) {
            LogUtil.warn(String.format("Failed to close %s.", filename));
        }
    }

}
