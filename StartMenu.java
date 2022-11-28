package PokemonPanels;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class StartMenu extends JFrame implements ActionListener{

    public static void main(String[] args) {
        StartMenu startMenu = new StartMenu("StartMenu");
        startMenu.setVisible(true);
    }

    int i;              // カウンタ変数
    String cmd;         // ActionCommandを格納する変数
    JPanel panel;       // JPanel
    JButton button[];   // JButton配列

    StartMenu(String title){
        panel = new JPanel();
        button = new JButton[3];

        setTitle(title);
        setSize(300, 100);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        DrawButton();

        getContentPane().add(panel, BorderLayout.CENTER);
    }

    void DrawButton(){
        panel.setLayout(null);
        for(i=0; i<button.length; i++){
            button[i] = new JButton();
            button[i].setBackground(Color.ORANGE);
            button[i].setBounds(i*100 + 10, 10, 50, 50);
            button[i].addActionListener(this);
            button[i].setActionCommand(Integer.valueOf(i+1).toString());
            button[i].setText(Integer.valueOf(i+1).toString());
            panel.add(button[i]);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        cmd = e.getActionCommand();
        switch(cmd){
            case "1":
                new PokemonPanels("MineSweeper", 1);
                setVisible(false);
                break;
            case "2":
                new PokemonPanels("MineSweeper", 2);
                setVisible(false);
                break;
            case "3":
                new PokemonPanels("MineSweeper", 3);
                setVisible(false);
                break;
    }

    }
}
