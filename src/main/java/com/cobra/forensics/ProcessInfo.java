/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.cobra.forensics;

import java.io.BufferedReader;
import java.io.InputStreamReader;

/**
 *
 * @author guest_home
 */
public class ProcessInfo {

    public static void getProcessInfo() {
     try {
        String line;
        Process p = Runtime.getRuntime().exec
        (System.getenv("windir") +"\\system32\\"+"tasklist.exe");

        BufferedReader input =
                new BufferedReader(new InputStreamReader(p.getInputStream()));
        while ((line = input.readLine()) != null) {
            System.out.println(line); //<-- Parse data here.
        }
        input.close();
    } catch (Exception err) {
        err.printStackTrace();
    }
  }
}
