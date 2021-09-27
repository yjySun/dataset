package com.yjy.action;

import com.yjy.gui.MyFrame;
import com.yjy.pojo.Dataset;
import com.yjy.util.ReadExcel;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;

import javax.swing.*;
import javax.swing.filechooser.FileSystemView;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

/**
 * @Author: Jiyuan Yao
 * @Date: 2021/9/17 10:17
 * @Description: 生成 abc-dataset-cdc-kettle 项目所需的java文件
 */
public class KettleJavaFile {

    public static void generateKettleJavaFile(String filePath, double sortNumber, int startIndex) throws IOException, InvalidFormatException {
        String[] split1 = filePath.split("\\\\");
        String fileName = split1[split1.length - 1];//获取文件名

        FileSystemView fsv = FileSystemView.getFileSystemView();
        File com = fsv.getHomeDirectory();
        String deskTopPath = com.getPath() + "\\kettleJavaFile\\";//获取桌面路径

        List<Dataset> datasets = ReadExcel.readExcel(filePath);
        if (datasets == null) {
            return;
        } else {
            JLabel analyseLabel = MyFrame.analyseLabel;
            for (int i = 0; i < datasets.size(); i++) {
                File file = new File(deskTopPath);
                if (!file.exists()) {
                    file.mkdir();
                }

                Dataset dataset = datasets.get(i);
                StringBuffer stringBufferID = new StringBuffer();
                StringBuffer stringBufferTableName = new StringBuffer();

                String javaFileName = setJavaFileName(dataset.getDatasetId(), startIndex);
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
                    stringBufferTableName.append("        result.add(new DataSetTask(\"" + dataset.getTableName().get(j) + "\", getId(), " + javaFileName + "." + ID + "));\n");

                }
                String idList = stringBufferID.toString();
                String tableNameList = stringBufferTableName.toString();

                String packageEnd = dataset.getDatasetId().split("_")[0];

                String datasetContent = setDataSetFile(javaFileName, idList, dataset.getName(), tableNameList, sortNumber, packageEnd);
                String datasetFullContent = setDataSetFullFile(javaFileName, dataset.getName(), packageEnd);
                String datasetIncrContent = setDataSetIncrFile(javaFileName, dataset.getName(), packageEnd);

                FileWriter datasetFileWriter = new FileWriter(deskTopPath + javaFileName + ".java");
                datasetFileWriter.write(datasetContent);
                datasetFileWriter.close();

                FileWriter datasetFullFileWriter = new FileWriter(deskTopPath + javaFileName + "FullTask.java");
                datasetFullFileWriter.write(datasetFullContent);
                datasetFullFileWriter.close();

                FileWriter datasetIncrFileWriter = new FileWriter(deskTopPath + javaFileName + "IncrTask.java");
                datasetIncrFileWriter.write(datasetIncrContent);
                datasetIncrFileWriter.close();

                if (i == 0) {
                    analyseLabel.setText("<html>");
                    analyseLabel.setText(analyseLabel.getText() + "------从" + fileName + "中提取数据------" + "<br>");
                    analyseLabel.setText(analyseLabel.getText() + "准备解析数据...." + "<br>");
                }
                analyseLabel.setText(analyseLabel.getText() + dataset.getDatasetId() + " 解析完成" + "<br>");
                if (i == datasets.size() - 1) {
                    analyseLabel.setText(analyseLabel.getText() + "------前置机所需Java文件生成完毕------" + "</html>");
                }

                sortNumber++;
            }
        }
    }

    public static String setJavaFileName(String datasetId, int startIndex) {
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

    public static String setDataSetFullFile(String javaFileName, String name, String packageEnd) {
        String s = "package com.shinow.abc.dataset." + packageEnd + ";\n" +
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

    public static String setDataSetIncrFile(String javaFileName, String name, String packageEnd) {
        String s = "package com.shinow.abc.dataset." + packageEnd + ";\n" +
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
