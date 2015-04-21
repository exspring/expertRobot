package Json;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.List;

import com.sun.jna.Library;
import com.sun.jna.Native;
import com.sun.jna.ptr.IntByReference;

public class ParagraphProcess
{

	private DirtWordsDict dirtWordsFilter = new DirtWordsDict();
	// 定义接口CLibrary，继承自com.sun.jna.Library
	public interface CLibrary extends Library
	{
		// 定义并初始化接口的静态变量
		File Path = new File("lib\\NLPIR");
		CLibrary Instance = (CLibrary) Native.loadLibrary(
				Path.getAbsolutePath(), CLibrary.class);

		public int NLPIR_Init(String sDataPath, int encoding,
				String sLicenceCode);

		public String NLPIR_ParagraphProcess(String sSrc, int bPOSTagged);
		
		public Result[] NLPIR_ParagraphProcessA(String sParagraph, IntByReference pResultCount, boolean bUserDict);

		public String NLPIR_GetKeyWords(String sLine, int nMaxKeyLimit,
				boolean bWeightOut);

		public String NLPIR_GetFileKeyWords(String sLine, int nMaxKeyLimit,
				boolean bWeightOut);

		public int NLPIR_AddUserWord(String sWord);// add by qp 2008.11.10

		public int NLPIR_DelUsrWord(String sWord);// add by qp 2008.11.10

		public int NLPIR_ImportUserDict(String sFileName, boolean bOverWrite);

		public String NLPIR_GetLastErrorMsg();

		public void NLPIR_Exit();
	}

	public static String transString(String aidString, String ori_encoding,
			String new_encoding)
	{
		try
		{
			return new String(aidString.getBytes(ori_encoding), new_encoding);
		} catch (UnsupportedEncodingException e)
		{
			e.printStackTrace();
		}
		return null;
	}

	public void FileWriter(File file, String[] content)
	{
		BufferedWriter writer = null;
		try
		{
			writer = new BufferedWriter(new OutputStreamWriter(
					new FileOutputStream(file, true), "UTF-8"));
			System.out.println("Write to :" + file.getAbsolutePath());
//			String filterArray[] = { ",", ".", "\"", "/", ":", ";", "、", "～",
//					"℃", "-", "(", ")", "_", "|", "【", "】", "!", "=", "&", "《",
//					"》", "。", "--", "_", "]", "]", "~" ," "};
//			List<String> filter = Arrays.asList(filterArray);
			for (String s : content)
			{
				if (dirtWordsFilter.isContain(s) == false)
				{
					writer.append(s);
					writer.append("\n");
				}
			}
			writer.close();
		} catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	public void FileParagraphProcess(File file)
	{
		StringBuffer userDictPath = new StringBuffer("KeyWords\\");
		StringBuffer userDictName = userDictPath.append(file.getName());
		int initFlag = CLibrary.Instance.NLPIR_Init("", 1, "0");
		if (0 == initFlag)
		{
			System.out.println("Init failed!");
			return;
		}
		int UserDictInitCount = CLibrary.Instance.NLPIR_ImportUserDict(
				userDictName.toString(), true);
		System.out.println("Count" + UserDictInitCount);

		String line = null;
		BufferedReader reader = null;
		try
		{
			reader = new BufferedReader(new InputStreamReader(
					new FileInputStream(file), "UTF-8"));
			int wordsCount;
			String result = null;
			while ((line = reader.readLine()) != null)
			{
				result = CLibrary.Instance.NLPIR_ParagraphProcess(line, 0);
//				System.out.println(wordsCount.toString());
				String r[] = result.split(" ");
				File words = new File("words\\" + file.getName());
				this.FileWriter(words, r);
			}
		} catch (IOException e)
		{
			e.printStackTrace();
		}
		CLibrary.Instance.NLPIR_Exit();
	}

	public static void main(String[] args) throws Exception
	{
		String system_charset = "UTF-8";
		int charset_type = 1;
		ParagraphProcess ph = new ParagraphProcess();
		int init_flag = CLibrary.Instance.NLPIR_Init("", charset_type, "0");
		File direction = new File("target1");
		File[] files = direction.listFiles();
		for (File file : files)
		{
			System.out.println("Process : " + file.getAbsolutePath());
			ph.FileParagraphProcess(file);
		}
	}
}
