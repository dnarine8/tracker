/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.cobra.tracker.app;

import com.cobra.tracker.db.model.FileInfo;
import com.cobra.tracker.ProcessInfo;

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
 *    create an an instance of FileInfo, add the following
 *     - the file/name
 *     - if file or dir
 *     - hidden
 *     - read only, write, executable
 *     - last date/time modified
 *    if directory, call step 1
 * Step 2 - New, modified, deleted
 *
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
        getDiskInfo();
    }
    public static void getDiskInfo(){
        File file = new File("c:\\");

/*        String [] contents = file.list();
        for (int i = 0; i < contents.length -1 ;i++) {
            System.err.println(contents[i]);
         */

        try {
          table = readTable();
         // dumpTable();
          TrackDir(file);
          removeDeleteEntries();
          writeTable();
          ProcessInfo.getProcessInfo();
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
            ex.printStackTrace();
        } catch (FileNotFoundException ex) {
           Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
           ex.printStackTrace();
        } catch (IOException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
            ex.printStackTrace();
        }
    }
        

        

    private static void TrackDir(File file){
  /*      File [] contents = file.listFiles();
        if (contents != null) {
          for (int i = 0; i < contents.length -1 ;i++) {
              FileInfo fi = new FileInfo(contents[i]);
              FileInfo fiorig = table.get(fi.key());

              if (fiorig != null){
                  fiorig.checkAndSetFileChange(fi);
              }
              else {
                  fiorig = fi;
                  fiorig.setNew();
                  table.put(fiorig.key(), fiorig);
                  System.out.println(fiorig);
              }

              TrackDir(contents[i]);

          }
        }*/

    }
    public static void writeTable() throws FileNotFoundException, IOException{
        File file = new File("c:\\data\\table.obj");
        FileOutputStream f = new FileOutputStream(file);
        ObjectOutputStream s = new ObjectOutputStream(f);
        s.writeObject(table);
        s.close();
    }

    public static HashMap<String, FileInfo> readTable() throws FileNotFoundException, IOException, ClassNotFoundException{
      File file = new File("c:\\data\\table.obj");
      HashMap<String, FileInfo> tableFromFile = null;

      if (file.exists()){
        FileInputStream f = new FileInputStream(file);
        ObjectInputStream s = new ObjectInputStream(f);
        tableFromFile = (HashMap<String, FileInfo>) s.readObject();
        s.close();
      }
      else {
          tableFromFile = new HashMap<String, FileInfo> ();
      }

      return tableFromFile;
    }

    public static void dumpTable(){
        Collection<FileInfo> values = table.values();
        Iterator itr = values.iterator();
        while (itr.hasNext()) {
            System.out.println(itr.next());
        }

    }

    public static void removeDeleteEntries(){
        Collection<FileInfo> values = table.values();
        ArrayList<String> deleteList = new ArrayList<String>();
        Iterator<FileInfo> itr = values.iterator();
        System.out.println("removing deleted entries");
        while (itr.hasNext()) {
            FileInfo fi = itr.next();
            if (fi.isDeleted()) {
                System.out.println(fi);
                deleteList.add(fi.key());
            }
        }

        Iterator<String> itrDelete = deleteList.iterator();
        while (itrDelete.hasNext()) {
            table.remove(itrDelete.next());
        }
        System.out.println("removed deleted entries");

    }


}

