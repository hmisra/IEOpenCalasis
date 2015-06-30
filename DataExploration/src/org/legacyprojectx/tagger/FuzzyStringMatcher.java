package org.legacyprojectx.tagger;
import java.util.*;
public class FuzzyStringMatcher {
public static void main(String[] args) {
	ArrayList<String> a=new ArrayList<String>();
	
	

	a.add("Shilpa");
	a.add("Gulati");
	a.add("Himanshu.");
	a.add("Robin Markus Jr");
	a.add("Robin Markus Jr.");
	Set<String> set=new HashSet<String>();
	for(int i=0;i<a.size()-1;i++)
	{
		for (int j=i+1;j<a.size();j++)
		{
			if (GeneralPurposeFunctions.fuzzyCompare(a.get(i), a.get(j))>0.65)
			{
				set.add(a.get(i));
				set.add(a.get(j));
			}
		}
	}
	
	Iterator it=set.iterator();
	while(it.hasNext())
	{
		System.out.println(it.next());
	}

	
	
}
}
