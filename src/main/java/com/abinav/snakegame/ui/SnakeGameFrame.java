package com.abinav.snakegame.ui;

import javax.swing.JFrame;

public class SnakeGameFrame extends JFrame {

    private static final long serialVersionUID = 1L;

    public SnakeGameFrame() {
        setTitle("Snake Game");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        add(new SnakeGamePanel());
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

}
