package com.yjy.pojo;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: Jiyuan Yao
 * @Date: 2021/9/25 21:44
 * @Description:
 */
public class Dataset {
    private String DatasetId;
    private String name;
    private List<String> tableName = new ArrayList<String>();
    private List<String> datasetIdDetail = new ArrayList<String>();
    private List<String> tableColumn = new ArrayList<String>();

    public String getDatasetId() {
        return DatasetId;
    }

    public void setDatasetId(String datasetId) {
        DatasetId = datasetId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getTableName() {
        return tableName;
    }

    public void setTableName(List<String> tableName) {
        this.tableName = tableName;
    }

    public List<String> getDatasetIdDetail() {
        return datasetIdDetail;
    }

    public void setDatasetIdDetail(List<String> datasetIdDetail) {
        this.datasetIdDetail = datasetIdDetail;
    }

    public List<String> getTableColumn() {
        return tableColumn;
    }

    public void setTableColumn(List<String> tableColumn) {
        this.tableColumn = tableColumn;
    }

    @Override
    public String toString() {
        return "Dataset{" +
                "DatasetId='" + DatasetId + '\'' +
                ", name='" + name + '\'' +
                ", tableName=" + tableName +
                ", datasetIdDetail=" + datasetIdDetail +
                '}';
    }
}
