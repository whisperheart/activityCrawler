package access;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.sql.Blob;
import java.sql.Connection; 
import java.sql.DriverManager; 
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException; 
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;

import constents.ActConst;
import constents.DBConst;

public class DBAccess extends AbstractAccess{
      
	protected static Connection conn;
	protected static PreparedStatement st;
	
	public Collection fetchActUrls(int website) {
		String urlsTable = DBConst.getActUrlsTable(website);
		return query(urlsTable, "url", null); 
	}
	
	public void saveActList(String[][] actContentList) {
		replace(null, actContentList);
		for(int i = 0; i < actContentList.length; i++){
			 saveImage(actContentList[i][7], actContentList[i][0], true);
		 }
	}
	
	public void saveAct(String[] actContent) {
		replace(null, actContent);
	 }
	
	public void saveImage(String imgUrl, String p, boolean http) {
		saveUrlFile(imgUrl, String.valueOf(p), http);
	}
	
	public void saveUrl(int website, int p, String url) {
		insert(DBConst.getActUrlsTable(website), p, url);
	}
	
	public void saveUrlList(int website, String[] urls) {
		for (int i = 0; i < urls.length; i++) {
			saveUrl(website, i, urls[i]);
		}
	}
	
	private InputStream getInputStream(String httpUrl) throws IOException {

	        URL url = new URL(httpUrl);
	        URLConnection uc = url.openConnection();
	        uc.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");
	        return uc.getInputStream();
	    }
	
	public void saveUrlFile(String url, String p, boolean http) {
		InputStream input = null;
		try {
	  	if (http) 
	  		input = getInputStream(url);
	  	else 
	  		input = new FileInputStream(new File(url));
		} catch (IOException e) {
			log("Act " + url , "fetch poster fails.");
			e.printStackTrace();
		}
		replace(p, new BufferedInputStream(input));  	
	}
    	
	public String[] fetchAct(String activity) {
		Collection list = query(DBConst.ACTTABLE, null, activity);
		String[] content = (String[])list.toArray();
		return content;    
	}	

	public String[][] fetchActList(){
		Collection list = query(DBConst.ACTTABLE, null, null);
		Object[] arr = list.toArray();
		String[][] contentList = new String[arr.length][];
		for (int i = 0; i < arr.length; i++) 
			contentList[i] = (String[]) arr[i];		
		return contentList;
	}
	
	public byte[] fetchImage(String activity){
		Collection list = query(DBConst.POSTERTABLE, "image", activity);
		byte[] imageStream = (byte[])(list.toArray()[0]);
		return imageStream;
	}
	
	public void clear(int target) {
		String table;
		if (target >= 0) clear(DBConst.getActUrlsTable(target));
		else {
			clear(DBConst.ACTTABLE);
			clear(DBConst.POSTERTABLE);
		}
		
	}
	
	public void clear(String table) {
		conn = getConnection(false);
		String sql = "TRUNCATE TABLE " + table;
		try {
			st = conn.prepareStatement(sql);
			st.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void insert(String table, int p, String url) {
		conn = getConnection(false);
		String sql = "INSERT INTO " + table + " VALUES(?, ?)";
		try {
//		System.out.println(sql);
		st = conn.prepareStatement(sql);
		st.setString(1, String.valueOf(p));
		st.setString(2, url);
		st.executeUpdate();
		} catch (SQLException e) {
			log("Act URLS", "insert fails");
			e.printStackTrace();
		}
	}
	
    public void replace(String p, Object value) {  
    	if (value instanceof InputStream) 
    		conn = getConnection(false);
    	else 
    		conn = getConnection(true);
    	
        String sql = "REPLACE INTO ";
        try {  
        	if (value.getClass().isArray()) {
         
        		sql += DBConst.ACTTABLE + " VALUES(";         
        		for (int i = 0; i < ActConst.ACTITEM.length - 1; i++){
                 	sql += "?,";
                }
                sql += "?)";
                st = conn.prepareStatement(sql);
                
            	Object[] list = (Object[])value;
            	if (list[0].getClass().isArray()) {
            		String[][] contentList = (String[][]) value;
            		for (int i = 0; i < contentList.length; i++) {
            			for (int j = 0; j < ActConst.ACTITEM.length; j ++) 
            				st.setString(j + 1, contentList[i][j]);
            			st.executeUpdate();
            		}	
            	} else 
            		if (list[0] instanceof String) {
            			String[] content = (String[])value;
            			for (int j = 0; j < ActConst.ACTITEM.length; j ++) 
            				st.setString(j + 1, content[j]); 
            			st.executeUpdate();
            		}          	
            } else {
            	if (value instanceof InputStream) {
            		sql += DBConst.POSTERTABLE + " VALUES(?,?)";
            		st = conn.prepareStatement(sql);
            		st.setString(1, p);
            		st.setBinaryStream(2, (InputStream)value);
            		st.executeUpdate();
            	}
            }
            
        	if (p == null) 
        		log("All Acts", "Insert executes successfully.");       
        	else 
        		log("Act " + p, "Insert executes successfully.");
        	
            conn.close();   
              
        } catch (SQLException e) {  
        	if (p == null) 
        		log("All Acts", "Replace fails.");       
        	else 
        		log("Act " + p, "Replace fails.");
        	e.printStackTrace();
        }  
    }  
         
    public Collection query(String table, String col, String where) {  
    	Collection actList = new ArrayList(3);
    	if (col != null) 
    		if (col.equals("image"))
    			conn = getConnection(false);  
    		else 
    			conn = getConnection(true);
    	else conn = getConnection(true);
        try {  
            String sql = "SELECT * FROM " + table;    
            if (where != null) {
            	sql += " WHERE activity=?";
            }
            st = conn.prepareStatement(sql);    
            if (where != null) st.setString(1, where);
            ResultSet rs = st.executeQuery();   
            log("Database", "Query " + table + " executes successfully.");  
            while (rs.next()) { 
            	String value;
            	if (col == null) {
            		String[] content = new String[ActConst.ACTITEM.length];
            		for (int i = 0; i < content.length; i++) 
            			content[i] = rs.getString(i + 1);
            		actList.add(content);
            	} else
            	if (col.equals("url")) { 
            		if (!actList.contains(col))
            			actList.add(rs.getString(col)); 
            	} else 
            		if (col.equals("image")) {
            			Blob blob = rs.getBlob(col);
            			actList.add(blob.getBytes(1, (int)blob.length()));
            		}
            }  
            conn.close(); 
              
        } catch (SQLException e) {  
            log("Database", "Query " + table + " fails.");  
            e.printStackTrace();
        }  
        return actList;
    }  
      
    private Connection getConnection(boolean GBK) {  
        Connection con = null;  

    	String driver = DBConst.DRIVER;
    	String url;
    	if (GBK) url = DBConst.getSchemaUrlGBK();
    	else url = DBConst.getSchemaUrl();

    	String user = DBConst.USER;	
    	String password = DBConst.PWD;
    	try {
    		Class.forName(driver);
    		conn = DriverManager.getConnection(url, user, password);

    		if(!conn.isClosed())
    			log("Database", "Succeeded connecting to the Database!");
    		return conn;
    	} catch(ClassNotFoundException e) {   
    		log("Database", "Sorry,can`t find the Driver!");   
    		e.printStackTrace();   
    	} catch (SQLException e) {
    		log("Database", "Sql statement error!"); 
    		e.printStackTrace();
    	}
		return con; 
    }
    
static public void main(String args[]) {
	try {
		Statement statement = conn.createStatement();

		String sql = "select * from student";
		
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