package com.cobra.tracker.app;

import com.cobra.tracker.util.CobraException;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

public class Database {

    private final Connection db;

        public Database(String dbName) throws CobraException {
            try {
                Class.forName("org.sqlite.JDBC");
                db = DriverManager.getConnection("jdbc:sqlite:" + dbName);
            } catch ( Exception e ) {
                throw new CobraException("Failed to open database.");
            }
            System.out.println("Opened database successfully");
        }

        private void createTable(){
            Statement stmt = null;

            try {
                stmt = db.createStatement();
                String sql = "CREATE TABLE Inventory " +
                        "(ID INT PRIMARY KEY     NOT NULL," +
                        " Filename       CHAR(200), " +
                        " AGE            INT     NOT NULL, " +
                        " ADDRESS        CHAR(50), " +
                        " SALARY         REAL)";
                stmt.executeUpdate(sql);
                stmt.close();
            } catch ( Exception e ) {
                System.err.println( e.getClass().getName() + ": " + e.getMessage() );
                System.exit(0);
            }
            System.out.println("Table created successfully");
        }

}
