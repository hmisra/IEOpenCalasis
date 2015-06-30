package org.legacyprojectx.tagger;
import java.io.*;
import java.util.ArrayList;
public class temp {
	public static void createStopWordsFile() throws IOException {
		BufferedReader br=new BufferedReader(new FileReader(new File("./stop-words_english_6_en.txt")));
		BufferedWriter bw=new BufferedWriter(new FileWriter(new File("./newstopwords.txt")));



		String str="";
		StringBuilder sb=new StringBuilder();
		while((str=br.readLine())!=null)
		{
			sb.append("\""+str+"\",");
		}
		br.close();
		bw.write(sb.toString());
		bw.close();
	}
	public static void getincorrectinformation() throws Exception
	{
		ArrayList<String> filePaths=GeneralPurposeFunctions.listOfAllFilesInTheDirectory("/Users/himanshumisra/Downloads/05-26-15");
		
		for (String path: filePaths)
		{
			DataObject obj=GeneralPurposeFunctions.dataObjectFactory(path);
			if(obj!=null)
				
			
			{
			if(obj.getOriginalObit()!=null && (obj.getOriginalObit().contains("education")||obj.getOriginalObit().contains("college")|| obj.getOriginalObit().contains("university")))
			{
				System.out.println(path);
			}
			}
		}
	}
	
	public static void main(String [] args) throws FileNotFoundException
	{
		GeneralPurposeFunctions.tokenize1("");
	}

}
