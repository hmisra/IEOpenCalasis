package org.legacyprojectx.tagger;

import java.io.*;
import java.util.ArrayList;

import org.legacyprojectx.datastructures.Result;

import com.opencsv.CSVReader;
public class SelfCSVProcessor {
	public static ArrayList<Result> process() throws IOException {
		
		ArrayList<Result> list=new ArrayList<Result>();
		
		CSVReader reader = new CSVReader(new FileReader("/Users/himanshumisra/Desktop/meaning.csv"), ',' , '"' , 1);
		String nextLine[];
		 while ((nextLine = reader.readNext()) != null) {
	         if (nextLine != null) {
	            //Verifying the read data here
	            
	            list.add(new Result(nextLine[0], nextLine[2].replaceAll("\\<.*?>","").trim()));
	         }
	       }
		 reader.close();
		 return list;
	}
	

}
