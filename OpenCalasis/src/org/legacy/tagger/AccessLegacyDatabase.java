package org.legacy.tagger;
import java.io.*;
public class AccessLegacyDatabase {
	public static void main(String[] args) throws IOException  {
		BufferedReader br=new BufferedReader(new FileReader(new File("/Users/himanshumisra/Desktop/meaning.csv")));
        String str="";
        while((str=br.readLine())!=null)
        {
        	System.out.println(str);
        }
	}

}
