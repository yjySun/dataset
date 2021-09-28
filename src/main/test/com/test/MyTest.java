package com.test;

import com.yjy.util.DBUtil;

import org.apache.commons.dbutils.QueryRunner;

import org.apache.commons.dbutils.handlers.ColumnListHandler;
import org.junit.Test;

import java.util.List;

/**
 * @Author: Jiyuan Yao
 * @Date: 2021/9/25 20:57
 * @Description:
 */

public class MyTest {

    @Test
    public void testExcel() throws Exception {
        QueryRunner queryRunner = new QueryRunner(DBUtil.getDataSource());
        List<String> columnName = queryRunner.query("SELECT COLUMN_NAME FROM ALL_TAB_COLUMNS WHERE TABLE_NAME='T_S_BEA_CTM_APPLY_LIST_EVAL'", new ColumnListHandler<String>("COLUMN_NAME"));
        for (int i = 0; i < columnName.size(); i++) {
            System.out.println(columnName.get(i)+i);
        }

    }
}
