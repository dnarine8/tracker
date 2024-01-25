package com.cobra.forensics.audit;

import com.cobra.forensics.db.model.WindowsGenericForensicData;
import com.cobra.forensics.db.repo.InventoryStore;
import com.cobra.forensics.util.CobraException;

import java.io.*;

public abstract class GenericWindowsAuditor extends GenericAuditor {
    private final static String CMD = "powershell.exe %s | Export-Csv %s";
    private final static String REGISTRY_CMD = "powershell.exe scripts%s%s %s %s";

    private final static String FAILED_CMD = "Failed to execute %s.";
    private final static String CMD_ERROR = "Failed to execute %s. %s";

    private final static String OUTPUT_FILE_FORMAT = "%soutput.csv";
    private final String key;
    private final String type;
    private final String command;
    private String path = null;
    private boolean executeScript = true;

    public GenericWindowsAuditor(String type, String key, String command) {
        this.type = type;
        this.key = key;
        this.command = command;
    }

    public GenericWindowsAuditor(String type, String key, String command, String path) {
        this.type = type;
        this.key = key;
        this.command = command;
        this.path = path;
        executeScript = false;
    }

    @Override
    public boolean supportInventory() {
        return false;
    }

    @Override
    public InventorySummary buildInventory(InventoryStore inventoryStore, String sourceDir) throws CobraException {
        InventorySummary summary = new InventorySummary();
        String outputFile = String.format(OUTPUT_FILE_FORMAT, inventoryStore.getInventoryDir());
        String fullCommand;
        if (path != null) {
            fullCommand = String.format(REGISTRY_CMD, File.separator, command, path, outputFile);
        } else {
            fullCommand = String.format(CMD, command, outputFile);
        }

        String[] names = null;

        try {
            if (executeScript) {
                System.out.println("Executing command: " + fullCommand);
                Process process = Runtime.getRuntime().exec(fullCommand);
                BufferedReader error = new BufferedReader(new InputStreamReader(process.getErrorStream()));
                String errorString = error.readLine();
                if (errorString != null) {
                    throw new CobraException(String.format(CMD_ERROR, fullCommand, errorString));
                }
            }
            importCSV(inventoryStore, outputFile);
/*            try (BufferedReader bufferedReader = new BufferedReader(new FileReader(outputFile))) {
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
            }*/
        } catch (IOException e) {
            throw new CobraException(String.format(FAILED_CMD, fullCommand));
        }

        return summary;
    }

    @Override
    public String getType() {
        return type;
    }

    protected InventorySummary importCSV(InventoryStore inventoryStore, String outputFile) throws CobraException {
        InventorySummary summary = new InventorySummary();
        String[] names = null;

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
            return summary;

        } catch (IOException e) {
            throw new CobraException("Failed to import " + outputFile);
        }

    }

}
