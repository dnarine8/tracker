package com.cobra.tracker.app;

import com.cobra.tracker.util.CobraException;

import java.io.*;

public class DataStore {

    private final String filename;
    private BufferedWriter writer = null;

    public DataStore(String filename) {
        this.filename = filename;
    }

    public void write(String outputString) throws CobraException {
        try {
            if (writer == null) {
                writer = new BufferedWriter(new FileWriter(filename));
            }
            writer.write(outputString);
            writer.newLine();
        } catch (IOException e) {
            throw new CobraException("Failed to create output file: " + filename);
        }
    }

    public void close() {
        try {
            writer.flush();
            writer.close();
        } catch (Exception e) {
        }
    }

}
