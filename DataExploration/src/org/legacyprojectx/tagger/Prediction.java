package org.legacyprojectx.tagger;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import org.legacyprojectx.datastructures.QueryResult1;
import org.legacyprojectx.datastructures.TaggedSentences;
import org.legacyprojectx.datastructures.TaggerResults;

import opennlp.tools.sentdetect.SentenceDetectorME;
import opennlp.tools.sentdetect.SentenceModel;
import opennlp.tools.util.InvalidFormatException;

public class Prediction {

	public static void predict()
	{
		// from directory read all the csv files
		String directoryPath="/Users/himanshumisra/Downloads/testfiles/";
		File dir = new File(directoryPath);
		if (dir.isDirectory()) { 
			for (final File f : dir.listFiles()) {
				try {
					DataObject obj=GeneralPurposeFunctions.dataObjectFactory(f.getAbsolutePath());


					//till this point data object is created
					// Now do what you want to do with data


					//Train Module should be different
					//for each type of field there should be a different train module
					// prediction module should use these modules to predict the data

					InputStream modelIn = new FileInputStream("/Users/himanshumisra/Downloads/en-sent.bin");
					//InputStream modelIn2 = new FileInputStream("/Users/himanshumisra/Desktop/ETME-en.bin");

					try {
						SentenceModel model = new SentenceModel(modelIn);
						SentenceDetectorME sentenceDetector = new SentenceDetectorME(model);
						String sentences[] = sentenceDetector.sentDetect(obj.getOriginalObit());
						StringBuilder sb=new StringBuilder();
						HashMap<String, String> person=new HashMap<String, String>();
						HashMap<String, String> location=new HashMap<String, String>();
						HashMap<String, String> date=new HashMap<String, String>();
						for(int k=0;k<sentences.length;k++)
						{
							Double serviceProb=DocumentClassifierServiceInformation.getProb(sentences[k], "service");

							if(serviceProb!=null)
							{
								serviceProb=(serviceProb*0.5)+(DocumentClassifierServiceInformation.checkForWords(sentences[k], "service")*0.5);
								if(serviceProb>0.51)
								{
									sb.append(sentences[k]+" ");
								}
							}
							String tokens1[]=GeneralPurposeFunctions.tokenize(sentences[k]); 
							String postags[] = EntityTrainerMaximumEntropy.tagWords(tokens1);

							int counter=0;
							while(counter<postags.length)
							{

								StringBuilder sbld=new StringBuilder();
								if(postags[counter].equalsIgnoreCase("Person"))
								{
									sbld.append(tokens1[counter]+" ");
									counter++;
									while(counter<postags.length && postags[counter].equalsIgnoreCase("Person"))
									{
										sbld.append(tokens1[counter]+" ");
										counter++;
									}
									person.put(sbld.toString(), sbld.toString());
								}
								else if(postags[counter].equalsIgnoreCase("Date"))
								{
									sbld.append(tokens1[counter]+" ");
									counter++;
									while(counter<postags.length && postags[counter].equalsIgnoreCase("Date"))
									{
										sbld.append(tokens1[counter]+" ");
										counter++;
									}
									date.put(sbld.toString(), sbld.toString());
								}
								else if(postags[counter].equalsIgnoreCase("City"))
								{

									sbld.append(tokens1[counter]+" ");
									counter++;
									while(counter<postags.length && postags[counter].equalsIgnoreCase("City"))
									{
										sbld.append(tokens1[counter]+" ");
										counter++;
									}
									location.put(sbld.toString(), sbld.toString());
								}
								else if(postags[counter].equalsIgnoreCase("State"))
								{
									sbld.append(tokens1[counter]+" ");
									counter++;
									while(counter<postags.length && postags[counter].equalsIgnoreCase("State"))
									{
										sbld.append(tokens1[counter]+" ");
										counter++;
									}
									location.put(sbld.toString(), sbld.toString());
								}
								else if(postags[counter].equalsIgnoreCase("Location"))
								{
									sbld.append(tokens1[counter]+" ");
									counter++;
									while(counter<postags.length && postags[counter].equalsIgnoreCase("Location"))
									{
										sbld.append(tokens1[counter]+" ");
										counter++;
									}
									location.put(sbld.toString(), sbld.toString());
								}
								else
								{
									counter++;
								}
							}


						}
						System.out.println("___________________________________________________________________________");

						System.out.println("___________________________________________________________________________");

						System.out.println("-> Names of all the people detected by the Algorithm in the obituary");

						@SuppressWarnings("rawtypes")
						Iterator it=person.keySet().iterator();
						while(it.hasNext())
						{
							System.out.println(it.next());
						}
						System.out.println();
						System.out.println("-> Name of Deceased (Human) :- "+ obj.getSalutation()+" "+ obj.getFname()+" "+ obj.getMname()+" "+ obj.getLname());
						it=location.keySet().iterator();
						System.out.println("___________________________________________________________________________");

						System.out.println("-> Names of all the locations detected by the Algorithm in the obituary");
						while(it.hasNext())
						{
							System.out.println(it.next());
						}
						System.out.println();
						System.out.println("-> Location (Human) :- "+obj.getCity()+" "+obj.getState());
						it=date.keySet().iterator();
						System.out.println("___________________________________________________________________________");

						System.out.println("-> All the Dates mentioned in the Obituary");
						while(it.hasNext())
						{
							System.out.println(it.next());
						}
						System.out.println();
						System.out.println("-> Date of Death (Human) :- "+ obj.getDod());
						System.out.println("-> Date of Birth (Human) :- "+ obj.getDob());
						System.out.println("___________________________________________________________________________");

						if(sb.toString().length()>1)
						{

							System.out.println("-> ALGORITHM's PREDICTION of Service Information :- " +sb.toString());
						}
						System.out.println();
						System.out.println("-> Service Information fielded by humans :- "+ obj.getServiceInformation());
						System.out.println("___________________________________________________________________________");






					}
					catch (IOException e) {
						e.printStackTrace();
					}
					finally {
						if (modelIn != null ) {
							try {
								modelIn.close();

							}
							catch (IOException e) {
							}
						}
					}


				}
				catch(Exception e)
				{
					e.printStackTrace();
				}
				finally
				{

					System.out.println("\n\n");
					System.out.println("END OF OBITUARY");
				}
			}
		}
	}
	public static TaggerResults predict(DataObject obj)
	{
		try {

			InputStream modelIn = new FileInputStream("/Users/himanshumisra/Downloads/en-sent.bin");
			//InputStream modelIn2 = new FileInputStream("/Users/himanshumisra/Desktop/ETME-en.bin");

			try {
				SentenceModel model = new SentenceModel(modelIn);
				SentenceDetectorME sentenceDetector = new SentenceDetectorME(model);
				String sentences[] = sentenceDetector.sentDetect(obj.getOriginalObit());
				StringBuilder sb=new StringBuilder();
				HashMap<String, String> person=new HashMap<String, String>();
				HashMap<String, String> location=new HashMap<String, String>();
				HashMap<String, String> date=new HashMap<String, String>();
				for(int k=0;k<sentences.length;k++)
				{
					Double serviceProb=DocumentClassifierServiceInformation.getProb(sentences[k], "service");

					if(serviceProb!=null)
					{
						serviceProb=(serviceProb*0.5)+(DocumentClassifierServiceInformation.checkForWords(sentences[k], "service")*0.5);
						if(serviceProb>0.51)
						{
							sb.append(sentences[k]+" ");
						}
					}
					String tokens1[]=GeneralPurposeFunctions.tokenize(sentences[k]); 
					String postags[] = EntityTrainerMaximumEntropy.tagWords(tokens1);

					int counter=0;
					while(counter<postags.length)
					{

						StringBuilder sbld=new StringBuilder();
						if(postags[counter].equalsIgnoreCase("Person"))
						{
							sbld.append(tokens1[counter]+" ");
							counter++;
							while(counter<postags.length && postags[counter].equalsIgnoreCase("Person"))
							{
								sbld.append(tokens1[counter]+" ");
								counter++;
							}
							person.put(sbld.toString(), sbld.toString());
						}
						else if(postags[counter].equalsIgnoreCase("Date"))
						{
							sbld.append(tokens1[counter]+" ");
							counter++;
							while(counter<postags.length && postags[counter].equalsIgnoreCase("Date"))
							{
								sbld.append(tokens1[counter]+" ");
								counter++;
							}
							date.put(sbld.toString(), sbld.toString());
						}
						else if(postags[counter].equalsIgnoreCase("City"))
						{

							sbld.append(tokens1[counter]+" ");
							counter++;
							while(counter<postags.length && postags[counter].equalsIgnoreCase("City"))
							{
								sbld.append(tokens1[counter]+" ");
								counter++;
							}
							location.put(sbld.toString(), sbld.toString());
						}
						else if(postags[counter].equalsIgnoreCase("State"))
						{
							sbld.append(tokens1[counter]+" ");
							counter++;
							while(counter<postags.length && postags[counter].equalsIgnoreCase("State"))
							{
								sbld.append(tokens1[counter]+" ");
								counter++;
							}
							location.put(sbld.toString(), sbld.toString());
						}
						else if(postags[counter].equalsIgnoreCase("Location"))
						{
							sbld.append(tokens1[counter]+" ");
							counter++;
							while(counter<postags.length && postags[counter].equalsIgnoreCase("Location"))
							{
								sbld.append(tokens1[counter]+" ");
								counter++;
							}
							location.put(sbld.toString(), sbld.toString());
						}
						else
						{
							counter++;
						}
					}


				}
				System.out.println("___________________________________________________________________________");

				System.out.println("___________________________________________________________________________");

				System.out.println("-> Names of all the people detected by the Algorithm in the obituary");

				@SuppressWarnings("rawtypes")
				Iterator it=person.keySet().iterator();
				while(it.hasNext())
				{
					System.out.println(it.next());
				}
				System.out.println();
				System.out.println("-> Name of Deceased (Human) :- "+ obj.getSalutation()+" "+ obj.getFname()+" "+ obj.getMname()+" "+ obj.getLname());
				it=location.keySet().iterator();
				System.out.println("___________________________________________________________________________");

				System.out.println("-> Names of all the locations detected by the Algorithm in the obituary");
				while(it.hasNext())
				{
					System.out.println(it.next());
				}
				System.out.println();
				System.out.println("-> Location (Human) :- "+obj.getCity()+" "+obj.getState());
				it=date.keySet().iterator();
				System.out.println("___________________________________________________________________________");

				System.out.println("-> All the Dates mentioned in the Obituary");
				while(it.hasNext())
				{
					System.out.println(it.next());
				}
				System.out.println();
				System.out.println("-> Date of Death (Human) :- "+ obj.getDod());
				System.out.println("-> Date of Birth (Human) :- "+ obj.getDob());
				System.out.println("___________________________________________________________________________");

				if(sb.toString().length()>1)
				{

					System.out.println("-> ALGORITHM's PREDICTION of Service Information :- " +sb.toString());
				}
				System.out.println();
				System.out.println("-> Service Information fielded by humans :- "+ obj.getServiceInformation());
				System.out.println("___________________________________________________________________________");






			}
			catch (IOException e) {
				e.printStackTrace();
			}
			finally {
				if (modelIn != null ) {
					try {
						modelIn.close();

					}
					catch (IOException e) {
					}
				}
			}
			//TODO
			return null;

		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		finally
		{

			System.out.println("\n\n");
			System.out.println("END OF OBITUARY");
		}
		//todo
		return null;
	}
	public static QueryResult1 classifyAndTag(String[] strings, QueryResult1 obj) throws InvalidFormatException, IOException, ClassCastException, ClassNotFoundException {
		

		StringBuilder serviceInformation=new StringBuilder();
		StringBuilder educationInformation=new StringBuilder();
		StringBuilder militaryInformation=new StringBuilder();
		StringBuilder ageInformation=new StringBuilder();
		StringBuilder dateOfBirthInformation=new StringBuilder();
		for(String instance:strings)
		{
			
			
			
			Double serviceProb=DocumentClassifierServiceInformation.getProb(instance, "service");
			Double educationProb=DocumentClassifierServiceInformation.getProb(instance, "education");
			Double militaryProb=DocumentClassifierServiceInformation.getProb(instance, "military");
			Double dobProb=DocumentClassifierServiceInformation.getProb(instance, "dob");
			Double AgeProb=DocumentClassifierServiceInformation.getProb(instance, "age");
			
			
			
			
			StringBuilder sb=new StringBuilder();
			if(serviceProb!=null)
			{
				serviceProb=(serviceProb*0.5)+(DocumentClassifierServiceInformation.checkForWords(instance, "service")*0.5);
				if(serviceProb>0.51)
				{
					
					serviceInformation.append(instance);
					
				}
			}
			
			if(educationProb!=null)
			{
				educationProb=(educationProb*0.5)+(DocumentClassifierServiceInformation.checkForWords(instance, "education")*0.5);
				if(educationProb>0.51)
				{
					educationInformation.append(instance+" ");
				}
			}
			if(militaryProb!=null)
			{
				militaryProb=(militaryProb*0.5)+(DocumentClassifierServiceInformation.checkForWords(instance, "military")*0.5);
				if(militaryProb>0.51)
				{
					militaryInformation.append(instance+" ");
				}
			}
			if(dobProb!=null)
			{
				dobProb=(dobProb*0.5)+(DocumentClassifierServiceInformation.checkForWords(instance, "dob")*0.5);
				if(dobProb>0.30)
				{
					dateOfBirthInformation.append(instance+" ");
				}
			}
			if(AgeProb!=null)
			{
				AgeProb=(AgeProb*0.5)+(DocumentClassifierServiceInformation.checkForWords(instance, "age")*0.5);
				if(AgeProb>0.30)
				{
					ageInformation.append(instance+" ");
				}
			}
			
		}

		
		
		
		
		
		
		
		obj.setServiceInformation( serviceInformation.toString());
		obj.setServiceInformation(true);
		obj.setEducationInformation(educationInformation.toString());
		obj.setEducationInformation(true);
		obj.setMilitaryInformation(militaryInformation.toString());
		obj.setMilitaryInformation(true);
		obj.setDobInformation(dateOfBirthInformation.toString());
		obj.setDobInformation(true);
		obj.setAgeInformation(ageInformation.toString());
		obj.setAgeInformation(true);
		
		
		
		
		
		obj=tag(serviceInformation,"service|time", obj);
		obj=tag(serviceInformation,"service|date",obj);
		obj=tag(serviceInformation, "service|location",obj);
		obj=tag(dateOfBirthInformation, "dob|date",obj);
		obj=tag(ageInformation, "age|date",obj);
		
		return obj;
	}

	
	public static QueryResult1 tag( StringBuilder serviceInformation, String tag, QueryResult1 obj) throws ClassCastException, ClassNotFoundException, IOException
	{
		TaggedSentences ts=GeneralPurposeFunctions.stanfordNERTagger(serviceInformation.toString());
		StringBuilder info=new StringBuilder();
		
		for(int i=0;i<ts.getTags().length;i++)
		{
			if(ts.getTags()[i].equalsIgnoreCase(tag.split("[|]")[1]))
			{
				
				if(tag.split("[|]")[0].equalsIgnoreCase("service"))
				{
					if(tag.split("[|]")[1].equalsIgnoreCase("date"))
					{
						obj.setDatesOfServiceInformation(obj.getDatesOfServiceInformation()+" "+ts.getTokens()[i]);
						obj.setDateOfService(true);
					}
					else if(tag.split("[|]")[1].equalsIgnoreCase("time"))
					{
						obj.setTimeOfServiceInformation(obj.getTimeOfServiceInformation()+" "+ts.getTokens()[i]);
						obj.setTimeOfService(true);
					}
					else if(tag.split("[|]")[1].equalsIgnoreCase("location"))
					{
						obj.setLocationOfServiceInformation(obj.getLocationOfServiceInformation()+" "+ ts.getTokens()[i]);
						obj.setLocationOfService(true);
					}
				}
				if(tag.split("[|]")[0].equalsIgnoreCase("dob"))
				{
					if(tag.split("[|]")[1].equalsIgnoreCase("date"))
					{
						obj.setDateinDOB(obj.getDateinDOB()+" "+ts.getTokens()[i]);
						obj.setDateOfDob(true);
					}
				}
				if(tag.split("[|]")[0].equalsIgnoreCase("age"))
				{
					if(tag.split("[|]")[1].equalsIgnoreCase("date"))
					{
						obj.setDateInAgeInformation(obj.getDateInAgeInformation()+" "+ ts.getTokens()[i]);
						obj.setDateInAge(true);
					}
				}
				
			}
		}
		
		return obj;
	}
	
}

