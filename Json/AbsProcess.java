package Json;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class AbsProcess
{

	public void FileWriter(File file, Set<String> content)
	{
		BufferedWriter writer = null;
		try
		{
			writer = new BufferedWriter(new OutputStreamWriter(
					new FileOutputStream(file, true), "UTF-8"));
			System.out.println("Write to :" + file.getAbsolutePath());
			Iterator<String> ite = content.iterator();
			while (ite.hasNext())
			{
				writer.append(ite.next());
				writer.append("\n");
			}
			writer.close();
		} catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	public void FileReader(File file)
	{
		String line = null;
		BufferedReader reader = null;
		Set<String> set = new HashSet<String>();
		try
		{
			reader = new BufferedReader(new InputStreamReader(
					new FileInputStream(file)));
			while ((line = reader.readLine()) != null)
			{
				String[] strs = line.split(" ", 2);
				if (strs[0].equals("abs") || strs[0].equals("title"))
				{
					set.add(strs[1]);
				}
			}
			reader.close();
			File target = new File("target1\\" + file.getName());
			this.FileWriter(target, set);

		} catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	public static void main(String[] args)
	{
		AbsProcess ap = new AbsProcess();
		File direction = new File("expert");
		File[] files = direction.listFiles();
		for (File f : files)
		{
			System.out.println("Processing : " + f.getAbsolutePath());
			ap.FileReader(f);
		}
	}
}
