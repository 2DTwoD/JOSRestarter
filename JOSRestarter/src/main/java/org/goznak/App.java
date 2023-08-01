package org.goznak;

import org.goznak.utils.Dialog;
import org.goznak.utils.ProcessKiller;
import org.jnativehook.GlobalScreen;
import org.jnativehook.NativeHookException;
import org.jnativehook.keyboard.NativeKeyEvent;
import org.jnativehook.keyboard.NativeKeyListener;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.net.InetAddress;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.*;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class App extends JFrame implements NativeKeyListener
{
    private static final int WIDTH = 300;
    private static final int HEIGHT = 300;
    private int combination = 0;
    private static int OSnum;
    public static Dialog dialog;
    public static void main( String[] args ) throws AWTException, UnknownHostException {
        InetAddress IP = InetAddress.getLocalHost();
        OSnum = Integer.parseInt(IP.getHostAddress().split("\\.")[3]) - 100;
        Logger logger = Logger.getLogger(GlobalScreen.class.getPackage().getName());
        logger.setLevel(Level.OFF);
        try {
            if(!GlobalScreen.isNativeHookRegistered()){
                GlobalScreen.registerNativeHook();
            }
        }
        catch (NativeHookException e) {
            App.dialog.getErrorDialog("Произошла ошибка: " + e.getMessage());
            System.exit(1);
        }
        GlobalScreen.getInstance().addNativeKeyListener(new App());
    }
    public App() throws AWTException {
        super("JOS" + OSnum + "Restarter");
        dialog = new Dialog(this);
        Image iconImage = null;
        try {
            URL resource = getClass().getResource("/ico.gif");
            iconImage = ImageIO.read(resource);
        } catch (IOException e) {
            App.dialog.getErrorDialog("Произошла ошибка: " + e.getMessage());
            System.exit(1);
        }
        final JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(3, 1));
        final List<JButton> buttons = new LinkedList<>();
        buttons.add(getButton("Закрыть WinCC", new MouseAdapter(){
            @Override
            public void mouseClicked(MouseEvent e) {
                if(dialog.getConfirmDialog("Закрыть WinCC?")) {
                    ProcessKiller.killProcess();
                }
            }
        }));
        buttons.add(getButton("Перезагрузить OS" + OSnum, new MouseAdapter(){
            @Override
            public void mouseClicked(MouseEvent e) {
                if(dialog.getConfirmDialog("Перезагрузить OS" + OSnum + "?")) {
                    ProcessKiller.restartWindows();
                }
            }
        }));
        buttons.add(getButton("Выключить OS" + OSnum, new MouseAdapter(){
            @Override
            public void mouseClicked(MouseEvent e) {
                if(dialog.getConfirmDialog("Выключить OS" + OSnum + "?")) {
                    ProcessKiller.shutDownWindows();
                }
            }
        }));
        setContentPane(panel);
        for(JButton button: buttons) {
            panel.add(button);
        }
        if(SystemTray.isSupported()){
            setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);

            SystemTray tray = SystemTray.getSystemTray();
            TrayIcon icon = new TrayIcon(iconImage);

            PopupMenu menu = new PopupMenu();
            MenuItem show = new MenuItem("Показать (Ctrl+1)");
            show.addActionListener(new AbstractAction() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    setVisible(true);
                }
            });

            MenuItem exit = new MenuItem("Выход");
            exit.addActionListener(new AbstractAction() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    System.exit(0);
                }
            });
            icon.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    if(e.getButton() == MouseEvent.BUTTON1) {
                        setVisible(true);
                    }
                }
            });
            menu.add(show);
            menu.add(exit);
            icon.setPopupMenu(menu);
            tray.add(icon);
        }

        setSize(WIDTH, HEIGHT);
        setVisible(false);
        setIconImage(iconImage);
        setResizable(false);
        setAlwaysOnTop(true);
        pack();
    }
    private JButton getButton(String title, MouseListener mouseListener){
        JButton button = new JButton(title);
        button.addMouseListener(mouseListener);
        button.setPreferredSize(new Dimension(WIDTH, HEIGHT / 3));
        return button;
    }
    public void nativeKeyPressed(NativeKeyEvent e) {
        if(e.getKeyCode() == NativeKeyEvent.VC_1){
            combination |= 1;
        }
        if(e.getKeyCode() == NativeKeyEvent.VC_CONTROL_L){
            combination |= 2;
        }
        if(combination >= 3){
            setExtendedState(Frame.NORMAL);
            setVisible(true);
        }
    }
    public void nativeKeyReleased(NativeKeyEvent e) {
        if(e.getKeyCode() == NativeKeyEvent.VC_1){
            combination &= ~1;
        }
        if(e.getKeyCode() == NativeKeyEvent.VC_CONTROL_L){
            combination &= ~2;
        }
    }
    @Override
    public void nativeKeyTyped(NativeKeyEvent nativeKeyEvent) {}
}
