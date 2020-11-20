package com.hikari.jdbc;

import org.apache.commons.dbutils.QueryRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DbUpdater {

    private static final Logger logger = LoggerFactory.getLogger(DbUpdater.class);
    private final QueryRunner queryRunner;

    public DbUpdater(QueryRunner queryRunner) {
        this.queryRunner = queryRunner;
    }

    public int update(String sql, Object... params) {
        try {
            return this.queryRunner.update(sql, params);
        } catch (Exception e) {
            logger.error("update() EXCEPTION", e);
            return -1;
        }
    }
}
