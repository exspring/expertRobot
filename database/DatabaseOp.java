package database;

import java.lang.Thread.State;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseOp
{
	public static void createDatabase(String databaseName)
	{
		 DbConntion dc = new DbConntion();
		 Connection con = dc.getConnection();
		 Statement stmt = null;
		 try
		 {
			 stmt = con.createStatement();
			 StringBuffer createSQL = new StringBuffer("CREATE DATABASE ").append(databaseName).append(";");
			 stmt.execute(createSQL.toString());
		 }
		 catch(SQLException e)
		 {
			 System.out.println(e.getMessage());
			 return;
		 }
	}

}
