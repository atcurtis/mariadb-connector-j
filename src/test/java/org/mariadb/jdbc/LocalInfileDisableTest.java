package org.mariadb.jdbc;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

public class LocalInfileDisableTest extends BaseTest {
    @Before
    public void setup() throws SQLException {
        setConnection("&allowLocalInfile=false");
        createTestTable("t","id int, test varchar(100)");
    }

    @Test
    public void testLocalInfileWithoutInputStream() throws SQLException {
        Statement stmt = null;
        Exception ex = null;
        try {
            stmt = connection.createStatement();
            stmt.executeUpdate("LOAD DATA LOCAL INFILE 'dummy.tsv' INTO TABLE t (id, test)");
        } catch (Exception e) {
            ex = e;
        } finally {
            try {
                stmt.close();
            } catch (Exception ignore) {
            }
        }

        Assert.assertNotNull("Expected an exception to be thrown", ex);
        String message = ex.getMessage();
        String expectedMessage = "Usage of LOCAL INFILE is disabled. To use it enable it via the connection property allowLocalInfile=true";
        Assert.assertEquals(message, expectedMessage);
    }


}
