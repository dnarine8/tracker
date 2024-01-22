package com.cobra.forensics.app;

import com.cobra.forensics.db.repo.DiffResultsStore;
import com.cobra.forensics.db.repo.InventoryStore;
import com.cobra.forensics.audit.*;
import com.cobra.forensics.util.CobraException;
import com.cobra.forensics.util.ForensicsUtil;
import com.cobra.forensics.util.LogUtil;

import java.io.File;

public class Forensics {
    private static String BASE_DIR = "data";
    private static String INVENTORY_BASE_DIR = BASE_DIR + File.separator + "inventory" + File.separator;
    private static String RESULTS_BASE_DIR = BASE_DIR + File.separator + "results" + File.separator;
    public static Auditor[] AUDITORS;

    static {
        AUDITORS = new Auditor[]{new FileSystemAuditor(),
                new ProcessAuditor(),
                new ServiceAuditor(),
                new StartupAuditor()
               // new TCPAuditor();
        };
    }

    /**
     * Set the global base directory. Use for unit test cases.
     *
     * @param baseDir the base directory.
     */
    public static void setBaseDir(String baseDir) {
        BASE_DIR = baseDir;
        INVENTORY_BASE_DIR = BASE_DIR + File.separator + "inventory" + File.separator;
        RESULTS_BASE_DIR = BASE_DIR + File.separator + "results" + File.separator;
    }


    public InventorySummary[] buildInventory(String sourceDir) {
        String timestamp = ForensicsUtil.dateToString();
        String baseDir = INVENTORY_BASE_DIR + timestamp + File.separator;
        InventorySummary[] summaries = new InventorySummary[AUDITORS.length];
        int i = 0;

        for (Auditor auditor : AUDITORS) {
            String type = auditor.getType();
            try {
                String inventoryDir = baseDir + type + File.separator;
                LogUtil.info("FileSystemForensics", String.format("Building inventory for %s.", inventoryDir));
                File f = new File(inventoryDir);
                f.mkdirs();
                InventoryStore inventoryStore = new InventoryStore(inventoryDir);
                InventorySummary summary = auditor.buildInventory(inventoryStore, sourceDir);
                summary.setTimeStamp(timestamp);
                summaries[i++] = summary;
                LogUtil.info("FileSystemForensics", String.format("Built inventory %s.", inventoryDir));
            } catch (CobraException e) {
                // log and keep going for other auditors
                LogUtil.error(String.format("Failed to build inventory for %s.", type), e);
            } catch (Exception e) {
                LogUtil.error(String.format("Unexpected error, while gathering inventory data for %s.", type), e);
            }

        }
        return summaries;
    }

    public DiffSummary[] diff(String oldInventoryDir, String newInventoryDir) {
        String diffResultsDirName = RESULTS_BASE_DIR + ForensicsUtil.dateToString() + File.separator;

        DiffSummary[] summaries = new DiffSummary[1];
        int i = 0;

        for (Auditor auditor : AUDITORS) {
            String type = auditor.getType();
            try {
                oldInventoryDir = buildInventoryPath(oldInventoryDir, type);
                newInventoryDir = buildInventoryPath(newInventoryDir, type);
                String resultDir = diffResultsDirName + type + File.separator;
                File f = new File(resultDir);
                f.mkdirs();
                DiffResultsStore diffResultsStore = new DiffResultsStore(resultDir);
                LogUtil.info("Forensics", String.format("Comparing %s with %s. Result is in %s.", oldInventoryDir, newInventoryDir, resultDir));
                summaries[i++] = auditor.diff(oldInventoryDir, newInventoryDir, diffResultsStore);
                LogUtil.info("Forensics", String.format("Done comparing %s with %s. Result is in %s.", oldInventoryDir, newInventoryDir, resultDir));
            } catch (CobraException e) {
                // log and keep going for other auditors
                LogUtil.error(String.format("Failed to build result for %s.", type), e);
            } catch (Exception e) {
                LogUtil.error(String.format("Unexpected error, while comparing inventory data for %s.", type), e);
            }

        }
        return summaries;
    }

    public static String buildInventoryPath(String inventoryDirName, String type) throws CobraException {
        if (inventoryDirName.lastIndexOf(File.separator) != inventoryDirName.length() - 1) {
            inventoryDirName = INVENTORY_BASE_DIR + inventoryDirName + File.separator;
        } else {
            inventoryDirName = INVENTORY_BASE_DIR + inventoryDirName;
        }
        return inventoryDirName + type + File.separator;
    }

}
