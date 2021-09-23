package com.yjy.gui;

import com.yjy.action.UpLoad;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * @Author: Jiyuan Yao
 * @Date: 2021/9/22 22:24
 * @Description:
 */
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
        choosePanel.add(jLabel);

        JTextField jTextField = new JTextField();
        jTextField.setPreferredSize(new Dimension(350,30));
        choosePanel.add(jTextField);

        JButton developer = new JButton("浏览");
        developer.setPreferredSize(new Dimension(100,30));
        choosePanel.add(developer);


        container.add(choosePanel, BorderLayout.NORTH);
        container.add(checkBoxPanel, BorderLayout.CENTER);
        container.add(contentPanel, BorderLayout.SOUTH);

        developer.addMouseListener(new MouseAdapter() { // 添加鼠标点击事件
            public void mouseClicked(MouseEvent event) {
                UpLoad.eventOnImport(new JButton());
            }
        }); // 文件上传功能
    }

    public class DateListener implements ActionListener{

        public void actionPerformed(ActionEvent e) {

        }
    }
}
