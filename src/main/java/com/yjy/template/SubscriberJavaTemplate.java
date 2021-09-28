package com.yjy.template;

/**
 * @Author: Jiyuan Yao
 * @Date: 2021/9/28 18:35
 * @Description:
 */
public class SubscriberJavaTemplate {

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
