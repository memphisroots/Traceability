package Indexer;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

public class Database {
	private final String dbName = "Traceability";
	private final String password = "vExKFHJpxy2XPUvt";
	private final String userName = "trace";
	private final String ip = "96.63.247.168:3306";
	private final String driver = "com.mysql.jdbc.Driver";
	private final String url = "jdbc:mysql://" +ip ;

	private Connection conn = null;

	private final String addTokenQuery = "INSERT INTO token (column2) VALUES ('%s') "; // Data
	private final String addLinkQuery = "INSERT INTO link (TokenID,DocID) VALUES ((SELECT ID FROM token WHERE Data='%s'),(SELECT ID FROM document WHERE Path='%s') )";
	private final String addDocQuery = "INSERT INTO document(path) VALUES ('%s')";
	public void buildQuery(){

	} 

	private void submitQuery(){

	}

	public void storeTokens(TokenTracker tt){
		Set<String> codeTokens = tt.getCodeKeys();
		Set<String> commentTokens = tt.getCommentKeys();

		HashMap<String, Boolean> tokenStored = new HashMap<String, Boolean>();

		Iterator<String> codeIterator = codeTokens.iterator();
		Iterator<String> commentIterator = commentTokens.iterator(); 

		StringBuffer queryBuffer = new StringBuffer();
		
		String eachQuery;
		String eachToken;
		
		while(codeIterator.hasNext()){
			eachToken = codeIterator.next();
			eachQuery = String.format(addTokenQuery, codeIterator.next());
			queryBuffer.append(eachQuery);
			tokenStored.put(eachToken,true);
			////    		queryBuffer.append(String.format(addTokenQuery,codeIterator.next()));
			
		}

		while(commentIterator.hasNext()){
			eachToken = codeIterator.next();
			if(!tokenStored.get(eachToken)){
				eachQuery = String.format(addTokenQuery, commentIterator.next());
				queryBuffer.append(eachQuery);
			}
			
		}
	}
	public void storeToken(String token, int quantity, String SourceType){

	}


	//-------------------------------------------------------------

	public void openConnect(){
		try {
			Class.forName(driver).newInstance();
			conn = DriverManager.getConnection(url+dbName,userName,password);

			// Do something with the Connection

		} 
		catch (SQLException | InstantiationException | IllegalAccessException | ClassNotFoundException ex) {
			// handle any errors
			System.out.println("SQLException: " + ex.getMessage());
			System.out.println("SQLState: " + ((SQLException) ex).getSQLState());
			System.out.println("VendorError: " + ((SQLException) ex).getErrorCode());   
		}

	}

	public void closeConnect(){
		try{
			conn.close();
		}
		catch(Exception e){

		}
	}
}