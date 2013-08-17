package test;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import constents.DBConst;


public class testDB {
    public static Connection getConnection() {  
        Connection conn = null;  
/*
    	String driver = DBConst.DBDRIVER;
    	String url = DBConst.getTableUrl();

    	String user = DBConst.DBUSER;	
    	String password = DBConst.DBPWD;
    	
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
		*/
        return null;
    }
    
    static public void main(String args[]) {
    	try {
    		Connection conn = getConnection();
    		
    		String table = "STUDENT", key = "SNAME", value = "klh0", id = "SNO", idvalue = "001";   		
    		
    		String sql0 = "INSERT INTO " + table + " VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?)";
    		
    		PreparedStatement statement = conn.prepareStatement(sql0);
    		for (int i = 0; i < 3; i ++) 
    			for (int j = 1; j <= 5; j++)
 //   				statement.setString
    		statement.executeUpdate(sql0);
    		
    		String sql = "select * from STUDENT";
    		
    		ResultSet rs = statement.executeQuery(sql);  
    		System.out.println("-----------------");  
    		System.out.println("The result is:");  
    		System.out.println("-----------------");  
    		System.out.println(" No." + "\t" + " Name");  
    		System.out.println("-----------------");  
    		String name = null;  
    		while(rs.next()) {

    			name = rs.getString("sname");
    			name = new String(name.getBytes("ISO-8859-1"),"GB2312");

    			System.out.println(rs.getString("sno") + "\t" + name);  
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
