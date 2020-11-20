package com.hikari.jdbc;

import org.apache.commons.dbutils.BasicRowProcessor;
import org.apache.commons.dbutils.GenerousBeanProcessor;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;
import java.util.Collections;
import java.util.List;

@SuppressWarnings({"unchecked", "rawtypes"})
public class DbExecutor {

    private static final Logger logger = LoggerFactory.getLogger(DbExecutor.class);
    private final QueryRunner queryRunner;

    public DbExecutor(QueryRunner queryRunner) {
        this.queryRunner = queryRunner;
    }

    public <T> List<T> selectResultAsListObj(String sql, Class<T> clazz, Object... params) {
        try {
            ResultSetHandler<List<T>> resultSetHandler = new BeanListHandler(clazz, new BasicRowProcessor(new GenerousBeanProcessor()));
            return this.queryRunner.query(sql, resultSetHandler, params);
        } catch (Exception e) {
            logger.error("selectResultAsListObj() EXCEPTION", e);
            return Collections.emptyList();
        }
    }
}
