package matrix;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import database.DatabaseOp;
import database.DbConntion;

public class SpMtrix
{
	private File keyWordsPath; // �ؼ����ļ������ļ���

	private String spMtrixPath; // ϡ��������λ��

	private Map<String, Integer> expertMap; // ר�ұ�ͷ

	private Map<String, Integer> keyMap; // �ؼ��ʱ�ͷ

	private int expertIndexCount;

	private int KeyIndexCount;

	private String databaseName;

	private Connection con; // database connnection

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
	// if ((index = map.get(key)) != null) //�ҵ�
	// {
	// return map.get(key);
	// } else
	// {
	// map.put(key, ++indexCount);
	// return indexCount;
	// }
	// }

	private void addToTripleTable(Integer keyindex, Integer expertindex) // ��ӻ������Ԫ��
	{
		Double value = null;
		if ((value = this.tripleTable.get(keyindex, expertindex)) != null)
		{
			this.tripleTable.put(keyindex, expertindex, value + 1);
		}
		else
		{
			this.tripleTable.put(keyindex, expertindex, 1);
		}
	}

	private int getIndex(String table, String first, String query)
	{
		DbConntion dc = new DbConntion();
		Connection con = dc.getConnection();
		Statement stmt = null;
		ResultSet rs = null;
		StringBuffer sbsql = new StringBuffer("SELECT ID FROM ").append(table)
				.append(" WHERE ").append(first).append(" = '").append(query)
				.append("';");
		try
		{
			stmt = con.createStatement();
			rs = stmt.executeQuery(sbsql.toString());
			if (rs.next())
			{
				return rs.getInt(1);
			}
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		return -1;
	}

	private Integer getExpertIndex(String name) // ��ȡר�ҵ�indexcount,û�о������ӵ���Ӧ��map��
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

	private Integer getKeyIndex(String key) // ��ȡ�ؼ��ʵ�indexcount,û�о���ӵ���Ӧmap��
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
				this.addToTripleTable(keyindex, expertindex); // ��ӻ������Ԫ��
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

	private void ergodic() // ����
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

	private void writeIntegerMapToFile(Map<Integer, Double> map,
			BufferedWriter writer)
	{
		try
		{
			Iterator<Map.Entry<Integer, Double>> ite = map.entrySet()
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

	private void writeMapToDatabase(Map<String, Integer> map, String tableName,
			String keyName, String valueName, boolean isprocess)
	{
		PreparedStatement pstmt = null;
		// ������
		StringBuffer createTableSQL = new StringBuffer(keyName)
				.append(" varchar(200) NOT NULL,").append(valueName)
				.append(" bigint NOT NULL PRIMARY KEY");

		DatabaseOp.createTable(tableName, createTableSQL.toString(),
				this.databaseName); //������

		StringBuffer indexSQL = new StringBuffer("CREATE NONCLUSTERED INDEX ")
				.append(keyName).append("index").append(" ON ").append(tableName)
				.append("(").append(keyName).append(");");

		DatabaseOp.createIndex(indexSQL.toString(), this.databaseName); //������id��(expert.name,keywords.keyword)�ķǾۼ�����

		try
		{
			StringBuffer insertSQL = new StringBuffer("INSERT INTO ").append(
					tableName).append(" VALUES( ?,?);");
			pstmt = this.con.prepareStatement(insertSQL.toString());

			Iterator<Map.Entry<String, Integer>> ite = map.entrySet()
					.iterator();
			while (ite.hasNext()) // ����
			{
				Map.Entry<String, Integer> me = ite.next();
				String name = me.getKey();
				Integer id = me.getValue();

				pstmt.setString(1, name);
//				System.out.println(name);
				pstmt.setInt(2, id);
//				System.out.println(id);
				pstmt.execute();
			}
			this.con.commit();
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
	}

	public void writeExpertToDatabase()
	{
		System.out.println("write expert to database ...");
		this.writeMapToDatabase(this.expertMap, "Expert", "Name", "ID", false);
	}

	public void writeKeyWordsToDatabase()
	{
		System.out.println("write keywords to database ...");
		this.writeMapToDatabase(this.keyMap, "KeyWords", "KeyWord", "ID", true);
	}

	public void writeTripleTableToDatabase()
	{
		System.out.println("write tripletable to database ...");
		this.tripleTable.writeToDatabase();
	}

	public void writeToDatabase()
	{
		DbConntion dc = new DbConntion();
		this.con = dc.getManualCommitConnection();

		this.databaseName = "expert";

		this.writeExpertToDatabase();
		this.writeKeyWordsToDatabase();
		this.writeTripleTableToDatabase();
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
		sm.writeToDatabase();
	}
}
