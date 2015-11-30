package model;

import java.util.ArrayList;

import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;

public class FusekiModel {

	private String url;
	
	public FusekiModel()
	{
		
	}
	
	// Connect to db
	public boolean connect(String storename)
	{
		// TODO add check if connection exists
		url = "http://localhost:3030/russia/query";
		
		
		return true;
	}
	
	public ArrayList<Object[]> execQuery(String query)
	{
		if (query == null)
		{
			query = "SELECT * WHERE {?x ?r ?y} LIMIT 5000";
		}
		
		ArrayList<Object[]> data = new ArrayList<Object[]>();
		
		QueryExecution qe = QueryExecutionFactory.sparqlService(url, query);
        ResultSet results = qe.execSelect();
		
        for ( ; results.hasNext() ; )	// Loop over resultset
        {
          QuerySolution soln = results.nextSolution() ;
          
          String subject = soln.get("?x").toString();       // Get a result variable by name.
          String predicate = soln.get("?r").toString();       // Get a result variable by name.
          String object = soln.get("?y").toString();       // Get a result variable by name.
          
          Object[] objs = { subject, predicate, object };
          data.add(objs);
          //tableModel.addRow(objs);
        }

        qe.close();
        return data;
	}
}
