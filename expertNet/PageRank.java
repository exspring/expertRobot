package expertNet;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class PageRank
{
	private String path;

	private String sim;

	private String pr;

	private String PageRankExe;

	private String contractFactory; // 收缩因子

	private String count; // 迭代次数

	private void init()
	{
		this.sim = path + "\\sim.txt";
		this.pr = path + "\\pr.txt";
		this.PageRankExe = path + "\\PageRank.exe";
		this.contractFactory = "0.8";
		this.count = "10";
	}

	public PageRank()
	{
		this.path = "PageRank";
		this.init();
	}

	public PageRank(String path)
	{
		this.path = path;
		this.init();
	}

	public void compute()
	{
		Runtime runtime = Runtime.getRuntime();

		String[] cmdarray = new String[] { this.PageRankExe, this.sim, this.pr,
				this.contractFactory, this.count };

		Process process = null;
		BufferedReader reader = null;
		String res = null;
		try
		{
			process = runtime.exec(cmdarray);

			reader = new BufferedReader(new InputStreamReader(
					process.getInputStream()));
			while ((res = reader.readLine()) != null)
			{
				System.out.println(res);
			}
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}

}
