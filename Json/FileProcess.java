package Json;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.FieldPosition;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class FileProcess
{
	private List<String[]> stringList;
	private String expertName;
	private String expertOrg;
	int line = 0;

	public void WriteToFile(String partName)
	{
		try
		{

			StringBuffer sb = new StringBuffer("expert\\");
			sb.append(expertName).append("_").append(expertOrg).append(".txt");
			FileWriter writer = new FileWriter(sb.toString(), true);
			writer.append(partName);
			writer.append("\n");
			Iterator<String[]> ite = stringList.iterator();
			while (ite.hasNext())
			{
				String[] s = ite.next();
				writer.append(s[0] + " " + s[1]);
				writer.append("\n");
			}
			writer.append("---\n");
			writer.close();
		} catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	public void Arrangement(String fileName,String partName)
	{
		File file = new File(fileName);
		BufferedReader reader = null;
		try
		{
			reader = new BufferedReader(new InputStreamReader(
					new FileInputStream(file), "UTF-8"));

			String[] strs = new String[3];

			while (reader.readLine() != null)
			{
				String str = null;
//				line++;
				stringList = new ArrayList<String[]>();
				str = reader.readLine();
				while (!str.equals("},"))
				{
//					System.out.println(line++);
					String[] strss = new String[2];
					String[] rStrs = new String[2];
					int last = 2;
					strss = str.split(":", 2);
					// System.out.println(strss[0]);
					// System.out.println(strss[1]);
					if (!strss[1].endsWith(","))
					{
						last = 1;
					} else
					{
						last = 2;
					}
					rStrs[0] = strss[0].substring(3, strss[0].length() - 2);
					rStrs[1] = strss[1].substring(2, strss[1].length() - last);
//					System.out.println(rStrs[0]);
//					System.out.println(rStrs[1]);
					if (strss[0].equals("  \"expert_name\" "))
					{
						expertName = rStrs[1];
					}
					if (strss[0].equals("  \"expert_org\" "))
					{
						expertOrg = rStrs[1];
					}
					stringList.add(rStrs);
					str = reader.readLine();
				}
				WriteToFile(partName);
			}
			reader.close();
		} catch (IOException e)
		{
			e.printStackTrace();
		} finally
		{
			if (reader != null)
			{
				try
				{
					reader.close();
				} catch (IOException e1)
				{

				}
			}
		}
	}

	public static void main(String[] args)
	{
		FileProcess fp = new FileProcess();
		fp.Arrangement("BitData\\patent.txt","patent");
		fp.Arrangement("BitData\\literature.txt","literature");
		fp.Arrangement("BitData\\Intelligence.txt","intelligence");
		System.out.println("done!");
		
	}
}
