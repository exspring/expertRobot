package recommend;

import java.util.ArrayList;
import java.util.List;

public class ExpertInfo
{
	private List<String> keyWords;
	
	
	private double tfidf;
	
	public List<String> getKeyWords()
	{
		return keyWords;
	}

	public void merge(ExpertInfo info)
	{
		this.keyWords.addAll(info.getKeyWords());
		this.tfidf += info.getTfidf(); 
	}

	public Double getTfidf()
	{
		return tfidf;
	}

	public void addKeyWords(String keyWords)
	{
		this.keyWords.add(keyWords);
	}

	public ExpertInfo(double tfidf)
	{
		this.tfidf = tfidf;
		
		this.keyWords = new ArrayList<String>();
	}

}
