package Test;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import database.DbConntion;

public class DatabaseTest
{
	private DbConntion dc;
	
	public DatabaseTest()
	{
		this.dc = new DbConntion();
	}
	
	public void queryTest()
	{
		Connection con = dc.getConnection();
		try
		{
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM E");
			while(rs.next())
			{
				System.out.println(rs.getString("name") + "\t" + rs.getString("id"));
			}
		}
		catch(SQLException e)
		{
			e.printStackTrace();
		}
	}
	public void insertTest()
	{
		Connection con = dc.getConnection();
		try
		{
			Statement stmt = con.createStatement();
			stmt.executeUpdate("INSERT INTO e VALUES('l','4')");
		}
		catch(SQLException e)
		{
			e.printStackTrace();
		}
	}
	public void createTest()
	{
		Connection con = dc.getConnection();
		try
		{
			Statement stmt = con.createStatement();
			String createStr = "CREATE TABLE j (id int NOT NULL,name varchar(100) NOT NULL)";
			stmt.execute(createStr);
		}
		catch(SQLException e)
		{
			e.printStackTrace();
		}
	}
	public static void main(String[] args)
	{
		DatabaseTest dt = new DatabaseTest();
		dt.createTest();
		dt.insertTest();
		dt.queryTest();
	}
}
