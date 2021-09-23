package com.yjy.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MyFrame extends JFrame {

    JPanel choosePanel= new JPanel();
    JPanel checkBoxPanel= new JPanel();
    JPanel contentPanel= new JPanel();

    public MyFrame(String title) {
        super(title);

        Container container = getContentPane();
        container.setLayout(new BorderLayout());

        choosePanel.setBorder(BorderFactory.createTitledBorder("请选取你的源文件"));
        choosePanel.setLayout(new FlowLayout(FlowLayout.LEFT, 20,10));

        JLabel jLabel = new JLabel("源文件");
        JTextField jTextField = new JTextField();
        jTextField.setPreferredSize(new Dimension(350,30));
        JButton jButton = new JButton("浏览");
        jButton.setPreferredSize(new Dimension(100,30));

        choosePanel.add(jLabel);
        choosePanel.add(jTextField);
        choosePanel.add(jButton);


        container.add(choosePanel, BorderLayout.NORTH);
        container.add(checkBoxPanel, BorderLayout.CENTER);
        container.add(contentPanel, BorderLayout.SOUTH);
    }

    public class DateListener implements ActionListener{

        public void actionPerformed(ActionEvent e) {

        }
    }
}
