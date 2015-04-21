package Json;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class ReadJson
{
	public static void appendMethod(String fileName, String content)
	{
		try
		{
			FileWriter writer = new FileWriter(fileName, true);
			writer.write(content);
			writer.close();
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
	}
	public static void readFileByLines(String fileName)
	{
		File file = new File(fileName);
		BufferedReader reader = null;
		try
		{
			System.out.println("Read By Line!");
			reader = new BufferedReader(new InputStreamReader(new FileInputStream(file),"UTF-8"));
			String tempString = null;
			StringBuffer totalString = new StringBuffer();
			int line = 1;
			while((tempString = reader.readLine()) != null)
			{
				System.out.println("Line" + line + ":" + tempString);
				totalString.append(tempString);
				line++;
			}
			reader.close();
			System.out.println(totalString.substring(0, 200));
			JSONArray jsonarray = JSONArray.fromObject(totalString.toString());
			System.out.println(jsonarray.size());
//			appendMethod("e:\\bbb.txt", jsonarray.toString());
			Object[] array = jsonarray.toArray();
			System.out.println(((JSONObject)array[0]).getString("inventor"));
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
		finally
		{
			if(reader != null)
			{
				try
				{
					reader.close();
				}
				catch(IOException e1)
				{
					
				}
			}
		}
	}
	
	public static void main(String[] args)
	{
		String path = ".\\";
		readFileByLines(path);
		
	}
}
