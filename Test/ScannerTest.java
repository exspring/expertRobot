package Test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Scanner;

public class ScannerTest
{
	public static void main(String[] args)
	{
		File file = new File("s.txt");
		int c = 0;
		BufferedReader reader = null;
		try
		{
			reader = new BufferedReader(new InputStreamReader(
					new FileInputStream(file)));
			while ((c = reader.read()) != (-1))
			{
//				System.out.println((char) c);
				String line = reader.readLine();
//				 System.out.println(line);
				Scanner scan = new Scanner(line).useDelimiter("\\s+");
				while (scan.hasNext())
				{
					System.out.println(scan.next());
				}
				scan.close();
			}
			reader.close();
		} catch (IOException e)
		{
			System.out.println(e);
		}
	}
}
