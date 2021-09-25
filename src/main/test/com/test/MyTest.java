package com.test;

import com.yjy.gui.MyFrame;
import com.yjy.pojo.Dataset;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.Test;

import javax.swing.*;
import javax.swing.filechooser.FileSystemView;
import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author: Jiyuan Yao
 * @Date: 2021/9/25 20:57
 * @Description:
 */

public class MyTest {

    @Test
    public void testExcel() throws Exception {
        String filePath = "C:\\Users\\yjy\\Desktop\\dataset1.xlsx";

        String[] split1 = filePath.split("\\\\");
        String fileName = split1[split1.length - 1];//获取文件名

        FileSystemView fsv = FileSystemView.getFileSystemView();
        File com = fsv.getHomeDirectory();
        String deskTopPath = com.getPath() + "\\kettleJavaFile\\";//获取桌面路径

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

        int firstRowIndex = sheet.getFirstRowNum();
        int lastRowIndex = sheet.getLastRowNum();

        String name = null;
        String tableName = null;
        String datasetId = null;
        String datasetIdDetail = null;
        JLabel analyseLabel = MyFrame.analyseLabel;

        List<Dataset> datasets = new ArrayList<Dataset>();
        for (int rIndex = firstRowIndex; rIndex <= lastRowIndex; rIndex++) {   //遍历行
            Row row = sheet.getRow(rIndex);
            if (row != null) {
                int firstCellIndex = row.getFirstCellNum();
                int lastCellIndex = row.getLastCellNum();
                for (int cIndex = firstCellIndex; cIndex < lastCellIndex; cIndex++) {   //遍历列
                    Cell cell = row.getCell(cIndex);
                    if (cell != null) {
                        if (cIndex == 0) {
                            datasetId = cell.toString();
                        } else if (cIndex == 1) {
                            name = cell.toString();
                        } else if (cIndex == 2) {
                            tableName = cell.toString();
                        } else if (cIndex == 3) {
                            datasetIdDetail = cell.toString();
                        }
                    }
                }
                String packageEnd = datasetId.split("_")[0];

                File file = new File(deskTopPath);
                if (!file.exists()) {
                    file.mkdir();
                }

                if ("".equals(datasetId) && "".equals(name)) {
                    datasets.get(datasets.size() - 1).getTableName().add(tableName);
                    datasets.get(datasets.size() - 1).getDatasetIdDetail().add(datasetIdDetail);
                } else {
                    Dataset dataset = new Dataset();
                    dataset.setDatasetId(datasetId);
                    dataset.setName(name);
                    dataset.getTableName().add(tableName);
                    dataset.getDatasetIdDetail().add(datasetIdDetail);

                    datasets.add(dataset);
                }

            }
        }

        for (int i = 0; i < datasets.size(); i++) {
            System.out.println(i + " " + datasets.get(i).getDatasetId() + " " + datasets.get(i).getName() + " " + datasets.get(i).getTableName() + " " + datasets.get(i).getTableName() + " " + datasets.get(i).getDatasetIdDetail());
        }
    }
}
