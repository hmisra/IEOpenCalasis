package org.legacyprojectx.tagger;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.*;

import org.legacyprojectx.datastructures.CalasisResult;
import org.legacyprojectx.datastructures.QueryResult1;
import org.legacyprojectx.datastructures.Result;
import org.legacyprojectx.datastructures.TaggerResults;



public class ProjectHead {
	public static void main(String[] args) throws Throwable {

				String directoryPath1="/Users/himanshumisra/Downloads/06-15-15";
				train(directoryPath1,false,true);
				String sIDCTrainingFilePath="/Users/himanshumisra/Desktop/SIDC.train";
				String sIDCTrainingModelFilePath="/Users/himanshumisra/Desktop/SIDC-en.bin";
				DocumentClassifierServiceInformation.trainModel(sIDCTrainingFilePath, sIDCTrainingModelFilePath);


		String directoryPath="/Users/himanshumisra/Desktop/test";
		File dir = new File(directoryPath);

		if (dir.isDirectory()) { 
			for (final File f : dir.listFiles()) {
				try {
					String performance="\""+f.getName()+"\",";
					DataObject obj=GeneralPurposeFunctions.dataObjectFactory(f.getAbsolutePath());
					QueryResult1 cr=OpenCalasis.callCalasis(obj.getOriginalObit());
					if(cr!=null)
					{
						System.out.println("done1");
						QueryResult1 cr1=Prediction.classifyAndTag(GeneralPurposeFunctions.sentenceTokenizer(obj.getOriginalObit()),cr);
						String[] names=cr1.getName().split(" ");
						if(names.length<2)
						{
							cr1.setGender(GeneralPurposeFunctions.genderize(names[0], ""));
						}
						else if(names.length<3)
						{
							cr1.setGender(GeneralPurposeFunctions.genderize(names[0], names[1]));
						}
						else if(names.length<4)
						{
							cr1.setGender(GeneralPurposeFunctions.genderize(names[0], names[2]));
						}
						else if(names.length<5)
						{
							cr1.setGender(GeneralPurposeFunctions.genderize(names[0], names[2]));
						}
						System.out.println("done2");
						performance=performance+GeneralPurposeFunctions.compareAndDisplay(cr1, obj);
						System.out.println("done");
						BufferedWriter bw=new BufferedWriter(new FileWriter(new File("/Users/himanshumisra/Desktop/Input/output.txt"), true));
						bw.write(performance);
						bw.close();
						System.out.println("File Created !!");
					}
				}
				catch(Exception e)
				{
					System.out.println(e.getMessage());
				}
			}

		}



		//
		//		String performance="";
		//
		//		ArrayList<Result> obits=SelfCSVProcessor.process();
		//
		//		
		//		for(Result obit:obits)
		//		{
		//			DataObject obj=new DataObject();
		//			obj.setOriginalObit(obit.getB());
		//			
		//			QueryResult1 cr=OpenCalasis.callCalasis(obj.getOriginalObit());
		//			if(cr!=null)
		//			{
		//
		//				QueryResult1 cr1=Prediction.classifyAndTag(GeneralPurposeFunctions.sentenceTokenizer(obj.getOriginalObit()),cr);
		//				cr1.setName(obit.getA());
		//				String[] names=cr1.getName().split(",");
		//				if(names.length<2)
		//				{
		//					cr1.setGender(GeneralPurposeFunctions.genderize(names[0], ""));
		//				}
		//				else
		//				{
		//					cr1.setGender(GeneralPurposeFunctions.genderize(names[1], names[0].split(" ")[0]));
		//				}
		//				
		//				
		//				
		//				BufferedWriter bw=new BufferedWriter(new FileWriter(new File("/Users/himanshumisra/Desktop/output.txt"),true));
		//				bw.write("\n\n\n"+GeneralPurposeFunctions.compareAndDisplay1(cr1, obj));
		//				bw.close();
		//			}
		//		}



	}

	/*
	 * Functions for calling other functions (top level functions representing modules)
	 */
	public static void trainandpredictversion1() throws Throwable
	{
		//TODO store all the file paths here and based on the availablity of the file path execute the required module
		String directoryPath="/Users/himanshumisra/Downloads/05-26-15";
		train(directoryPath,true,true);
		//train
		String eTMETrainingFilePath="/Users/himanshumisra/Desktop/ETME.train";
		String sIDCTrainingFilePath="/Users/himanshumisra/Desktop/SIDC.train";
		String eTMETrainingModelFilePath="/Users/himanshumisra/Desktop/ETME-en.bin";
		String sIDCTrainingModelFilePath="/Users/himanshumisra/Desktop/SIDC-en.bin";
		EntityTrainerMaximumEntropy.trainEntityModelMaximumEntropy(eTMETrainingFilePath, eTMETrainingModelFilePath);
		DocumentClassifierServiceInformation.trainModel(sIDCTrainingFilePath, sIDCTrainingModelFilePath);
		Prediction.predict();
		//get results from open calasis

		// store it in local memory store
		//perform queries to extract names and locations : take the individual with maximum instances and extract all the sentences where he is mentioned.
		// tag those sentences as education state location date time or person
		//predict
		//output
	}
	public static void train(String directoryPath, boolean etme, boolean sidc) throws Throwable
	{
		// use data path to find all the files in the directory
		ArrayList<String> filePaths=GeneralPurposeFunctions.listOfAllFilesInTheDirectory(directoryPath);
		int fileCount=0;
		long tStart = System.currentTimeMillis();
		for (String path: filePaths)
		{
			// call object factory to return a data object for each file
			DataObject obj=GeneralPurposeFunctions.dataObjectFactory(path);
			TrainingDataCreation.createTrainingData(obj,etme,sidc);
			fileCount++;
			System.out.println("File Number "+ fileCount+ " successfully processed !!");
			// call training data creation to create a training data set
			// call various training  modules to use the training data to generate the training files
		}
		long tEnd = System.currentTimeMillis();
		long tDelta = tEnd - tStart;
		double elapsedSeconds = tDelta / 1000.0;
		System.out.println("Total Time Elapsed in creating training dataset for "+fileCount+" files : "+ elapsedSeconds/60+" Minutes");

	}

	public static void predict(String dataPath)
	{
		// use prediction class as sub head to combine the results from various prediction models generated by the training modules

	}







	/*
	 * Functions for creating training data from the dataobject
	 */








	/*
	 * Functions for various training modules to generate training files
	 */
}
