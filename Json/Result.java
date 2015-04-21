package Json;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.sun.jna.Structure;

public class Result extends Structure
{
	public int start;
	public int length;
	public String sPOS;
	public int iPOS;
	public int word_ID;
	public int word_type;
	public int weight;
	@Override
	protected List getFieldOrder()
	{
		List fields = new ArrayList(super.getFieldList());
		fields.addAll(Arrays.asList(new String[]{"start","length","sPPS","iPos","word_ID","word_type","weight"}));
		return fields;
	}
}
