package matrix;

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

import Json.FileLister;

public class Matrix
{
	private Set<String> keyWords;
	private File keyDictDir;
	private File MatrixPath;
	private FileLister fileLister;
	boolean isFirstOfFile = true;

	private void loadKeyWords()
	{
		File[] files = keyDictDir.listFiles();
		for (File f : files)
		{
			this.loadAFileKeyWords(f);
			System.out.println(this.keyWords.size());
		}
	}

	private void loadAFileKeyWords(File file)
	{
		BufferedReader reader = null;
		String word = null;
		try
		{
			reader = new BufferedReader(new InputStreamReader(
					new FileInputStream(file), "UTF-8"));
			while ((word = reader.readLine()) != null)
			{
				this.AddKeyToDict(word);
			}
			reader.close();
		} catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	private Map<String, Integer> readAExpertFile(File file)
	{
		BufferedReader reader = null;
		String words = null;
		Map<String, Integer> expert = new HashMap<String, Integer>();
		try
		{
			reader = new BufferedReader(new InputStreamReader(
					new FileInputStream(file), "UTF-8"));
			while ((words = reader.readLine()) != null)
			{
				if (expert.containsKey(words))
				{
					Integer newValue = expert.get(words) + 1;
					expert.put(words, newValue);
				} else
				{
					expert.put(words, 1);
				}
			}
			reader.close();
		} catch (IOException e)
		{
			e.printStackTrace();
		}
		return expert;
	}

	private void processAExpert(File file)
	{
		Map<String, Integer> map = this.readAExpertFile(file);
		BufferedWriter writer = null;
		Iterator<String> ite = this.keyWords.iterator();
		try
		{
			writer = new BufferedWriter(new OutputStreamWriter(
					new FileOutputStream(this.MatrixPath, true), "UTF-8"));
//			if (this.isFirstOfFile == true)
//			{
//				System.out.println("Yes");
//				this.isFirstOfFile = false;
//				writer.append("Name");
//				writer.append("\t\t\t\t\t\t\t");
//				Iterator<String> it = this.keyWords.iterator();
//				while (it.hasNext())
//				{
//					writer.append("\t");
//					writer.append(it.next());
//				}
//				writer.append("\n");
//			}
			String name = file.getName();
//			writer.append(name.substring(0, name.length() - 4));
//			writer.append("\t\t\t\t");
			while (ite.hasNext())
			{
				String keyword = ite.next();
				System.out.println(keyword);
				 if(map.containsKey(keyword))
				 {
				 System.out.println("nnnnnnn");
				 writer.append("\t");
				 writer.append(map.get(keyword).toString());
				 }
				 else
				 {
				 writer.append("\t");
				 writer.append("0");
				 }
			}
			writer.append("\n");
			writer.close();
		} catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	public void writeToFile()
	{
		File[] files = this.keyDictDir.listFiles();
		for (File f : files)
		{
			System.out.println(f.getName());
			this.processAExpert(f);
		}
	}

	public Matrix()
	{
		this.keyWords = new HashSet<String>();
		this.keyDictDir = new File("KeyWords");
		this.MatrixPath = new File("Matrix.txt");
		// this.fileLister = new FileLister(keyDictDir);
		this.loadKeyWords();
	}

	public Matrix(String keyDictDir, String matrixPath)
	{
		this.keyWords = new HashSet<String>();
		this.keyDictDir = new File(keyDictDir);
		this.MatrixPath = new File(matrixPath);
		this.loadKeyWords();
	}

	public void AddKeyToDict(String key)
	{
		keyWords.add(key);
	}

	public static void main(String[] args)
	{
		Matrix matrix = new Matrix();
		matrix.writeToFile();
	}
}
