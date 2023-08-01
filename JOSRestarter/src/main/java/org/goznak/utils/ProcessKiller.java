package org.goznak.utils;

import org.goznak.App;

import java.io.IOException;

public class ProcessKiller {
    public static void killProcess() {
        try {
            Runtime.getRuntime().exec("wscript.exe Reset_WinCC.vbs");
            App.dialog.getInfoDialog("WinCC закрыт!");
        } catch (IOException e) {
            App.dialog.getErrorDialog("Произошла ошибка: " + e.getMessage());
        }
    }
    public static void shutDownWindows(){
        try {
            Runtime.getRuntime().exec("shutdown -s -t 0");
        }
        catch(IOException e) {
            App.dialog.getErrorDialog("Произошла ошибка: " + e.getMessage());
        }
    }
    public static void restartWindows(){
        try {
            Runtime.getRuntime().exec("shutdown -r -t 0");
        }
        catch(IOException e) {
            App.dialog.getErrorDialog("Произошла ошибка: " + e.getMessage());
        }
    }
}