package org.legacyprojectx.datawrangling;

import java.io.*;

import java.util.*;

import org.legacyprojectx.datastructures.StringSimilarityHeapEntity;
import org.legacyprojectx.tagger.DataObject;
import org.legacyprojectx.tagger.GeneralPurposeFunctions;

public class CreateFilesForRV101 {



	public static void main(String[] args) throws Throwable {

		//		Fixed Variables

		String output="/Users/himanshumisra/Desktop/Legacy/";
		File inputDirectory=new File("/Users/himanshumisra/Downloads/06-15-15");
		
		String path=output+"path";
		String fileName="data.csv";
		//  Code 

		for (File file : inputDirectory.listFiles()) {
			if (file.isFile())
			{
				DataObject obj=GeneralPurposeFunctions.dataObjectFactory(file.getAbsolutePath());
				if(obj.getOriginalObit()!=null && obj.getOriginalObit().length()>20)
				{
					getTaggedObits(obj, path+"/"+fileName);
				}
				if(obj.getServiceInformation()!=null && obj.getServiceInformation().length()>10)
				{
					createFile(path+"/"+fileName, obj.getServiceInformation(), "1");
				}
				if(obj.getEducationInformation()!=null && obj.getEducationInformation().length()>10)
				{
					createFile(path+"/"+fileName, obj.getEducationInformation(), "2");
				}
				if(obj.getMilitaryInformation()!=null && obj.getMilitaryInformation().length()>10)
				{
					createFile(path+"/"+fileName, obj.getMilitaryInformation(),"3");
				}
			}
			else
			{
				System.out.println("skipping "+file.getAbsolutePath());
			}
		}


	}

	public static void createFile(String path, String content, String label) throws IOException
	{
		BufferedWriter bw=new BufferedWriter(new FileWriter(new File(path), true));
		bw.write("\""+content+"\", \""+label+"\"");
		bw.newLine();
		bw.close();

	}
	
	private static DataObject getTaggedObits(DataObject obj, String trainingPathSIDC) throws Throwable
	{

		try {

			String obit=obj.getOriginalObit();
			if (obit==null)
			{

			}
			else
			{
				ArrayList<String> positiveStrings=new ArrayList<String>();
				ArrayList<String> positiveClasses=new ArrayList<String>();


				if(obj!=null && obj.getEducationInformation()!=null && !obj.getEducationInformation().equalsIgnoreCase("") && obj.getEducationInformation().length()>2)
				{
					positiveStrings.add(obj.getEducationInformation());
					positiveClasses.add("education");

				}
				if(obj!=null && obj.getServiceInformation()!=null && !obj.getServiceInformation().equalsIgnoreCase("") && obj.getServiceInformation().length()>2)
				{
					positiveStrings.add(obj.getServiceInformation());
					positiveClasses.add("service");
				}
				if(obj!=null && obj.getMilitaryInformation()!=null && !obj.getMilitaryInformation().equalsIgnoreCase("") && obj.getMilitaryInformation().length()>2)
				{
					positiveStrings.add(obj.getMilitaryInformation());
					positiveClasses.add("military");
				}

				createTrainingFileForTextInformation(positiveStrings.toArray(new String[positiveStrings.size()]),obj.getOriginalObit(),positiveClasses.toArray(new String[positiveClasses.size()]),"-1", trainingPathSIDC);

			}
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		finally {

		}

		
		return obj;
	}
	
	
	private static void createTrainingFileForTextInformation(String[] fieldedInformations, String wholeText, String[] positiveClass, String negativeClass,String outputFilePath) throws Throwable
	{
		ArrayList<String> ooarray=new ArrayList<String>(Arrays.asList(GeneralPurposeFunctions.sentenceTokenizer(wholeText)));
		ArrayList<Integer> matcharrayIndex=new ArrayList<Integer>();
		BufferedWriter bw=new BufferedWriter(new FileWriter(new File(outputFilePath), true));
		

		for(String fieldedInformation:fieldedInformations)
		{
			//		String filePath="/Users/himanshumisra/Downloads/doccat.train";
			if(fieldedInformation!=null && !fieldedInformation.equalsIgnoreCase(""))
			{
				String[] siarray=GeneralPurposeFunctions.sentenceTokenizer(fieldedInformation);
				Comparator<StringSimilarityHeapEntity> comparator=new Comparator<StringSimilarityHeapEntity>() {

					@Override
					public int compare(StringSimilarityHeapEntity o1,
							StringSimilarityHeapEntity o2) {
						if(o1.getScore()<o2.getScore())
						{
							return 1;
						}
						else if(o1.getScore()>o2.getScore())
						{
							return -1;
						}
						else
						{
							return 0;
						}
					}
				};
				String[] matcharray=new String[siarray.length];

				for(int count=0;count<siarray.length;count++)
				{
					PriorityQueue<StringSimilarityHeapEntity> bh=new PriorityQueue<StringSimilarityHeapEntity>(comparator);
					for(int k=0;k<ooarray.size();k++)
					{
						bh.add(new StringSimilarityHeapEntity(ooarray.get(k), GeneralPurposeFunctions.fuzzyCompare(siarray[count], ooarray.get(k)),k));
					}
					matcharray[count]=bh.peek().getSentence();
					matcharrayIndex.add(bh.peek().getSentIndex());
				}

			}
		}
		int count=0;
		int number=0;
		for(String s: ooarray)
		{
			if(number<=matcharrayIndex.size()+2)
			{
				if(!matcharrayIndex.contains(count))
				{
					if(s!=null && !(s.equalsIgnoreCase("")))
					{
						if((negativeClass+" "+ s).length()>11)
						{
							bw.write("\""+s+"\", \""+negativeClass+"\"");
							bw.newLine();
							number++;
						}
					}
				}
				count++;
			}
			else
			{
				break;
			}
		}
		bw.close();

	}
}



