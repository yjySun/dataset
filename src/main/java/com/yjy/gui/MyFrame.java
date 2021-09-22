package com.yjy.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MyFrame extends JFrame {

    JButton button = new JButton("按钮");
    JLabel lable = new JLabel("hello world!");
    public MyFrame(String title) {
        super(title);

        Container container = getContentPane();
        container.setLayout(new FlowLayout());

        container.add(button);
        container.add(lable);

        DateListener dateListener = new DateListener();
        button.addActionListener(dateListener);
    }

    public class DateListener implements ActionListener{

        public void actionPerformed(ActionEvent e) {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-mm-dd hh:MM:ss");
            String format = simpleDateFormat.format(new Date());
            lable.setText(format);
        }
    }
}
