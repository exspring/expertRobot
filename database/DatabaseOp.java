package database;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Map;

public class DatabaseOp
{
	public static Connection getConnection(String database)
	{
		DbConntion dc = null;
		dc = new DbConntion(database);
		return dc.getConnection();
	}
	
	public static Connection getConnection()
	{
		DbConntion dc = new DbConntion();

		return dc.getConnection();
	}

	public static String createDatabase(String databaseName)
	{
		Connection con = DatabaseOp.getConnection();
		Statement stmt = null;
		try
		{
			stmt = con.createStatement();
			StringBuffer createSQL = new StringBuffer("CREATE DATABASE ")
					.append(databaseName).append(";");
			stmt.execute(createSQL.toString());
		}
		catch (SQLException e)
		{
			System.out.println(e.getMessage());
			return null;
		}

		return databaseName;
	}

	/**
	 * 在指定数据库中创建一张表
	 * 
	 * @param tableName
	 *            要创建的表的名字
	 * @param columLable
	 *            创建的表的列信息
	 * @param databaseName
	 *            要创建表的数据库
	 * @return 成功时返回表的名字，失败时返回null
	 */
	public static String createTable(String tableName, String columLable,
			String databaseName)
	{
		Connection con = DatabaseOp.getConnection(databaseName);

		Statement stmt = null;
		try
		{
			stmt = con.createStatement();
		}
		catch (SQLException e)
		{
			e.printStackTrace();
			return null;
		}

		StringBuffer createTableSQL = new StringBuffer("CREATE TABLE ");
		createTableSQL.append(tableName).append(" ( ").append(columLable)
				.append(" );");

		try
		{
			System.out.println(createTableSQL.toString());
			stmt.execute(createTableSQL.toString());
		}
		catch (SQLException e)
		{
			System.out.println(e.getMessage());
			return null;
		}

		return tableName;
	}

	public static void createIndex(String sql ,String database)
	{
		Connection con = DatabaseOp.getConnection(database);
		
		System.out.println(sql);
		
		Statement stmt = null;
		
		try
		{
			stmt = con.createStatement();
			stmt.execute(sql);
		}
		catch(SQLException e)
		{
			e.printStackTrace();
		}
	}
	
	public static ResultSet getFromDatabse(String querySQL, String database)
	{
		Connection con = DatabaseOp.getConnection(database);
		
		Statement stmt = null;
		ResultSet res = null;
		try
		{
			stmt = con.createStatement();
			res = stmt.executeQuery(querySQL);
		}
		catch(SQLException e)
		{
			e.printStackTrace();
		}
		
		return res;
	}

}
