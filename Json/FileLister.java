package Json;

import java.io.File;

public class FileLister
{
	private File dir;
	
	public FileLister(File dir)
	{
		this.dir = dir;
	}
	
	public void listFile(FileErgodic fe)
	{
		File[] files = dir.listFiles();
		for(File f : files)
		{
			fe.doProcess(f);
		}
	}
}
