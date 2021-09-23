package com.yjy.action;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.*;
import java.util.HashSet;

/**
 * @Author: Jiyuan Yao
 * @Date: 2021/9/23 16:57
 * @Description:
 */
public class UpLoad {
    /**
     * 文件上传功能
     *
     * @param developer
     */
    public static String eventOnImport(JButton developer) {
        String filePath = null;
        JFileChooser chooser = new JFileChooser();
        chooser.setMultiSelectionEnabled(false);
        chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);

        /** 过滤文件类型 * */
        FileNameExtensionFilter filter = new FileNameExtensionFilter("Microsoft Excel文件(*.xlsx)", "xlsx");
        FileNameExtensionFilter filter1 = new FileNameExtensionFilter("Microsoft Excel 97-2003 文件(*.xls)", "xls");
        chooser.setFileFilter(filter);
        chooser.setFileFilter(filter1);
        int returnVal = chooser.showOpenDialog(developer);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            /** 得到选择的文件* */
            File arrfiles = chooser.getSelectedFile();
            filePath = arrfiles.getPath();
            FileInputStream input = null;
            FileOutputStream out = null;
            String path = "./";
            try {
                File dir = new File(path);
                /** 目标文件夹 * */
                File[] fs = dir.listFiles();
                HashSet<String> set = new HashSet<String>();
                for (File file : fs) {
                    set.add(file.getName());
                }

                /** 判断是否已有该文件* */
                if (set.contains(arrfiles.getName())) {
                    File file = new File(arrfiles.getName());
                    file.delete();
                }
                input = new FileInputStream(arrfiles);
                byte[] buffer = new byte[1024];
                File des = new File(path, arrfiles.getName());
                out = new FileOutputStream(des);
                int len = 0;
                while (-1 != (len = input.read(buffer))) {
                    out.write(buffer, 0, len);
                }
                out.close();
                input.close();
                JOptionPane.showMessageDialog(null, "上传成功！", "提示",
                        JOptionPane.INFORMATION_MESSAGE);

            } catch (FileNotFoundException e1) {
                JOptionPane.showMessageDialog(null, "上传失败！", "提示",
                        JOptionPane.ERROR_MESSAGE);
                e1.printStackTrace();
            } catch (IOException e1) {
                JOptionPane.showMessageDialog(null, "上传失败！", "提示",
                        JOptionPane.ERROR_MESSAGE);
                e1.printStackTrace();
            }
        }
        return filePath;
    }
}