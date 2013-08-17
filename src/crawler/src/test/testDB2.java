package test;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import constents.DBConst;


public class testDB2 {
	
    public static Connection getConnection() {  
        Connection conn = null;  

    	String driver = DBConst.DRIVER;
    	String url = DBConst.getSchemaUrl();

    	String user = DBConst.USER;	
    	String password = DBConst.PWD;
    	try {
    		Class.forName(driver);
    		conn = DriverManager.getConnection(url, user, password);

    		if(!conn.isClosed())
    			System.out.println("Succeeded connecting to the Database!");
    		return conn;
    	} catch(ClassNotFoundException e) {   
    		System.out.println("Sorry,can`t find the Driver!");   
    		e.printStackTrace();   
    	} catch (SQLException e) {
    		// TODO Auto-generated catch block
    		e.printStackTrace();
    	}
		return conn; 
    }
    
    static public void main(String args[]) {
    	try {
    		Connection conn = getConnection();
    		
    		String table = DBConst.ACTURLS;   		
    		
 /*   		String sql0 = "REPLACE INTO " + table + " VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?)";
    		
    		PreparedStatement statement = conn.prepareStatement(sql0);

    		for (int i = 0; i < 3; i ++) {
    			statement.setInt(1, i + 1);
    			for (int j = 1; j < 9; j++)
    				statement.setString(j + 1, "test" + j);
    			statement.executeUpdate();
    		}
    		
*/    		
    		String sql = "select * from " + table;
    		PreparedStatement statement = conn.prepareStatement(sql);
    		ResultSet rs = statement.executeQuery();  
    		System.out.println("-----------------");  
    		System.out.println("The result is:");  
    		System.out.println("-----------------");  
    		System.out.println(" No." + "\t" + " Name");  
    		System.out.println("-----------------");  
    		String name = null;  
    		while(rs.next()) {

    			name = rs.getString("url");
    			name = new String(name.getBytes("ISO-8859-1"),"GB2312");

    			System.out.println(rs.getString("activity") + "\t" + name);  
    		}  
    		rs.close();  
    		conn.close();   
    		} catch(SQLException e) {   
    			e.printStackTrace();   
    		} catch(Exception e) {   
    			e.printStackTrace();   
    		}   
    		}    
    
}
