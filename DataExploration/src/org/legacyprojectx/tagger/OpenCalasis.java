package org.legacyprojectx.tagger;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.legacyprojectx.datastructures.CalasisResult;
import org.legacyprojectx.datastructures.QueryResult1;
public class OpenCalasis {
	private static final String CALAIS_URL = "https://api.thomsonreuters.com/permid/calais";
	public static String uniqueAccessKey="vuN1l1hrEFmRA27EyNP79dzG4iJOESPP";		
	private String input;
	private HttpClient client;
	private PostMethod createPostMethod() {
		PostMethod method = new PostMethod(CALAIS_URL);

		// Set mandatory parameters
		method.setRequestHeader("X-AG-Access-Token", uniqueAccessKey);
		// Set input content type
		method.setRequestHeader("Content-Type", "text/raw");
		// Set response/output format
		method.setRequestHeader("outputformat", "text/n3");
		return method;
	}
	private QueryResult1 run() {
		try {
				return postFile(input, createPostMethod());
		
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	private QueryResult1 doRequest(String file, PostMethod method) {
		try {
			int returnCode = client.executeMethod(method);
			if (returnCode == HttpStatus.SC_NOT_IMPLEMENTED) {
				System.err.println("The Post method is not implemented by this URI");
				// still consume the response body
				method.getResponseBodyAsString();
				
			} else if (returnCode == HttpStatus.SC_OK) {
				System.out.println("File post succeeded: " + file);
				return saveResponse(file, method);
			} else {
				System.err.println("File post failed: " + file);
				System.err.println("Got code: " + returnCode);
				System.err.println("response: "+method.getResponseBodyAsString());
				
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			method.releaseConnection();
		}
		return null;
	}
	private QueryResult1 saveResponse(String file, PostMethod method) throws IOException {
		PrintWriter writer = null;
		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					method.getResponseBodyAsStream(), "UTF-8"));
			String line;
			String result="";
			while ((line = reader.readLine()) != null) {
				result=result+line+"\n";	
			}
			//Jena store data and query results
			JenaData.storeData(result);
			return JenaData.getResults();
				
		} finally {
			if (writer != null) try {writer.close();} catch (Exception ignored) {}
		}
	}
	@SuppressWarnings("deprecation")
	private QueryResult1 postFile(String content, PostMethod method) throws IOException {
		if(content!=null)
		{
		method.setRequestEntity(new StringRequestEntity(content));
		return doRequest(content, method);
		}
		else
		{
			return null;
		}
	}
	public static QueryResult1 callCalasis(String input) throws IOException {
		JenaData.deleteAllTriples();
		OpenCalasis httpClientPost = new OpenCalasis();
		httpClientPost.input= input;
		httpClientPost.client = new HttpClient();
		httpClientPost.client.getParams().setParameter("http.useragent", "Calais Rest Client");
		QueryResult1 q=httpClientPost.run();
		JenaData.deleteAllTriples();
		//TODO
		return q;
	}
}

