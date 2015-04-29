package Json;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.concurrent.locks.StampedLock;

public class TripleTable
{
	private Map<Integer, Map<Integer, Integer>> tripleTable;

	private int databaseIndexCount = 0;

	public TripleTable()
	{
		this.tripleTable = new TreeMap<Integer, Map<Integer, Integer>>();
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

	public void put(Integer keyindex, Integer expertindex, Integer value)
	{
		Map<Integer, Integer> entryMap = new TreeMap<Integer, Integer>();
		entryMap.put(expertindex, value);
		this.tripleTable.put(keyindex, entryMap);
	}

	public Integer get(int keyindex, int expertindex)
	{
		Map<Integer, Integer> map = null;
		if ((map = this.tripleTable.get(keyindex)) != null)
		{
			return map.get(expertindex);
		}
		return null;
	}

	public Map<Integer, Integer> get(Integer keyindex)
	{
		return this.tripleTable.get(keyindex);
	}

	public Set<Integer> keySet()
	{
		return this.tripleTable.keySet();
	}

	public void writeToDatabase()
	{
		DbConntion dc = new DbConntion();
		Connection con = dc.getConnection();
		Statement stmt = null;
		try
		{
			stmt = con.createStatement();
			stmt.execute("CREATE TABLE TripleTable ( keyindex int NOT NULL, keywordid int NOT NULL, expertid int NOT NULL, value int NOT NULL )");
		}
		catch (SQLException e)
		{
//			e.printStackTrace();
			System.out.println(e.getMessage());
			return;
		}
		Iterator<Map.Entry<Integer, Map<Integer, Integer>>> iite = this.tripleTable
				.entrySet().iterator();
		while (iite.hasNext())
		{
			Map.Entry<Integer, Map<Integer, Integer>> wme = iite.next();
			Integer keywordID = wme.getKey();
			Map<Integer, Integer> m = wme.getValue();
			Iterator<Map.Entry<Integer, Integer>> ite = m.entrySet().iterator();
			while (ite.hasNext())
			{
				Map.Entry<Integer, Integer> me = ite.next();
				Integer expertID = me.getKey();
				Integer value = me.getValue();
				try
				{
					StringBuffer insertSQL = new StringBuffer()
							.append("INSERT INTO TripleTable VALUES ('")
							.append((++this.databaseIndexCount)).append("',")
							.append("'").append(keywordID.toString())
							.append("',").append("'")
							.append(expertID.toString()).append("',")
							.append("'").append(value.toString()).append("')");
					stmt.executeUpdate(insertSQL.toString());
				}
				catch (SQLException e)
				{
					System.out.println(e.getMessage());
				}
			}
		}
		System.out.println("total : " + this.databaseIndexCount);
	}
}
