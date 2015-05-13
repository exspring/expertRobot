package matrix;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.concurrent.locks.StampedLock;

import database.DatabaseOp;
import database.DbConntion;

public class TripleTable
{
	private Map<Integer, Map<Integer, Double>> tripleTable;

	private int databaseIndexCount = 0;

	public TripleTable()
	{
		this.tripleTable = new TreeMap<Integer, Map<Integer, Double>>();
	}

	public boolean contains(int keyindex, int expertindex)
	{
		if (this.tripleTable.containsKey(keyindex))
		{
			if (this.tripleTable.get(keyindex).containsKey(expertindex))
			{
				return true;
			}
		}
		return false;
	}

	public void put(Integer keyindex, Integer expertindex, double value)
	{
		Map<Integer, Double> entryMap = new TreeMap<Integer, Double>();
		entryMap.put(expertindex, value);
		this.tripleTable.put(keyindex, entryMap);
	}

	public Double get(int keyindex, int expertindex)
	{
		Map<Integer, Double> map = null;
		if ((map = this.tripleTable.get(keyindex)) != null)
		{
			return map.get(expertindex);
		}
		return null;
	}

	public Map<Integer, Double> get(Integer keyindex)
	{
		return this.tripleTable.get(keyindex);
	}

	public Set<Integer> keySet()
	{
		return this.tripleTable.keySet();
	}

	public void writeToDatabase()
	{
		String tableName = "SpMatrix";
		String columLable = "id int NOT NULL PRIMARY KEY,"
				+ " keywordid int NOT NULL REFERENCE keywords (id) ON UPDATE CASCADE ON DELETE CASCADE, "
				+ "expertid int NOT NULL  REFERENCE expert (id) ON UPDATE CASCADE ON DELETE CASCADE, "
				+ "value float NOT NULL ";

		DatabaseOp.createTable(tableName, columLable, "expert"); // 创建表
		
		

		DbConntion dc = new DbConntion();
		Connection mcon = dc.getManualCommitConnection();

		StringBuffer insertSQL = new StringBuffer("INSERT INTO ").append(
				tableName).append(" VALUES (?,?,?,?);");
		PreparedStatement pstmt = null;
		try
		{
			pstmt = mcon.prepareStatement(insertSQL.toString());
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}

		Iterator<Map.Entry<Integer, Map<Integer, Double>>> iite = this.tripleTable
				.entrySet().iterator();

		while (iite.hasNext())
		{
			Map.Entry<Integer, Map<Integer, Double>> wme = iite.next();
			Integer keywordID = wme.getKey();
			Map<Integer, Double> m = wme.getValue(); // 关键词对应的 expert<-->value
														// Map
			Iterator<Map.Entry<Integer, Double>> ite = m.entrySet().iterator();
			while (ite.hasNext())
			{
				Map.Entry<Integer, Double> me = ite.next();
				Integer expertID = me.getKey();
				Double value = me.getValue();
				try
				{
					pstmt.setInt(1, ++this.databaseIndexCount);
					pstmt.setInt(2, keywordID);
					pstmt.setInt(3, expertID);
					pstmt.setFloat(4, value.floatValue());
					pstmt.executeUpdate();
				}
				catch (SQLException e)
				{
					System.out.println(e.getMessage());
				}
			}
		}
		
		// TODO : 建立 keywordid expertid 的 非聚集索引
		try
		{
			mcon.commit();
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		System.out.println("total : " + this.databaseIndexCount);
	}
}
