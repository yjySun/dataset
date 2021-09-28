package com.yjy.action;

import com.yjy.gui.MyFrame;
import com.yjy.pojo.Dataset;
import com.yjy.template.KettleJobTemplate;
import com.yjy.util.ReadExcel;

import javax.swing.*;
import javax.swing.filechooser.FileSystemView;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

/**
 * @Author: Jiyuan Yao
 * @Date: 2021/9/28 18:19
 * @Description:
 */
public class KettleJobFile {

    public static void main(String[] args) {
        try {
            generateKettleJobFile("C:\\Users\\yjy\\Desktop\\dataset4.xlsx", true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void generateKettleJobFile(String filePath, boolean isGenerateKettleJob) throws IOException {
        String[] split1 = filePath.split("\\\\");
        String fileName = split1[split1.length - 1];//获取文件名

        FileSystemView fsv = FileSystemView.getFileSystemView();
        File com = fsv.getHomeDirectory();
        String deskTopPath = com.getPath() + "\\kettleJobFile\\";//获取桌面路径

        List<Dataset> datasets = ReadExcel.readExcel(filePath, isGenerateKettleJob);

        if (datasets == null) {
            return;
        } else {
            File file = new File(deskTopPath);
            if (!file.exists()) {
                file.mkdir();
            }

            String datasetId = null;
            String tableName = null;
            JLabel analyseLabel = MyFrame.analyseLabel;
            for (int i = 0; i < datasets.size(); i++) {
                Dataset dataset = datasets.get(i);
                for (int j = 0; j < dataset.getTableName().size(); j++) {
                    datasetId = dataset.getDatasetId();
                    tableName = dataset.getTableName().get(j);
                }

                String kettleFullFilePath = deskTopPath + datasetId + "_dataset_full\\";
                File kettleFullFile = new File(kettleFullFilePath);
                if (!kettleFullFile.exists()) {
                    kettleFullFile.mkdir();
                }

                String truncateSql = "TRUNCATE TABLE " + tableName + ";";
                String kettleFullKjbContent = KettleJobTemplate.setKettleFullKjbFile(datasetId, truncateSql);

                FileWriter kettleJobFullKjbFileWriter = new FileWriter(kettleFullFilePath + datasetId + "_dataset_full.kjb");
                kettleJobFullKjbFileWriter.write(kettleFullKjbContent);
                kettleJobFullKjbFileWriter.close();

                StringBuffer columnBuffer = new StringBuffer();
                StringBuffer selectSqlBuffer = new StringBuffer();

                for (int j = 0; j < dataset.getTableColumn().size(); j++) {
                    String column = dataset.getTableColumn().get(j);
                    //生成对应字段
                    columnBuffer.append("    <field>\n");
                    columnBuffer.append("        <column_name>" + column + "</column_name>\n");
                    columnBuffer.append("        <stream_name>" + column + "</stream_name>\n");
                    columnBuffer.append("    </field>\n");
                    //生成sql语句
                    if (j == 0) {
                        selectSqlBuffer.append("SELECT ");
                    }

                    if (!("RUN_TIME".equals(column) || "PLANET_CODE".equals(column))) {
                        selectSqlBuffer.append("T." + column + " AS " + column + ",\n");
                    }

                    if (j == dataset.getTableColumn().size() - 1) {
                        selectSqlBuffer.append("FROM " + dataset.getTableName().get(i) + " T");
                    }
                    selectSqlBuffer = new StringBuffer(selectSqlBuffer.toString().replace(",\n" + "FROM", "\n" + "FROM"));
                }

                String kettleFullKtrContent = KettleJobTemplate.setKettleFullKtrFile(selectSqlBuffer.toString(), tableName, columnBuffer.toString());

                FileWriter kettleJobFullKtrFileWriter = new FileWriter(kettleFullFilePath + "FullRecordsExtract.ktr");
                kettleJobFullKtrFileWriter.write(kettleFullKtrContent);
                kettleJobFullKtrFileWriter.close();

//                if (i == 0) {
//                    analyseLabel.setText(analyseLabel.getText() + "------从" + fileName + "中提取数据------" + "<br>");
//                    analyseLabel.setText(analyseLabel.getText() + "准备解析数据...." + "<br>");
//                }
//                analyseLabel.setText(analyseLabel.getText() + dataset.getDatasetId() + " 解析完成" + "<br>");
//                if (i == datasets.size() - 1) {
//                    analyseLabel.setText(analyseLabel.getText() + "------前置机所需Java文件生成完毕------" + "<br>");
//                }
            }
        }
    }
}