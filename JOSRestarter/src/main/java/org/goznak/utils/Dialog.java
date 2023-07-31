package org.goznak.utils;

import javax.swing.*;

public class Dialog {
    JFrame parent;
    public Dialog(JFrame parent){
        this.parent = parent;
    }
    public boolean getConfirmDialog(String message){
        return JOptionPane.showConfirmDialog(parent, message) == JOptionPane.YES_OPTION;
    }
    public void getInfoDialog(String message){
        JOptionPane.showMessageDialog(parent, message, "Сообщение", JOptionPane.INFORMATION_MESSAGE);
    }
    public void getErrorDialog(String message){
        JOptionPane.showMessageDialog(parent, message, "Ошибка", JOptionPane.ERROR_MESSAGE);
    }
}
