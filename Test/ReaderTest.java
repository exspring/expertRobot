package Test;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

public class ReaderTest
{
	public static void main(String[] args)
	{
		BufferedReader reader = null;
		char[] str = null;
		try
		{
			reader = new BufferedReader(new InputStreamReader(new FileInputStream("s.txt")));
			str = new char[100];
//			while(reader.read(str) != -1)
//			{
//				System.out.println(str);
//			}
			reader.read(str);
			System.out.println(str);
			reader.close();
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
	}
}
