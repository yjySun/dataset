package com.yjy.gui;

import javax.swing.*;

/**
 * @Author: Jiyuan Yao
 * @Date: 2021/9/17 10:24
 * @Description:
 */
public class Main {

    public static void createGUI() {
        MyFrame myFrame = new MyFrame("数据集生成工具");
        myFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        myFrame.setSize(400, 400);
        myFrame.setVisible(true);
    }

    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createGUI();
            }
        });
    }
}
