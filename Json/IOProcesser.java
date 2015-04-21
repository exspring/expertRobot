package Json;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class IOProcesser
{
	private String charset;
	
	public IOProcesser()
	{
		charset = "UTF-8";
	}
	
	public IOProcesser(String charSetType)
	{
		charset = charSetType;
	}
	public void write(File file, IOProcessI fp, String content, boolean isAppend)
	{
		BufferedWriter writer = null;
		try
		{
			writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file, isAppend),charset));
//			fp(writer, content);
			writer.close();
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
	}
	
	public static void Read(File file, String charset, IOProcessI fp)
	{
		BufferedReader reader = null;
		String strLine = null;
		try
		{
			reader = new BufferedReader(new InputStreamReader(new FileInputStream(file),charset));
			fp.doRead(reader);
			reader.close();
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
	}
}
