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
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

import database.DatabaseOp;
import database.DbConntion;

/**
 * @author lxx
 *
 */
public class TfIdf
{
	/**
	 * 过滤处出来的没有用的词
	 */
	private Set<String> dirtWordList;

	private Map<String, Map<String, Integer>> tfMap;

	private Map<String, Double> idf;

	private Map<String, Map<String, Double>> tfidf;

	/**
	 * 分词后的文件或文件夹，亦即输入路径
	 */
	private File wordPath;

	private File keyWordPath;

	private int fileCount;

	private int fileFlag = 0; // is name of a file?

	private Map<String, Integer> frequencyMap;

	private String fileName;

	private Connection con;

	private String databaseName;

	private String tableName;

	private int idCount = 0;

	public Set<String> getDirtWordList()
	{
		return dirtWordList;
	}

	public Map<String, Map<String, Integer>> getTfMap()
	{
		return tfMap;
	}

	public Map<String, Double> getIdf()
	{
		return idf;
	}

	public Map<String, Map<String, Double>> getTfidf()
	{
		return tfidf;
	}

	public File getWordPath()
	{
		return wordPath;
	}

	public File getKeyWordPath()
	{
		return keyWordPath;
	}

	public int getFileCount()
	{
		return fileCount;
	}

	public int getFileFlag()
	{
		return fileFlag;
	}

	public Map<String, Integer> getFrequencyMap()
	{
		return frequencyMap;
	}

	public String getFileName()
	{
		return fileName;
	}

	private void addFrequencyMapTotfMap(String fileName,
			Map<String, Integer> frequencyMap)
	{
		this.tfMap.put(fileName, frequencyMap);
	}

	private void addToFrequencyMap(Map<String, Integer> map, String str)
	{
		Integer frequency = null;
		if ((frequency = map.get(str)) == null)
		{
			map.put(str, 1);
		}
		else
		{
			map.put(str, frequency + 1);
		}
	}

	private Map<String, Integer> buildExpertTFMap(List<String> list)
	{
		Iterator<String> ite = list.iterator();
		Map<String, Integer> map = new HashMap<String, Integer>();
		while (ite.hasNext())
		{
			this.addToFrequencyMap(map, ite.next());
		}
		return map;
	}

