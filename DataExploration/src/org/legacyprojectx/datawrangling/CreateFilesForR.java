package org.legacyprojectx.datawrangling;

import java.io.*;
import java.util.*;

import org.legacyprojectx.tagger.DataObject;
import org.legacyprojectx.tagger.GeneralPurposeFunctions;

public class CreateFilesForR {
	
public static void main(String[] args) throws Exception {
	
//	Fixed Variables
	
	String output="/Users/himanshumisra/Desktop/Legacy/";
	File inputDirectory=new File("/Users/himanshumisra/Downloads/06-15-15");
	String obitPath="/Users/himanshumisra/Desktop/Legacy/obit";
	String servicePath=output+"service";
	String educationPath=output+"education";
	String militaryPath=output+"military";
	
//  Code 

	for (File file : inputDirectory.listFiles()) {
        if (file.isFile())
        {
        	DataObject obj=GeneralPurposeFunctions.dataObjectFactory(file.getAbsolutePath());
        	if(obj.getOriginalObit()!=null && obj.getOriginalObit().length()>20)
        	{
        	createFile(obitPath+"/"+file.getName(), obj.getOriginalObit());
        	}
        	if(obj.getServiceInformation()!=null && obj.getServiceInformation().length()>10)
        	{
        	createFile(servicePath+"/"+file.getName(), obj.getServiceInformation());
        	}
        	if(obj.getEducationInformation()!=null && obj.getEducationInformation().length()>10)
        	{
        	createFile(educationPath+"/"+file.getName(), obj.getEducationInformation());
        	}
        	if(obj.getMilitaryInformation()!=null && obj.getMilitaryInformation().length()>10)
        	{
        	createFile(militaryPath+"/"+file.getName(), obj.getMilitaryInformation());
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
