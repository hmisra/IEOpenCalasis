import java.io.IOException;

import org.json.simple.JSONArray;
import org.json.simple.parser.ParseException;

import java.nio.file.Files;
import java.nio.file.Paths;
public class JSONParser {
	
	   @SuppressWarnings("unchecked")
	public static void main(String[] args) throws IOException 
	   {
		  org.json.simple.parser.JSONParser parser=new org.json.simple.parser.JSONParser();
	      String s = new String(Files.readAllBytes(Paths.get("obit1.xml")));
	      try{
	         Object obj = parser.parse(s);
	         JSONArray array = new JSONArray();
	         array.add(obj);
	         System.out.println(array.get(0));
	      }catch(ParseException pe){
	         System.out.println("position: " + pe.getPosition());
	         System.out.println(pe);
	      }
	   }
	}

