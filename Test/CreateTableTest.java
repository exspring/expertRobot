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
		DatabaseOp.createDatabase("test"); // �������ݿ�test

		String columLable = "id int, value int";
		DatabaseOp.createTable("example1", columLable, "test"); // ������exanple1

		DbConntion dc = new DbConntion("test");
		Connection con = dc.getConnection(); // ��ȡ����
		Statement stmt = null;
		try
		{
			stmt = con.createStatement();

			// �ܹ�100w���ݣ���ʮ�Σ�ÿ��10w�в���
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
		DatabaseOp.createTable("example2", columLable, "test"); //�����ݿ�test�д�����example2
		
		DbConntion dc = new DbConntion("test");
		Connection mcon = dc.getManualCommitConnection();//��ȡ���� ע�� ������example1�в�ͬ
		PreparedStatement pstmt = null;
		try
		{
			pstmt = mcon.prepareStatement("INSERT INTO example2 VALUES (?,?)");
			
			//����Ҳ�ǹ�100w�����ݣ���10�Σ�ÿ��10w��д�����ݿ�
			//������example1�з�����ͬ
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
