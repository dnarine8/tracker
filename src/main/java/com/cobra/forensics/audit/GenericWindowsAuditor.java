package com.cobra.forensics.audit;

import com.cobra.forensics.db.model.WindowsGenericForensicData;
import com.cobra.forensics.db.repo.InventoryStore;
import com.cobra.forensics.util.CobraException;
import com.cobra.forensics.util.ForensicsUtil;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

public abstract class GenericWindowsAuditor extends GenericAuditor {
    private final static String CMD = "powershell.exe %s | Export-Csv %s";
    private final static String FAILED_CMD = "Failed to execute %s.";
    private final static String CMD_ERROR = "Failed to execute %s. %s";

    private final static String OUTPUT_FILE_FORMAT = "%spshell%s%s.csv";
    private final String key;
    private final String type;
    private final  String command;

    public GenericWindowsAuditor(String type, String key, String command) {
        this.type = type;
        this.key = key;
        this.command = command;
    }

    @Override
    public InventorySummary buildInventory(InventoryStore inventoryStore, String sourceDir) throws CobraException {
        InventorySummary summary = new InventorySummary();
        String outputFile = String.format(OUTPUT_FILE_FORMAT, inventoryStore.getInventoryDir(),type, ForensicsUtil.dateToString());
        String fullCommand = String.format(CMD, command, outputFile);
        String[] names = null;

        try {
            Process process = Runtime.getRuntime().exec(fullCommand);
            BufferedReader error = new BufferedReader(new InputStreamReader(process.getErrorStream()));
            String errorString = error.readLine();
            if (errorString != null) {
                throw new CobraException(String.format(CMD_ERROR, fullCommand, errorString));
            }
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
                            WindowsGenericForensicData forensicData = new WindowsGenericForensicData(key, names, line.split(","));
                            inventoryStore.add(forensicData);
                        }

                    }
                }
                inventoryStore.write();
                summary.setInventoryDirName(inventoryStore.getInventoryDir());
                summary.setKeys(inventoryStore.getKeys());

            } catch (IOException e) {
                throw new CobraException(String.format(CMD_ERROR, fullCommand, e.getMessage()));
            }
        } catch (IOException e) {
            throw new CobraException(String.format(FAILED_CMD, fullCommand));
        }

        return summary;
    }

    @Override
    public String getType() {
        return type;
    }

}
