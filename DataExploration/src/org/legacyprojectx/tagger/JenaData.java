package org.legacyprojectx.tagger;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import org.legacyprojectx.datastructures.QueryResult1;

import com.hp.hpl.jena.query.DatasetAccessor;
import com.hp.hpl.jena.query.DatasetAccessorFactory;
import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.rdf.model.Literal;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.update.UpdateExecutionFactory;
import com.hp.hpl.jena.update.UpdateProcessor;
import com.hp.hpl.jena.update.UpdateRequest;
public class JenaData {
	public static ArrayList<QueryResult1> findPersonWithMaxMentions()
	{
		String queryString=	"prefix c:     <http://s.opencalais.com/1/pred/>"+
				" prefix rdf:   <http://www.w3.org/1999/02/22-rdf-syntax-ns#>"+
				" SELECT ?name ?x (count(?mention) as ?count)"+
				" WHERE {"+
				"  ?x a <http://s.opencalais.com/1/type/em/e/Person>."+
				"  ?y a <http://s.opencalais.com/1/type/sys/InstanceInfo>."+
				"  ?y c:subject ?x."+
				"  ?y c:detection ?mention."+
				" ?x c:name ?name."+
				" } "+
				" GROUP BY ?name ?x "+
				" ORDER BY DESC(?count)"+
				" LIMIT 5";
		Query query = QueryFactory.create(queryString) ;
		QueryExecution qexec = QueryExecutionFactory.sparqlService("http://127.0.0.1:3030/legacy/query", query);
		ResultSet results = qexec.execSelect() ;

		ArrayList<QueryResult1> resultSet=new ArrayList<QueryResult1>();
		try
		{
			for ( ; results.hasNext() ; )
			{
				QuerySolution soln = results.nextSolution() ;
				RDFNode x = soln.get("name") ;       // Get a result variable by name.
				RDFNode y = soln.get("count") ;  
				RDFNode z = soln.get("x") ;
				QueryResult1 result=new QueryResult1();
				result.setName(x.toString());
				result.setInstanceCount(y.asLiteral().getInt());
				result.setUri(z.toString());
				resultSet.add(result);
			}
		}
		catch(Exception e)
		{
			System.out.println("Error");
		}
		finally
		{
			qexec.close();
		}


		return resultSet;

	}

	private static ArrayList<QueryResult1> findAllAttributes(
			ArrayList<QueryResult1> finalResult1) {
		for(QueryResult1 qr: finalResult1)
		{
			String queryString=" prefix c:     <http://s.opencalais.com/1/pred/>"+
					" prefix rdf:   <http://www.w3.org/1999/02/22-rdf-syntax-ns#>"+
					" SELECT ?name ?age ?gender ?birthdate ?birthplace"+
					" WHERE {"+
					" ?x a <http://s.opencalais.com/1/type/em/r/PersonAttributes>."+
					" ?x c:person <"+qr.getUri()+">."+
					" optional {?x c:age ?age}"+
					" optional {?x c:gender ?gender}"+
					" optional {?x c:birthplace ?birthplace}"+
					" optional {?x c:birthdate ?birthdate}"+
					" }";


			Query query = QueryFactory.create(queryString) ;
			QueryExecution qexec = QueryExecutionFactory.sparqlService("http://127.0.0.1:3030/legacy/query", query);
			ResultSet results = qexec.execSelect();
			ArrayList<QueryResult1> resultSet=new ArrayList<QueryResult1>();
			try
			{
				for ( ; results.hasNext() ; )
				{
					QuerySolution soln = results.nextSolution() ;
					RDFNode x = soln.get("age") ;
					RDFNode y=soln.get("gender");
					RDFNode z=soln.get("birthplace");
					RDFNode a=soln.get("birthdate");
					if(x!=null)
					{
						finalResult1.get(0).setAge(x.asLiteral().getInt());
					}
					if(y!=null)
					{
						finalResult1.get(0).setGender(y.toString());
					}
					
					if(z!=null)
					{
						finalResult1.get(0).setPlaceOfBirth(z.toString());
					}
					if(a!=null)
					{
						finalResult1.get(0).setDateOfBirth(a.toString());
					}
				}
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
		}
		return finalResult1;
	}

	private static ArrayList<QueryResult1> findAllInstances(
			ArrayList<QueryResult1> deceasedPerson) {
		for(QueryResult1 qr: deceasedPerson)
		{
			String queryString=	"prefix c:     <http://s.opencalais.com/1/pred/>"+
					"prefix rdf:   <http://www.w3.org/1999/02/22-rdf-syntax-ns#>"+
					"SELECT ?instance"+
					" WHERE {"+
					" ?x a <http://s.opencalais.com/1/type/sys/InstanceInfo>."+
					" ?x c:subject <"+qr.getUri()+">."+
					" ?x c:detection ?instance."+
					" }";
			Query query = QueryFactory.create(queryString) ;
			QueryExecution qexec = QueryExecutionFactory.sparqlService("http://127.0.0.1:3030/legacy/query", query);
			ResultSet results = qexec.execSelect() ;

			ArrayList<QueryResult1> resultSet=new ArrayList<QueryResult1>();
			try
			{
				for ( ; results.hasNext() ; )
				{
					QuerySolution soln = results.nextSolution() ;
					RDFNode x = soln.get("instance") ;       // Get a result variable by name.
					deceasedPerson.get(0).setInstances(x.toString().replaceAll("[\\[]", "").replaceAll("[\\]]", " "));
				}
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
		}
		return deceasedPerson;
	}

	public static void deleteAllTriples()
	{
		UpdateRequest ur=new UpdateRequest();
		ur.add("DROP ALL");
		UpdateProcessor up=UpdateExecutionFactory.createRemote(ur, "http://127.0.0.1:3030/legacy/update");
		up.execute();
	}
	public static void storeData(String result) throws FileNotFoundException {
		try
		{
			String serviceURI = "http://127.0.0.1:3030/legacy/data";
			//TODO storing string in a temp file just because ByteArrayInputStream is not working.
			//TODO find a way to store the string directly in the jena store
			DatasetAccessor accessor;
			accessor = DatasetAccessorFactory.createHTTP(serviceURI);
			Model m = ModelFactory.createDefaultModel();
			BufferedWriter bw=new BufferedWriter(new FileWriter(new File("./tempDataFile")));
			bw.write(result);
			bw.close();
			InputStream in=new FileInputStream("./tempDataFile");
			String base = "http://www.legacy.com/";
			m.read(in, base, "N3");
			in.close();
			accessor.putModel(m);
		}

		catch(Exception e){
			System.out.println(e.getMessage());

		}

	}

	public static QueryResult1 getResults()
	{
		ArrayList<QueryResult1> resultSet=findPersonWithMaxMentions();
		System.out.println(resultSet.size());
		ArrayList<QueryResult1> deceasedPerson=GeneralPurposeFunctions.getSimilarName(resultSet);
		ArrayList<QueryResult1> finalResult1=findAllInstances(deceasedPerson);
		ArrayList<QueryResult1> finalResult2=findAllAttributes(finalResult1);
		if(finalResult2.size()>0)
		{
		return finalResult2.get(0);
		}
		else {
			return null;
		}
	}

}
