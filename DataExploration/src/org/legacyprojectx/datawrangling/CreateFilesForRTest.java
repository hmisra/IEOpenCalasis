package org.legacyprojectx.datawrangling;

import java.io.*;

import org.legacyprojectx.tagger.DataObject;
import org.legacyprojectx.tagger.GeneralPurposeFunctions;

public class CreateFilesForRTest {
	
public static void main(String[] args) throws Exception {
	
//	Fixed Variables
	
	String output="/Users/himanshumisra/Desktop/Legacy/";
	File inputDirectory=new File("/Users/himanshumisra/Desktop/test");
	String obitPath="/Users/himanshumisra/Desktop/Legacy/test";
	
	
//  Code 

	for (File file : inputDirectory.listFiles()) {
        if (file.isFile())
        {
        	DataObject obj=GeneralPurposeFunctions.dataObjectFactory(file.getAbsolutePath());
        	if(obj.getOriginalObit()!=null && obj.getOriginalObit().length()>20)
        	{
        	createFile(obitPath+"/"+file.getName(), obj.getOriginalObit());
        	}	
        }
        else
        {
            System.out.println("skipping "+file.getAbsolutePath());
        }
    }
	
	
}

public static void createFile(String path, String content) throws IOException
{
	BufferedWriter bw=new BufferedWriter(new FileWriter(new File(path)));
	bw.write(content);
	bw.close();
	
}
}
