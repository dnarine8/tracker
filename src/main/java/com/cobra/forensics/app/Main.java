/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.cobra.forensics.app;

import com.cobra.forensics.db.model.FileInfo;
import com.cobra.forensics.util.LogUtil;

import java.io.File;
import java.util.HashMap;



public class Main {

    private static final String TAG = "Main";
    private static final String CREATE_INVENTORY_CMD = "inv";
    private static final String COMPARE_INVENTORIES_CMD = "cmp";

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        if (args.length > 1) {
            String cmd = args[0];
            if (cmd.equals(CREATE_INVENTORY_CMD)) {
                if (args.length != 3){
                    System.out.println("Invalid number of arguments. Please specify source and output directories. ");
                } else {
                    String sourceDir = args[1];
                    String outputDir = args[2];

                    String msg = String.format("Building inventory for %s. Results directory is %s.", sourceDir, outputDir);
                    System.out.println(msg);
                    LogUtil.info(TAG, msg);

                    File f = new File(sourceDir);
                    if (f.exists()) {
                        Forensics forensics = new Forensics();
                        forensics.buildInventory(sourceDir, outputDir);
                    } else {
                        msg = String.format("Error: Invalid filename, %s does not exists.", sourceDir);
                        System.out.println(msg);
                        LogUtil.info(TAG, msg);
                    }
                }
            } else if (cmd.equals(COMPARE_INVENTORIES_CMD)) {
                if (args.length != 3){
                    System.out.println("Invalid number of arguments. Please provide two directories to compare. ");
                }else {
                    String dir1 = args[1];
                    String dir2 = args[2];
                    String msg = String.format("Comparing inventories %s and %s.", dir1, dir2);

                    Forensics forensics = new Forensics();
                    forensics.diff(dir1,dir2);

                }
            } else {
                System.out.println("Invalid command: " + cmd);
            }
        } else {
            System.out.println("Invalid number of arguments.");
        }

    }

}

