package com.yjy.action;

import com.yjy.gui.MyFrame;
import com.yjy.pojo.Dataset;
import com.yjy.pojo.FullKtr;
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
 * @Date: 2021/9/28 18:19
 * @Description:
 */
public class KettleFullJobFile {

    public static void generateKettleJobFile(String filePath, String databaseIp, String databaseId, String databaseUserName, String databasePassword) throws IOException, SQLException {
        String[] split1 = filePath.split("\\\\");
        String fileName = split1[split1.length - 1];//获取文件名

        FileSystemView fsv = FileSystemView.getFileSystemView();
        File com = fsv.getHomeDirectory();
        String deskTopPath = com.getPath() + "\\kettleJobFile\\";//获取桌面路径

        List<Dataset> datasets = ReadExcel.readExcel(filePath,  databaseIp, databaseId, databaseUserName, databasePassword);

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
                String kettleFullFilePath = deskTopPath + datasetId + "_dataset_full\\";
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
                for (int j = 0; j < dataset.getTableName().size(); j++) {
                    tableName = dataset.getTableName().get(j);
                    List<String> columnName = dataset.getTableColumn().get(tableName);

                    //生成outputHopBuffer语句
                    if (j == 0) {
                        outputHopBuffer.append("    <hop>\n" +
                                "      <from>InputMaster</from>\n" +
                                "      <to>OutputMaster</to>\n" +
                                "      <enabled>Y</enabled>\n" +
                                "    </hop>\n");
                    } else {
                        outputHopBuffer.append("    <hop>\n" +
                                "      <from>InputSlave" + j + "</from>\n" +
                                "      <to>OutputSlave" + j + "</to>\n" +
                                "      <enabled>Y</enabled>\n" +
                                "    </hop>\n");
                    }

                    //生成beginHopBuffer语句
                    if (dataset.getTableName().size() > 1) {
                        if (j == 0) {
                            beginHopBuffer.append("    <hop>\n" +
                                    "      <from>Begin</from>\n" +
                                    "      <to>InputMaster</to>\n" +
                                    "      <enabled>Y</enabled>\n" +
                                    "    </hop>\n");
                        } else {
                            beginHopBuffer.append("    <hop>\n" +
                                    "      <from>Begin</from>\n" +
                                    "      <to>InputSlave" + j + "</to>\n" +
                                    "      <enabled>Y</enabled>\n" +
                                    "    </hop>\n");
                        }
                    }

                    StringBuffer selectSqlBuffer = new StringBuffer();
                    StringBuffer columnBuffer = new StringBuffer();
                    for (int k = 0; k < columnName.size(); k++) {
                        //生成sql语句
                        if (k == 0) {
                            selectSqlBuffer.append("SELECT ");
                        }
                        if (!("RUN_TIME".equals(columnName.get(k)) || "PLANET_CODE".equals(columnName.get(k)))) {
                            if (k == 0) {
                                selectSqlBuffer.append("T." + columnName.get(k) + " AS " + columnName.get(k) + ",\n");
                            } else {
                                selectSqlBuffer.append("    T." + columnName.get(k) + " AS " + columnName.get(k) + ",\n");
                            }
                        }
                        if (k == columnName.size() - 1) {
                            selectSqlBuffer.append("FROM " + dataset.getTableName().get(j) + " T");
                        }

                        //生成对应字段
                        if (!("RUN_TIME".equals(columnName.get(k)) || "PLANET_CODE".equals(columnName.get(k)))) {
                            columnBuffer.append("    <field>\n");
                            columnBuffer.append("        <column_name>" + columnName.get(k) + "</column_name>\n");
                            columnBuffer.append("        <stream_name>" + columnName.get(k) + "</stream_name>\n");
                            columnBuffer.append("    </field>\n");
                        }
                        if (k == columnName.size() - 1) {
                            columnBuffer.append("    <field>\n");
                            columnBuffer.append("        <column_name>" + "OPER_TYPE" + "</column_name>\n");
                            columnBuffer.append("        <stream_name>" + "OPER_TYPE" + "</stream_name>\n");
                            columnBuffer.append("    </field>\n");
                        }
                    }
                    selectSqlBuffer = new StringBuffer(selectSqlBuffer.toString().replace(",\n" + "FROM", ",\n   '1' AS OPER_TYPE\n" + "\nFROM"));

                    //生成select查询语句的整体部分
                    if (j == 0) {
                        selectPartBuffer.append("<step>\n" +
                                "    <name>InputMaster</name>\n" +
                                "    <type>TableInput</type>\n" +
                                "    <description/>\n" +
                                "    <distribute>Y</distribute>\n" +
                                "    <custom_distribution/>\n" +
                                "    <copies>1</copies>\n" +
                                "    <partitioning>\n" +
                                "      <method>none</method>\n" +
                                "      <schema_name/>\n" +
                                "    </partitioning>\n" +
                                "    <connection>Shinow90</connection>\n" +
                                "    <sql>" + selectSqlBuffer.toString() + "</sql>\n" +
                                "    <limit>0</limit>\n" +
                                "    <lookup/>\n" +
                                "    <execute_each_row>N</execute_each_row>\n" +
                                "    <variables_active>N</variables_active>\n" +
                                "    <lazy_conversion_active>N</lazy_conversion_active>\n" +
                                "    <attributes/>\n" +
                                "    <cluster_schema/>\n" +
                                "    <remotesteps>\n" +
                                "      <input>\n" +
                                "      </input>\n" +
                                "      <output>\n" +
                                "      </output>\n" +
                                "    </remotesteps>\n" +
                                "    <GUI>\n" +
                                "      <xloc>208</xloc>\n" +
                                "      <yloc>32</yloc>\n" +
                                "      <draw>Y</draw>\n" +
                                "    </GUI>\n" +
                                "  </step>\n");
                    } else {
                        int position = 32 + j * 80;
                        selectPartBuffer.append("<step>\n" +
                                "    <name>InputSlave" + j + "</name>\n" +
                                "    <type>TableInput</type>\n" +
                                "    <description/>\n" +
                                "    <distribute>Y</distribute>\n" +
                                "    <custom_distribution/>\n" +
                                "    <copies>1</copies>\n" +
                                "    <partitioning>\n" +
                                "      <method>none</method>\n" +
                                "      <schema_name/>\n" +
                                "    </partitioning>\n" +
                                "    <connection>Shinow90</connection>\n" +
                                "    <sql>" + selectSqlBuffer.toString() + "</sql>\n" +
                                "    <limit>0</limit>\n" +
                                "    <lookup/>\n" +
                                "    <execute_each_row>N</execute_each_row>\n" +
                                "    <variables_active>N</variables_active>\n" +
                                "    <lazy_conversion_active>N</lazy_conversion_active>\n" +
                                "    <attributes/>\n" +
                                "    <cluster_schema/>\n" +
                                "    <remotesteps>\n" +
                                "      <input>\n" +
                                "      </input>\n" +
                                "      <output>\n" +
                                "      </output>\n" +
                                "    </remotesteps>\n" +
                                "    <GUI>\n" +
                                "      <xloc>208</xloc>\n" +
                                "      <yloc>" + position + "</yloc>\n" +
                                "      <draw>Y</draw>\n" +
                                "    </GUI>\n" +
                                "  </step>\n");
                    }

                    //生成对应字段的整体部分
                    if (j == 0) {
                        columnPartBuffer.append("<step>\n" +
                                "    <name>OutputMaster</name>\n" +
                                "    <type>TableOutput</type>\n" +
                                "    <description/>\n" +
                                "    <distribute>Y</distribute>\n" +
                                "    <custom_distribution/>\n" +
                                "    <copies>1</copies>\n" +
                                "    <partitioning>\n" +
                                "      <method>none</method>\n" +
                                "      <schema_name/>\n" +
                                "    </partitioning>\n" +
                                "    <connection>Planet</connection>\n" +
                                "    <schema/>\n" +
                                "    <table>" + tableName + "</table>\n" +
                                "    <commit>1000</commit>\n" +
                                "    <truncate>N</truncate>\n" +
                                "    <ignore_errors>N</ignore_errors>\n" +
                                "    <use_batch>Y</use_batch>\n" +
                                "    <specify_fields>Y</specify_fields>\n" +
                                "    <partitioning_enabled>N</partitioning_enabled>\n" +
                                "    <partitioning_field/>\n" +
                                "    <partitioning_daily>N</partitioning_daily>\n" +
                                "    <partitioning_monthly>Y</partitioning_monthly>\n" +
                                "    <tablename_in_field>N</tablename_in_field>\n" +
                                "    <tablename_field/>\n" +
                                "    <tablename_in_table>Y</tablename_in_table>\n" +
                                "    <return_keys>N</return_keys>\n" +
                                "    <return_field/>\n" +
                                "    <fields>\n" +
                                "      " + columnBuffer + "\n" +
                                "    </fields>\n" +
                                "    <attributes/>\n" +
                                "    <cluster_schema/>\n" +
                                "    <remotesteps>\n" +
                                "      <input>\n" +
                                "      </input>\n" +
                                "      <output>\n" +
                                "      </output>\n" +
                                "    </remotesteps>\n" +
                                "    <GUI>\n" +
                                "      <xloc>368</xloc>\n" +
                                "      <yloc>32</yloc>\n" +
                                "      <draw>Y</draw>\n" +
                                "    </GUI>\n" +
                                "  </step>\n");
                    } else {
                        int position = 32 + j * 80;
                        columnPartBuffer.append("<step>\n" +
                                "    <name>OutputSlave" + j + "</name>\n" +
                                "    <type>TableOutput</type>\n" +
                                "    <description/>\n" +
                                "    <distribute>Y</distribute>\n" +
                                "    <custom_distribution/>\n" +
                                "    <copies>1</copies>\n" +
                                "    <partitioning>\n" +
                                "      <method>none</method>\n" +
                                "      <schema_name/>\n" +
                                "    </partitioning>\n" +
                                "    <connection>Planet</connection>\n" +
                                "    <schema/>\n" +
                                "    <table>" + tableName + "</table>\n" +
                                "    <commit>1000</commit>\n" +
                                "    <truncate>N</truncate>\n" +
                                "    <ignore_errors>N</ignore_errors>\n" +
                                "    <use_batch>Y</use_batch>\n" +
                                "    <specify_fields>Y</specify_fields>\n" +
                                "    <partitioning_enabled>N</partitioning_enabled>\n" +
                                "    <partitioning_field/>\n" +
                                "    <partitioning_daily>N</partitioning_daily>\n" +
                                "    <partitioning_monthly>Y</partitioning_monthly>\n" +
                                "    <tablename_in_field>N</tablename_in_field>\n" +
                                "    <tablename_field/>\n" +
                                "    <tablename_in_table>Y</tablename_in_table>\n" +
                                "    <return_keys>N</return_keys>\n" +
                                "    <return_field/>\n" +
                                "    <fields>\n" +
                                "      " + columnBuffer + "\n" +
                                "    </fields>\n" +
                                "    <attributes/>\n" +
                                "    <cluster_schema/>\n" +
                                "    <remotesteps>\n" +
                                "      <input>\n" +
                                "      </input>\n" +
                                "      <output>\n" +
                                "      </output>\n" +
                                "    </remotesteps>\n" +
                                "    <GUI>\n" +
                                "      <xloc>368</xloc>\n" +
                                "      <yloc>" + position + "</yloc>\n" +
                                "      <draw>Y</draw>\n" +
                                "    </GUI>\n" +
                                "  </step>\n");
                    }

                    if (dataset.getTableName().size() > 1) {
                        beginNode = "<step>\n" +
                                "    <name>Begin</name>\n" +
                                "    <type>Dummy</type>\n" +
                                "    <description/>\n" +
                                "    <distribute>Y</distribute>\n" +
                                "    <custom_distribution/>\n" +
                                "    <copies>1</copies>\n" +
                                "    <partitioning>\n" +
                                "      <method>none</method>\n" +
                                "      <schema_name/>\n" +
                                "    </partitioning>\n" +
                                "    <attributes/>\n" +
                                "    <cluster_schema/>\n" +
                                "    <remotesteps>\n" +
                                "      <input>\n" +
                                "      </input>\n" +
                                "      <output>\n" +
                                "      </output>\n" +
                                "    </remotesteps>\n" +
                                "    <GUI>\n" +
                                "      <xloc>48</xloc>\n" +
                                "      <yloc>112</yloc>\n" +
                                "      <draw>Y</draw>\n" +
                                "    </GUI>\n" +
                                "  </step>";
                    } else {
                        beginNode = "";
                    }
                }

                FullKtr fullKtr = new FullKtr();
                fullKtr.setTableName(tableName);
                fullKtr.setOutputHop(outputHopBuffer.toString());
                fullKtr.setBeginHop(beginHopBuffer.toString());
                fullKtr.setSelectSql(selectPartBuffer.toString());
                fullKtr.setColumn(columnPartBuffer.toString());
                fullKtr.setBeginNode(beginNode);

                String kettleFullKtrContent = KettleJobTemplate.setKettleFullKtrFile(fullKtr);

                FileWriter kettleJobFullKtrFileWriter = new FileWriter(kettleFullFilePath + "FullRecordsExtract.ktr");
                kettleJobFullKtrFileWriter.write(kettleFullKtrContent);
                kettleJobFullKtrFileWriter.close();

                if (i == 0) {
                    analyseLabel.setText(analyseLabel.getText() + "------从" + fileName + "中提取数据------" + "<br>");
                    analyseLabel.setText(analyseLabel.getText() + "准备解析数据...." + "<br>");
                }
                analyseLabel.setText(analyseLabel.getText() + dataset.getDatasetId() + " 解析完成" + "<br>");
                if (i == datasets.size() - 1) {
                    analyseLabel.setText(analyseLabel.getText() + "------kettle全量作业生成完毕------" + "<br>");
                }
            }
        }
    }
}