package org.goznak.utils;

import org.goznak.App;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class ProcessKiller {
    public static boolean isProcessRunning(String serviceName) {
        try {
            Process pro = Runtime.getRuntime().exec("tasklist");
            BufferedReader reader = new BufferedReader(new InputStreamReader(pro.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.startsWith(serviceName)) {
                    return true;
                }
            }

        } catch (IOException e) {
            App.dialog.getErrorDialog("Произошла ошибка: " + e.getMessage());
        }
        return false;
    }
    public static void killProcess(String serviceName) {
        if(isProcessRunning(serviceName)) {
            try {
                Runtime.getRuntime().exec("taskkill /IM " + serviceName);
                App.dialog.getInfoDialog("WinCC закрыт!");
            } catch (IOException e) {
                App.dialog.getErrorDialog("Произошла ошибка: " + e.getMessage());
            }
        } else {
            App.dialog.getInfoDialog("WinCC не запущен");
        }
    }
    public static void shutDownWindows(){
        try {
            Runtime.getRuntime().exec("shutdown -s -t 0");
        }
        catch(IOException e)
        {
            App.dialog.getErrorDialog("Произошла ошибка: " + e.getMessage());
        }
    }
    public static void restartWindows(){
        try {
            Runtime.getRuntime().exec("shutdown -r -t 5 -f");
        }
        catch(IOException e)
        {
            App.dialog.getErrorDialog("Произошла ошибка: " + e.getMessage());
        }
    }
}