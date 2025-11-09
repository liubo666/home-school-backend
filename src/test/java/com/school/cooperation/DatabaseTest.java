package com.school.cooperation;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;
import java.sql.Connection;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 数据库连接测试
 *
 * @author Home School Team
 */
@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class DatabaseTest {

    @Autowired
    private DataSource dataSource;

    @Test
    void testDatabaseConnection() {
        assertNotNull(dataSource, "DataSource should not be null");

        try (Connection connection = dataSource.getConnection()) {
            assertNotNull(connection, "Connection should not be null");
            assertTrue(connection.isValid(1), "Connection should be valid");

            // 测试数据库基本功能
            var statement = connection.createStatement();
            var resultSet = statement.executeQuery("SELECT 1");
            assertTrue(resultSet.next(), "Query should return a result");
            assertEquals(1, resultSet.getInt(1), "Query should return 1");
            resultSet.close();
            statement.close();
        } catch (Exception e) {
            fail("Database connection test failed: " + e.getMessage());
        }
    }

    @Test
    void testDatabaseTables() {
        try (Connection connection = dataSource.getConnection()) {
            var statement = connection.createStatement();

            // 检查表是否存在
            String[] tables = {
                "sys_user", "edu_class", "edu_student",
                "edu_parent_student", "edu_student_record",
                "sys_notification", "sys_log", "sys_file"
            };

            for (String tableName : tables) {
                var resultSet = statement.executeQuery(
                    "SELECT COUNT(*) FROM information_schema.tables " +
                    "WHERE table_schema = 'home_school' AND table_name = '" + tableName + "'"
                );
                assertTrue(resultSet.next(), "Result should have a row");
                assertTrue(resultSet.getInt(1) > 0, "Table " + tableName + " should exist");
                resultSet.close();
            }

            statement.close();
        } catch (Exception e) {
            fail("Database tables test failed: " + e.getMessage());
        }
    }

    @Test
    void testInitialData() {
        try (Connection connection = dataSource.getConnection()) {
            var statement = connection.createStatement();

            // 检查初始管理员用户
            var resultSet = statement.executeQuery(
                "SELECT COUNT(*) FROM sys_user WHERE username = 'admin'"
            );
            assertTrue(resultSet.next(), "Result should have a row");
            assertEquals(1, resultSet.getInt(1), "Admin user should exist");
            resultSet.close();

            statement.close();
        } catch (Exception e) {
            fail("Initial data test failed: " + e.getMessage());
        }
    }
}