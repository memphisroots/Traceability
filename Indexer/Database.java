package Indexer;

import java.sql.*;
//import java.sql.Connection;
//import java.sql.DriverManager;
//import java.sql.SQLException;
//import java.sql.Statement;
//import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;


public class Database {
	private final String dbName = "traceability";
	private final String password = "ab1234";
	private final String userName = "root";
	private final String ip = "localhost:3306";
	private final String driver = "com.mysql.jdbc.Driver";
	//	private final String url = ip;
	private final String url = "jdbc:mysql://" +ip + "/";

	private Connection conn = null;

	private final String addTokenQuery = "INSERT INTO token (data) VALUES ('%s'); "; // Data
	private final String addLinkQuery = "INSERT INTO link (TokenID,DocID, Quantity, SourceType) VALUES ((SELECT ID FROM token WHERE data='%s'),(SELECT ID FROM document WHERE path='%s') ,'%s', '%s');";
	private final String addDocQuery = "INSERT INTO document(path) VALUES ('%s');";

	//singleton code
	private static Database db = null;

	//getInstance() is how you access the Database
	public static Database getInstance(){
		if(db==null){
			db = new Database();
			return db;
		}
		else
		{
			return db;
		}

	}


	// constructor
	protected Database(){
		openConnect();

	}


	public void buildQuery(){

	} 

	private void submitQuery(String statement){
		try{
//		System.out.println(statement);
		Statement stmt = conn.createStatement();
        int rs = stmt.executeUpdate(statement);
		}
		catch(Exception e){
			System.out.println(e);
		}


	}

	public void storeTokens(TokenTracker tt, String docName){
		submitQuery(String.format(addDocQuery,docName));
		
		Set<String> codeTokens = tt.getCodeKeys();
		Set<String> commentTokens = tt.getCommentKeys();

//		HashMap<String, Boolean> tokenStored = new HashMap<String, Boolean>();

		Iterator<String> codeIterator = codeTokens.iterator();
		Iterator<String> commentIterator = commentTokens.iterator(); 

//		StringBuffer queryBuffer = new StringBuffer();

//		String eachTokenInsertQuery;
		String eachToken;
		String eachTokenCount;
//		String eachLinkInsertQuery;

		while(codeIterator.hasNext()){
			eachToken = codeIterator.next();
			eachTokenCount = Integer.toString(tt.getCodeTokCount(eachToken));

			submitQuery(String.format(addTokenQuery, eachToken));
			submitQuery(String.format(addLinkQuery, eachToken,docName,eachTokenCount,"CODE"));
//			eachTokenInsertQuery = String.format(addTokenQuery, eachToken);
//			queryBuffer.append(eachTokenInsertQuery);

//			eachLinkInsertQuery = String.format(addLinkQuery, eachToken,docName,eachTokenCount,"CODE");
//			queryBuffer.append(eachLinkInsertQuery);
			
//			System.out.println(eachTokenInsertQuery.toString());
//			submitQuery(eachTokenInsertQuery.toString());
//			submitQuery(eachLinkInsertQuery.toString());
//			tokenStored.put(eachToken,true);

		}

		while(commentIterator.hasNext()){
			
			eachToken = commentIterator.next();
			eachTokenCount = Integer.toString(tt.getCommentTokCount(eachToken));

			submitQuery(String.format(addTokenQuery, eachToken));
			submitQuery(String.format(addLinkQuery, eachToken,docName,eachTokenCount,"COMMENT"));
//			if(!tokenStored.containsKey(eachToken)){
//				eachTokenInsertQuery = String.format(addTokenQuery, commentIterator.next());
//				System.out.println(eachTokenInsertQuery.toString());
//				submitQuery(eachTokenInsertQuery.toString());
////				queryBuffer.append(eachTokenInsertQuery);
//			}

//			eachLinkInsertQuery = String.format(addLinkQuery, eachToken,docName,eachTokenCount,"COMMENT");
//			queryBuffer.append(eachLinkInsertQuery);
			
			
			
		}
		//once its done building it submits the Query

	}



	//-------------------------------------------------------------

	public void openConnect(){
		try {
			Class.forName(driver).newInstance();
			conn = DriverManager.getConnection(url+dbName,userName,password);
			// Do something with the Connection

		} 
		catch (Exception e) {
			System.out.println("Connection to database failed");
			System.out.println(e.toString());
			// handle any errors

		}

	}


	public void closeConnect(){
		if(db!=null){
			try{
				conn.close();
				db = null;

			}
			catch(Exception e){

			}
		}
	}
}
