package Json;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.HashSet;
import java.util.Set;

public class DirtWordsDict
{
	private Set<String> dict;
	
	private File dictPath;
	
	private String charSet;
	
	private void load()
	{
		BufferedReader reader = null;
		String word = null;
		dict = new HashSet<String>();
		try
		{
			reader = new BufferedReader(new InputStreamReader(new FileInputStream(dictPath), charSet));
			while((word = reader.readLine()) != null)
			{
				dict.add(word);
			}
			reader.close();
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
	}
	
	private void writeToFile(String word)
	{
		BufferedWriter writer = null;
		try
		{
			writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(dictPath, true), charSet));
			writer.write(word);
			writer.close();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	public DirtWordsDict()
	{
		this.dictPath = new File("dict.txt");
		this.charSet = "UTF-8";
		this.load();
	}
	
	public DirtWordsDict(String filePath)
	{
		this.dictPath = new File(filePath);
		this.charSet = "UTF-8";
		this.load();
	}
	
	public DirtWordsDict(String filePath, String charSet)
	{
		this.dictPath = new File(filePath);
		this.charSet = charSet;
		this.load();
	}
	
	public boolean isContain(String word)
	{
		return this.dict.contains(word);
	}
	
	public boolean remove(String word)
	{
		return dict.remove(word);
	}
	
	public boolean add(String word)
	{
		this.writeToFile(word);
		return dict.add(word);
	}
}
