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
import java.util.HashSet;
import java.util.Set;

import com.sun.jna.Library;
import com.sun.jna.Native;
import com.sun.jna.ptr.IntByReference;

public class ParagraphProcess
{

	private String[] reault;

	private DirtWordsDict dirtWordsFilter = new DirtWordsDict();

	// ����ӿ�CLibrary���̳���com.sun.jna.Library
	public interface CLibrary extends Library
	{
		// ���岢��ʼ���ӿڵľ�̬����
		File Path = new File("lib\\NLPIR");
		CLibrary Instance = (CLibrary) Native.loadLibrary(
				Path.getAbsolutePath(), CLibrary.class);

		public int NLPIR_Init(String sDataPath, int encoding,
				String sLicenceCode);

		public String NLPIR_ParagraphProcess(String sSrc, int bPOSTagged);

		public Result[] NLPIR_ParagraphProcessA(String sParagraph,
				IntByReference pResultCount, boolean bUserDict);

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
		}
		catch (UnsupportedEncodingException e)
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
			// String filterArray[] = { ",", ".", "\"", "/", ":", ";", "��", "��",
			// "��", "-", "(", ")", "_", "|", "��", "��", "!", "=", "&", "��",
			// "��", "��", "--", "_", "]", "]", "~" ," "};
			// List<String> filter = Arrays.asList(filterArray);
			for (String s : content)
			{
				if (dirtWordsFilter.isContain(s) == false)
				{
					writer.append(s);
					writer.append("\n");
				}
			}
			writer.close();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}
	
	/**
	 * ��set����ʽ��÷ִʺ�Ľ��
	 * ���ô˷���֮ǰȷ�����ù�splite(String sentence)����
	 * @return 
	 * �ִʺ�Ľ��set
	 */
	public Set<String> getSpliteResult()
	{
		Set<String> rs = new HashSet<String>();
		
		for(String s : this.reault)
		{
			rs.add(s);
		}
		
		return rs;
	}

	/**
	 * �ִ�
	 * @param sentence
	 * ���ִ����
	 */
	public void split(String sentence)
	{
		this.init();
		this.splitSentence(sentence);
		this.free();
	}

	private void splitSentence(String sentence)
	{
		String r = null;
		r = CLibrary.Instance.NLPIR_ParagraphProcess(sentence, 0);
		this.reault = r.split(" ");
	}

	private boolean init()//������Դ
	{
		int initFlag = CLibrary.Instance.NLPIR_Init("", 1, "0");
		if (0 == initFlag)
		{
			System.out.println("Init failed!");
			return false;
		}
		return true;
	}
	
	private void free() //�ͷ���Դ
	{
		CLibrary.Instance.NLPIR_Exit();
	}
	
	/**
	 * �����û��ʵ�
	 * @param dictPath �û��ʵ�·��
	 * @return ����Ĵ���
	 */
	public int importUserDict(String dictPath)
	{
		int UserDictInitCount = CLibrary.Instance.NLPIR_ImportUserDict(
				dictPath, true);

		return UserDictInitCount;
	}

	public void FileParagraphProcess(File file)
	{
		StringBuffer userDictPath = new StringBuffer("KeyWords\\").append(file.getName()); //�û��ʵ�λ��

		if (!this.init())
		{
			return;
		}

		int userDictInitCount = this.importUserDict(userDictPath.toString()); //������Ӧ�û��ʵ�

		System.out.println("Count" + userDictInitCount);

		String line = null;
		BufferedReader reader = null;
		try
		{
			reader = new BufferedReader(new InputStreamReader(
					new FileInputStream(file), "UTF-8"));
			while ((line = reader.readLine()) != null)
			{
				this.splitSentence(line);
				File words = new File("words\\" + file.getName());
				this.FileWriter(words, this.reault);
			}
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}

		this.free();
	}

	public static void main(String[] args) throws Exception
	{
//		String system_charset = "UTF-8";
//		int charset_type = 1;
		ParagraphProcess ph = new ParagraphProcess();
//		int init_flag = CLibrary.Instance.NLPIR_Init("", charset_type, "0");
		File direction = new File("target1");
		File[] files = direction.listFiles();
		for (File file : files)
		{
			System.out.println("Process : " + file.getAbsolutePath());
			ph.FileParagraphProcess(file);
		}
	}
}
