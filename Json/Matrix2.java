package Json;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class Matrix2
{
	private File expertPath;

	private String MatrixPath;

	private Map<String, Set<String>> keyWords;

	private void addToMatrix(String word, String expert)
	{
		if (this.keyWords.containsKey(word))
		{
			this.keyWords.get(word).add(expert);
		} else
		{
			Set<String> set = new HashSet<String>();
			set.add(expert);
			this.keyWords.put(word, set);
		}
	}

	public void write()
	{
		BufferedWriter writer = null;
		Set<String> keySet = this.keyWords.keySet();
		Iterator<String> keyIte = keySet.iterator();
		while (keyIte.hasNext())
		{
			String keyWord = keyIte.next();
			System.out.println(keyWord);
			Set<String> expertSet = this.keyWords.get(keyWord);
			File keyWordFile = new File(this.MatrixPath + keyWord + ".txt");
			try
			{
				writer = new BufferedWriter(new OutputStreamWriter(
						new FileOutputStream(keyWordFile, true), "UTF-8"));
				Iterator<String> expertIte = expertSet.iterator();
				while (expertIte.hasNext())
				{
					writer.append(expertIte.next());
					writer.append("\n");
				}
				writer.close();
			} catch (IOException e)
			{
				e.printStackTrace();
			}
		}
	}

	public void readAExpert(File file)
	{
		BufferedReader reader = null;
		String word = null;
		try
		{
			reader = new BufferedReader(new InputStreamReader(
					new FileInputStream(file), "UTF-8"));
			while ((word = reader.readLine()) != null)
			{
				String expert = file.getName();
				this.addToMatrix(word, expert.substring(0, expert.length() - 5));
			}
			reader.close();
		} catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	public void loadExpert()
	{
		File[] files = this.expertPath.listFiles();
		for (File f : files)
		{
			System.out.println(f.getName());
			this.readAExpert(f);
		}
	}

	public Matrix2()
	{
		this.expertPath = new File("keywords");
		this.MatrixPath = "key\\";
		this.keyWords = new HashMap<String, Set<String>>();
	}

	public static void main(String[] args)
	{
		Matrix2 m = new Matrix2();
		m.loadExpert();
		m.write();
	}
}
