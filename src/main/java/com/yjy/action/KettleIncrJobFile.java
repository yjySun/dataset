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
import java.sql.SQLException;
import java.util.List;

/**
 * @Author: Jiyuan Yao
 * @Date: 2021/9/29 18:22
 * @Description:
 */
public class KettleIncrJobFile {

    public static void generateKettleIncrJobFile(String filePath, String databaseIp, String databaseId, String databaseUserName, String databasePassword) throws IOException, SQLException {
        String[] split1 = filePath.split("\\\\");
        String fileName = split1[split1.length - 1];//获取文件名

        FileSystemView fsv = FileSystemView.getFileSystemView();
        File com = fsv.getHomeDirectory();
        String deskTopPath = com.getPath() + "\\kettleJobFile\\";//获取桌面路径

        List<Dataset> datasets = ReadExcel.readExcel(filePath, databaseIp, databaseId, databaseUserName, databasePassword);

        if (datasets == null) {
            return;
        } else {
            File file = new File(deskTopPath);
            if (!file.exists()) {
                file.mkdir();
            }

            String datasetId;
            JLabel analyseLabel = MyFrame.analyseLabel;

            for (int i = 0; i < datasets.size(); i++) {
                Dataset dataset = datasets.get(i);
                String tableName = null;

                datasetId = dataset.getDatasetId();
                String kettleFullFilePath = deskTopPath + datasetId + "_dataset_incr\\";
                File kettleFullFile = new File(kettleFullFilePath);
                if (!kettleFullFile.exists()) {
                    kettleFullFile.mkdir();
                }

                StringBuffer truncateSql = new StringBuffer();
                for (int j = 0; j < dataset.getTableName().size(); j++) {
                    truncateSql.append("TRUNCATE TABLE " + dataset.getTableName().get(j) + ";\n");
                }

                String kettleFullKjbContent = KettleJobTemplate.setKettleFullKjbFile(datasetId, truncateSql.toString());

                FileWriter kettleJobFullKjbFileWriter = new FileWriter(kettleFullFilePath + datasetId + "_dataset_full.kjb");
                kettleJobFullKjbFileWriter.write(kettleFullKjbContent);
                kettleJobFullKjbFileWriter.close();

                StringBuffer outputHopBuffer = new StringBuffer();
                StringBuffer beginHopBuffer = new StringBuffer();
                StringBuffer selectPartBuffer = new StringBuffer();
                StringBuffer columnPartBuffer = new StringBuffer();
                String beginNode = null;
            }
        }
    }
}
