package com.yjy.gui;

import com.yjy.action.KettleFullJobFile;
import com.yjy.action.KettleJavaFile;
import com.yjy.action.SubscriberJavaFile;
import com.yjy.action.UpLoad;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;

/**
 * @Author: Jiyuan Yao
 * @Date: 2021/9/22 22:24
 * @Description:
 */
public class MyFrame extends JFrame {

    String filePath = null;

    JPanel choosePanel = new JPanel();
    JPanel checkBoxPanel = new JPanel();
    JPanel contentPanel = new JPanel(new GridLayout(1, 1));
    JScrollPane myScrollPanel = new JScrollPane(contentPanel);
    public static JLabel analyseLabel = new JLabel();

    JLabel jLabel = new JLabel("源文件");
    JTextField jTextField = new JTextField();
    JButton developer = new JButton("浏览");

    JLabel startLabel = new JLabel("截取数据集位置");
    JTextField startText = new JTextField();
    JLabel versionLabel = new JLabel("版本号");
    JTextField versionText = new JTextField();
    JLabel sortNumLabel = new JLabel("起始排序码");
    JTextField sortNumText = new JTextField();
    JCheckBox planetCheckbox = new JCheckBox("前置机Java文件", true);
    JCheckBox subscriberCheckbox = new JCheckBox("消费者Java文件");
    public static JCheckBox kettleCheckbox = new JCheckBox("kettle全量作业");
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

        checkBoxPanel.add(planetCheckbox);
        checkBoxPanel.add(subscriberCheckbox);
        checkBoxPanel.add(kettleCheckbox);

        checkBoxPanel.add(generateButton);

        myScrollPanel.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        myScrollPanel.setBackground(Color.white);
        myScrollPanel.setPreferredSize(new Dimension(0, 400));

        contentPanel.setBackground(Color.WHITE);
        contentPanel.add(analyseLabel);

        container.add(choosePanel, BorderLayout.NORTH);
        container.add(checkBoxPanel, BorderLayout.CENTER);
        container.add(myScrollPanel, BorderLayout.SOUTH);

        developer.addMouseListener(new MouseAdapter() { // 添加鼠标点击事件
            public void mouseClicked(MouseEvent event) {
                filePath = UpLoad.eventOnImport(new JButton());
                jTextField.setText(filePath);
            }
        }); // 文件上传功能

        generateButton.addMouseListener(new MouseAdapter() { // 添加鼠标点击事件
            public void mouseClicked(MouseEvent event) {
                String startIndex = startText.getText();
                String version = versionText.getText();
                String sortNum = sortNumText.getText();
                boolean isGenerateKettleJob = kettleCheckbox.isSelected();
                if (filePath != null) {
                    analyseLabel.setText("");
                    analyseLabel.setText("<html>");
                    if (planetCheckbox.isSelected()) {
                        try {
                            KettleJavaFile.generateKettleJavaFile(filePath, Double.parseDouble(sortNum), Integer.parseInt(startIndex), isGenerateKettleJob);
                        } catch (Exception e) {
                            JOptionPane.showMessageDialog(null, "生成前置机所需Java文件失败！请检查数据！", "提示",
                                    JOptionPane.INFORMATION_MESSAGE);
                            e.printStackTrace();
                        }
                    }
                    if (subscriberCheckbox.isSelected()) {
                        if ("".equals(version) || version == null) {
                            JOptionPane.showMessageDialog(null, "生成消费者所需Java文件失败！请检查数据！", "提示",
                                    JOptionPane.INFORMATION_MESSAGE);
                        } else {
                            try {
                                SubscriberJavaFile.generateSubscriberJavaFile(filePath, version, Integer.parseInt(startIndex), isGenerateKettleJob);
                            } catch (Exception e) {
                                JOptionPane.showMessageDialog(null, "生成消费者所需Java文件失败！请检查数据！", "提示",
                                        JOptionPane.INFORMATION_MESSAGE);
                                e.printStackTrace();
                            }
                        }
                    }
                    if (kettleCheckbox.isSelected()) {
                        try {
                            KettleFullJobFile.generateKettleJobFile(filePath, isGenerateKettleJob);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    analyseLabel.setText(analyseLabel.getText() + "</html>");
                }
            }
        }); // 生成文件功能
    }
}
