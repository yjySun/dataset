package com.yjy.util;

import com.yjy.pojo.Dataset;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.ColumnListHandler;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author: Jiyuan Yao
 * @Date: 2021/9/27 10:41
 * @Description:
 */
public class ReadExcel {
    public static List<Dataset> datasets = new ArrayList<Dataset>();

    public static List<Dataset> readExcel(String filePath, boolean isGenerateKettleJob) {
        File excel = new File(filePath);
        String[] split = excel.getName().split("\\.");  //.是特殊字符，需要转义！！！！！
        Workbook wb = null;
        //根据文件后缀（xls/xlsx）进行判断
        if ("xls".equals(split[1])) {
            FileInputStream fis = null;   //文件流对象
            try {
                fis = new FileInputStream(excel);
                wb = new HSSFWorkbook(fis);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if ("xlsx".equals(split[1])) {
            try {
                wb = new XSSFWorkbook(excel);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (InvalidFormatException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("文件类型错误!");
            return null;
        }

        //开始解析
        Sheet sheet = wb.getSheetAt(0);     //读取sheet 0

        int firstRowIndex = sheet.getFirstRowNum();
        int lastRowIndex = sheet.getLastRowNum();

        String datasetId = null;
        String name = null;
        String tableName = null;
        String datasetIdDetail = null;

        for (int rIndex = firstRowIndex; rIndex <= lastRowIndex; rIndex++) {   //遍历行
            Row row = sheet.getRow(rIndex);
            if (row != null) {
                int firstCellIndex = row.getFirstCellNum();
                int lastCellIndex = row.getLastCellNum();
                for (int cIndex = firstCellIndex; cIndex < lastCellIndex; cIndex++) {   //遍历列
                    Cell cell = row.getCell(cIndex);
                    if (cell != null) {
                        if (cIndex == firstCellIndex) {
                            datasetId = cell.toString();
                        } else if (cIndex == firstCellIndex + 1) {
                            name = cell.toString();
                        } else if (cIndex == firstCellIndex + 2) {
                            tableName = cell.toString();
                        } else if (cIndex == firstCellIndex + 3) {
                            datasetIdDetail = cell.toString();
                        }
                    }
                }

                List<String> columnName = null;
                if (isGenerateKettleJob) {
                    //查询表的字段
                    QueryRunner queryRunner;
                    try {
                        queryRunner = new QueryRunner(DBUtil.getDataSource());
                        columnName = queryRunner.query("SELECT COLUMN_NAME FROM ALL_TAB_COLUMNS WHERE TABLE_NAME = ? ORDER BY COLUMN_ID", new ColumnListHandler<String>("COLUMN_NAME"), tableName);
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }

                if ("".equals(datasetId) && "".equals(name)) {
                    datasets.get(datasets.size() - 1).getTableName().add(tableName);
                    datasets.get(datasets.size() - 1).getDatasetIdDetail().add(datasetIdDetail);
                    datasets.get(datasets.size() - 1).getTableColumn().add(columnName.toString());
                } else {
                    Dataset dataset = new Dataset();
                    dataset.setDatasetId(datasetId);
                    dataset.setName(name);
                    dataset.getTableName().add(tableName);
                    dataset.getDatasetIdDetail().add(datasetIdDetail);
                    if (isGenerateKettleJob) {
                        dataset.getTableColumn().add(columnName.toString());
                    }

                    datasets.add(dataset);
                }
            }
        }

        for (int i = 0; i < datasets.size(); i++) {
            for (int j = 0; j < datasets.get(i).getTableColumn().size(); j++) {
                System.out.println(datasets.get(i).getDatasetId() + " " + datasets.get(i).getTableColumn().get(j));
            }
        }
        return datasets;
    }
}
