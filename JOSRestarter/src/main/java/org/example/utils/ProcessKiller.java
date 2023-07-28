package org.example.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class ProcessKiller {
    private static final String TASKLIST = "tasklist";
    private static final String KILL = "taskkill /IM ";
    public static boolean isProcessRunning(String serviceName) {
        try {
            Process pro = Runtime.getRuntime().exec(TASKLIST);
            BufferedReader reader = new BufferedReader(new InputStreamReader(pro.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.startsWith(serviceName)) {
                    return true;
                }
            }

        } catch (IOException e) {
            System.out.println("Exception: " +e);
        }
        return false;
    }
    public static void killProcess(String serviceName) {
        if(isProcessRunning(serviceName)) {
            try {
                Runtime.getRuntime().exec(KILL + serviceName);
                System.out.println(serviceName + " killed successfully!");
            } catch (IOException e) {
                System.out.println("Exception: " + e);
            }
        } else {
            System.out.println("Process not working");
        }
    }
    public static void shutDownWindows(){
        try {
            Runtime.getRuntime().exec("shutdown -s -t 0");
        }
        catch(IOException e)
        {
            System.out.println("Exception: " +e);
        }
    }
    public static void restartWindows(){
        try {
            Runtime.getRuntime().exec("shutdown -r -t 5 -f");
        }
        catch(IOException e)
        {
            System.out.println("Exception: " +e);
        }
    }
}