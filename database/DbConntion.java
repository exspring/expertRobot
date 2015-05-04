package database;

import java.sql.Connection;
import java.sql.DriverManager;

public class DbConntion
{
	private String url;

	private String serverName;

	private String portName;

	private String databaseName;

	private String userName;

	private String passWord;

	public DbConntion()
	{
		this.url = "jdbc:jtds:sqlserver://";
		this.serverName = "localhost";
		this.portName = "1433";
		this.databaseName = "expert";
		this.userName = "xin";
		this.passWord = "123";
	}

	public DbConntion(String databaseName)
	{
		this.url = "jdbc:jtds:sqlserver://";
		this.serverName = "localhost";
		this.portName = "1433";
		this.databaseName = databaseName;
		this.userName = "xin";
		this.passWord = "123";
	}
	
	public DbConntion(String databaseName, String userName, String passWord)
	{
		this.url = "jdbc:jtds:sqlserver://";
		this.serverName = "localhost";
		this.portName = "1433";
		this.databaseName = databaseName;
		this.userName = userName;
		this.passWord = passWord;
	}

	public String getConnectionUrl()
	{
		return this.url + this.serverName + ":" + this.portName
				+ ";databaseName=" + this.databaseName + ";";
	}

	public Connection getConnection()
	{
		Connection connection = null;
		try
		{
			Class.forName("net.sourceforge.jtds.jdbc.Driver");
			connection = DriverManager.getConnection(this.getConnectionUrl(),
					this.userName, this.passWord);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return connection;
	}
}
