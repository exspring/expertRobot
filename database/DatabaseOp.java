package database;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Map;

public class DatabaseOp
{
	public static String createDatabase(String databaseName)
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
			 return null;
		 }

		 return databaseName;
	}
	
	/**
	 * ��ָ�����ݿ��д���һ�ű�
	 * @param tableName
	 * Ҫ�����ı������
	 * @param columLable
	 * �����ı������Ϣ
	 * @param databaseName
	 * Ҫ����������ݿ�
	 * @return
	 * �ɹ�ʱ���ر�����֣�ʧ��ʱ����null
	 */
	public static String createTable(String tableName, String columLable,String databaseName)
	{
		DbConntion dc = new DbConntion(databaseName);
		Connection con = dc.getConnection();
		Statement stmt = null;
		try
		{
			stmt = con.createStatement();
		}
		catch(SQLException e)
		{
			e.printStackTrace();
			return null;
		}
		
		StringBuffer createTableSQL = new StringBuffer("CREATE TABLE ");
		createTableSQL.append(tableName).append(" ( ").append(columLable).append(" );");
		
		try
		{
			System.out.println(createTableSQL.toString());
			stmt.execute(createTableSQL.toString());
		}
		catch(SQLException e)
		{
			System.out.println(e.getMessage());
			return null;
		}
		
		return tableName;
	}

}
