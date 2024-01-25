/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.cobra.forensics.app;

import com.cobra.forensics.db.model.FileInfo;
import com.cobra.forensics.ProcessInfo;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 * Goal 1 - Search through user account
 * Step 1 Start from root dir, get list of dir, for each file/dir
 * create an an instance of FileInfo, add the following
 * - the file/name
 * - if file or dir
 * - hidden
 * - read only, write, executable
 * - last date/time modified
 * if directory, call step 1
 * Step 2 - New, modified, deleted
 */


/**
 *
 * @author guest_home
 */
public class Main {

    private static HashMap<String, FileInfo> table = null;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        if (args.length > 1) {
            String cmd = args[0];
            if (cmd.equals("inv")) {
                if (args.length != 2){
                    System.out.println("Invalid number of arguments. Please provide one source directory. ");
                } else {
                    String dir = args[1];
                    System.out.println("Building inventory for: " + dir);
                    File f = new File(dir);
                    if (f.exists()) {
                        Forensics forensics = new Forensics();
                        forensics.buildInventory(dir);
                    } else {
                        System.out.println("Invalid filename. " + dir + " does not exists.");
                    }
                }
            } else if (cmd.equals("diff")) {
                if (args.length != 3){
                    System.out.println("Invalid number of arguments. Please provide two directories to compare. ");
                }else {
                    String dir1 = args[1];
                    String dir2 = args[2];
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

