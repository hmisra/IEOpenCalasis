package org.legacyprojectx.tagger;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.text.Normalizer;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.ZipInputStream;
import java.io.*;

import opennlp.tools.postag.POSModel;
import opennlp.tools.postag.POSTaggerME;
import opennlp.tools.sentdetect.SentenceDetectorME;
import opennlp.tools.sentdetect.SentenceModel;
import opennlp.tools.tokenize.Tokenizer;
import opennlp.tools.tokenize.TokenizerME;
import opennlp.tools.tokenize.TokenizerModel;
import opennlp.tools.util.InvalidFormatException;

import org.legacyprojectx.datastructures.CalasisResult;
import org.legacyprojectx.datastructures.QueryResult1;
import org.legacyprojectx.datastructures.TaggedSentences;
import org.legacyprojectx.datastructures.TaggerResults;

import uk.ac.shef.wit.simmetrics.similaritymetrics.Levenshtein;
import edu.stanford.nlp.ie.AbstractSequenceClassifier;
import edu.stanford.nlp.ie.crf.CRFClassifier;
import edu.stanford.nlp.ling.CoreLabel;

import java.net.URLEncoder;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import com.fasterxml.jackson.core.JsonParser;
import com.googlecode.clearnlp.tokenization.EnglishTokenizer;
import com.json.parsers.JSONParser;
import com.json.parsers.JsonParserFactory;

public class GeneralPurposeFunctions {

