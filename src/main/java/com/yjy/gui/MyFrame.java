package com.yjy.gui;

import com.yjy.action.KettleFullJobFile;
import com.yjy.action.KettleJavaFile;
import com.yjy.action.SubscriberJavaFile;
import com.yjy.action.UpLoad;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
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
    JPanel configPanel = new JPanel(new BorderLayout());
    JPanel checkBoxPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 20));
    JPanel inputPanel = new JPanel(new BorderLayout());
    JPanel input1Panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 10));
    JPanel input2Panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 20));


    JPanel contentPanel = new JPanel(new GridLayout(1, 1));
    JScrollPane myScrollPanel = new JScrollPane(contentPanel);
    public static JLabel analyseLabel = new JLabel();

    JLabel jLabel = new JLabel("源文件");
    JTextField jTextField = new JTextField();
    JButton developer = new JButton("浏览");

    JLabel startLabel = new JLabel("截取数据集位置", SwingConstants.RIGHT);
    JTextField startText = new JTextField();
    JLabel versionLabel = new JLabel("版本号", SwingConstants.RIGHT);
    JTextField versionText = new JTextField();
    JLabel sortNumLabel = new JLabel("起始排序码", SwingConstants.RIGHT);
    JTextField sortNumText = new JTextField();
    JLabel databaseIpLabel = new JLabel("数据库IP", SwingConstants.RIGHT);
    JTextField databaseIpText = new JTextField();
    JLabel databaseIdLabel = new JLabel("数据库ID", SwingConstants.RIGHT);
    JTextField databaseIdText = new JTextField();
    JLabel databaseUserNameLabel = new JLabel("数据库用户名", SwingConstants.RIGHT);
    JTextField databaseUserNameText = new JTextField();
    JLabel databasePasswordLabel = new JLabel("数据库密码", SwingConstants.RIGHT);
    JTextField databasePasswordText = new JPasswordField();

    JCheckBox planetCheckbox = new JCheckBox("前置机Java文件", true);
    JCheckBox subscriberCheckbox = new JCheckBox("消费者Java文件", true);
    public static JCheckBox kettleFullCheckbox = new JCheckBox("kettle全量作业", true);
    public static JCheckBox kettleIncrCheckbox = new JCheckBox("kettle增量作业");
    JButton generateButton = new JButton("生成");

    public MyFrame(String title) {
        super(title);
        startLabel.setAlignmentX(SwingConstants.RIGHT);

        Container container = getContentPane();
        container.setLayout(new BorderLayout());

        choosePanel.setLayout(new FlowLayout(FlowLayout.LEFT, 20, 10));
        choosePanel.setBorder(BorderFactory.createTitledBorder("请选取你的源文件"));

        jTextField.setPreferredSize(new Dimension(350, 30));
        developer.setPreferredSize(new Dimension(100, 30));

        choosePanel.add(jLabel);
        choosePanel.add(jTextField);
        choosePanel.add(developer);

        configPanel.add(checkBoxPanel, BorderLayout.NORTH);
        checkBoxPanel.add(planetCheckbox);
        checkBoxPanel.add(subscriberCheckbox);
        checkBoxPanel.add(kettleFullCheckbox);
        checkBoxPanel.add(kettleIncrCheckbox);
        checkBoxPanel.add(generateButton);

        configPanel.add(inputPanel, BorderLayout.CENTER);
        inputPanel.add(input1Panel, BorderLayout.NORTH);
        inputPanel.add(input2Panel, BorderLayout.CENTER);
        inputPanel.setPreferredSize(new Dimension(0, 200));
        startLabel.setPreferredSize(new Dimension(100, 20));
        startText.setPreferredSize(new Dimension(80, 30));
        versionLabel.setPreferredSize(new Dimension(60, 20));
        versionText.setPreferredSize(new Dimension(80, 30));
        sortNumLabel.setPreferredSize(new Dimension(80, 20));
        sortNumText.setPreferredSize(new Dimension(80, 30));
        input1Panel.add(startLabel);
        input1Panel.add(startText);
        input1Panel.add(versionLabel);
        input1Panel.add(versionText);
        input1Panel.add(sortNumLabel);
        input1Panel.add(sortNumText);

        databaseIpLabel.setPreferredSize(new Dimension(100, 20));
        databaseIpText.setPreferredSize(new Dimension(80, 30));
        databaseIdLabel.setPreferredSize(new Dimension(60, 20));
        databaseIdText.setPreferredSize(new Dimension(80, 30));
        databaseUserNameLabel.setPreferredSize(new Dimension(80, 20));
        databaseUserNameText.setPreferredSize(new Dimension(80, 30));
        databasePasswordLabel.setPreferredSize(new Dimension(100, 20));
        databasePasswordText.setPreferredSize(new Dimension(80, 30));

        input2Panel.add(databaseIpLabel);
        input2Panel.add(databaseIpText);
        input2Panel.add(databaseIdLabel);
        input2Panel.add(databaseIdText);
        input2Panel.add(databaseUserNameLabel);
        input2Panel.add(databaseUserNameText);
        input2Panel.add(databasePasswordLabel);
        input2Panel.add(databasePasswordText);

        myScrollPanel.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        myScrollPanel.setBackground(Color.white);
        myScrollPanel.setPreferredSize(new Dimension(0, 400));

        contentPanel.setBackground(Color.WHITE);
        contentPanel.add(analyseLabel);

        container.add(choosePanel, BorderLayout.NORTH);
        container.add(configPanel, BorderLayout.CENTER);
        container.add(myScrollPanel, BorderLayout.SOUTH);

        developer.addMouseListener(new MouseAdapter() { // 添加鼠标点击事件
            public void mouseClicked(MouseEvent event) {
                filePath = UpLoad.eventOnImport(new JButton());
                jTextField.setText(filePath);
            }
        }); // 文件上传功能

        //kettle全量作业选项的监听（级联输入框的显示）
        kettleFullCheckbox.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                if (kettleFullCheckbox.isSelected()) {
                    databaseIpLabel.setVisible(true);
                    databaseIpText.setVisible(true);
                    databaseIdLabel.setVisible(true);
                    databaseIdText.setVisible(true);
                    databaseUserNameLabel.setVisible(true);
                    databaseUserNameText.setVisible(true);
                    databasePasswordLabel.setVisible(true);
                    databasePasswordText.setVisible(true);
                } else {
                    databaseIpLabel.setVisible(false);
                    databaseIpText.setVisible(false);
                    databaseIdLabel.setVisible(false);
                    databaseIdText.setVisible(false);
                    databaseUserNameLabel.setVisible(false);
                    databaseUserNameText.setVisible(false);
                    databasePasswordLabel.setVisible(false);
                    databasePasswordText.setVisible(false);
                }
            }
        });

        //前置机Java文件选项的监听（级联输入框的显示）
        planetCheckbox.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                if (planetCheckbox.isSelected()) {
                    startLabel.setVisible(true);
                    startText.setVisible(true);
                    sortNumLabel.setVisible(true);
                    sortNumText.setVisible(true);
                    //样式对齐
                    if (!planetCheckbox.isSelected() && subscriberCheckbox.isSelected()) {
                        versionLabel.setPreferredSize(new Dimension(60, 20));
                    }
                } else {
                    if (!subscriberCheckbox.isSelected()) {
                        startLabel.setVisible(false);
                        startText.setVisible(false);
                    }
                    sortNumLabel.setVisible(false);
                    sortNumText.setVisible(false);
                }
            }
        });

        //消费者Java文件选项的监听（级联输入框的显示）
        subscriberCheckbox.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                if (subscriberCheckbox.isSelected()) {
                    startLabel.setVisible(true);
                    startText.setVisible(true);
                    versionLabel.setVisible(true);
                    versionText.setVisible(true);
                    //样式对齐
                    if (!planetCheckbox.isSelected() && subscriberCheckbox.isSelected()) {
                        versionLabel.setPreferredSize(new Dimension(60, 20));
                    }
                } else {
                    if (!planetCheckbox.isSelected()) {
                        startLabel.setVisible(false);
                        startText.setVisible(false);
                    }
                    versionLabel.setVisible(false);
                    versionText.setVisible(false);
                }
            }
        });

        generateButton.addMouseListener(new MouseAdapter() { // 添加鼠标点击事件

            public void mouseClicked(MouseEvent event) {
                String startIndex = startText.getText();
                String version = versionText.getText();
                String sortNum = sortNumText.getText();
                boolean isGenerateKettleJob = kettleFullCheckbox.isSelected();
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
                    if (kettleFullCheckbox.isSelected()) {
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
