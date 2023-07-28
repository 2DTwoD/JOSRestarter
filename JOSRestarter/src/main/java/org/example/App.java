package org.example;

import com.github.kwhat.jnativehook.GlobalScreen;
import com.github.kwhat.jnativehook.NativeHookException;
import com.github.kwhat.jnativehook.keyboard.NativeKeyEvent;
import com.github.kwhat.jnativehook.keyboard.NativeKeyListener;
import org.example.utils.ProcessKiller;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.net.URL;
import java.util.*;
import java.util.List;

public class App extends JFrame implements NativeKeyListener
{
    private static final int WIDTH = 300;
    private static final int HEIGHT = 300;
    private int combination = 0;
    public static void main( String[] args ) throws AWTException {
        try {
            GlobalScreen.registerNativeHook();
        }
        catch (NativeHookException ex) {
            System.err.println("There was a problem registering the native hook.");
            System.err.println(ex.getMessage());
            System.exit(1);
        }
        GlobalScreen.addNativeKeyListener(new App());
    }
    public App() throws AWTException {
        super("JOSRestarter");
        Image iconImage;
        try {
            URL resource = getClass().getResource("/ico.gif");
            iconImage = ImageIO.read(resource);
        } catch (IOException e) {
            System.out.println("icon not found");
            return;
        }
        setIconImage(iconImage);
        setResizable(false);
        setAlwaysOnTop(true);

        final JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(3, 1));

        final List<JButton> buttons = new LinkedList<>();

        buttons.add(getButton("Закрыть winCC", new MouseAdapter(){
            @Override
            public void mouseClicked(MouseEvent e) {
                ProcessKiller.killProcess("win32calc.exe");
            }
        }));
        buttons.add(getButton("Перезагрузить Windows", new MouseAdapter(){
            @Override
            public void mouseClicked(MouseEvent e) {
                //ProcessKiller.restartWindows();
            }
        }));
        buttons.add(getButton("Выключить Windows", new MouseAdapter(){
            @Override
            public void mouseClicked(MouseEvent e) {
                //ProcessKiller.shutDownWindows();
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
            MenuItem show = new MenuItem("Показать");
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
        setVisible(true);
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
        if(e.getKeyCode() == NativeKeyEvent.VC_CONTROL){
            combination |= 2;
        }
        if(combination >= 3){
            setVisible(true);
        }
    }
    public void nativeKeyReleased(NativeKeyEvent e) {
        if(e.getKeyCode() == NativeKeyEvent.VC_1){
            combination &= ~1;
        }
        if(e.getKeyCode() == NativeKeyEvent.VC_CONTROL){
            combination &= ~2;
        }
    }
}
