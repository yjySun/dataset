package com.yjy.gui;

import com.yjy.action.UpLoad;
import com.yjy.util.KettleJavaFile;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * @Author: Jiyuan Yao
 * @Date: 2021/9/22 22:24
 * @Description:
 */
public class MyFrame extends JFrame {

    String filePath = null;

    JPanel choosePanel = new JPanel();
    JPanel checkBoxPanel = new JPanel();
    JPanel contentPanel = new JPanel();
    JLabel contentLabel = new JLabel();

    JLabel jLabel = new JLabel("源文件");
    JTextField jTextField = new JTextField();
    JButton developer = new JButton("浏览");

    JLabel startLabel = new JLabel("截取数据集位置");
    JTextField startText = new JTextField();
    JLabel versionLabel = new JLabel("版本号");
    JTextField versionText = new JTextField();
    JLabel sortNumLabel = new JLabel("起始排序码");
    JTextField sortNumText = new JTextField();
    JCheckBox chkbox1 = new JCheckBox("前置机Java文件", true);
    JCheckBox chkbox2 = new JCheckBox("消费者Java文件");
    JCheckBox chkbox3 = new JCheckBox("kettle文件");
    JButton generateButton = new JButton("生成");

    public MyFrame(String title) {
        super(title);

        Container container = getContentPane();
        container.setLayout(new BorderLayout());

        choosePanel.setLayout(new FlowLayout(FlowLayout.LEFT, 20, 10));
        choosePanel.setBorder(BorderFactory.createTitledBorder("请选取你的源文件"));

        jTextField.setPreferredSize(new Dimension(350, 30));
        developer.setPreferredSize(new Dimension(100, 30));

        choosePanel.add(jLabel);
        choosePanel.add(jTextField);
        choosePanel.add(developer);

        checkBoxPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 15, 20));

        startText.setPreferredSize(new Dimension(70, 30));
        versionText.setPreferredSize(new Dimension(70, 30));
        sortNumText.setPreferredSize(new Dimension(70, 30));

        checkBoxPanel.add(startLabel);
        checkBoxPanel.add(startText);
        checkBoxPanel.add(versionLabel);
        checkBoxPanel.add(versionText);
        checkBoxPanel.add(sortNumLabel);
        checkBoxPanel.add(sortNumText);

        checkBoxPanel.add(chkbox1);
        checkBoxPanel.add(chkbox2);
        checkBoxPanel.add(chkbox3);

        checkBoxPanel.add(generateButton);

        contentPanel.setBorder(BorderFactory.createLineBorder(Color.lightGray));
        contentPanel.setBackground(Color.white);
        contentPanel.setPreferredSize(new Dimension(0, 400));

        contentPanel.add(contentLabel);

        container.add(choosePanel, BorderLayout.NORTH);
        container.add(checkBoxPanel, BorderLayout.CENTER);
        container.add(contentPanel, BorderLayout.SOUTH);

        developer.addMouseListener(new MouseAdapter() { // 添加鼠标点击事件
            public void mouseClicked(MouseEvent event) {
                filePath = UpLoad.eventOnImport(new JButton());
                jTextField.setText(filePath);
            }
        }); // 文件上传功能

        generateButton.addMouseListener(new MouseAdapter() { // 添加鼠标点击事件
            public void mouseClicked(MouseEvent event) {
                String[] split = filePath.split("\\\\");
                String uploadFilePath = UpLoad.path + split[split.length - 1];
                try {
                    KettleJavaFile.generateKettleJavaFile(uploadFilePath);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }); // 生成文件功能
    }
}
