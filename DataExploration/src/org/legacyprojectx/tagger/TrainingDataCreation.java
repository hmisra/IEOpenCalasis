package org.legacyprojectx.tagger;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.PriorityQueue;

import org.legacyprojectx.datastructures.StringSimilarityHeapEntity;
import org.legacyprojectx.datastructures.TaggedSentences;

import opennlp.tools.postag.POSModel;
import opennlp.tools.postag.POSTaggerME;
import opennlp.tools.util.InvalidFormatException;

public class TrainingDataCreation {
	public static void createTrainingData(DataObject obj, boolean etme, boolean sidc) throws Throwable
	{
		//flags for various training files
		boolean createTrainingFileForETME=etme;// Entity trainer maximum entropy
		boolean createTrainingFileForSIDC=sidc;//service information document classifier

		//file paths for the training files
		String trainingPathETME="/Users/himanshumisra/Desktop/ETME.train";
		String trainingPathSIDC="/Users/himanshumisra/Desktop/SIDC.train";
		getTaggedObits(obj, createTrainingFileForETME, createTrainingFileForSIDC, trainingPathETME, trainingPathSIDC);

		//



	}


	/*
	 * function to create training data file for Entity Trainer (For POS Training)
	 */
	private static void createTrainingFilesForETME(DataObject obj,String trainFilePath) throws ParseException, IOException
	{
		if(obj.tagged)
		{			

			BufferedWriter bw=new BufferedWriter(new FileWriter(new File(trainFilePath), true));
			for(int i=0;i<obj.gettags().size();i++)
			{
				String sentence="";
				for(int j=0;j<obj.gettags().get(i).length;j++)
				{
					sentence=sentence+obj.getSentences().get(i)[j]+"_"+obj.gettags().get(i)[j]+" ";
				}
				sentence.substring(0, sentence.length()-2);
				bw.write(sentence);
				bw.newLine();
			}
			bw.close();
		}
	}

