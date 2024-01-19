package com.cobra.tracker.forensics;

import com.cobra.tracker.db.repo.InventoryStore;
import com.cobra.tracker.util.CobraException;
import com.cobra.tracker.util.LogUtil;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Forensics {
    private static String BASE_DIR = "data";
    private static  String INVENTORY_BASE_DIR = BASE_DIR + File.separator + "inventory" + File.separator;
    private static  String RESULTS_BASE_DIR = BASE_DIR + File.separator + "results" + File.separator;
    public static GenericAuditor[] auditors;

    static {
        auditors = new GenericAuditor[1];
        auditors[0] = new FileSystemAuditor();
    }

    public InventorySummary [] buildInventory(String sourceDir) {
        String baseDir = INVENTORY_BASE_DIR + getTimestamp() + File.separator;
        InventorySummary [] summaries = new InventorySummary[1];
        int i = 0;

        for (Auditor auditor: auditors) {
            String type = auditor.getType();
            try {
                String inventoryDir = baseDir + type + File.separator;
                LogUtil.info("FileSystemForensics", String.format("Building inventory for %s.", inventoryDir));
                File f = new File(inventoryDir);
                f.mkdirs();
                InventoryStore inventoryStore =  new InventoryStore(inventoryDir);
                summaries[i++] = auditor.buildInventory(inventoryStore);
                LogUtil.info("FileSystemForensics", String.format("Built inventory %s.", inventoryDir));
            } catch (CobraException e) {
                // log and keep going for other auditors
                LogUtil.error(String.format("Failed to build inventory for %s.",type),e);
            }

        }
        return summaries;
    }

    public DiffSummary diff(String oldInventoryDir, String newInventoryDir) throws CobraException {
        return null;
    }

    private static String getTimestamp() {
        long currentDateTime = System.currentTimeMillis();
        Date currentDate = new Date(currentDateTime);
        DateFormat df = new SimpleDateFormat("dd_MM_yy_HH_mm_ss");
        return df.format(currentDate);
    }
}
