package Test;

import database.DatabaseOp;

public class CreateTableTest
{
	public static void main(String[] args)
	{
		String columLable = " id INT , name varchar(40) ";
		DatabaseOp.createTable("createtest", columLable, DatabaseOp.createDatabase("ttt"));
	}

}
