package Test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

import javax.naming.ldap.StartTlsRequest;

import database.DatabaseOp;
import database.DbConntion;

public class CreateTableTest
{
	public void example1()
	{
		DatabaseOp.createDatabase("test"); // 建立数据库test

		String columLable = "id int, value int";
		DatabaseOp.createTable("example1", columLable, "test"); // 建立表exanple1

		DbConntion dc = new DbConntion("test");
		Connection con = dc.getConnection(); // 获取连接
		Statement stmt = null;
		try
		{
			stmt = con.createStatement();

			// 总共100w数据，分十次，每次10w行插入
			for (int i = 0; i < 10; i++)
			{
				long startTime = System.currentTimeMillis();

				for (int j = 0; j < 100000; j++)
				{
					StringBuffer insertSQL = new StringBuffer(
							"INSERT INTO example1 VALUES ( ").append(j + 1)
							.append(",").append(j).append(" );");
					stmt.executeUpdate(insertSQL.toString());
				}

				long endTime = System.currentTimeMillis();

				System.out.println("Total Time: " + (endTime - startTime)
						/ 1000 + "s");
			}
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
	}

	public void example2()
	{
		String columLable = "id int, value int";
		DatabaseOp.createTable("example2", columLable, "test"); //在数据库test中创建表example2
		
		DbConntion dc = new DbConntion("test");
		Connection mcon = dc.getManualCommitConnection();//获取连接 注意 这里与example1中不同
		PreparedStatement pstmt = null;
		try
		{
			pstmt = mcon.prepareStatement("INSERT INTO example2 VALUES (?,?)");
			
			//这里也是共100w条数据，分10次，每次10w条写入数据库
			//这里与example1中方法不同
			for(int i = 0; i < 10; i++)
			{
				long startTime = System.currentTimeMillis();

				for(int j = 0; j < 100000; j++)
				{
					pstmt.setInt(1, j+1);
					pstmt.setInt(2, j);
					pstmt.executeUpdate();
				}
				mcon.commit();
				
				long endTime = System.currentTimeMillis();
				System.out.println("Total Time: " + (endTime - startTime)/1000 + "s");
			}
		}
		catch(SQLException e)
		{
			e.printStackTrace();
		}
	}

	public static void main(String[] args)
	{
		CreateTableTest ct = new CreateTableTest();

		ct.example2();
	}

}