	private void readFileOfDirectory(File file)
	{
		BufferedReader reader = null;
		String line = null;
		Map<String, Integer> map = new HashMap<String, Integer>();
		try
		{
			reader = new BufferedReader(new InputStreamReader(
					new FileInputStream(file), "UTF-8"));
			String fileName = file.getName();
			while ((line = reader.readLine()) != null)
			{
				this.addToFrequencyMap(map, line);
			}
			this.addFrequencyMapTotfMap(fileName, map);
			reader.close();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	private void readDirectory()
	{
		File[] files = this.wordPath.listFiles();
		for (File f : files)
		{
			System.out.println("Reading : " + f.toString());
			this.readFileOfDirectory(f);
		}
	}

	private String createFileName(String line)
	{
		return line.replace(" ", "");
	}

	private void scanLine(String line)
	{
		Scanner sc = new Scanner(line).useDelimiter("\\s+");
		while (sc.hasNext())
		{
			this.addToFrequencyMap(this.frequencyMap, sc.next());
		}
		sc.close();
	}

	private void addToMap(String line, boolean isFileName)
	{
		if (isFileName)
		{
			if (this.frequencyMap != null)
			{
				this.addFrequencyMapTotfMap(fileName, this.frequencyMap);
			}
			this.fileName = this.createFileName(line);
			System.out.println(fileName);
			this.frequencyMap = new HashMap<String, Integer>();
		}
		this.scanLine(line);
	}

	private boolean isFileName(String lineNum)
	{
		if (!lineNum.equals(Integer.toString(this.fileFlag)))
		{
			this.fileFlag++;
			return true;
		}
		return false;
	}

	private void readFile()
	{
		BufferedReader reader = null;
		String line = null;
		String[] aline = null;
		int lineNum = 0;
		try
		{
			reader = new BufferedReader(new InputStreamReader(
					new FileInputStream(this.wordPath), "UTF-8"));
			while ((line = reader.readLine()) != null)
			{
				aline = line.split("\\s+", 2);
				System.out.println(aline[0]);
				System.out.println(aline[1]);
				// System.out.println(this.isFileName(aline[0]));
				this.addToMap(aline[1], this.isFileName(aline[0]));
			}
			this.addToMap("", true);
			reader.close();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	private void computeNiOfFile(Map<String, Integer> frequencyMap,
			Map<String, Double> ni)
	{
		Iterator<String> ite = frequencyMap.keySet().iterator();
		Double value = null;
		while (ite.hasNext())
		{
			String word = ite.next();
			if ((value = ni.get(word)) == null)
			{
				ni.put(word, 1.0);
			}
			else
			{
				ni.put(word, value + 1);
			}
		}
	}

	private Map<String, Double> computeNi()
	{
		Iterator<Map.Entry<String, Map<String, Integer>>> ite = this.tfMap
				.entrySet().iterator();
		Map<String, Double> ni = new HashMap<String, Double>();
		while (ite.hasNext())
		{
			Map<String, Integer> frequencyMap = ite.next().getValue();
			System.out.println("computingNi....");
			this.computeNiOfFile(frequencyMap, ni);
		}
		return ni;
	}

	private void idf()
	{
		Map<String, Double> Ni = this.computeNi();
		this.fileCount = this.tfMap.size();
		Iterator<Map.Entry<String, Double>> ite = Ni.entrySet().iterator();
		while (ite.hasNext())
		{
			Map.Entry<String, Double> ni = ite.next();
			double idf = Math.log(this.fileCount / ni.getValue());
			this.idf.put(ni.getKey(), idf);
		}
	}

	private Map<String, Double> computeFileTFIDF(Map<String, Integer> map)
	{
		Map<String, Double> tfidfmap = new HashMap<String, Double>();
		Iterator<Map.Entry<String, Integer>> ite = map.entrySet().iterator();
		while (ite.hasNext())
		{
			Map.Entry<String, Integer> mapentry = ite.next();
			String word = mapentry.getKey();
			double tfidf = mapentry.getValue() * this.idf.get(word);
			if (this.isImportant(tfidf))
			{
				tfidfmap.put(word, tfidf);
			}
			else
			{
				this.dirtWordList.add(word);
			}
		}
		return tfidfmap;
	}

	public void read() // TODO directory or a file?
	{
		if (this.wordPath.isDirectory())
		{
			this.readDirectory();
		}
		else
		{
			this.readFile();
		}
	}

	public TfIdf(String wordPath)
	{
		this.tfMap = new HashMap<String, Map<String, Integer>>();
		this.idf = new HashMap<String, Double>();
		this.wordPath = new File(wordPath);
		this.keyWordPath = new File("keyWord");
	}

	public TfIdf(String wordPath, String keyWordPath)
	{
		this.tfMap = new HashMap<String, Map<String, Integer>>();
		this.idf = new HashMap<String, Double>();
		this.wordPath = new File(wordPath);
		this.keyWordPath = new File(keyWordPath);
	}

	private void computeTFIDF()
	{
		Iterator<Map.Entry<String, Map<String, Integer>>> ite = this.tfMap
				.entrySet().iterator();
		while (ite.hasNext())
		{
			Map.Entry<String, Map<String, Integer>> mapEntry = ite.next();
			String fileName = mapEntry.getKey();
			Map<String, Double> fileTfidf = this.computeFileTFIDF(mapEntry
					.getValue());
			this.tfidf.put(mapEntry.getKey(), fileTfidf);
		}
	}

	private boolean isImportant(Map.Entry<String, Double> me) // 判断一个词是否足重要
	{
		return me.getValue() > 1.0000;
	}

	private boolean isImportant(double tfidf)
	{
		return tfidf > 5.0000;
		// return true;
	}

	public Map<String, Map<String, Double>> getTFIDF()
	{
		return this.tfidf;
	}

	public void write() // TODO todosomething
	{
		File file = new File("tf_idf.txt");
		BufferedWriter writer = null;
		try
		{
			writer = new BufferedWriter(new OutputStreamWriter(
					new FileOutputStream(file), "UTF-8"));
			Iterator<Map.Entry<String, Map<String, Double>>> ite = this.tfidf
					.entrySet().iterator();
			while (ite.hasNext())
			{
				Map.Entry<String, Map<String, Double>> me = ite.next();
				String fileName = me.getKey();
				writer.append("*");
				writer.append(fileName);
				writer.append("\n");
				this.writeAFile(me.getValue(), writer);
			}
			writer.close();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	public void writeAFile(Map<String, Double> map, BufferedWriter writer)
	{
		Iterator<Map.Entry<String, Double>> ite = map.entrySet().iterator();
		try
		{
			while (ite.hasNext())
			{
				Map.Entry<String, Double> me = ite.next();
				// if (this.isImportant(me) == true) // output the word that is
				// important
				// {
				writer.append(me.toString());
				writer.append("\n");
				// }
			}
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	public void createDirtWordsDict()
	{
		BufferedWriter writer = null;
		try
		{
			writer = new BufferedWriter(new OutputStreamWriter(
					new FileOutputStream("dirtDict"), "UTF-8"));
			Iterator<String> ite = this.dirtWordList.iterator();
			while (ite.hasNext())
			{
				writer.append(ite.next());
				writer.append("\n");
			}
			writer.close();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	/**
	 * 建立数据库
	 * 
	 * @return 建好的数据库名称
	 */
	private String createDatabase()
	{
		return DatabaseOp.createDatabase(this.databaseName);
	}

	/**
	 * 建立一张表
	 * 
	 * @param tableName
	 *            要建立的表名
	 * @param databaseName
	 *            把表建在哪个数据库
	 * @return 建立好的表的名称
	 */
	private String createTable(String tableName, String databaseName)
	{
		String columLable = "id bigint NOT NULL PRIMARY KEY, name varchar(200), word varchar(200) , value float ";

		return DatabaseOp.createTable(tableName, columLable, databaseName);
	}

	/**
	 * 根据this.databaseName获取一个关闭自动提交(setAutoCommint(false))的connection
	 * 
	 */
	private void getDatabaseConnection()
	{
		DbConntion dc = new DbConntion(this.databaseName);
		this.con = dc.getManualCommitConnection();
	}

	/**
	 * 将词语的tfidf写入表中
	 * 
	 * @param word
	 *            词语
	 * @param value
	 *            tfidf值
	 * @param expertName
	 *            专家名
	 * @param pstmt
	 *            预编译的statement,用来向表插入数据
	 */
	private void insertIntoTable(String word, Double value, String expertName,
			PreparedStatement pstmt)
	{
		try
		{
			// pstmt.setString(1, "tfiidf");
			pstmt.setInt(2 - 1, ++this.idCount);
			pstmt.setString(3 - 1, expertName);
			pstmt.setString(4 - 1, word);
			pstmt.setFloat(5 - 1, value.floatValue());
			pstmt.executeUpdate();
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
	}

	/**
	 * 把某个专家的所有词写入到表中
	 * 
	 * @param expertName
	 *            专家名称
	 * @param expert
	 *            专家对应的所有词及其tfidf
	 * @param pstmt
	 *            预编译的statement,用来向表插入数据
	 */
	private void writeTfidfToDataBase(String expertName,
			Map<String, Double> expert, PreparedStatement pstmt)
	{

		Iterator<Map.Entry<String, Double>> ite = expert.entrySet().iterator();
		while (ite.hasNext()) // 遍历
		{
			Map.Entry<String, Double> me = ite.next();
			String word = me.getKey();
			Double value = me.getValue();

			this.insertIntoTable(word, value, expertName, pstmt);
		}
	}

	/**
	 * 将tfidf值写入到数据库中
	 * 
	 */
	public void writeToDatabase()
	{
		this.databaseName = "expert";

		this.tableName = "tfidf";

//		String databaseName = this.createDatabase();

		if (databaseName == null) // 数据库创建失败
		{
			System.out.println("Failed to createDatabase!");
			return;
		}

		this.getDatabaseConnection();

		PreparedStatement pstmt = null;
		StringBuffer insertSQL = new StringBuffer("INSERT INTO ");
		insertSQL.append(this.tableName).append(" VALUES(?,?,?,?);");
		// String insertSQL = "INSERT INTO tfiidf VALUES (?,?,?,?);";
		try
		{
			pstmt = this.con.prepareStatement(insertSQL.toString());
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}

		this.createTable(this.tableName, this.databaseName);

		Iterator<Map.Entry<String, Map<String, Double>>> ite = this.tfidf
				.entrySet().iterator();
		while (ite.hasNext()) // 遍历所有专家
		{
			Map.Entry<String, Map<String, Double>> me = ite.next();
			Map<String, Double> expert = me.getValue();
			String expertName = me.getKey();
			this.writeTfidfToDataBase(expertName, expert, pstmt); // 遍历某个专家对应的所有词
		}

		try
		{
			this.con.commit();
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
	}

	public TfIdf()
	{
		this.tfMap = new HashMap<String, Map<String, Integer>>();
		this.idf = new HashMap<String, Double>();
		this.tfidf = new HashMap<String, Map<String, Double>>();
		this.wordPath = new File("word.txt");
		this.keyWordPath = new File("keyWords");
		this.dirtWordList = new HashSet<String>();
		this.read();
		// this.outputwordlist();
		// this.buildTFMap();
		this.idf();
		this.computeTFIDF();
	}

	public static void main(String[] args)
	{
		TfIdf ti = new TfIdf();
		// ti.write();
		ti.writeToDatabase();
		ti.createDirtWordsDict();
	}
}
