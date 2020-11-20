package com.hikari.jdbc;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.ScalarHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class DbInserter {

    private static final Logger logger = LoggerFactory.getLogger(DbExecutor.class);
    private final QueryRunner queryRunner;

    public DbInserter(QueryRunner queryRunner) {
        this.queryRunner = queryRunner;
    }

    public <T> T insertResultAsObj(String sql, Object... params) {
        try {
            return this.queryRunner.insert(sql, new ScalarHandler<T>(), params);
        } catch (Exception e) {
            logger.error("insertResultAsObj() EXCEPTION", e);
            return null;
        }
    }

}
