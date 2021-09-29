package com.yjy.pojo;

/**
 * @Author: Jiyuan Yao
 * @Date: 2021/9/29 8:36
 * @Description:
 */
public class FullKtr {
    private String tableName;
    private String outputHop;
    private String beginHop;
    private String selectSql;
    private String column;
    private String beginNode;

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getOutputHop() {
        return outputHop;
    }

    public void setOutputHop(String outputHop) {
        this.outputHop = outputHop;
    }

    public String getBeginHop() {
        return beginHop;
    }

    public void setBeginHop(String beginHop) {
        this.beginHop = beginHop;
    }

    public String getSelectSql() {
        return selectSql;
    }

    public void setSelectSql(String selectSql) {
        this.selectSql = selectSql;
    }

    public String getColumn() {
        return column;
    }

    public void setColumn(String column) {
        this.column = column;
    }

    public String getBeginNode() {
        return beginNode;
    }

    public void setBeginNode(String beginNode) {
        this.beginNode = beginNode;
    }
}
