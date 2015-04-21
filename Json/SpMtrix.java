package Json;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

public class SpMtrix
{
	private File keyWordsPath; // 关键词文件所在文件夹

	private String spMtrixPath; // 稀疏矩阵输出位置

	private Map<String, Integer> expertMap; // 专家表头

	private Map<String, Integer> keyMap; // 关键词表头

	private int expertIndexCount;

	private int KeyIndexCount;

	private TripleTable tripleTable;

	private String getName(File file)
	{
		String fileName = file.getName();
		return fileName.substring(0, fileName.length() - 5);
	}

	// private Integer getIndex(Map<String, Integer> map, String key,
	
	// Integer indexCount)
	// {
	// Integer index = null;
	// if ((index = map.get(key)) != null) //找到
	// {
	// return map.get(key);
	// } else
	// {
	// map.put(key, ++indexCount);
	// return indexCount;
	// }
	// }

	private void addToTripleTable(Integer keyindex, Integer expertindex) // 添加或更新三元组
	{
		Integer value = null;
		if ((value = this.tripleTable.get(keyindex, expertindex)) != null)
		{
			this.tripleTable.put(keyindex, expertindex, value + 1);
		}
		else
		{
			this.tripleTable.put(keyindex, expertindex, 1);
		}
	}

	private Integer getExpertIndex(String name) // 获取专家的indexcount,没有就添加添加到对应的map中
	{
		Integer index = null;
		if ((index = this.expertMap.get(name)) != null)
		{
			return index;
		}
		else
		{
			this.expertMap.put(name, ++this.expertIndexCount);
			return this.expertIndexCount;
		}
	}

	private Integer getKeyIndex(String key) // 获取关键词的indexcount,没有就添加到对应map中
	{
		Integer index = null;
		if ((index = this.keyMap.get(key)) != null)
		{
			return index;
		}
		else
		{
			this.keyMap.put(key, ++this.KeyIndexCount);
			return this.KeyIndexCount;
		}
	}

	private void processAFile(File file)
	{
		String name = null;
		String key = null;
		BufferedReader reader = null;
		try
		{
			reader = new BufferedReader(new InputStreamReader(
					new FileInputStream(file), "UTF-8"));
			name = this.getName(file);
			Integer expertindex = this.getExpertIndex(name);
			while ((key = reader.readLine()) != null)
			{
				Integer keyindex = this.getKeyIndex(key);
				this.addToTripleTable(keyindex, expertindex); // 添加或更新三元组
			}
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	private boolean isExpertName(String name)
	{
		if (name.charAt(0) == '*')
		{
			return true;
		}
		return false;
	}

	private void readFile()
	{
		BufferedReader reader = null;
		Integer expertindex = null;
		Integer keyindex = null;
		String line = null;
		try
		{
			reader = new BufferedReader(new InputStreamReader(
					new FileInputStream(this.keyWordsPath), "UTF-8"));
			while ((line = reader.readLine()) != null)
			{
				if (this.isExpertName(line))
				{
					expertindex = this.getExpertIndex(line);
				}
				else
				{
					keyindex = this.getKeyIndex(line);
					this.addToTripleTable(keyindex, expertindex);
				}
			}
			reader.close();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	private void ergodic() // 遍历
	{
		File[] files = this.keyWordsPath.listFiles();
		for (File f : files)
		{
			this.processAFile(f);
		}
	}

	private void load() // load expertMap keyMap and build SpMtrix
	{
		if (this.keyWordsPath.isDirectory())
		{
			this.ergodic();
		}
		else
		{
			this.readFile();
		}
		// System.out.println(this.expertMap);
		// System.out.println(this.keyMap);
		System.out.println("keyIndexCount : " + this.KeyIndexCount);
		System.out.println("expertIndexCount : " + this.expertIndexCount);
	}

	private void writeExpertToFile()
	{
		this.writeMapToFile(this.expertMap, "expert.txt");
	}

	private void writeKeyTOFile()
	{
		this.writeMapToFile(this.keyMap, "key.txt");
	}

	private void writeMapToFile(Map<String, Integer> map, String fileName)
	{
		File file = new File(this.spMtrixPath + fileName);
		try
		{
			BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(
					new FileOutputStream(file), "UTF-8"));
			Iterator<Map.Entry<String, Integer>> ite = map.entrySet()
					.iterator();
			while (ite.hasNext())
			{
				String entry = ite.next().toString();
				System.out.println(entry);
				writer.append(entry);
				writer.append("\n");
			}
			writer.close();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	private void writeIntegerMapToFile(Map<Integer, Integer> map,
			BufferedWriter writer)
	{
		try
		{
			Iterator<Map.Entry<Integer, Integer>> ite = map.entrySet()
					.iterator();
			while (ite.hasNext())
			{
				String entry = ite.next().toString();
				// System.out.println(entry);
				writer.append(entry);
				writer.append("\n");
			}
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	private void writeTripleTableToFile()
	{
		Set<Integer> keySet = this.tripleTable.keySet();
		Iterator<Integer> ite = keySet.iterator();
		try
		{
			File file = new File(this.spMtrixPath + "SpMatrix.txt");
			BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(
					new FileOutputStream(file), "UTF-8"));
			while (ite.hasNext())
			{
				Integer index = ite.next();
				writer.append(index.toString());
				writer.append("\n");
				this.writeIntegerMapToFile(this.tripleTable.get(index), writer);
			}
			writer.close();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}

	}

	public void writToFile()
	{
		this.writeExpertToFile();
		this.writeKeyTOFile();
		this.writeTripleTableToFile();
	}

	public SpMtrix()
	{
		this.expertIndexCount = 0;
		this.KeyIndexCount = 0;
		this.keyWordsPath = new File("keywords");
		this.keyMap = new TreeMap<String, Integer>();
		this.spMtrixPath = new String("spMatrix\\");
		this.expertMap = new TreeMap<String, Integer>();
		this.tripleTable = new TripleTable();
		this.load();
	}

	public static void main(String[] args)
	{
		SpMtrix sm = new SpMtrix();
		sm.writToFile();
	}
}
