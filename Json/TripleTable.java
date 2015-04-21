package Json;

import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

public class TripleTable
{
	private Map<Integer, Map<Integer, Integer>> tripleTable;
	
	public TripleTable()
	{
		this.tripleTable = new TreeMap<Integer, Map<Integer,Integer>>();
	}
	
	public  boolean contains(int keyindex, int expertindex)
	{
		if(this.tripleTable.containsKey(keyindex))
		{
			if(this.tripleTable.get(keyindex).containsKey(expertindex))
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
		if((map = this.tripleTable.get(keyindex)) != null)
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
}
