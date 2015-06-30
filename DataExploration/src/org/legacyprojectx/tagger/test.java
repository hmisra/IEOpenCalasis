package org.legacyprojectx.tagger;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import edu.mit.jwi.Dictionary;
import edu.mit.jwi.item.POS;
import edu.mit.jwi.morph.WordnetStemmer;

import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.PostMethod;

public class test {
public static void main(String[] args) throws IOException {
	String str="([A-Z][a-z]+)([A-Z][a-z]+)";
	
	String input="GillardAugust 15,1931 - June 11,2015.FactsBorn: August 15, 1931Death: June 11, 2015    BiographyAimee ";
	System.out.println(input.replaceAll("([A-Z][a-z]+)([A-Z][a-z]+)", "$1. $2").replaceAll("([0-9]+)([A-Z][a-z]+)", "$1. $2").replaceAll("([,|.|;|-|:])([A-Z a-z 0-9]+)", "$1 $2").replaceAll("([\\s])+", "$1"));
	Dictionary dict = new Dictionary(new File("/Users/himanshumisra/Downloads/dict"));
	dict.open();
	WordnetStemmer ws=new WordnetStemmer(dict);
	System.out.println(ws.findStems("preparation",POS.VERB));

}
}