	/*
	 * function to create training data file for service Information (Document Classification File)
	 */
	private static void createTrainingFileForTextInformation(String[] fieldedInformations, String wholeText, String[] positiveClass, String negativeClass,String outputFilePath) throws Throwable
	{
		ArrayList<String> ooarray=new ArrayList<String>(Arrays.asList(GeneralPurposeFunctions.sentenceTokenizer(wholeText)));
		ArrayList<Integer> matcharrayIndex=new ArrayList<Integer>();
		BufferedWriter bw=new BufferedWriter(new FileWriter(new File(outputFilePath), true));
		int count1=0;

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

				for(String s: matcharray)
				{
					if(s!=null && !(s.equalsIgnoreCase("")))
					{

						if((positiveClass[count1]+" "+ GeneralPurposeFunctions.removeStopWord(s)).length()>9)
						{
							bw.write(positiveClass[count1]+" "+ GeneralPurposeFunctions.stem(GeneralPurposeFunctions.removeStopWord(s.replaceAll("\\n+", " ").replaceAll("\\s+", " "))));
							bw.newLine();
						}
					}
				}

				//				String[] servicewords={"service","visitation","funeral service","interment", "memorial service", "rosary", "shiva","graveside service", "funeral mass", "funeral procession", "gathering", "mass of christian burial", "rite of christian burrial", "burial","inumment", "entombment", "vigil", "calling hours", "prayer service", "life celebration", "celebration of life", "online webcast", "prayer of final commendation"};
				//				for(String s:servicewords)
				//				{
				//					for(int l=0;l<1000;l++)
				//					{
				//						bw.write(positiveClass+" "+ s);
				//						bw.newLine();
				//					}
				//				}
				//				bw.close();
			}
			count1++;
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
						if((negativeClass+" "+ GeneralPurposeFunctions.removeStopWord(s)).length()>11)
						{
							bw.write(negativeClass+" "+GeneralPurposeFunctions.stem(GeneralPurposeFunctions.removeStopWord(s.replaceAll("\\n+", " ").replaceAll("\\s+", " "))));
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




	//	private static HashMap<String, String> getNonEmptyFields(DataObject obj)
	//	{
	//		HashMap<String, String> fields=new HashMap<String, String>();
	//		if(!(obj.getOriginalObit()==null||obj.getOriginalObit().equalsIgnoreCase("")))
	//		{
	//			if (!(obj.getFname()==null||obj.getFname().equalsIgnoreCase("")))
	//			{
	//				fields.put("fname", obj.getFname());
	//			}
	//			if(!(obj.getLname()==null||obj.getLname().equalsIgnoreCase("")))
	//			{
	//				fields.put("lname", obj.getLname());
	//			}
	//			if(!(obj.getMname()==null||obj.getMname().equalsIgnoreCase("")))
	//			{
	//				fields.put("mname", obj.getMname());
	//			}
	//			if(!(obj.getMaidenName()==null||obj.getMaidenName().equalsIgnoreCase("")))
	//			{
	//				fields.put("maidenName", obj.getMaidenName());
	//			}
	//			if(!(obj.getNickname()==null||obj.getNickname().equalsIgnoreCase("")))
	//			{
	//				fields.put("nickname", obj.getNickname());
	//			}
	//			if(!(obj.getSpouseName()==null||obj.getSpouseName().equalsIgnoreCase("")))
	//			{
	//				fields.put("spouseName", obj.getSpouseName());
	//			}
	//			if(!(obj.getGender()==null||obj.getGender().equalsIgnoreCase("")))
	//			{
	//				fields.put("gender", obj.getGender());
	//			}
	//			if(!(obj.getSalutation()==null||obj.getSalutation().equalsIgnoreCase("")))
	//			{
	//				fields.put("salutation", obj.getSalutation());
	//			}
	//			if(!(obj.getDod()==null||obj.getDod().equalsIgnoreCase("") ))
	//			{
	//				fields.put("dod", obj.getDod());
	//			}
	//			if(!(obj.getDob()==null||obj.getDob().equalsIgnoreCase("") ))
	//			{
	//				fields.put("dob", obj.getDob());
	//			}
	//			if(!(obj.getFormerCity()==null||obj.getFormerCity().equalsIgnoreCase("")))
	//			{
	//				fields.put("formerCity", obj.getFormerCity());
	//			}
	//			if(!(obj.getFormerState()==null||obj.getFormerState().equalsIgnoreCase("")))
	//			{
	//				fields.put("formerState", obj.getFormerState());
	//			}
	//			if(!(obj.getCity()==null||obj.getCity().equalsIgnoreCase("")))
	//			{
	//				fields.put("city", obj.getCity());
	//			}
	//			if(!(obj.getState()==null||obj.getState().equalsIgnoreCase("")))
	//			{
	//				fields.put("state", obj.getState());
	//			}
	//			if(!(obj.getSpouseStatus()==null||obj.getSpouseStatus().equalsIgnoreCase("")))
	//			{
	//				fields.put("spouseStatus",obj.getSpouseStatus());
	//			}
	//			if(!(obj.getAge()==null||obj.getAge().equalsIgnoreCase("")))
	//			{
	//				fields.put("age", obj.getAge());
	//			}
	//			if(!(obj.getServiceInformation()==null||obj.getServiceInformation().equalsIgnoreCase("")))
	//			{
	//				fields.put("serviceInformation", obj.getServiceInformation());
	//			}
	//			if(!(obj.getEducationInformation()==null||obj.getEducationInformation().equalsIgnoreCase("")))
	//			{
	//				fields.put("educationInformation", obj.getEducationInformation());
	//			}
	//			if(!(obj.getMilitaryInformation()==null || obj.getMilitaryInformation().equalsIgnoreCase("")))
	//			{
	//				fields.put("militaryInformation", obj.getMilitaryInformation());
	//			}
	//			if(!(obj.getIsLifeCompanion()==null||obj.getIsLifeCompanion().equalsIgnoreCase("")))
	//			{
	//				fields.put("isLifeCompanion", obj.getIsLifeCompanion());
	//			}
	//			if(!(obj.getDonationDetails()==null||obj.getDonationDetails().equalsIgnoreCase("")))
	//			{
	//				fields.put("donationDetails", obj.getDonationDetails());
	//			}
	//		}
	//		return fields;
	//	}
	private static DataObject tagBack(String obit, DataObject obj) throws ParseException, InvalidFormatException, IOException, ClassCastException, ClassNotFoundException
	{
		//get all the sentences ready for tagging
		//obit=getAllTheSentencesReadyForTagging(//obit, obj);

		//create model for tokenizing and pos tagging
		//		InputStream modelIn = new FileInputStream("/Users/himanshumisra/Downloads/en-token.bin");
		InputStream modelIn1 = new FileInputStream("/Users/himanshumisra/Downloads/en-pos-maxent.bin");
		//		TokenizerModel modeltoken = new TokenizerModel(modelIn);
		//		//		Tokenizer tokenizer = new TokenizerME(modeltoken);
		//		WhitespaceTokenizer tokenizer=WhitespaceTokenizer.INSTANCE;
		POSModel model = new POSModel(modelIn1);
		POSTaggerME tagger = new POSTaggerME(model);
		try
		{
			TaggedSentences ts=GeneralPurposeFunctions.stanfordNERTagger(obit);
			String[] tokens=ts.getTokens();
			String[] selftags=ts.getTags();



			//pos tagging

			String postags[] = tagger.tag(tokens);


			//combine tags (custom tags + pos tags)
			if(postags.length==selftags.length)
			{
				for(int i=0;i<selftags.length;i++)
				{
					if(selftags[i].equalsIgnoreCase("O"))
					{
						selftags[i]=postags[i];
					}
				}
			}
			int start=0;
			int end=0;
			while(end<=tokens.length-1)
			{
				if(selftags[end].equalsIgnoreCase("."))
				{
					String tokens1[] = Arrays.copyOfRange(tokens, start, end+1);
					String tags1[]=Arrays.copyOfRange(selftags, start, end+1);
					obj.sentencesInObits.add(tokens1);
					obj.tags.add(tags1);
					end++;
					start=end;

				}
				else
				{
					end++;
				}
			}

		}
		catch(Exception e)
		{
			System.out.println(e.getMessage());
		}

		obj.tagged=true;
		return obj;
	}
	private static DataObject getTaggedObits(DataObject obj, boolean createTrainingFileForETME, boolean createTrainingFileForSIDC, String trainingPathETME, String trainingPathSIDC) throws Throwable
	{

		try {

			String obit=obj.getOriginalObit();
			if (obit==null)
			{

			}
			else
			{
				String [] sentences = GeneralPurposeFunctions.sentenceTokenizer(obj.getOriginalObit());
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



				for(String stopRemoved:sentences)
				{
					
					if(obj!=null && obj.getAge()!=null && !obj.getAge().equalsIgnoreCase(""))
					{
						if(stopRemoved.contains(obj.getAge()) && (stopRemoved.contains("age")))
						{
							positiveStrings.add(stopRemoved);
							positiveClasses.add("age");
						}

					}
					if(obj!=null && obj.getDob()!=null && !obj.getDob().equalsIgnoreCase(""))
					{

						SimpleDateFormat sdf=new SimpleDateFormat("MM/dd/yyyy");
						if (!(obj.getDob().length()<4) )
						{
							Date d=sdf.parse(obj.getDob());
							SimpleDateFormat sdf1=new SimpleDateFormat("MMMM dd, yyyy");
							SimpleDateFormat sdf2=new SimpleDateFormat("MMM dd, yyyy");
							SimpleDateFormat sdf3=new SimpleDateFormat("MM-dd-yyyy");
							SimpleDateFormat sdf4=new SimpleDateFormat("MMMM dd, yyyy,");
							SimpleDateFormat sdf5=new SimpleDateFormat("MMM dd, yyyy,");
							SimpleDateFormat sdf6=new SimpleDateFormat("MM-dd-yyyy,");
							SimpleDateFormat sdf7=new SimpleDateFormat("M/dd/yyyy");
							if (stopRemoved.contains(obj.getDob()))
							{
								positiveStrings.add(stopRemoved);
								positiveClasses.add("dob");
							}
							else if(stopRemoved.contains(sdf1.format(d)))
							{
								positiveStrings.add(stopRemoved);
								positiveClasses.add("dob");
							}
							else if (stopRemoved.contains(sdf2.format(d)))
							{
								positiveStrings.add(stopRemoved);
								positiveClasses.add("dob");
							}
							else if (stopRemoved.contains(sdf3.format(d)))
							{
								positiveStrings.add(stopRemoved);
								positiveClasses.add("dob");
							}
							else if (stopRemoved.contains(sdf4.format(d)))
							{
								positiveStrings.add(stopRemoved);
								positiveClasses.add("dob");
							}
							else if (stopRemoved.contains(sdf5.format(d)))
							{
								positiveStrings.add(stopRemoved);
								positiveClasses.add("dob");
							}
							else if (stopRemoved.contains(sdf6.format(d)))
							{
								positiveStrings.add(stopRemoved);
								positiveClasses.add("dob");
							}
							else if (stopRemoved.contains(sdf7.format(d)))
							{
								positiveStrings.add(stopRemoved);
								positiveClasses.add("dob");						
							}

						}
					}
				}

				if(createTrainingFileForSIDC)
					createTrainingFileForTextInformation(positiveStrings.toArray(new String[positiveStrings.size()]),obj.getOriginalObit(),positiveClasses.toArray(new String[positiveClasses.size()]),"noinfo", trainingPathSIDC);

			}
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		finally {

		}

		if(createTrainingFileForETME)
		{
			obj=tagBack(obj.getOriginalObit(), obj);
			createTrainingFilesForETME(obj, trainingPathETME);
		}
		return obj;
	}

	//function to get all the sentences ready to be tagged
	//	private static String[] getAllTheSentencesReadyForTagging(String[] sentence, DataObject obj) throws ParseException {
	//		HashMap<String,String> map=getNonEmptyFields(obj);
	//		String[] returnSentences=new String[sentence.length];
	//		//Iterator it = map.entrySet().iterator();
	//		int i=0;
	//		for(String sentences:sentence)
	//		{
	//
	//			if(!(sentences==null||sentences.equalsIgnoreCase("")))
	//
	//			{
	//
	//
	//				//Making sure that the words are tokenizable
	//				// if fields are available in the code, make sure they are tokenizable by adding " ".
	//
	//				if (map.containsKey("salutation"))
	//				{
	//					sentences=(tagSalutationInObit(sentences, map.get("salutation")));
	//				}
	//				if (map.containsKey("fname"))
	//				{
	//					sentences=(tagFnameSalutationInObit(sentences, map.get("fname")));
	//				}
	//				if(map.containsKey("mname"))
	//				{
	//					sentences=(tagMnameSalutationInObit(sentences, map.get("mname")));
	//				}
	//				if(map.containsKey("lname"))
	//				{
	//					sentences=(tagLnameSalutationInObit(sentences, map.get("lname")));
	//				}
	//				if(map.containsKey("dod"))
	//				{
	//					sentences=(tagDodInObit(sentences,map.get("dod")));
	//				}
	//				if(map.containsKey("dob"))
	//				{
	//					sentences=(tagDodInObit(sentences,map.get("dob")));
	//				}
	//
	//				//remove all the pipe(|) symbols.
	//
	//				sentences.replaceAll("[|]", " ");
	//				returnSentences[i]=sentences;
	//				i++;
	//			}
	//
	//		}
	//		return returnSentences;
	//	}

	/*
	 * Check Tag Functions
	 */
	//	private static boolean checktagSalutationInObit(String originalObit, String salutation) {
	//		if(originalObit.equalsIgnoreCase(salutation))
	//		{
	//			return true;
	//		}
	//		else
	//		{
	//			return false;
	//		}
	//	}
	//	private static boolean checktagCityInObit(String sentence, String city) {
	//		if(sentence.equalsIgnoreCase(city))
	//		{
	//			return true;
	//		}
	//		else
	//		{
	//			return false;
	//		}
	//	}
	//	private static boolean checktagStateInObit(String sentence, String state) {
	//		if(sentence.equalsIgnoreCase(state))
	//		{
	//			return true;
	//		}
	//		else
	//		{
	//			return false;
	//		}
	//	}
	//	private static boolean checktagFnameSalutationInObit(String originalObit, String fname){
	//		if(originalObit.equalsIgnoreCase(fname) )
	//		{
	//			return true;
	//		}
	//		else
	//		{
	//
	//			return false;
	//		}
	//	}
	//	private static boolean checktagMnameSalutationInObit(String originalObit, String mname){
	//		if(originalObit.equalsIgnoreCase(mname))
	//		{
	//			return true;
	//		}
	//		else
	//		{
	//
	//			return false;
	//		}
	//	}
	//	private static boolean checktagLnameSalutationInObit(String originalObit, String lname){
	//		if(originalObit.equalsIgnoreCase(lname))
	//		{
	//			return true;
	//		}
	//		else
	//		{
	//			return false;
	//		}
	//	}
	//	private static boolean checktagDodInObit(String originalObit, String taggedDate) throws ParseException{
	//		SimpleDateFormat sdf=new SimpleDateFormat("MM/dd/yyyy");
	//		if (taggedDate.length()<4) return false;
	//		Date d=sdf.parse(taggedDate);
	//		SimpleDateFormat sdf1=new SimpleDateFormat("MMMM dd, yyyy");
	//		SimpleDateFormat sdf2=new SimpleDateFormat("MMM dd, yyyy");
	//		SimpleDateFormat sdf3=new SimpleDateFormat("MM-dd-yyyy");
	//		SimpleDateFormat sdf4=new SimpleDateFormat("MMMM dd, yyyy,");
	//		SimpleDateFormat sdf5=new SimpleDateFormat("MMM dd, yyyy,");
	//		SimpleDateFormat sdf6=new SimpleDateFormat("MM-dd-yyyy,");
	//		SimpleDateFormat sdf7=new SimpleDateFormat("M/dd/yyyy");
	//		if (originalObit.contains(taggedDate))
	//		{
	//			return true;
	//		}
	//		else if(originalObit.equalsIgnoreCase(sdf1.format(d)))
	//		{
	//			return true;
	//		}
	//		else if (originalObit.equalsIgnoreCase(sdf2.format(d)))
	//		{
	//			return true;
	//		}
	//		else if (originalObit.equalsIgnoreCase(sdf3.format(d)))
	//		{
	//			return true;
	//		}
	//		else if (originalObit.equalsIgnoreCase(sdf4.format(d)))
	//		{
	//			return true;
	//		}
	//		else if (originalObit.equalsIgnoreCase(sdf5.format(d)))
	//		{
	//			return true;
	//		}
	//		else if (originalObit.equalsIgnoreCase(sdf6.format(d)))
	//		{
	//			return true;
	//		}
	//		else if (originalObit.equalsIgnoreCase(sdf7.format(d)))
	//		{
	//			return true;
	//		}
	//		else
	//		{	
	//			return false;
	//		}
	//	}
	//
	//	/*
	//	 * Tagging Functions
	//	 */
	//
	//	private static String tagDodInObit(String originalObit, String taggedDate) throws ParseException{
	//		SimpleDateFormat sdf=new SimpleDateFormat("MM/dd/yyyy");
	//		if (taggedDate.length()<4) return originalObit;
	//		Date d=sdf.parse(taggedDate);
	//		SimpleDateFormat sdf1=new SimpleDateFormat("MMMM dd, yyyy");
	//		SimpleDateFormat sdf2=new SimpleDateFormat("MMM dd, yyyy");
	//		SimpleDateFormat sdf3=new SimpleDateFormat("MM-dd-yyyy");
	//		SimpleDateFormat sdf4=new SimpleDateFormat("MMMM dd, yyyy,");
	//		SimpleDateFormat sdf5=new SimpleDateFormat("MMM dd, yyyy,");
	//		SimpleDateFormat sdf6=new SimpleDateFormat("MM-dd-yyyy,");
	//		SimpleDateFormat sdf7=new SimpleDateFormat("M/dd/yyyy");
	//		if (originalObit.contains(taggedDate))
	//		{
	//			return originalObit.replaceAll(taggedDate, " "+taggedDate+" ");
	//		}
	//		else if(originalObit.contains(sdf1.format(d)))
	//		{
	//			return originalObit.replaceAll(sdf1.format(d), " "+sdf1.format(d)+" ");
	//		}
	//		else if (originalObit.contains(sdf2.format(d)))
	//		{
	//			return originalObit.replaceAll(sdf2.format(d), " "+sdf2.format(d)+" ");
	//		}
	//		else if (originalObit.contains(sdf3.format(d)))
	//		{
	//			return originalObit.replaceAll(sdf3.format(d), " "+sdf3.format(d)+" ");
	//		}
	//		else if (originalObit.contains(sdf4.format(d)))
	//		{
	//			return originalObit.replaceAll(sdf4.format(d), " "+sdf4.format(d)+" ");
	//		}
	//		else if (originalObit.contains(sdf5.format(d)))
	//		{
	//			return originalObit.replaceAll(sdf5.format(d), " "+sdf5.format(d)+" ");
	//		}
	//		else if (originalObit.contains(sdf6.format(d)))
	//		{
	//			return originalObit.replaceAll(sdf6.format(d), " "+sdf6.format(d)+" ");
	//		}
	//		else if (originalObit.contains(sdf7.format(d)))
	//		{
	//			return originalObit.replaceAll(sdf7.format(d), " "+sdf7.format(d)+" ");
	//		}
	//		else
	//		{
	//
	//			return originalObit;
	//		}
	//	}
	//	private static String tagSalutationInObit(String originalObit, String salutation) {
	//		if (!salutation.contains("."))
	//		{
	//			salutation=salutation+".";
	//		}
	//		if(originalObit.contains(salutation) && !salutation.contains("<"))
	//		{
	//			return originalObit.replaceAll(salutation, " "+salutation+" ");
	//		}
	//		else
	//		{
	//			return originalObit;
	//		}
	//	}
	//	private static String tagFnameSalutationInObit(String originalObit, String fname){
	//		if(originalObit.contains(fname) && !fname.contains("<"))
	//		{
	//			return originalObit.replaceAll(fname, " "+fname+" ");
	//		}
	//		else
	//		{
	//
	//			return originalObit;
	//		}
	//	}
	//	private static String tagMnameSalutationInObit(String originalObit, String mname){
	//
	//		//trying to distinguish between Middle name and gender
	//		//TODO find a better way to do so.
	//		if (!mname.contains("."))
	//		{
	//			mname=mname+".";
	//		}
	//		else
	//		{
	//			if(mname.length()<2)
	//			{
	//				mname=mname+".";
	//			}
	//		}
	//		if(originalObit.contains(mname)&& !mname.contains("<"))
	//		{
	//			return originalObit.replaceAll(mname, " "+mname+" ");
	//		}
	//		else
	//		{
	//
	//			return originalObit;
	//		}
	//	}
	//	private static String tagLnameSalutationInObit(String originalObit, String lname){
	//		if(originalObit.contains(lname) && !lname.contains("<"))
	//		{
	//			return originalObit.replaceAll(lname, " "+lname+" ");
	//		}
	//		else
	//		{
	//			return originalObit;
	//		}
	//	}

}
