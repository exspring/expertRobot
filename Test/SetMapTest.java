package Test;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

public class SetMapTest
{

	public static void main(String[] args)
	{
		Map<String,Double> john = new HashMap<String, Double>();
		john.put("hello", 1.0);
		
		Set<Map<String,Double>> s = new HashSet<Map<String,Double>>();
		
		s.add(john);
		
		System.out.println(s.contains(john));
	}
}
