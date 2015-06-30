package org.legacy.tagger;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import com.hp.hpl.jena.query.DatasetAccessor;
import com.hp.hpl.jena.query.DatasetAccessorFactory;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
public class JenaData {
	public static void storeData() throws FileNotFoundException {
		try
		{
			String serviceURI = "http://127.0.0.1:3030/temp/data";
			
			String directoryPath="./output";
			int fileCount=0;
			File dir = new File(directoryPath);
			if (dir.isDirectory()) { 
				DatasetAccessor accessor;
				accessor = DatasetAccessorFactory.createHTTP(serviceURI);
				Model m = ModelFactory.createDefaultModel();
				for (final File f : dir.listFiles()) {

					if(f.getName().charAt(0)!='.')
					{
					InputStream in=new FileInputStream(f.getAbsolutePath());
					
					String base = "http://www.legacy.com/";
					m.read(in, base, "N3");
					
					in.close();
					fileCount++;
					}
				}
				accessor.putModel(m);
				
			}
			System.out.println("Data from "+fileCount+" files uploaded successfuly.");
		}
		catch(Exception e){
			System.out.println(e.getMessage());

		}

	}
}
