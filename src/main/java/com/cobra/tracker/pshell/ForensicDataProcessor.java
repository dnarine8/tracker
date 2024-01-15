package com.cobra.tracker.pshell;

import com.cobra.tracker.util.CobraException;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class ForensicDataProcessor {
    private final static String INVALID_NAMES = "Invalid value for name(%s), command(%s) or key(%s).";
    private final static String CMD = "powershell.exe %s | Export-Csv %s";
    private final static String FAILED_CMD = "Failed to execute %s.";
    private final static String CMD_ERROR = "Failed to execute %s. %s";

    private final Map<String, ForensicDataItem> forensicDataMap = new HashMap<>();
    private final String key;
    private final String name;
    private final String outputFile;
    private String fullCommand;

    public ForensicDataProcessor(String name, String key, String command) throws CobraException {
        if (key == null || command == null || name == null|| key.isEmpty() || command.isEmpty()) {
            throw new CobraException(String.format(INVALID_NAMES, name, command, key));
        }
        this.key = key;
        this.name = name;
        outputFile = "pshell" + name + System.currentTimeMillis() + ".csv";
        fullCommand = String.format(CMD, command, outputFile);
    }

    public void execute() throws CobraException {
        try {
            Process process = Runtime.getRuntime().exec(fullCommand);
            BufferedReader error = new BufferedReader(new InputStreamReader(process.getErrorStream()));
            String errorString = error.readLine();
            if (errorString != null) {
                throw new CobraException(String.format(CMD_ERROR, fullCommand, errorString));
            }
            loadResults();
        } catch (IOException e) {
            throw new CobraException(String.format(FAILED_CMD, fullCommand));
        }
    }

    private void loadResults() throws CobraException {
        String [] names = null;
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(outputFile))) {
            String line;
            while (true) {
                line = bufferedReader.readLine();
                if (line == null) {
                    break;
                }
                if (!line.startsWith("#")) {
                    if (names == null) {
                        names = line.split(",");
                        System.out.println("names: " + names.length + ". " + line);
                    } else {
                        ForensicDataItem forensicData = new ForensicDataItem(key, names, line.split(","));
                        forensicDataMap.put(forensicData.key(),forensicData);
                    }

                }
            }

        } catch (IOException e) {
            throw new CobraException(String.format(CMD_ERROR, fullCommand, e.getMessage()));
        }
    }
}
