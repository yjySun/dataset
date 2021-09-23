package com.yjy.util;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import javax.swing.filechooser.FileSystemView;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;

/**
 * @Author: Jiyuan Yao
 * @Date: 2021/9/17 10:17
 * @Description: 生成 abc-dataset-cdc-kettle 项目所需的java文件
 */
public class KettleJavaFile {

    public static void generateKettleJavaFile(String filePath) throws Exception {
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

        int firstRowIndex = sheet.getFirstRowNum();   //第一行是列名，所以不读
        int lastRowIndex = sheet.getLastRowNum();

        String name = null;
        String tableName = null;
        String datasetId = null;
        double sortNumber = 150.0;
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
                String javaFileName = setJavaFileName(datasetId);
                String datasetContent = setDataSetFile(javaFileName, datasetId, name, tableName, sortNumber);
                String datasetFullContent = setDataSetFullFile(javaFileName, name);
                String datasetIncrContent = setDataSetIncrFile(javaFileName, name);

                File file = new File(deskTopPath);
                if (!file.exists()) {
                    file.mkdir();
                }

                FileWriter datasetFileWriter = new FileWriter(deskTopPath + javaFileName + ".java");
                datasetFileWriter.write(datasetContent);
                datasetFileWriter.close();

                FileWriter datasetFullFileWriter = new FileWriter(deskTopPath + javaFileName + "FullTask.java");
                datasetFullFileWriter.write(datasetFullContent);
                datasetFullFileWriter.close();

                FileWriter datasetIncrFileWriter = new FileWriter(deskTopPath + javaFileName + "IncrTask.java");
                datasetIncrFileWriter.write(datasetIncrContent);
                datasetIncrFileWriter.close();

                sortNumber++;
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
        javaFileName.append("DataSet");
        return javaFileName.toString();
    }

    public static String setDataSetFile(String javaFileName, String datasetId, String name, String tableName, double sortNumber) {
        String str1 = "package com.shinow.abc.dataset.bea;\n" +
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
                "    public static String ID = \"" + datasetId + "_dataset\";\n" +
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
                "        result.add(new DataSetTask(\"" + tableName + "\", getId()," + javaFileName + ".ID));\n" +
                "        return result;\n" +
                "    }\n" +
                "}\n";
        return str1;
    }

    public static String setDataSetFullFile(String javaFileName, String name) {
        String s = "package com.shinow.abc.dataset.bea;\n" +
                "\n" +
                "import com.shinow.abc.amili.dataset.DataSet;\n" +
                "import com.shinow.abc.amili.dataset.DataSetJobTask;\n" +
                "import com.shinow.abc.amili.dataset.DataSetJobTaskInfo;\n" +
                "import com.shinow.abc.amili.dataset.KettleThenGenerateReliableEventJob;\n" +
                "\n" +
                "@DataSetJobTaskInfo\n" +
                "public class " + javaFileName + "FullTask extends KettleThenGenerateReliableEventJob implements DataSetJobTask {\n" +
                "    @Override\n" +
                "    public String groupName() {\n" +
                "        return " + javaFileName + ".ID;\n" +
                "    }\n" +
                "\n" +
                "    @Override\n" +
                "    public String getId() {\n" +
                "        return this.groupName() + DataSet.FULL;\n" +
                "    }\n" +
                "\n" +
                "    @Override\n" +
                "    public String getName() {\n" +
                "        return \"" + name + "全量任务\";\n" +
                "    }\n" +
                "\n" +
                "    @Override\n" +
                "    public String getDescription() {\n" +
                "        return \"" + name + "全量任务\";\n" +
                "    }\n" +
                "}\n";
        return s;
    }

    public static String setDataSetIncrFile(String javaFileName, String name) {
        String s = "package com.shinow.abc.dataset.bea;\n" +
                "\n" +
                "import com.shinow.abc.amili.dataset.DataSet;\n" +
                "import com.shinow.abc.amili.dataset.DataSetJobTask;\n" +
                "import com.shinow.abc.amili.dataset.DataSetJobTaskInfo;\n" +
                "import com.shinow.abc.amili.dataset.KettleThenGenerateReliableEventJob;\n" +
                "\n" +
                "@DataSetJobTaskInfo\n" +
                "public class " + javaFileName + "IncrTask extends KettleThenGenerateReliableEventJob implements DataSetJobTask {\n" +
                "    @Override\n" +
                "    public String groupName() {\n" +
                "        return " + javaFileName + ".ID;\n" +
                "    }\n" +
                "\n" +
                "    @Override\n" +
                "    public String getId() {\n" +
                "        return this.groupName() + DataSet.INCR;\n" +
                "    }\n" +
                "\n" +
                "    @Override\n" +
                "    public String getName() {\n" +
                "        return \"" + name + "增量任务\";\n" +
                "    }\n" +
                "\n" +
                "    @Override\n" +
                "    public String getDescription() {\n" +
                "        return \"" + name + "增量任务\";\n" +
                "    }\n" +
                "}\n";
        return s;
    }
}
