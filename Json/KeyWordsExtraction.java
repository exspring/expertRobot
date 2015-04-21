package Json;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;

public class KeyWordsExtraction 
{
	public static void write(String fileName, String content)
	{
		try
		{
			
			StringBuffer sb = new StringBuffer("KeyWords\\");
			sb.append(fileName);
			File file = new File(sb.toString());
			Writer writer = new OutputStreamWriter(new FileOutputStream(file, true),"UTF-8");
			writer.append(content);
			writer.append("\n");
			writer.close();
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
	}
	public static void FileKeyWordsExtraction(File file)
	{
		BufferedReader reader = null;
		try
		{
			reader = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
			String strLine = null;
			while((strLine = reader.readLine()) != null)
			{
				System.out.println(strLine);
				if(strLine.length() > 4 && strLine.substring(0, 3).equals("key"))
				{
//					System.out.println(strLine.substring(0, 3));
					strLine = strLine.substring(4, strLine.length() - 1).replace(" ", "");
//					System.out.println(strLine);
					String[] strArray = strLine.split(";");
					for(String s : strArray)
					{
//						System.out.println(s);
						write(file.getName(), s);
					}
				}
			}
			reader.close();
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
		System.out.println("Done!");
	}
	
	public static void main(String[] args)
	{
		File direction = new File("expert\\");
		File[] files = direction.listFiles();
		for(File file : files)
		{
//			System.out.println(file.getAbsolutePath());
			FileKeyWordsExtraction(file);
		}
	}
}
