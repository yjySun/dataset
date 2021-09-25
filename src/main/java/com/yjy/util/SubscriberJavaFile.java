package com.yjy.util;

import com.yjy.gui.MyFrame;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import javax.swing.*;
import javax.swing.filechooser.FileSystemView;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;

/**
 * @Author: Jiyuan Yao
 * @Date: 2021/9/17 9:47
 * @Description: 生成 abc-dataset-cdc-subscriber 所需要的Java文件
 */
public class SubscriberJavaFile {

    public static void generateSubscriberJavaFile(String filePath, String version) throws Exception {
        String[] split1 = filePath.split("\\\\");
        String fileName = split1[split1.length - 1];//获取文件名

        FileSystemView fsv = FileSystemView.getFileSystemView();
        File com = fsv.getHomeDirectory();
        String deskTopPath = com.getPath() + "\\subscriberJavaFile\\";//获取桌面路径

        File excel = new File(filePath);
        String[] split = excel.getName().split("\\.");  //.是特殊字符，需要转义！！！！！
        Workbook wb;
        //根据文件后缀（xls/xlsx）进行判断
        if ("xls".equals(split[1])) {
            FileInputStream fis = new FileInputStream(excel);   //文件流对象
            wb = new HSSFWorkbook(fis);
        } else if ("xlsx".equals(split[1])) {
            wb = new XSSFWorkbook(excel);
        } else {
            System.out.println("文件类型错误!");
            return;
        }

        //开始解析
        Sheet sheet = wb.getSheetAt(0);     //读取sheet 0

        int firstRowIndex = sheet.getFirstRowNum();   //第一行是列名，所以不读
        int lastRowIndex = sheet.getLastRowNum();

        String name = null;
        String tableName = null;
        String datasetId = null;
        JLabel analyseLabel = MyFrame.analyseLabel;
        for (int rIndex = firstRowIndex; rIndex <= lastRowIndex; rIndex++) {   //遍历行
            Row row = sheet.getRow(rIndex);
            if (row != null) {
                int firstCellIndex = row.getFirstCellNum();
                int lastCellIndex = row.getLastCellNum();
                for (int cIndex = firstCellIndex; cIndex < lastCellIndex; cIndex++) {   //遍历列
                    Cell cell = row.getCell(cIndex);
                    if (cell != null) {
                        if (cIndex == 0) {
                            name = cell.toString();
                        } else if (cIndex == 1) {
                            tableName = cell.toString();
                        } else if (cIndex == 2) {
                            datasetId = cell.toString();
                        }
                    }
                }

                File file = new File(deskTopPath);
                if (!file.exists()) {
                    file.mkdir();
                }

                String packageEnd = datasetId.split("_")[0];

                String javaFileName = setJavaFileName(datasetId);
                String datasetContent = setDataSetSubscriberFile(javaFileName, datasetId, name, tableName, version, packageEnd);

                FileWriter datasetSubscriberFileWriter = new FileWriter(deskTopPath + javaFileName + ".java");
                datasetSubscriberFileWriter.write(datasetContent);
                datasetSubscriberFileWriter.close();

                if (rIndex == firstRowIndex) {
                    analyseLabel.setText("<html>");
                    analyseLabel.setText(analyseLabel.getText() + "------从" + fileName + "中提取数据------" + "<br>");
                    analyseLabel.setText(analyseLabel.getText() + "准备解析数据...." + "<br>");
                }
                analyseLabel.setText(analyseLabel.getText() + datasetId + " 解析完成" + "<br>");
                if (rIndex == lastRowIndex) {
                    analyseLabel.setText(analyseLabel.getText() + "------消费者所需Java文件生成完毕------" + "</html>");
                }
            }
        }
    }

    public static String setJavaFileName(String datasetId) {
        int startIndex = 4;
        String[] datasetIdSplit = datasetId.split("");
        StringBuffer javaFileName = new StringBuffer();
        for (int i = startIndex; i < datasetIdSplit.length; i++) {
            datasetIdSplit[startIndex] = datasetIdSplit[startIndex].toUpperCase();
            if (datasetIdSplit[i].equals("_")) {
                i++;
                String s = datasetIdSplit[i].toUpperCase();
                javaFileName.append(s);
            } else {
                javaFileName.append(datasetIdSplit[i]);
            }
        }
        javaFileName.append("Subscriber");
        return javaFileName.toString();
    }

    public static String setDataSetSubscriberFile(String javaFileName, String datasetId, String name, String tableName, String version, String packageEnd) {
        String s = "package com.shinow.abc.subscriber." + packageEnd + ";\n" +
                "\n" +
                "import com.shinow.abc.amili.subscribe.AbstractSubscriber;\n" +
                "\n" +
                "public class " + javaFileName + " extends AbstractSubscriber {\n" +
                "\n" +
                "    public String getId() {\n" +
                "        return \"" + datasetId + "_dataset\";\n" +
                "    }\n" +
                "\n" +
                "    public String getName() {\n" +
                "        return \"" + name + "订阅者\";\n" +
                "    }\n" +
                "\n" +
                "    public String getVersion() {\n" +
                "        return \"" + version + "\";\n" +
                "    }\n" +
                "\n" +
                "    public String getDescription() {\n" +
                "        return \"\";\n" +
                "    }\n" +
                "\n" +
                "    public String getTableName() {\n" +
                "        return \"" + tableName + "\";\n" +
                "    }\n" +
                "}\n";
        return s;
    }

}

