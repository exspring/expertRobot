package Json;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public class WordsFrecStat
{
	private class SortClass implements Comparator<Map.Entry<String, Integer>>
	{

		@Override
		public int compare(Entry<String, Integer> o1, Entry<String, Integer> o2)
		{
			return o2.getValue().intValue() - o1.getValue().intValue();
		}
		
	}
	public List<Map.Entry<String, Integer>> mapSort(Map<String, Integer> map)
	{
		List<Map.Entry<String, Integer>> unsorted = new ArrayList<Map.Entry<String, Integer>>(
				map.entrySet());
		SortClass sortClass = new SortClass();
		unsorted.sort(sortClass);
		return unsorted;
	}

	public void writeToFile(File file, Map<String, Integer> map)
	{
		BufferedWriter writer = null;
		try
		{
			writer = new BufferedWriter(new OutputStreamWriter(
					new FileOutputStream(file, true), "UTF-8"));
			List<Map.Entry<String, Integer>> wordList = this.mapSort(map);
			Iterator<Map.Entry<String, Integer>> ite = wordList.iterator();
			while (ite.hasNext())
			{
				Map.Entry<String, Integer> entry = ite.next();
				writer.append(entry.getKey());
				writer.append("\n");
				writer.append(entry.getValue().toString());
				writer.append("\n");
			}
			writer.close();
		} catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	public void fileFrecStat(File file)
	{
		BufferedReader reader = null;
		Map<String, Integer> wordMap = new HashMap<String, Integer>();
		String word = null;
		try
		{
			reader = new BufferedReader(new InputStreamReader(
					new FileInputStream(file), "UTF-8"));
			while ((word = reader.readLine()) != null)
			{
				if (wordMap.containsKey(word))
				{
					Integer value = new Integer(wordMap.get(word) + 1);
					wordMap.put(word, value);
				} else
				{
					wordMap.put(word, 1);
				}
			}
			File targetPath = new File("hasWordsFrecStat\\" + file.getName());
			this.writeToFile(targetPath, wordMap);
			reader.close();
		} catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	public static void main(String[] args)
	{
		File direction = new File("words");
		File[] files = direction.listFiles();
		WordsFrecStat wfs = new WordsFrecStat();
		for (File f : files)
		{
			wfs.fileFrecStat(f);
		}
	}
}