	public static String genderize(String firstName, String lastName) {
		try {
			DefaultHttpClient httpclient = new DefaultHttpClient();
			String url = "http://api.namsor.com/onomastics/api/json/gendre/"
					+ URLEncoder.encode(firstName, "UTF-8") + "/"
					+ URLEncoder.encode(lastName, "UTF-8");
			HttpGet httpget = new HttpGet(url);
			HttpResponse response = httpclient.execute(httpget);
			BufferedReader in = new BufferedReader(new InputStreamReader(
					response.getEntity().getContent()));
			String line = in.readLine();
			JsonParserFactory factory=JsonParserFactory.getInstance();
			JSONParser parser=factory.newJsonParser();
			Map jsonData=parser.parseJson(line);

			return (String)jsonData.get("gender");
			//				Double result=0.0;
			//				if (line != null && !line.trim().equals("")) {
			//					result = Double.parseDouble(line);
			//				}
			//				in.close();
			//				if(0-result>0)
			//				{
			//					return "M";
			//				}
			//				else
			//				{
			//					return "F";
			//				}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	public static ArrayList<String> listOfAllFilesInTheDirectory(String directoryPath) throws Exception {
		ArrayList<String> fileList=new ArrayList<String>();
		//		String directoryPath="/Users/himanshumisra/Downloads/testfiles/";
		File dir = new File(directoryPath);
		if (dir.isDirectory()) { 
			for (final File f : dir.listFiles()) {
				fileList.add(f.getAbsolutePath());
			}

		}
		else if(dir.isFile())
		{
			// if directory path is not a directory by a file the store the file path in the arrayList
			fileList.add(directoryPath);
		}
		else
		{
			throw new Exception("Not a valid File (org.Legacy.projectX.SelfTagger.GeneralPurposeFunctions.listOfAllFilesInTheDirectory().Exception 1");
		}
		return fileList;

	}







	/*
	 * 
	 */

	//PUPDATE : should be independent of the data object class : no string values should be hard coded in this function

	public static DataObject dataObjectFactory(String filePath) throws Exception
	{
		BufferedReader br=new BufferedReader(new FileReader(new File(filePath)));
		DataObject obj=new DataObject();
		try {

			String str="";
			br.readLine();

			while((str=br.readLine())!=null)
			{
				int count2=0;

				do
				{
					count2=0;
					for(int i=0;i<str.length();i++)
					{
						if (str.charAt(i)=='\"')
						{
							count2++;
						}
					}
					if(count2 % 2 !=0)
					{
						str=str+" "+br.readLine();
					}

				}
				while(count2%2!=0);

				String[] tokens = str.split(",(?=([^\"]*\"[^\"]*\")*[^\"]*$)", -1);
				if(tokens[1].length()>0 && tokens[1].charAt(0)=='"' && tokens[1].charAt(tokens[1].length()-1)=='"')
				{
					tokens[1]=tokens[1].substring(1, tokens[1].length()-1);
				}
				//extract all the fields and store it in data object
				if (tokens[0].equalsIgnoreCase("Nickname")||tokens[0].equalsIgnoreCase("Nickname "))
				{
					obj.setNickname(tokens[1]);
				}
				else if (tokens[0].equalsIgnoreCase("Date of birth")||tokens[0].equalsIgnoreCase("Date of birth "))
				{

					obj.setDob(tokens[1]);
				}
				else if (tokens[0].equalsIgnoreCase("Former city in obit title")||tokens[0].equalsIgnoreCase("Former city in obit title "))
				{
					obj.setFormerCity(tokens[1]);
				}  
				else if (tokens[0].equalsIgnoreCase("State in obit title")|| tokens[0].equalsIgnoreCase("State in obit title "))
				{
					obj.setState(tokens[1]);
				} 
				else if (tokens[0].equalsIgnoreCase("Service information")||tokens[0].equalsIgnoreCase("Service information "))
				{
					obj.setServiceInformation(tokens[1]);
				} 
				else if (tokens[0].equalsIgnoreCase("Salutation")|| tokens[0].equalsIgnoreCase("Salutation "))
				{
					obj.setSalutation(tokens[1]);
				}
				else if (tokens[0].equalsIgnoreCase("City in obit title")||tokens[0].equalsIgnoreCase("City in obit title "))
				{
					obj.setCity(tokens[1]);
				} 
				else if (tokens[0].equalsIgnoreCase("Former state in obit title") || tokens[0].equalsIgnoreCase("Former state in obit title "))
				{
					obj.setState(tokens[1]);
				} 
				else if (tokens[0].equalsIgnoreCase("Original obituary listing")|| tokens[0].equalsIgnoreCase("Original obituary listing "))
				{
					
					String preProcess1=tokens[1];
					preProcess1=Normalizer.normalize(preProcess1,java.text.Normalizer.Form.NFC );
					preProcess1=preProcess1.replaceAll("�", "");
					preProcess1.replaceAll("\\(", " (");
					preProcess1.replaceAll("\\)", ") ");
					preProcess1.replaceAll("www.[^\\s]*", "");
					
					preProcess1=removeUrl(preProcess1).replaceAll("([A-Z][a-z]+)([A-Z][a-z]+)", "$1. $2").replaceAll("([0-9]+)([A-Z][a-z]+)", "$1. $2").replaceAll("([,|.|!|?|;])([A-Z a-z 0-9]+)", "$1 $2").replaceAll("([\\s])+", "$1");
					preProcess1=preProcess1.toLowerCase();
					preProcess1=removeContent(preProcess1, "pdf printable version", "removeafter");
					preProcess1=removeContent(preProcess1, "share on", "removeafter");
					preProcess1=removeContent(preProcess1, "click to send", "removeafter");
					preProcess1=removeContent(preProcess1, "guestbook-form-head", "removeafter");
					preProcess1=removeContent(preProcess1, "if(typeof(", "removeafter");
					preProcess1=removeContent(preProcess1, "please visit(.*)website", "removeafterreg");
					preProcess1=removeContent(preProcess1, "print view", "removeafter");
					preProcess1=removeContent(preProcess1, "get order flowers now", "removeafter");
					preProcess1=removeContent(preProcess1, "show less", "removeafter");
					preProcess1=removeContent(preProcess1, "go share a memory", "removebefore");
					preProcess1=removeContent(preProcess1,"create an online memorial", "removeafter");
					
					obj.setOriginalObit(preProcess1);
				} 
				else if (tokens[0].equalsIgnoreCase("Is Life companion") || tokens[0].equalsIgnoreCase("Is Life companion "))
				{
					obj.setIsLifeCompanion(tokens[1]);
				} 
				else if (tokens[0].equalsIgnoreCase("Spouses Name") || tokens[0].equalsIgnoreCase("Spouses Name "))
				{
					obj.setSpouseName(tokens[1]);
				}
				else if (tokens[0].equalsIgnoreCase("Donation/Contribution Details") || tokens[0].equalsIgnoreCase("Donation/Contribution Details "))
				{
					obj.setDonationDetails(tokens[1]);
				}
				else if (tokens[0].equalsIgnoreCase("Date of death")||tokens[0].equalsIgnoreCase("Date of death "))
				{
					obj.setDod(tokens[1]);
				}
				else if (tokens[0].equalsIgnoreCase("Maiden name")||tokens[0].equalsIgnoreCase("Maiden name "))
				{
					obj.setMaidenName(tokens[1]);
				}
				else if (tokens[0].equalsIgnoreCase("First name")||tokens[0].equalsIgnoreCase("First name "))
				{
					obj.setFname(tokens[1]);
				}
				else if (tokens[0].equalsIgnoreCase("Education information")||tokens[0].equalsIgnoreCase("Education information "))
				{
					obj.setEducationInformation(tokens[1]);
				}
				else if (tokens[0].equalsIgnoreCase("Gender")||tokens[0].equalsIgnoreCase("Gender "))
				{
					obj.setGender(tokens[1]);
				}
				else if (tokens[0].equalsIgnoreCase("Military Information")|tokens[0].equalsIgnoreCase("Military Information "))
				{
					obj.setMilitaryInformation(tokens[1]);
				}
				else if (tokens[0].equalsIgnoreCase("Middle name or initial")||tokens[0].equalsIgnoreCase("Middle name or initial "))
				{
					obj.setMname(tokens[1]);
				}
				else if (tokens[0].equalsIgnoreCase("Spouse�s Living Status")||tokens[0].equalsIgnoreCase("Spouse�s Living Status "))
				{
					obj.setSpouseStatus(tokens[1]);
				}
				else if (tokens[0].equalsIgnoreCase("Surname")||tokens[0].equalsIgnoreCase("Surname "))
				{
					obj.setLname(tokens[1]);
				}
				else if (tokens[0].equalsIgnoreCase("Age")||tokens[0].equalsIgnoreCase("Age "))
				{
					obj.setAge(tokens[1]);
				}

			}
			obj.setDataLoaded(true);
		}
		catch (FileNotFoundException e)
		{
			e.printStackTrace();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			br.close();

		}
		if (obj==null)
		{
			throw new NullPointerException("Data Object Empty, General Purpose Functions/ DataObjectFactory/ end/.");
		}
		else if (!obj.getDataLoaded())
		{
			throw new Exception("Data Object not created successfully, complete file data not loaded in the object  General Purpose Functions/ DataObjectFactory/ end/");
		}
		else
		{
			return obj;
		}
	}

	public static int getRegexIndex(String text, String regex) {
	    Pattern pattern = Pattern.compile(regex);
	    Matcher matcher = pattern.matcher(text);
	    // Check all occurrences
	    while (matcher.find()) {
	       return matcher.start();
	    }
	    return -1;
	}
	public static String removeContent(String original, String pattern, String command) {
		
		if(command.equalsIgnoreCase("removeafter"))
		{
		int index=original.indexOf(pattern);
		if(index!=-1)
		{
			original=original.substring(0, index);
		}
		return original;
		}
		else if(command.equalsIgnoreCase("removebefore"))
		{
			int index=original.indexOf(pattern);
			if(index!=-1)
			{
				original=original.substring(index+pattern.length(), original.length());
			}
			return original;
		}
		else if(command.equalsIgnoreCase("removebeforereg"))
		{
			int index=getRegexIndex(original, pattern);
			if(index!=-1)
			{
				original=original.substring(index+pattern.length(), original.length());
			}
			return original;
		}
		else if(command.equalsIgnoreCase("removeafterreg"))
		{
		int index=getRegexIndex(original, pattern);
		if(index!=-1)
		{
			original=original.substring(0, index);
		}
		return original;
		}
		else
		{
			return original;
		}
	}
	public static TaggedSentences stanfordNERTagger(String str) throws ClassCastException, ClassNotFoundException, IOException
	{
		str= str.replaceAll("\\s+", " ");
		str= str.replaceAll("[�]", "");
		String serializedClassifier = "classifiers/english.muc.7class.distsim.crf.ser.gz";
		AbstractSequenceClassifier<CoreLabel> classifier = CRFClassifier.getClassifier(serializedClassifier);
		String[] tagged= classifier.classifyToString(str, "slashTags", false).split(" ");
		ArrayList<String> tokens=new ArrayList<String>();
		ArrayList<String> tags=new ArrayList<String>();
		for(String word:tagged)
		{
			if(word.length()>1)
			{
				tokens.add(word.substring(0, word.lastIndexOf('/')));
				tags.add(word.substring(word.lastIndexOf('/')+1, word.length()));
			}
		}
		TaggedSentences ts=new TaggedSentences();
		ts.setTokens(tokens.toArray(new String [tokens.size()]));
		ts.setTags(tags.toArray(new String[tags.size()]));
		return ts;


	}
	
	public static String removeUrl(String commentstr)
    {
        String urlPattern = "((https?|ftp|gopher|telnet|file|Unsure|http):((//)|(\\\\))+[\\w\\d:#@%/;$()~_?\\+-=\\\\\\.&]*)";
        Pattern p = Pattern.compile(urlPattern,Pattern.CASE_INSENSITIVE);
        Matcher m = p.matcher(commentstr);
        int i = 0;
        while (m.find()) {
        	try{
            commentstr = commentstr.replaceAll(m.group(i),"").trim();
        	}
        	catch(Exception e)
        	{
        		System.out.println(commentstr);
        	}
            i++;
        }
        return commentstr;
    }

	public static String[] sentenceTokenizer(String sentence) throws InvalidFormatException, IOException
	{

		InputStream modelIn = new FileInputStream("/Users/himanshumisra/Downloads/en-sent.bin");
		SentenceModel model = new SentenceModel(modelIn);
		SentenceDetectorME sentenceDetector = new SentenceDetectorME(model);
		String [] tokens=sentenceDetector.sentDetect(sentence);
		modelIn.close();
		return tokens;
	}

	public static String[] postags(String[] tokens) throws InvalidFormatException, IOException
	{
		InputStream modelIn1 = new FileInputStream("/Users/himanshumisra/Downloads/en-pos-maxent.bin");
		POSModel model = new POSModel(modelIn1);
		POSTaggerME tagger = new POSTaggerME(model);
		String[] tags= tagger.tag(tokens);
		modelIn1.close();
		return tags;
	}
	public static String[] tokenize(String str) throws FileNotFoundException
	{
		InputStream modelIn = new FileInputStream("/Users/himanshumisra/Downloads/en-token.bin");
		String tokens[]=null;
		try {
			TokenizerModel model = new TokenizerModel(modelIn);
			Tokenizer tokenizer = new TokenizerME(model);
			tokens= tokenizer.tokenize(str);
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		finally {
			if (modelIn != null) {
				try {
					modelIn.close();
				}
				catch (IOException e) {
				}
			}
		}

		return tokens;
	}
	public static String[] tokenize1(String str) throws FileNotFoundException
	{
		String string = "my birthday is on 25th june 2015 and my age is 24himanshu is my name.";
		EnglishTokenizer et=new EnglishTokenizer(new ZipInputStream(new FileInputStream("/Users/himanshumisra/downloads/dictionary-1.4.0.zip")));
		List<String> results=et.getTokens(string);
		for(String result:results)
		{
			System.out.println(result);
		}
		return null;
	}
	public static Double fuzzyCompare(String a, String b)
	{
		return new Double(new Levenshtein().getSimilarity(a, b));
	}

	public static boolean isStopWord(String word)
	{
		ArrayList<String> stopWords=new ArrayList<String>(Arrays.asList("﻿a","able","about","above","abst","accordance","according","accordingly","across","act","actually","added","adj","adopted","affected","affecting","affects","after","afterwards","again","against","ah","all","almost","alone","along","already","also","although","always","am","among","amongst","an","and","announce","another","any","anybody","anyhow","anymore","anyone","anything","anyway","anyways","anywhere","apparently","approximately","are","aren","arent","arise","around","as","aside","ask","asking","at","auth","available","away","awfully","b","back","be","became","because","become","becomes","becoming","been","before","beforehand","begin","beginning","beginnings","begins","behind","being","believe","below","beside","besides","between","beyond","biol","both","brief","briefly","but","by","c","ca","came","can","cannot","can't","cause","causes","certain","certainly","co","com","come","comes","contain","containing","contains","could","couldnt","d","date","did","didn't","different","do","does","doesn't","doing","done","don't","down","downwards","due","during","e","each","ed","edu","effect","eg","eight","eighty","either","else","elsewhere","end","ending","enough","especially","et","et-al","etc","even","ever","every","everybody","everyone","everything","everywhere","ex","except","f","far","few","ff","fifth","first","five","fix","followed","following","follows","for","former","formerly","forth","found","four","from","further","furthermore","g","gave","get","gets","getting","give","given","gives","giving","go","goes","gone","got","gotten","h","had","happens","hardly","has","hasn't","have","haven't","having","he","hed","hence","her","here","hereafter","hereby","herein","heres","hereupon","hers","herself","hes","hi","hid","him","himself","his","hither","home","how","howbeit","however","hundred","i","id","ie","if","i'll","im","immediate","immediately","importance","important","in","inc","indeed","index","information","instead","into","invention","inward","is","isn't","it","itd","it'll","its","itself","i've","j","just","k","keep","keeps","kept","keys","kg","km","know","known","knows","l","largely","last","lately","later","latter","latterly","least","less","lest","let","lets","like","liked","likely","line","little","'ll","look","looking","looks","ltd","m","made","mainly","make","makes","many","may","maybe","me","mean","means","meantime","meanwhile","merely","mg","might","million","miss","ml","more","moreover","most","mostly","mr","mrs","much","mug","must","my","myself","n","na","name","namely","nay","nd","near","nearly","necessarily","necessary","need","needs","neither","never","nevertheless","new","next","nine","ninety","no","nobody","non","none","nonetheless","noone","nor","normally","nos","not","noted","nothing","now","nowhere","o","obtain","obtained","obviously","of","off","often","oh","ok","okay","old","omitted","on","once","one","ones","only","onto","or","ord","other","others","otherwise","ought","our","ours","ourselves","out","outside","over","overall","owing","own","p","page","pages","part","particular","particularly","past","per","perhaps","placed","please","plus","poorly","possible","possibly","potentially","pp","predominantly","present","previously","primarily","probably","promptly","proud","provides","put","q","que","quickly","quite","qv","r","ran","rather","rd","re","readily","really","recent","recently","ref","refs","regarding","regardless","regards","related","relatively","research","respectively","resulted","resulting","results","right","run","s","said","same","saw","say","saying","says","sec","section","see","seeing","seem","seemed","seeming","seems","seen","self","selves","sent","seven","several","shall","she","shed","she'll","shes","should","shouldn't","show","showed","shown","showns","shows","significant","significantly","similar","similarly","since","six","slightly","so","some","somebody","somehow","someone","somethan","something","sometime","sometimes","somewhat","somewhere","soon","sorry","specifically","specified","specify","specifying","state","states","still","stop","strongly","sub","substantially","successfully","such","sufficiently","suggest","sup","sure","t","take","taken","taking","tell","tends","th","than","thank","thanks","thanx","that","that'll","thats","that've","the","their","theirs","them","themselves","then","thence","there","thereafter","thereby","thered","therefore","therein","there'll","thereof","therere","theres","thereto","thereupon","there've","these","they","theyd","they'll","theyre","they've","think","this","those","thou","though","thoughh","thousand","throug","through","throughout","thru","thus","til","tip","to","together","too","took","toward","towards","tried","tries","truly","try","trying","ts","twice","two","u","un","under","unfortunately","unless","unlike","unlikely","until","unto","up","upon","ups","us","use","used","useful","usefully","usefulness","uses","using","usually","v","value","various","'ve","very","via","viz","vol","vols","vs","w","want","wants","was","wasn't","way","we","wed","welcome","we'll","went","were","weren't","we've","what","whatever","what'll","whats","when","whence","whenever","where","whereafter","whereas","whereby","wherein","wheres","whereupon","wherever","whether","which","while","whim","whither","who","whod","whoever","whole","who'll","whom","whomever","whos","whose","why","widely","willing","wish","with","within","without","won't","words","world","would","wouldn't","www","x","y","yes","yet","you","youd","you'll","your","youre","yours","yourself","yourselves","you've","z","zero"));
		if (stopWords.contains(word))
		{
			return true;
		}
		else
		{
			return false;
		}
	}
	public static String removeStopWord(String str)
	{
		str.replaceAll("\\s+", " ");
		String[] words=str.split(" ");
		StringBuilder sb=new StringBuilder();
		for(String word:words)
		{
			if(!isStopWord(word))
			{
				sb.append(word+" ");
			}
		}
		return sb.toString();

	}

	 
    public static String stem(String inputString) throws Throwable {
	

    	String[] words=inputString.split(" ");
    	StringBuilder sb=new StringBuilder();
    	for(int i=0;i<words.length;i++)
    	{
    	Class stemClass = Class.forName("org.tartarus.snowball.ext.englishStemmer");
        SnowballStemmer stemmer = (SnowballStemmer) stemClass.newInstance();
        stemmer.setCurrent(words[i]);
        stemmer.stem();
       	sb.append(stemmer.getCurrent()+" ");
    	}
    	return sb.toString().trim();
    	
    }

	public static String compareAndDisplay(QueryResult1 cr, DataObject obj) throws IOException, ParseException {
		String str="";
		try
		{

			String objectName=obj.getSalutation()+" "+obj.getFname()+" "+ obj.getMname()+" "+obj.getLname();
			str=str+"\""+cr.getName()+ "\", \""+ objectName+"\", "+"\""+getNameSimilarity(cr.getName(),objectName)+"\", ";
			str=str+"\""+cr.getGender()+ "\", \""+ obj.getGender()+"\", "+"\""+getGenderSimilarity(cr.getGender(),obj.getGender())+"\", ";
			str=str+"\""+cr.getDateOfBirth()+"\", \""+ obj.getDob()+"\", "+"\""+getDobSimilarity(cr.getDateOfBirth(),obj.getDob())+"\", ";
			str=str+"\""+ cr.getPlaceOfBirth()+ "\", \""+ obj.getCity()+"\", "+"\""+getCitySimilarity(cr.getPlaceOfBirth(),obj.getCity())+"\", ";
			str=str+"\""+ cr.getAge()+"\", \""+obj.getAge()+"\", "+"\""+getAgeSimilarity(cr.getAge(),obj.getAge())+"\", ";
			str=str+"\""+cr.getServiceInformation()+"\", \""+ obj.getServiceInformation()+"\", "+"\""+getServiceSimilarity(cr.getServiceInformation(),obj.getServiceInformation())+"\", ";
			str=str+"\""+cr.getEducationInformation()+"\", \""+ obj.getEducationInformation()+"\", "+"\""+getServiceSimilarity(cr.getEducationInformation(),obj.getEducationInformation())+"\", ";
			str=str+"\""+cr.getMilitaryInformation()+"\", \""+ obj.getMilitaryInformation()+"\", "+"\""+getServiceSimilarity(cr.getMilitaryInformation(),obj.getMilitaryInformation())+"\", ";


			if(cr.isDateOfService()&& cr.getDatesOfServiceInformation().length()>2)
			{
				str=str+"\""+cr.getDatesOfServiceInformation()+"\", ";
			}
			else
			{
				str=str+"\"\", ";
			}
			if(cr.isTimeOfService() && cr.getTimeOfServiceInformation().length()>2)
			{
				str=str+"\""+cr.getTimeOfServiceInformation()+"\", ";
			}
			else
			{
				str=str+"\"\", ";
			}
			if(cr.isLocationOfService() && cr.getLocationOfServiceInformation().length()>2)
			{
				str=str+"\""+cr.getLocationOfServiceInformation()+"\", ";
			}
			else
			{
				str=str+"\"\", ";
			}

			if(cr.isDateOfDob() && cr.getDateinDOB().length()>2)
			{
				str=str+"\"" + cr.getDateinDOB()+"\", ";
			}
			else
			{
				str=str+"\"\", ";
			}
			str=str+"\n";

		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return str;
	}


	private static String getServiceSimilarity(String serviceInformation,
			String serviceInformation2) throws InvalidFormatException, IOException {

		Double score=0.0;
		String [] origSent=GeneralPurposeFunctions.sentenceTokenizer(serviceInformation2.trim());

		for(String s: origSent)
		{
			if(serviceInformation.contains(s))
			{
				score=score+1;
			}
		}
		if(score>0)
		{
			score = score/origSent.length;
			return (score.toString());
		}
		else
		{
			return fuzzyCompare(serviceInformation, serviceInformation2).toString();
		}
	}
	private static String getAgeSimilarity(int age, String age2) {
		if(age==0 && age2.length()>0)
		{
			return "0";
		}
		else if(age!=0 && age2.length()<1)
		{
			return "1";
		}
		else if(age!=0 && age2.length()>0)
		{
			if(age2.trim().contains(String.valueOf(age)))
			{
				return "1";
			}
			else
			{
				return "0";
			}
		}

		return "0";
	}
	private static String getCitySimilarity(String placeOfBirth, String city) {
		if((city==null||city.equalsIgnoreCase("") && (placeOfBirth==null||placeOfBirth.equalsIgnoreCase(""))))
		{
			return "1";
		}
		else if((city==null||city.equalsIgnoreCase("") && !(placeOfBirth==null||placeOfBirth.equalsIgnoreCase(""))))
		{
			return "1";
		}
		else if ((placeOfBirth==null||placeOfBirth.equalsIgnoreCase("")) && !(city==null||city.equalsIgnoreCase("") ))
		{
			return "0";
		}

		if(city.trim().contains(placeOfBirth.trim()))
		{
			return "1";
		}
		else
		{
			return "0";
		}
	}
	private static String getDobSimilarity(String dateOfBirth, String dob) throws ParseException {
		return checktagDodInObit(dob, dateOfBirth);
	}


	private static String checktagDodInObit(String originalObit, String taggedDate) throws ParseException{
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
		if ( (originalObit==null || originalObit.equalsIgnoreCase(""))&& (taggedDate==null||taggedDate.equalsIgnoreCase("")))
		{
			return "0";
		}
		else if ( (originalObit==null || originalObit.compareToIgnoreCase("")==0)&& taggedDate.length()>0)
		{
			return "1";
		}
		else if((taggedDate==null|| taggedDate.equalsIgnoreCase(""))&&(originalObit!=null && originalObit.length()>0))
		{
			return "0";
		}

		if(taggedDate==null)
			if (taggedDate.trim().length()<4 && originalObit.trim().length()>4) return "0";
		Date d=sdf.parse(taggedDate);
		SimpleDateFormat sdf1=new SimpleDateFormat("MMMM dd, yyyy");
		SimpleDateFormat sdf2=new SimpleDateFormat("MMM dd, yyyy");
		SimpleDateFormat sdf3=new SimpleDateFormat("MM-dd-yyyy");
		SimpleDateFormat sdf4=new SimpleDateFormat("MMMM dd, yyyy,");
		SimpleDateFormat sdf5=new SimpleDateFormat("MMM dd, yyyy,");
		SimpleDateFormat sdf6=new SimpleDateFormat("dd-MM-yyyy,");
		SimpleDateFormat sdf7=new SimpleDateFormat("M/dd/yyyy");
		if (originalObit.contains(taggedDate))
		{
			return "1";
		}
		else if(originalObit.equalsIgnoreCase(sdf1.format(d)))
		{
			return "1";
		}
		else if (originalObit.equalsIgnoreCase(sdf2.format(d)))
		{
			return "1";
		}
		else if (originalObit.equalsIgnoreCase(sdf3.format(d)))
		{
			return "1";
		}
		else if (originalObit.equalsIgnoreCase(sdf4.format(d)))
		{
			return "1";
		}
		else if (originalObit.equalsIgnoreCase(sdf5.format(d)))
		{
			return "1";
		}
		else if (originalObit.equalsIgnoreCase(sdf6.format(d)))
		{
			return "1";
		}
		else if (originalObit.equalsIgnoreCase(sdf7.format(d)))
		{
			return "1";
		}
		else
		{	
			return "0";
		}
	}



	private static String getGenderSimilarity(String gender, String gender2) {
		String [] M={"m","male","Male","M"};
		String [] F={"f", "female", "Female", "F"};
		if((gender2==null || gender2.equalsIgnoreCase("")) &&(gender ==null || gender.equalsIgnoreCase("")) )
		{
			return "1";
		}
		else if ((gender2==null || gender2.equalsIgnoreCase("")) && !(gender ==null || gender.equalsIgnoreCase("")) )
		{
			return "1";
			
		}
		else if ( !(gender2==null || gender2.equalsIgnoreCase("")) && (gender ==null || gender.equalsIgnoreCase("")))
		{
			return "0";
		}
		ArrayList<String> m=new ArrayList<String>(Arrays.asList(M));
		ArrayList<String> f=new ArrayList<String>(Arrays.asList(F));
		if(gender2.trim().length()<1&& gender.trim().length()>1)
		{
			return "0";
		}
		else if (m.contains(gender.trim())&& m.contains(gender2.trim()))
		{
			return "1";
		}
		else if(f.contains(gender.trim())&& f.contains(gender2.trim()))
		{
			return "1";
		}
		else
		{
			return "0";
		}
	}
	private static String getNameSimilarity(String name, String objectName) {
		if((name==null || name.equalsIgnoreCase("")) &&(objectName ==null || objectName.equalsIgnoreCase("")) )
		{
			return "1";
		}
		else if ((name==null || name.equalsIgnoreCase("")) && !(objectName ==null || objectName.equalsIgnoreCase("")) )
		{
			return "1";
			
		}
		else if ( !(name==null || name.equalsIgnoreCase("")) && (objectName ==null || objectName.equalsIgnoreCase("")))
		{
			return "0";
		}
		return GeneralPurposeFunctions.fuzzyCompare(name.trim(), objectName.trim()).toString();

	}
	public static String compareAndDisplay1(QueryResult1 cr, DataObject obj) throws IOException {

		String str="";
		str=str+"---------------------------------------------------------------------------------------------------";
		str=str+" \n NAME - "+ cr.getName();
		str=str+" \n Gender - "+cr.getGender();
		str=str+" \n DATEOFBIRTH - ";
		str=str+" \n CITY - "+ cr.getPlaceOfBirth();
		str=str+" \nAGE - "+ cr.getAge();
		str=str+" \nService Information -"+cr.getServiceInformation();


		if(cr.isDateOfService()&& cr.getDatesOfServiceInformation().length()>2)
		{
			str=str+" \n"+cr.getDatesOfServiceInformation()+"\n";
		}
		if(cr.isTimeOfService() && cr.getTimeOfServiceInformation().length()>2)
		{
			str=str+" \n"+cr.getTimeOfServiceInformation()+"\n";
		}
		if(cr.isLocationOfService() && cr.getLocationOfServiceInformation().length()>2)
		{
			str=str+" \n"+cr.getLocationOfServiceInformation()+"\n";
		}

		str=str+" \n"+"Education Information - "+ cr.getEducationInformation();
		str=str+" \n"+"Military Information - "+ cr.getMilitaryInformation();
		if(cr.isDateOfDob() && cr.getDateinDOB().length()>2)
		{
			str=str+"\nProbable Date of DOB : " + cr.getDateinDOB()+"\n";
		}
		str=str+"---------------------------------------------------------------------------------------------------";

		return str;
	}

	public static ArrayList<QueryResult1> getSimilarName(ArrayList<QueryResult1> resultSet) 
	{
		ArrayList<QueryResult1> newResult=new ArrayList<QueryResult1>();
		HashSet<String> tempHash=new HashSet<String>();
		for(int i=0;i<resultSet.size()-1;i++)
		{
			for (int j=i+1;j<resultSet.size();j++)
			{
				if (GeneralPurposeFunctions.fuzzyCompare(resultSet.get(i).getName(), resultSet.get(j).getName())>0.65)
				{
					if(!tempHash.contains(resultSet.get(i).getName()))
					{
						QueryResult1 newres=new QueryResult1();
						newres.setName(resultSet.get(i).getName());
						newres.setInstanceCount(resultSet.get(i).getInstanceCount());
						newres.setUri(resultSet.get(i).getUri());
						newResult.add(newres);
						tempHash.add(newres.getName());
						System.out.println(resultSet.get(i).getName());
						System.out.println(resultSet.get(i).getInstanceCount());
					}
					if(!tempHash.contains(resultSet.get(j).getName()))
					{
						QueryResult1 newres=new QueryResult1();
						newres.setName(resultSet.get(j).getName());
						newres.setInstanceCount(resultSet.get(j).getInstanceCount());
						newres.setUri(resultSet.get(j).getUri());
						newResult.add(newres);
						tempHash.add(newres.getName());
						System.out.println(resultSet.get(j).getName());
						System.out.println(resultSet.get(j).getInstanceCount());
					}

				}
			}
		}

		if(newResult.size()==0 && resultSet.size()>0)
		{

			QueryResult1 qr=new QueryResult1();
			qr.setName(resultSet.get(0).getName());
			qr.setInstanceCount(resultSet.get(0).getInstanceCount());
			qr.setUri(resultSet.get(0).getUri());
			newResult.add(qr);
			System.out.println("No Similar Names : All different");
			return newResult;
		}
		else
		{
			return newResult;
		}


	}

}
