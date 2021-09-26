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
import javax.tools.JavaCompiler;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
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
        String filePath = "C:\\Users\\shinow\\Desktop\\dataset1.xlsx";

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

        String datasetId= null;
        String name = null;
        String tableName = null;
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
            Dataset dataset = datasets.get(i);
            StringBuffer stringBufferID = new StringBuffer();
            StringBuffer stringBufferTableName = new StringBuffer();
            for (int j = 0; j < dataset.getTableName().size(); j++) {
                String ID;
                if (j == 0) {
                    ID = "ID";
                } else {
                    ID = "ID" + j;
                }
                //生成多个ID
                stringBufferID.append("    public static String " + ID + " = \"" + dataset.getDatasetIdDetail().get(j) + "_dataset\";\n");

                //生成多个result
                stringBufferTableName.append("        result.add(new DataSetTask(\"" + dataset.getTableName().get(j) + "\", getId(), " + "666" + "." + ID + "));\n");
            }
            String idList = stringBufferID.toString();
            String tableNameList = stringBufferTableName.toString();
            String bea = setDataSetFile("123", idList, "321", tableNameList, 15.0, "bea");

            FileWriter datasetFileWriter = new FileWriter("C:\\Users\\shinow\\Desktop\\beatest\\" + i + ".java");
            datasetFileWriter.write(bea);
            datasetFileWriter.close();


        }
    }

    public static String setDataSetFile(String javaFileName, String idList, String name, String tableNameList, double sortNumber, String packageEnd) {
        String s = "package com.shinow.abc.dataset." + packageEnd + "\";\n" +
                "\n" +
                "import com.shinow.abc.amili.dataset.AbstractDataSet;\n" +
                "import com.shinow.abc.amili.dataset.DataSetInfo;\n" +
                "import com.shinow.abc.amili.dataset.DataSetTask;\n" +
                "import com.shinow.abc.dataset.bea." + javaFileName + ";\n" +
                "\n" +
                "import java.util.ArrayList;\n" +
                "import java.util.List;\n" +
                "\n" +
                "@DataSetInfo\n" +
                "public class " + javaFileName + " extends AbstractDataSet {\n" +
                idList +
                "\n" +
                "    @Override\n" +
                "    public String getId() {\n" +
                "        return ID;\n" +
                "    }\n" +
                "\n" +
                "    @Override\n" +
                "    public String getName() {\n" +
                "        return \"" + name + "数据集\";\n" +
                "    }\n" +
                "\n" +
                "    @Override\n" +
                "    public String getDescription() {\n" +
                "        return \"\";\n" +
                "    }\n" +
                "\n" +
                "    @Override\n" +
                "    public double sort() {\n" +
                "        return " + sortNumber + ";\n" +
                "    }\n" +
                "\n" +
                "    @Override\n" +
                "    public List<DataSetTask> tasks() {\n" +
                "        List<DataSetTask> result = new ArrayList<>();\n" +
                tableNameList +
                "        return result;\n" +
                "    }\n" +
                "}\n";
        return s;
    }
}
