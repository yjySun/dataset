package com.yjy.pojo;

/**
 * @Author: Jiyuan Yao
 * @Date: 2021/9/30 10:29
 * @Description:
 */
public class IncrKjb {
    private String group;
    private String hop;
    private String tableName;
    private String selectSql;
    private String column;

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public String getHop() {
        return hop;
    }

    public void setHop(String hop) {
        this.hop = hop;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
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
}
