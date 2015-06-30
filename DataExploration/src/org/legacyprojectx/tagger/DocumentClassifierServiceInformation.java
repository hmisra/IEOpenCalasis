package org.legacyprojectx.tagger;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import opennlp.tools.doccat.DoccatModel;
import opennlp.tools.doccat.DocumentCategorizerME;
import opennlp.tools.doccat.DocumentSample;
import opennlp.tools.doccat.DocumentSampleStream;
import opennlp.tools.util.InvalidFormatException;
import opennlp.tools.util.ObjectStream;
import opennlp.tools.util.PlainTextByLineStream;

public class DocumentClassifierServiceInformation {
	public static String getCategory(String inputText) throws InvalidFormatException, IOException {
		InputStream is = new FileInputStream(new File("/Users/himanshumisra/Desktop/SIDC-en.bin"));
		DoccatModel m = new DoccatModel(is);
		DocumentCategorizerME myCategorizer = new DocumentCategorizerME(m);
		double[] outcomes = myCategorizer.categorize(GeneralPurposeFunctions.removeStopWord(inputText));
		String category = myCategorizer.getBestCategory(outcomes);
		return category;
	}
	public static Double getProb(String inputText, String tag) throws InvalidFormatException, IOException {
		InputStream is = new FileInputStream(new File("/Users/himanshumisra/Desktop/SIDC-en.bin"));
		DoccatModel m = new DoccatModel(is);
		DocumentCategorizerME myCategorizer = new DocumentCategorizerME(m);
		double[] outcomes = myCategorizer.categorize(GeneralPurposeFunctions.removeStopWord(inputText));
		String allres=myCategorizer.getAllResults(outcomes);
		String[] allCat=allres.split("  ");
		for(String str:allCat)
		{
			String classtag=str.substring(0, str.indexOf('['));

			if(classtag.equalsIgnoreCase(tag))
			{
				return Double.parseDouble(str.substring(str.indexOf('[')+1, str.indexOf(']')));
			}
		}
		return null;
	}
	public static Double checkForWords(String sentence, String tag)
	{

		if(tag.equalsIgnoreCase("service"))
		{
			String [] words={"visitation","funeral services","funeral service","interment", "memorial services","memorial service", "rosary", "shiva","graveside services","graveside service", "funeral mass", "funeral procession","wake services","wake service", "gathering", "mass of christian burial", "rite of christian burial", "burial","inumment", "entombment", "vigil", "calling hours", "prayer service", "life celebration", "celebration of life", "online webcast", "prayer of final commendation","funeral","service"};
			for(String word:words)
			{
				if(sentence.toLowerCase().contains(word))
				{
					return 1.0;
				}

			}
			return 0.0;
		}
		else if(tag.equalsIgnoreCase("education"))
		{
			String [] words={" school "," graduated ", " college ", " studied ", " graduate ", " student "};
			for(String word:words)
			{
				if(sentence.toLowerCase().contains(word))
				{
					return 1.0;
				}

			}
			return 0.0;
		}
		else if(tag.equalsIgnoreCase("military"))
		{
			String [] words={" navy ", " army ", "veteran"};
			for(String word:words)
			{
				if(sentence.toLowerCase().contains(word))
				{
					return 1.0;
				}

			}
			return 0.0;
		}
		else if(tag.equalsIgnoreCase("dob"))
		{
			String [] words={"born"};
			for(String word:words)
			{
				if(sentence.toLowerCase().contains(word))
				{
					return 1.0;
				}

			}
			return 0.0;
		}
		else if(tag.equalsIgnoreCase("age"))
		{
			String [] words={"age"};
			
			for(String word:words)
			{
				if(sentence.toLowerCase().contains(word))
				{
					return 1.0;
				}

			}
			return 0.0;
		}
		else
		{
			return 0.0;
		}

	}
	public static void trainModel(String trainFilePath, String outputModelFilePath)
	{

		//model training with service information
		DoccatModel model = null;

		InputStream dataIn = null;
		try {
			dataIn = new FileInputStream(new File(trainFilePath));
			ObjectStream<String> lineStream =
					new PlainTextByLineStream(dataIn, "UTF-8");
			ObjectStream<DocumentSample> sampleStream = new DocumentSampleStream(lineStream);

			model = DocumentCategorizerME.train("en", sampleStream);
		}
		catch (IOException e) {
			// Failed to read or parse training data, training failed
			e.printStackTrace();
		}
		finally {
			if (dataIn != null) {
				try {
					dataIn.close();
				}
				catch (IOException e) {
					// Not an issue, training already finished.
					// The exception should be logged and investigated
					// if part of a production system.
					e.printStackTrace();
				}
			}
		}

		//store the training file in disk
		OutputStream modelOut = null;
		try {
			modelOut = new BufferedOutputStream(new FileOutputStream(new File(outputModelFilePath)));
			model.serialize(modelOut);
		}
		catch (IOException e) {
			// Failed to save model
			e.printStackTrace();
		}
		finally {
			if (modelOut != null) {
				try {
					modelOut.close();
				}
				catch (IOException e) {
					// Failed to correctly save model.
					// Written model might be invalid.
					e.printStackTrace();
				}
			}
		}
	}

}
