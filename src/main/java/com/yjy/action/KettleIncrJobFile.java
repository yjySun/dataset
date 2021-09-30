package com.yjy.action;

import com.yjy.gui.MyFrame;
import com.yjy.pojo.Dataset;
import com.yjy.pojo.IncrKjb;
import com.yjy.template.KettleJobTemplate;
import com.yjy.util.ReadExcel;

import javax.swing.*;
import javax.swing.filechooser.FileSystemView;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
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

                //生成增量文件夹
                datasetId = dataset.getDatasetId();
                String kettleIncrFilePath = deskTopPath + datasetId + "_dataset_incr\\";
                File kettleIncrFile = new File(kettleIncrFilePath);
                if (!kettleIncrFile.exists()) {
                    kettleIncrFile.mkdir();
                }

                StringBuffer groupBuffer = new StringBuffer();
                StringBuffer hopBuffer = new StringBuffer();
                for (int j = 0; j < dataset.getTableName().size(); j++) {
                    tableName = dataset.getTableName().get(j);
                    List<String> columnName = dataset.getTableColumn().get(tableName);
                    int yposition = 160 + j * 96;
                    int[] xposition = new int[]{160, 288, 400};

                    ArrayList arraylist = new ArrayList(xposition.length);

                    for (int k = 0; k < xposition.length; k++) {
                        arraylist.add(xposition[k]); //存放元素
                    }
                    if (j % 2 != 0) {
                        Collections.reverse(arraylist);
                    }

                    if (dataset.getTableName().size() > 1) {
                        if (j == 0) {
                            groupBuffer.append("    <entry>\n" +
                                    "      <name>GetOrgCode</name>\n" +
                                    "      <description/>\n" +
                                    "      <type>TRANS</type>\n" +
                                    "      <attributes/>\n" +
                                    "      <specification_method>filename</specification_method>\n" +
                                    "      <trans_object_id/>\n" +
                                    "      <filename>${Internal.Entry.Current.Directory}/GetOrgCode.ktr</filename>\n" +
                                    "      <transname/>\n" +
                                    "      <arg_from_previous>N</arg_from_previous>\n" +
                                    "      <params_from_previous>N</params_from_previous>\n" +
                                    "      <exec_per_row>N</exec_per_row>\n" +
                                    "      <clear_rows>N</clear_rows>\n" +
                                    "      <clear_files>N</clear_files>\n" +
                                    "      <set_logfile>N</set_logfile>\n" +
                                    "      <logfile/>\n" +
                                    "      <logext/>\n" +
                                    "      <add_date>N</add_date>\n" +
                                    "      <add_time>N</add_time>\n" +
                                    "      <loglevel>Basic</loglevel>\n" +
                                    "      <cluster>N</cluster>\n" +
                                    "      <slave_server_name/>\n" +
                                    "      <set_append_logfile>N</set_append_logfile>\n" +
                                    "      <wait_until_finished>Y</wait_until_finished>\n" +
                                    "      <follow_abort_remote>N</follow_abort_remote>\n" +
                                    "      <create_parent_folder>N</create_parent_folder>\n" +
                                    "      <logging_remote_work>N</logging_remote_work>\n" +
                                    "      <run_configuration>Pentaho local</run_configuration>\n" +
                                    "      <parameters>\n" +
                                    "        <pass_all_parameters>Y</pass_all_parameters>\n" +
                                    "      </parameters>\n" +
                                    "      <parallel>N</parallel>\n" +
                                    "      <draw>Y</draw>\n" +
                                    "      <nr>0</nr>\n" +
                                    "      <xloc>160</xloc>\n" +
                                    "      <yloc>64</yloc>\n" +
                                    "      <attributes_kjc/>\n" +
                                    "    </entry>\n");
                            groupBuffer.append("    <entry>\n" +
                                    "      <name>GetMasterMaxUuid</name>\n" +
                                    "      <description/>\n" +
                                    "      <type>TRANS</type>\n" +
                                    "      <attributes/>\n" +
                                    "      <specification_method>filename</specification_method>\n" +
                                    "      <trans_object_id/>\n" +
                                    "      <filename>${Internal.Entry.Current.Directory}/GetMasterMaxUuid.ktr</filename>\n" +
                                    "      <transname/>\n" +
                                    "      <arg_from_previous>N</arg_from_previous>\n" +
                                    "      <params_from_previous>N</params_from_previous>\n" +
                                    "      <exec_per_row>N</exec_per_row>\n" +
                                    "      <clear_rows>N</clear_rows>\n" +
                                    "      <clear_files>N</clear_files>\n" +
                                    "      <set_logfile>N</set_logfile>\n" +
                                    "      <logfile/>\n" +
                                    "      <logext/>\n" +
                                    "      <add_date>N</add_date>\n" +
                                    "      <add_time>N</add_time>\n" +
                                    "      <loglevel>Basic</loglevel>\n" +
                                    "      <cluster>N</cluster>\n" +
                                    "      <slave_server_name/>\n" +
                                    "      <set_append_logfile>N</set_append_logfile>\n" +
                                    "      <wait_until_finished>Y</wait_until_finished>\n" +
                                    "      <follow_abort_remote>N</follow_abort_remote>\n" +
                                    "      <create_parent_folder>N</create_parent_folder>\n" +
                                    "      <logging_remote_work>N</logging_remote_work>\n" +
                                    "      <run_configuration>Pentaho local</run_configuration>\n" +
                                    "      <parameters>\n" +
                                    "        <pass_all_parameters>Y</pass_all_parameters>\n" +
                                    "      </parameters>\n" +
                                    "      <parallel>N</parallel>\n" +
                                    "      <draw>Y</draw>\n" +
                                    "      <nr>0</nr>\n" +
                                    "      <xloc>" + arraylist.get(0) + "</xloc>\n" +
                                    "      <yloc>" + yposition + "</yloc>\n" +
                                    "      <attributes_kjc/>\n" +
                                    "    </entry>\n" +
                                    "    <entry>\n" +
                                    "      <name>MasterIncr</name>\n" +
                                    "      <description/>\n" +
                                    "      <type>TRANS</type>\n" +
                                    "      <attributes/>\n" +
                                    "      <specification_method>filename</specification_method>\n" +
                                    "      <trans_object_id/>\n" +
                                    "      <filename>${Internal.Entry.Current.Directory}/MasterIncr.ktr</filename>\n" +
                                    "      <transname/>\n" +
                                    "      <arg_from_previous>N</arg_from_previous>\n" +
                                    "      <params_from_previous>N</params_from_previous>\n" +
                                    "      <exec_per_row>N</exec_per_row>\n" +
                                    "      <clear_rows>N</clear_rows>\n" +
                                    "      <clear_files>N</clear_files>\n" +
                                    "      <set_logfile>N</set_logfile>\n" +
                                    "      <logfile/>\n" +
                                    "      <logext/>\n" +
                                    "      <add_date>N</add_date>\n" +
                                    "      <add_time>N</add_time>\n" +
                                    "      <loglevel>Basic</loglevel>\n" +
                                    "      <cluster>N</cluster>\n" +
                                    "      <slave_server_name/>\n" +
                                    "      <set_append_logfile>N</set_append_logfile>\n" +
                                    "      <wait_until_finished>Y</wait_until_finished>\n" +
                                    "      <follow_abort_remote>N</follow_abort_remote>\n" +
                                    "      <create_parent_folder>N</create_parent_folder>\n" +
                                    "      <logging_remote_work>N</logging_remote_work>\n" +
                                    "      <run_configuration>Pentaho local</run_configuration>\n" +
                                    "      <parameters>\n" +
                                    "        <pass_all_parameters>Y</pass_all_parameters>\n" +
                                    "      </parameters>\n" +
                                    "      <parallel>N</parallel>\n" +
                                    "      <draw>Y</draw>\n" +
                                    "      <nr>0</nr>\n" +
                                    "      <xloc>" + arraylist.get(1) + "</xloc>\n" +
                                    "      <yloc>" + yposition + "</yloc>\n" +
                                    "      <attributes_kjc/>\n" +
                                    "    </entry>\n" +
                                    "    <entry>\n" +
                                    "      <name>DeleteMasterSycn</name>\n" +
                                    "      <description/>\n" +
                                    "      <type>SQL</type>\n" +
                                    "      <attributes/>\n" +
                                    "      <sql>DELETE FROM " + dataset.getTableName().get(j) + " WHERE UUID &lt;= ${MASTER_MAX_UUID}</sql>\n" +
                                    "      <useVariableSubstitution>T</useVariableSubstitution>\n" +
                                    "      <sqlfromfile>F</sqlfromfile>\n" +
                                    "      <sqlfilename/>\n" +
                                    "      <sendOneStatement>F</sendOneStatement>\n" +
                                    "      <connection>Shinow90</connection>\n" +
                                    "      <parallel>N</parallel>\n" +
                                    "      <draw>Y</draw>\n" +
                                    "      <nr>0</nr>\n" +
                                    "      <xloc>" + arraylist.get(2) + "</xloc>\n" +
                                    "      <yloc>" + yposition + "</yloc>\n" +
                                    "      <attributes_kjc/>\n" +
                                    "    </entry>");
                        } else {
                            groupBuffer.append("    <entry>\n" +
                                    "      <name>GetSlave" + j + "MaxUuid</name>\n" +
                                    "      <description/>\n" +
                                    "      <type>TRANS</type>\n" +
                                    "      <attributes/>\n" +
                                    "      <specification_method>filename</specification_method>\n" +
                                    "      <trans_object_id/>\n" +
                                    "      <filename>${Internal.Entry.Current.Directory}/GetSlave" + j + "MaxUuid.ktr</filename>\n" +
                                    "      <transname/>\n" +
                                    "      <arg_from_previous>N</arg_from_previous>\n" +
                                    "      <params_from_previous>N</params_from_previous>\n" +
                                    "      <exec_per_row>N</exec_per_row>\n" +
                                    "      <clear_rows>N</clear_rows>\n" +
                                    "      <clear_files>N</clear_files>\n" +
                                    "      <set_logfile>N</set_logfile>\n" +
                                    "      <logfile/>\n" +
                                    "      <logext/>\n" +
                                    "      <add_date>N</add_date>\n" +
                                    "      <add_time>N</add_time>\n" +
                                    "      <loglevel>Basic</loglevel>\n" +
                                    "      <cluster>N</cluster>\n" +
                                    "      <slave_server_name/>\n" +
                                    "      <set_append_logfile>N</set_append_logfile>\n" +
                                    "      <wait_until_finished>Y</wait_until_finished>\n" +
                                    "      <follow_abort_remote>N</follow_abort_remote>\n" +
                                    "      <create_parent_folder>N</create_parent_folder>\n" +
                                    "      <logging_remote_work>N</logging_remote_work>\n" +
                                    "      <run_configuration>Pentaho local</run_configuration>\n" +
                                    "      <parameters>\n" +
                                    "        <pass_all_parameters>Y</pass_all_parameters>\n" +
                                    "      </parameters>\n" +
                                    "      <parallel>N</parallel>\n" +
                                    "      <draw>Y</draw>\n" +
                                    "      <nr>0</nr>\n" +
                                    "      <xloc>" + arraylist.get(0) + "</xloc>\n" +
                                    "      <yloc>" + yposition + "</yloc>\n" +
                                    "      <attributes_kjc/>\n" +
                                    "    </entry>\n" +
                                    "    <entry>\n" +
                                    "      <name>Slave" + j + "Incr</name>\n" +
                                    "      <description/>\n" +
                                    "      <type>TRANS</type>\n" +
                                    "      <attributes/>\n" +
                                    "      <specification_method>filename</specification_method>\n" +
                                    "      <trans_object_id/>\n" +
                                    "      <filename>${Internal.Entry.Current.Directory}/Slave" + j + "Incr.ktr</filename>\n" +
                                    "      <transname/>\n" +
                                    "      <arg_from_previous>N</arg_from_previous>\n" +
                                    "      <params_from_previous>N</params_from_previous>\n" +
                                    "      <exec_per_row>N</exec_per_row>\n" +
                                    "      <clear_rows>N</clear_rows>\n" +
                                    "      <clear_files>N</clear_files>\n" +
                                    "      <set_logfile>N</set_logfile>\n" +
                                    "      <logfile/>\n" +
                                    "      <logext/>\n" +
                                    "      <add_date>N</add_date>\n" +
                                    "      <add_time>N</add_time>\n" +
                                    "      <loglevel>Basic</loglevel>\n" +
                                    "      <cluster>N</cluster>\n" +
                                    "      <slave_server_name/>\n" +
                                    "      <set_append_logfile>N</set_append_logfile>\n" +
                                    "      <wait_until_finished>Y</wait_until_finished>\n" +
                                    "      <follow_abort_remote>N</follow_abort_remote>\n" +
                                    "      <create_parent_folder>N</create_parent_folder>\n" +
                                    "      <logging_remote_work>N</logging_remote_work>\n" +
                                    "      <run_configuration>Pentaho local</run_configuration>\n" +
                                    "      <parameters>\n" +
                                    "        <pass_all_parameters>Y</pass_all_parameters>\n" +
                                    "      </parameters>\n" +
                                    "      <parallel>N</parallel>\n" +
                                    "      <draw>Y</draw>\n" +
                                    "      <nr>0</nr>\n" +
                                    "      <xloc>" + arraylist.get(1) + "</xloc>\n" +
                                    "      <yloc>" + yposition + "</yloc>\n" +
                                    "      <attributes_kjc/>\n" +
                                    "    </entry>\n" +
                                    "    <entry>\n" +
                                    "      <name>DeleteSlave" + j + "Sycn</name>\n" +
                                    "      <description/>\n" +
                                    "      <type>SQL</type>\n" +
                                    "      <attributes/>\n" +
                                    "      <sql>DELETE FROM " + dataset.getTableName().get(j) + " WHERE UUID &lt;= ${SLAVE" + j + "_MAX_UUID}</sql>\n" +
                                    "      <useVariableSubstitution>T</useVariableSubstitution>\n" +
                                    "      <sqlfromfile>F</sqlfromfile>\n" +
                                    "      <sqlfilename/>\n" +
                                    "      <sendOneStatement>F</sendOneStatement>\n" +
                                    "      <connection>Shinow90</connection>\n" +
                                    "      <parallel>N</parallel>\n" +
                                    "      <draw>Y</draw>\n" +
                                    "      <nr>0</nr>\n" +
                                    "      <xloc>" + arraylist.get(2) + "</xloc>\n" +
                                    "      <yloc>" + yposition + "</yloc>\n" +
                                    "      <attributes_kjc/>\n" +
                                    "    </entry>");
                            if (j == dataset.getTableName().size() - 1) {
                                groupBuffer.append("    <entry>\n" +
                                        "      <name>Success</name>\n" +
                                        "      <description/>\n" +
                                        "      <type>SUCCESS</type>\n" +
                                        "      <attributes/>\n" +
                                        "      <parallel>N</parallel>\n" +
                                        "      <draw>Y</draw>\n" +
                                        "      <nr>0</nr>\n" +
                                        "      <xloc>" + arraylist.get(2) + "</xloc>\n" +
                                        "      <yloc>" + (yposition + 96) + "</yloc>\n" +
                                        "      <attributes_kjc/>\n" +
                                        "    </entry>\n");
                            }
                        }
                    } else {
                        groupBuffer.append("    <entry>\n" +
                                "      <name>GetMasterMaxUuid</name>\n" +
                                "      <description/>\n" +
                                "      <type>TRANS</type>\n" +
                                "      <attributes/>\n" +
                                "      <specification_method>filename</specification_method>\n" +
                                "      <trans_object_id/>\n" +
                                "      <filename>${Internal.Entry.Current.Directory}/GetMasterMaxUuid.ktr</filename>\n" +
                                "      <transname/>\n" +
                                "      <arg_from_previous>N</arg_from_previous>\n" +
                                "      <params_from_previous>N</params_from_previous>\n" +
                                "      <exec_per_row>N</exec_per_row>\n" +
                                "      <clear_rows>N</clear_rows>\n" +
                                "      <clear_files>N</clear_files>\n" +
                                "      <set_logfile>N</set_logfile>\n" +
                                "      <logfile/>\n" +
                                "      <logext/>\n" +
                                "      <add_date>N</add_date>\n" +
                                "      <add_time>N</add_time>\n" +
                                "      <loglevel>Basic</loglevel>\n" +
                                "      <cluster>N</cluster>\n" +
                                "      <slave_server_name/>\n" +
                                "      <set_append_logfile>N</set_append_logfile>\n" +
                                "      <wait_until_finished>Y</wait_until_finished>\n" +
                                "      <follow_abort_remote>N</follow_abort_remote>\n" +
                                "      <create_parent_folder>N</create_parent_folder>\n" +
                                "      <logging_remote_work>N</logging_remote_work>\n" +
                                "      <run_configuration>Pentaho local</run_configuration>\n" +
                                "      <parameters>\n" +
                                "        <pass_all_parameters>Y</pass_all_parameters>\n" +
                                "      </parameters>\n" +
                                "      <parallel>N</parallel>\n" +
                                "      <draw>Y</draw>\n" +
                                "      <nr>0</nr>\n" +
                                "      <xloc>176</xloc>\n" +
                                "      <yloc>64</yloc>\n" +
                                "      <attributes_kjc/>\n" +
                                "    </entry>\n" +
                                "    <entry>\n" +
                                "      <name>MasterIncr</name>\n" +
                                "      <description/>\n" +
                                "      <type>TRANS</type>\n" +
                                "      <attributes/>\n" +
                                "      <specification_method>filename</specification_method>\n" +
                                "      <trans_object_id/>\n" +
                                "      <filename>${Internal.Entry.Current.Directory}/MasterIncr.ktr</filename>\n" +
                                "      <transname/>\n" +
                                "      <arg_from_previous>N</arg_from_previous>\n" +
                                "      <params_from_previous>N</params_from_previous>\n" +
                                "      <exec_per_row>N</exec_per_row>\n" +
                                "      <clear_rows>N</clear_rows>\n" +
                                "      <clear_files>N</clear_files>\n" +
                                "      <set_logfile>N</set_logfile>\n" +
                                "      <logfile/>\n" +
                                "      <logext/>\n" +
                                "      <add_date>N</add_date>\n" +
                                "      <add_time>N</add_time>\n" +
                                "      <loglevel>Basic</loglevel>\n" +
                                "      <cluster>N</cluster>\n" +
                                "      <slave_server_name/>\n" +
                                "      <set_append_logfile>N</set_append_logfile>\n" +
                                "      <wait_until_finished>Y</wait_until_finished>\n" +
                                "      <follow_abort_remote>N</follow_abort_remote>\n" +
                                "      <create_parent_folder>N</create_parent_folder>\n" +
                                "      <logging_remote_work>N</logging_remote_work>\n" +
                                "      <run_configuration>Pentaho local</run_configuration>\n" +
                                "      <parameters>\n" +
                                "        <pass_all_parameters>Y</pass_all_parameters>\n" +
                                "      </parameters>\n" +
                                "      <parallel>N</parallel>\n" +
                                "      <draw>Y</draw>\n" +
                                "      <nr>0</nr>\n" +
                                "      <xloc>304</xloc>\n" +
                                "      <yloc>64</yloc>\n" +
                                "      <attributes_kjc/>\n" +
                                "    </entry>\n" +
                                "    <entry>\n" +
                                "      <name>DeleteMasterSycn</name>\n" +
                                "      <description/>\n" +
                                "      <type>SQL</type>\n" +
                                "      <attributes/>\n" +
                                "      <sql>DELETE FROM " + dataset.getTableName().get(j) + " WHERE UUID &lt;= ${MASTER_MAX_UUID}</sql>\n" +
                                "      <useVariableSubstitution>T</useVariableSubstitution>\n" +
                                "      <sqlfromfile>F</sqlfromfile>\n" +
                                "      <sqlfilename/>\n" +
                                "      <sendOneStatement>F</sendOneStatement>\n" +
                                "      <connection>Shinow90</connection>\n" +
                                "      <parallel>N</parallel>\n" +
                                "      <draw>Y</draw>\n" +
                                "      <nr>0</nr>\n" +
                                "      <xloc>416</xloc>\n" +
                                "      <yloc>64</yloc>\n" +
                                "      <attributes_kjc/>\n" +
                                "    </entry>\n" +
                                "    <entry>\n" +
                                "      <name>Success</name>\n" +
                                "      <description/>\n" +
                                "      <type>SUCCESS</type>\n" +
                                "      <attributes/>\n" +
                                "      <parallel>N</parallel>\n" +
                                "      <draw>Y</draw>\n" +
                                "      <nr>0</nr>\n" +
                                "      <xloc>528</xloc>\n" +
                                "      <yloc>64</yloc>\n" +
                                "      <attributes_kjc/>\n" +
                                "    </entry>");
                    }

                    if (dataset.getTableName().size() > 1) {
                        if (j == 0) {
                            hopBuffer.append("    <hop>\n" +
                                    "      <from>Start</from>\n" +
                                    "      <to>GetOrgCode</to>\n" +
                                    "      <from_nr>0</from_nr>\n" +
                                    "      <to_nr>0</to_nr>\n" +
                                    "      <enabled>Y</enabled>\n" +
                                    "      <evaluation>Y</evaluation>\n" +
                                    "      <unconditional>Y</unconditional>\n" +
                                    "    </hop>\n" +
                                    "    <hop>\n" +
                                    "      <from>GetOrgCode</from>\n" +
                                    "      <to>GetMasterMaxUuid</to>\n" +
                                    "      <from_nr>0</from_nr>\n" +
                                    "      <to_nr>0</to_nr>\n" +
                                    "      <enabled>Y</enabled>\n" +
                                    "      <evaluation>Y</evaluation>\n" +
                                    "      <unconditional>N</unconditional>\n" +
                                    "    </hop>\n" +
                                    "    <hop>\n" +
                                    "      <from>GetMasterMaxUuid</from>\n" +
                                    "      <to>MasterIncr</to>\n" +
                                    "      <from_nr>0</from_nr>\n" +
                                    "      <to_nr>0</to_nr>\n" +
                                    "      <enabled>Y</enabled>\n" +
                                    "      <evaluation>Y</evaluation>\n" +
                                    "      <unconditional>N</unconditional>\n" +
                                    "    </hop>\n" +
                                    "    <hop>\n" +
                                    "      <from>MasterIncr</from>\n" +
                                    "      <to>DeleteMasterSycn</to>\n" +
                                    "      <from_nr>0</from_nr>\n" +
                                    "      <to_nr>0</to_nr>\n" +
                                    "      <enabled>Y</enabled>\n" +
                                    "      <evaluation>Y</evaluation>\n" +
                                    "      <unconditional>N</unconditional>\n" +
                                    "    </hop>\n" +
                                    "    <hop>\n" +
                                    "      <from>DeleteMasterSycn</from>\n" +
                                    "      <to>GetSlave1MaxUuid</to>\n" +
                                    "      <from_nr>0</from_nr>\n" +
                                    "      <to_nr>0</to_nr>\n" +
                                    "      <enabled>Y</enabled>\n" +
                                    "      <evaluation>Y</evaluation>\n" +
                                    "      <unconditional>N</unconditional>\n" +
                                    "    </hop>\n");
                        } else {
                            if (j != dataset.getTableName().size() - 1) {
                                hopBuffer.append("    <hop>\n" +
                                        "      <from>GetSlave" + j + "MaxUuid</from>\n" +
                                        "      <to>Slave" + j + "Incr</to>\n" +
                                        "      <from_nr>0</from_nr>\n" +
                                        "      <to_nr>0</to_nr>\n" +
                                        "      <enabled>Y</enabled>\n" +
                                        "      <evaluation>Y</evaluation>\n" +
                                        "      <unconditional>N</unconditional>\n" +
                                        "    </hop>\n" +
                                        "    <hop>\n" +
                                        "      <from>Slave" + j + "Incr</from>\n" +
                                        "      <to>DeleteSlave" + j + "Sycn</to>\n" +
                                        "      <from_nr>0</from_nr>\n" +
                                        "      <to_nr>0</to_nr>\n" +
                                        "      <enabled>Y</enabled>\n" +
                                        "      <evaluation>Y</evaluation>\n" +
                                        "      <unconditional>N</unconditional>\n" +
                                        "    </hop>\n" +
                                        "    <hop>\n" +
                                        "      <from>DeleteSlave" + j + "Sycn</from>\n" +
                                        "      <to>GetSlave" + (j + 1) + "MaxUuid</to>\n" +
                                        "      <from_nr>0</from_nr>\n" +
                                        "      <to_nr>0</to_nr>\n" +
                                        "      <enabled>Y</enabled>\n" +
                                        "      <evaluation>Y</evaluation>\n" +
                                        "      <unconditional>N</unconditional>\n" +
                                        "    </hop>\n");
                            } else {
                                hopBuffer.append("    <hop>\n" +
                                        "      <from>GetSlave" + j + "MaxUuid</from>\n" +
                                        "      <to>Slave" + j + "Incr</to>\n" +
                                        "      <from_nr>0</from_nr>\n" +
                                        "      <to_nr>0</to_nr>\n" +
                                        "      <enabled>Y</enabled>\n" +
                                        "      <evaluation>Y</evaluation>\n" +
                                        "      <unconditional>N</unconditional>\n" +
                                        "    </hop>\n" +
                                        "    <hop>\n" +
                                        "      <from>Slave" + j + "Incr</from>\n" +
                                        "      <to>DeleteSlave" + j + "Sycn</to>\n" +
                                        "      <from_nr>0</from_nr>\n" +
                                        "      <to_nr>0</to_nr>\n" +
                                        "      <enabled>Y</enabled>\n" +
                                        "      <evaluation>Y</evaluation>\n" +
                                        "      <unconditional>N</unconditional>\n" +
                                        "    </hop>\n" +
                                        "    <hop>\n" +
                                        "      <from>DeleteSlave" + j + "Sycn</from>\n" +
                                        "      <to>Success</to>\n" +
                                        "      <from_nr>0</from_nr>\n" +
                                        "      <to_nr>0</to_nr>\n" +
                                        "      <enabled>Y</enabled>\n" +
                                        "      <evaluation>Y</evaluation>\n" +
                                        "      <unconditional>N</unconditional>\n" +
                                        "    </hop>\n");
                            }
                        }
                    } else {
                        hopBuffer.append("    <hop>\n" +
                                "      <from>Start</from>\n" +
                                "      <to>GetMasterMaxUuid</to>\n" +
                                "      <from_nr>0</from_nr>\n" +
                                "      <to_nr>0</to_nr>\n" +
                                "      <enabled>Y</enabled>\n" +
                                "      <evaluation>Y</evaluation>\n" +
                                "      <unconditional>Y</unconditional>\n" +
                                "    </hop>\n" +
                                "    <hop>\n" +
                                "      <from>GetMasterMaxUuid</from>\n" +
                                "      <to>MasterIncr</to>\n" +
                                "      <from_nr>0</from_nr>\n" +
                                "      <to_nr>0</to_nr>\n" +
                                "      <enabled>Y</enabled>\n" +
                                "      <evaluation>Y</evaluation>\n" +
                                "      <unconditional>N</unconditional>\n" +
                                "    </hop>\n" +
                                "    <hop>\n" +
                                "      <from>MasterIncr</from>\n" +
                                "      <to>DeleteMasterSycn</to>\n" +
                                "      <from_nr>0</from_nr>\n" +
                                "      <to_nr>0</to_nr>\n" +
                                "      <enabled>Y</enabled>\n" +
                                "      <evaluation>Y</evaluation>\n" +
                                "      <unconditional>N</unconditional>\n" +
                                "    </hop>\n" +
                                "    <hop>\n" +
                                "      <from>DeleteMasterSycn</from>\n" +
                                "      <to>Success</to>\n" +
                                "      <from_nr>0</from_nr>\n" +
                                "      <to_nr>0</to_nr>\n" +
                                "      <enabled>Y</enabled>\n" +
                                "      <evaluation>Y</evaluation>\n" +
                                "      <unconditional>N</unconditional>\n" +
                                "    </hop>\n");
                    }

                    //生成Incr文件
                    StringBuffer selectSqlBuffer = new StringBuffer();
                    StringBuffer columnBuffer = new StringBuffer();
                    String IncrFileName = "MasterIncr";
                    String inputName = "InputMasterIncr";
                    String outputName = "OuterMasterIncr";
                    if (j != 0) {
                        IncrFileName = "Slave" + j + "Incr";
                        inputName = "InputSlave" + j + "Incr";
                        outputName = "OuterSlave" + j + "Incr";
                    }
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
                            columnBuffer.append("      <value>\n");
                            columnBuffer.append("        <name>" + columnName.get(k) + "</name>\n");
                            columnBuffer.append("        <rename>" + columnName.get(k) + "</rename>\n");
                            if ("UUID".equals(columnName.get(k))) {
                                columnBuffer.append("        <update>" + "N" + "</update>\n");
                            } else {
                                columnBuffer.append("        <update>" + "Y" + "</update>\n");
                            }
                            columnBuffer.append("      </value>\n");
                        }
                    }

                    //生成GetMaxUuid文件
                    String maxUuidFileName = "GetMasterMaxUuid";
                    String maxUuidInputName = "GetMasterMaxUuid";
                    String maxUuidSetName = "SetMasterMaxUuid";
                    String maxUuidColumnName = "MASTER_MAX_UUID";
                    if (j != 0) {
                        maxUuidFileName = "GetSlave" + j + "MaxUuid";
                        maxUuidInputName = "GetSlave" + j + "MaxUuid";
                        maxUuidSetName = "SetSlave" + j + "MaxUuid";
                        maxUuidColumnName = "SLAVE" + j + "_MAX_UUID";
                    }
                    String maxUuidContent = KettleJobTemplate.setMaxUuidIncrKtrFile(maxUuidFileName, maxUuidInputName, maxUuidSetName, tableName, maxUuidColumnName);
                    FileWriter kettleJobMaxUuidKtrFileWriter = new FileWriter(kettleIncrFilePath + maxUuidFileName + ".ktr");
                    kettleJobMaxUuidKtrFileWriter.write(maxUuidContent);
                    kettleJobMaxUuidKtrFileWriter.close();

                    //生成主要抽取数据的文件
                    selectSqlBuffer = new StringBuffer(selectSqlBuffer.toString().replace(",\n" + "FROM", ",\n   '1' AS OPER_TYPE\n" + "\nFROM"));
                    String masterIncrContent = KettleJobTemplate.setMasterIncrKtrFile(selectSqlBuffer.toString(), tableName, columnBuffer.toString(), IncrFileName, inputName, outputName);
                    FileWriter kettleJobFullKjbFileWriter = new FileWriter(kettleIncrFilePath + IncrFileName + ".ktr");
                    kettleJobFullKjbFileWriter.write(masterIncrContent);
                    kettleJobFullKjbFileWriter.close();
                }

                //生成kjb文件
                IncrKjb incrKjb = new IncrKjb();
                incrKjb.setGroup(groupBuffer.toString());
                incrKjb.setHop(hopBuffer.toString());

                String kettleIncrKjbContent = KettleJobTemplate.setKettleIncrKjbFile(incrKjb);
                FileWriter kettleJobFullKjbFileWriter = new FileWriter(kettleIncrFilePath + datasetId + "_dataset_incr.kjb");
                kettleJobFullKjbFileWriter.write(kettleIncrKjbContent);
                kettleJobFullKjbFileWriter.close();

                //生成GetOrgCode文件
                if (dataset.getTableName().size() > 1) {
                    String kettleGetOrgCodeContent = KettleJobTemplate.setKettleGetOrgCodeKtrFile();

                    FileWriter kettleJobGetOrgCodeKtrFileWriter = new FileWriter(kettleIncrFilePath + "GetOrgCode.ktr");
                    kettleJobGetOrgCodeKtrFileWriter.write(kettleGetOrgCodeContent);
                    kettleJobGetOrgCodeKtrFileWriter.close();
                }

                if (i == 0) {
                    analyseLabel.setText(analyseLabel.getText() + "------从" + fileName + "中提取数据------" + "<br>");
                    analyseLabel.setText(analyseLabel.getText() + "准备解析数据...." + "<br>");
                }
                analyseLabel.setText(analyseLabel.getText() + dataset.getDatasetId() + " 解析完成" + "<br>");
                if (i == datasets.size() - 1) {
                    analyseLabel.setText(analyseLabel.getText() + "------kettle增量作业生成完毕------" + "<br>");
                }
            }
        }
    }
}
