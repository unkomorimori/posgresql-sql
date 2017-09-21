package jp.co.jdbc;

import java.io.PrintWriter;
import java.io.Writer;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Properties;

public class SampleJdbc {
    public static void main(String args[]) {

        try {

            Writer fw = new java.io.FileWriter("./filename.log",true);
            DriverManager.setLogWriter(new PrintWriter(fw,true));
            //org.postgresql.Driver.setLogLevel(org.postgresql.Driver.DEBUG);


            Connection conn;
            Class.forName("org.postgresql.Driver");

            String url = "jdbc:postgresql://192.168.11.171/sample?loglevel=2";
            //String url = "jdbc:postgresql://192.168.11.171/sample";
            Properties props = new Properties();
            props.setProperty("user", "postgres");
            props.setProperty("password", "");
            props.setProperty("loglevel", "2");

            conn = DriverManager.getConnection(url, props);
            conn.setAutoCommit(false);

            PreparedStatement ps =
                    conn.prepareStatement("select * from a");
            try {
                ps.setFetchSize(6);
                ResultSet rs = ps.executeQuery();
                try {
                    while (rs.next()) {
                        System.out.println(rs.getString(1));
                    }
                } finally {
                    rs.close();
                }

                ps = conn.prepareStatement("update a set name = 'bbb' where id = 1");
                ps = conn.prepareStatement("select * from a");
                rs = ps.executeQuery();
                try {
                    while (rs.next()) {
                        System.out.println(rs.getString(1));
                    }
                } finally {
                    rs.close();
                }

                conn.commit();

            } finally {
                ps.close();
            }

            if (conn != null) {
                conn.rollback();
                conn.setAutoCommit(true);
                conn.close();
            }

        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}
