package com.hikari.manager;

import com.hikari.jdbc.DbInserter;
import com.hikari.jdbc.DbExecutor;
import com.hikari.jdbc.DbUpdater;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.apache.commons.dbutils.QueryRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class DbManager {

    private static final Logger logger = LoggerFactory.getLogger(DbManager.class);
    private static DbManager instance = null;
    private HikariDataSource hikariDataSource;
    private DbExecutor dbExecutor;
    private DbUpdater dbUpdater;
    private DbInserter dbInserter;

    public static DbManager getInstance() {
        if (instance == null) instance = new DbManager();
        return instance;
    }

    private DbManager() {
    }

    public Connection getConnection() throws SQLException {
        return this.hikariDataSource.getConnection();
    }

    public boolean start(String serverIp, String port, String database, String userName, String password) {
        if (this.hikariDataSource != null) {
            logger.warn("DB Connection Pool has already been created.");
            return false;
        } else {
            try {
                HikariConfig hikariConfig = new HikariConfig("src/main/java/com/hikari/hikaricp.properties");
                hikariConfig.setJdbcUrl("jdbc:mysql://" + serverIp + ":" + port + "/" + database + "?serverTimezone=UTC&connectTimeout=30000&socketTimeout=60000");
                hikariConfig.setUsername(userName);
                hikariConfig.setPassword(password);

                this.hikariDataSource = new HikariDataSource(hikariConfig);
                logger.debug("DB Connection Pool has created.");

                QueryRunner queryRunner = new QueryRunner(this.hikariDataSource);
                this.dbExecutor = new DbExecutor(queryRunner);
                this.dbUpdater = new DbUpdater(queryRunner);
                this.dbInserter = new DbInserter(queryRunner);

                return true;
            } catch (Exception e) {
                logger.error("DB Connection Pool Creation has failed.", e);
                return false;
            }
        }
    }

    public void shutdown() {
        try {
            if (this.hikariDataSource != null) {
                this.hikariDataSource.close();
                logger.debug("DB Connection Pool is shutting down.");
            }
            this.hikariDataSource = null;
        } catch (Exception e) {
            logger.warn("Error when shutting down DB Connection Pool", e);
        }
    }

    public <T> List<T> selectResultAsListObj(String sql, Class<T> clazz, Object... params) {
        return this.dbExecutor.selectResultAsListObj(sql, clazz, params);
    }

    public <T> T insertResultAsObj(String sql, Object... params) {
        return this.dbInserter.insertResultAsObj(sql, params);
    }

    public int update(String sql, Object... params) {
        return this.dbUpdater.update(sql, params);
    }
}
