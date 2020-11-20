package com.hikari;

import com.hikari.manager.DbManager;
import com.hikari.data.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class HikariMain {
    private static final Logger logger = LoggerFactory.getLogger(HikariMain.class);

    public static void main(String[] args) {
        DbManager dbManager = DbManager.getInstance();

        boolean isStartSuccess;
        isStartSuccess = dbManager.start("127.0.0.1", "3306", "hikari_test", "jamesj", "abcde@12345");
        if (!isStartSuccess) {
            return;
        }

        ////////////////////////////////////////////////////////////////////////////
        // Common Variable
        String table1 = "test1";
        ////////////////////////////////////////////////////////////////////////////

        ////////////////////////////////////////////////////////////////////////////
        // insert #1
        String insertSql1;

        for (int i = 0; i < 10; i++) {
            insertSql1 = "INSERT INTO "
                    + table1
                    + " values(?, ?)";

            //char c = (char)(i + 63);
            //Object insertResult1 = dbManager.insertResultAsObj(insertSql1, i, Character.toString(c));
            //System.out.println("[" + i + "] insertResult1: " + insertResult1);
        }
        ////////////////////////////////////////////////////////////////////////////

        ////////////////////////////////////////////////////////////////////////////
        // select #1
        String selectSql1 = "SELECT * from "
                + table1
                + " where seq=?";

        //List<Integer> selectResult1 = dbManager.selectResultAsListObj(selectSql1, Integer.class, 1);
        //System.out.println("selectResult1: " + selectResult1);
        ////////////////////////////////////////////////////////////////////////////

        ////////////////////////////////////////////////////////////////////////////
        // insert #2
        String table2 = "test2";
        String insertSql2 = "INSERT INTO "
                + table2
                + " values(?, ?, ?)";

        User user1 = new User("jamesj", "010-9253-0675", "서욽특별시 관악구 관악로 11나길 13, 204호");
        User user2 = new User("sally", "010-1234-5678", "부산광역시");

        Object insertResult2_1 = dbManager.insertResultAsObj(insertSql2, user1.getName(), user1.getNumber(), user1.getAddress());
        Object insertResult2_2 = dbManager.insertResultAsObj(insertSql2, user2.getName(), user2.getNumber(), user2.getAddress());
        //System.out.println("insertResult2_1: " + insertResult2_1);
        //System.out.println("insertResult2_2: " + insertResult2_2);
        ////////////////////////////////////////////////////////////////////////////

        ////////////////////////////////////////////////////////////////////////////
        // select #2
        String selectSql2 = "SELECT * from "
                + table2;

        List<User> selectResult2 = dbManager.selectResultAsListObj(selectSql2, User.class);
        for (User user: selectResult2) {
            logger.info("[Name: {}, Number: {}, Address: {}]", user.getName(), user.getNumber(), user.getAddress());
        }
        ////////////////////////////////////////////////////////////////////////////

        ////////////////////////////////////////////////////////////////////////////
        // update #1
        String updateSql1 = "UPDATE "
                + table2
                + " SET "
                + "address=? "
                + "WHERE "
                + "name=?";

        Object updateResult1 = dbManager.update(updateSql1, "abc", "jamesj");
        //logger.info("updateResult1: {}", updateResult1.toString());

        selectResult2 = dbManager.selectResultAsListObj(selectSql2, User.class);
        for (User user: selectResult2) {
            logger.info("[Name: {}, Number: {}, Address: {}]", user.getName(), user.getNumber(), user.getAddress());
        }
        ////////////////////////////////////////////////////////////////////////////

        ////////////////////////////////////////////////////////////////////////////
        // delete #1
        String deleteSql1 = "DELETE FROM "
                + table2
                + " WHERE "
                + "name=?";

        Object deleteResult1_1 = dbManager.update(deleteSql1, "jamesj");
        Object deleteResult1_2 = dbManager.update(deleteSql1, "sally");

        selectResult2 = dbManager.selectResultAsListObj(selectSql2, User.class);

        if(selectResult2.isEmpty()) {
            logger.info("List is Empty!!");
        }

        for (User user: selectResult2) {
            logger.info("[Name: {}, Number: {}, Address: {}]", user.getName(), user.getNumber(), user.getAddress());
        }
        ////////////////////////////////////////////////////////////////////////////

        dbManager.shutdown();
    }
}
