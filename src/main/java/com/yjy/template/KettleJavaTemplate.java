package com.yjy.template;

/**
 * @Author: Jiyuan Yao
 * @Date: 2021/9/28 18:34
 * @Description:
 */
public class KettleJavaTemplate {

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
