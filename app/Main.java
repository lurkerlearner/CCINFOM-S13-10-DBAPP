package app;

/*
 * to run in VS code successfully:
 *         javac -cp ".;lib\mysql-connector-j-9.5.0.jar" *.java
 *         java -cp ".;lib\mysql-connector-j-9.5.0.jar" app.Main
 */

import view.*;

import java.sql.Connection;

public class Main 
{
    public static void main(String[] args) 
    {
        Connection conn = DBConnection.getConnection();
        if (conn != null) 
        {
            System.out.println("Successfully connected to floodpanda database!");
        } 
        else 
        {
            System.out.println("Failed to connect to MySQL.");
        }

        new FloodPandaWelcome().setVisible(true);
    }
}

