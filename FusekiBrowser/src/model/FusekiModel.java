package model;

import java.util.ArrayList;
import java.util.Stack;

import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;

public class FusekiModel {

	private String url;
	private Stack<String> backList;
	
	public FusekiModel()
	{
		
	}
	
	// Connect to db
	public boolean connect(String storename)
	{
		backList = new Stack<String>();	// Always reset stack when opening new connection		
		
		// TODO add check if connection exists & url is valid
		url = storename;	//"http://dbpedia.org/sparql";
		
		// hardcoded URL for testing
		// url = "http://localhost:3030/russia/query";
		return true;
	}
	
	public ArrayList<Object[]> execQuery(String query)
	{
		if (query == null)
		{
			query = "SELECT * WHERE {?x ?r ?y} LIMIT 5000";	// Default query & max
		}
		addToStack(query);
		
		ArrayList<Object[]> data = new ArrayList<Object[]>();
		
		QueryExecution qe = QueryExecutionFactory.sparqlService(url, query);
        ResultSet results = qe.execSelect();
		
        for ( ; results.hasNext() ; )	// Loop resultset
        {
          QuerySolution soln = results.nextSolution() ;
          
          String subject = soln.get("?x").toString();       // Get a result variable by name.
          String predicate = soln.get("?r").toString();       // Get a result variable by name.
          String object = soln.get("?y").toString();       // Get a result variable by name.          
          
          Object[] objs = { subject, predicate, object };
          data.add(objs);
        }

        qe.close();        
        return data;
	}
	
	// Double pop as current query will be re-added
	public ArrayList<Object[]> goBackInStack()
	{
		backList.pop();
		String query = backList.pop();
		return execQuery(query);
	}
	
	private void addToStack(String query)
	{
		backList.add(query);
	}
	
	// Can't go back if there's only 1 item left in stack
	public boolean isStackEmpty()
	{
		if (backList.size() == 1)
		{
			return true;
		}
		return false;
	}
}
