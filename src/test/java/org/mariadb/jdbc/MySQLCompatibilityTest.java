package org.mariadb.jdbc;

import org.junit.Test;

import java.sql.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class MySQLCompatibilityTest extends BaseTest {

    /**
     * CONJ-82: data type LONGVARCHAR not supported in setObject()
     *
     * @throws SQLException
     */
    @Test
    public void datatypesTest() throws SQLException {
        Statement stmt = connection.createStatement();
        createTestTable("datatypesTest","type_longvarchar TEXT NULL");
        PreparedStatement preparedStmt = connection.prepareStatement("INSERT INTO `datatypesTest` (`type_longvarchar`) VALUES ( ? )");
        preparedStmt.setObject(1, "longvarcharTest", Types.LONGVARCHAR);
        preparedStmt.executeUpdate();
        preparedStmt.close();
        ResultSet rs = stmt.executeQuery("SELECT * FROM datatypesTest");
        stmt.close();
        rs.next();
        assertEquals("longvarcharTest", rs.getString(1));
    }

    /**
     * The Mysql connector returns "0" or "1" for BIT(1) with ResultSet.getString().
     * CONJ-102: mariadb-java-client returned "false" or "true".
     *
     * @throws SQLException
     */
    @Test
    public void testBitConj102() throws SQLException {
        Statement stmt = connection.createStatement();
        createTestTable("mysqlcompatibilitytest","id int not null primary key auto_increment, test bit(1)");
        stmt.execute("insert into mysqlcompatibilitytest values(null, b'0')");
        stmt.execute("insert into mysqlcompatibilitytest values(null, b'1')");
        ResultSet rs = stmt.executeQuery("select * from mysqlcompatibilitytest");
        assertTrue(rs.next());
        assertTrue("0".equalsIgnoreCase(rs.getString(2)));
        assertTrue(rs.next());
        assertTrue("1".equalsIgnoreCase(rs.getString(2)));
    }

}
