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
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Vector;

import configParser.ActParser;
import configParser.CrawlerParser;
import editorUI.IMainUI;

public class DBAccess extends AbstractAccess{
      
	private static final Vector<Connection>[] pool;
	private static int[] MAX_SIZE = {0, 0};
	private static int[] MIN_SIZE = {0, 0};	
	private static final String[] connectName = {"poster", "content"};
	private static final CrawlerParser crawlerParser = new CrawlerParser();
	private static final ActParser actParser = new ActParser();
	private String source;
	
	static {
		pool = new Vector[2];
		for (int GBK = 0; GBK < 2; GBK ++){
			pool[GBK] = new Vector<Connection>();  
			for(int i = 0; i < MIN_SIZE[GBK]; i++)   
				pool[GBK].add(getConnection(GBK));
			MAX_SIZE[GBK] = crawlerParser.getConnectionPoolMax(connectName[GBK]);
			MIN_SIZE[GBK] = crawlerParser.getConnectionPoolMin(connectName[GBK]);
		  }	
	}
	
	public DBAccess(IMainUI mainUI, String source) {
		super(mainUI);
		this.source = source;
	}
	
	public static synchronized Connection getConnectionFromPool(int GBK) {

		Connection conn = null;
		
		if (pool[GBK].isEmpty()) {
			//System.out.println(connectName[GBK] + "[Empty] Connection Pool is Empty!");
			conn = getConnection(GBK);
		} else {
			//System.out.println(connectName[GBK] + "[Contain] Get Connection From Pool, size: " + pool[GBK].size());
			int last_idx = pool[GBK].size() - 1;
			conn = (Connection) pool[GBK].get(last_idx);
			pool[GBK].remove(conn);
		}
		return conn;
	} 
	
	public static synchronized void close(int GBK, Connection conn){
	 
	if (pool[GBK].size() < MAX_SIZE[GBK]) {
		pool[GBK].add(conn);
	} else {
		try {
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	}
	
	public Collection fetchActUrls(int website) {
		String urlsTable = getActURLTable(website);
		return query(urlsTable, "url", null, null); 
	}
	
	public void saveActList(String[][] actContentList) {
		replace(null, actContentList);
		for(int i = 0; i < actContentList.length; i++){
			 saveImage(actContentList[i][7], actContentList[i][0], true);
		 }
	}
	
	public boolean saveAct(String[] actContent) {
		return replace(null, actContent);		
	 }
	
	public boolean saveImage(String imgUrl, String p, boolean http) {
		return saveUrlFile(imgUrl, p, http);
	}
	
	public void saveUrl(int website, int p, String url) {
		insert(getActURLTable(website), p, url);
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
	
	public boolean saveUrlFile(String url, String p, boolean http) {
		InputStream input = null;
		try {
	  	if (http) 
	  		input = getInputStream(url);
	  	else 
	  		input = new FileInputStream(new File(url));
		} catch (IOException e) {
			failedlog("Act " + url , "fetch poster fails.", false);
			e.printStackTrace();
			return false;
		}
		return replace(p, new BufferedInputStream(input));  	
	}
    	
	public String[] fetchAct(String activity) {
		Collection list = query(getActTable(), null, "activity", activity);
		String[] content = (String[])list.toArray();
		return content;    
	}	

	public String[][] fetchActList(){
		Collection list = query(getActTable(), null,null, null);
		Object[] arr = list.toArray();
		String[][] contentList = new String[arr.length][];
		for (int i = 0; i < arr.length; i++) 
			contentList[i] = (String[]) arr[i];		
		return contentList;
	}
	
	public byte[] fetchImage(String activity){
		Collection list = query(getPostTable(), "image", "activity", activity);
		byte[] imageStream = (byte[])(list.toArray()[0]);
		return imageStream;
	}
	
	public void clear(int target) {
		String table;
		if (target >= 0) clear(getActURLTable(target));
		else {
			clear(getActTable());
			clear(getPostTable());
		}
		
	}
	
	public boolean clear(String table) {
		Connection conn;
		PreparedStatement st;
		
		conn = getConnection(0);
		
		String sql = "TRUNCATE TABLE " + table;
		try {
			st = conn.prepareStatement(sql);
			st.executeUpdate();
			return true;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
	}
	
	public boolean insert(String table, int p, String url) {	
		Connection conn;
		PreparedStatement st;
		int GBK = 0;
		conn = getConnectionFromPool(GBK);
		
		String sql = "INSERT INTO " + table + " VALUES(?, ?)";
		try {
//		System.out.println(sql);
		st = conn.prepareStatement(sql);
		st.setString(1, String.valueOf(p));
		st.setString(2, url);
		st.executeUpdate();
		succeedlog(source, "ID<" + p + "> Activity URL:" + url +" is saved successfully", false);
		close(GBK, conn);
		return true;
		} catch (SQLException e) {
			failedlog(source, "Activity URLs" + "fail to be saved", false);
			close(GBK, conn);
			return false;
		}
	}
	
    public boolean replace(String p, Object value) {  
    	Connection conn;
    	PreparedStatement st;
    	int GBK = 1;
    	if (value instanceof InputStream) GBK = 0;
    	conn = getConnectionFromPool(GBK);
    	
        String sql = "REPLACE INTO ";
        try {  
        	if (value.getClass().isArray()) {
        		int length = actParser.getActivityItems().length;
        		sql += getActTable() + " VALUES(";         
        		for (int i = 0; i < length - 1; i++){
                 	sql += "?,";
                }
                sql += "?)";
                st = conn.prepareStatement(sql);
                
            	Object[] list = (Object[])value;
            	if (list[0].getClass().isArray()) {
            		String[][] contentList = (String[][]) value;
            		for (int i = 0; i < contentList.length; i++) {
            			for (int j = 0; j < length; j ++) 
            				st.setString(j + 1, contentList[i][j]);
            			st.executeUpdate();
            			succeedlog(source, "ID<" + contentList[i][0] + "> Activity:" + contentList[i][1] +" is saved successfully.", true);
            		}	
            	} else 
            		if (list[0] instanceof String) {
            			String[] content = (String[])value;
            			for (int j = 0; j < length; j ++) 
            				st.setString(j + 1, content[j]); 
            			st.executeUpdate();
            			succeedlog(source, " ID<" + content[0] + "> Activity:" + content[1] +" is saved successfully.", true);
            		}     
            } else {
            	if (value instanceof InputStream) {
            		sql += getPostTable() + " VALUES (?,?)";
            		st = conn.prepareStatement(sql);
            		st.setString(1, p);
            		st.setBinaryStream(2, (InputStream)value);
            		st.executeUpdate();
            		succeedlog(source, "ID<" + p + "> Postor is saved successfully.", false);
            	}
            }
        	 close(GBK, conn); 
             return true;
        } catch (SQLException e) {  
        	if (p == null) 
        		failedlog(source, "All actitvities Fail to be saved", true);       
        	else 
        		failedlog(source, "ID<" + p + "> Fail to be saved", true);
        	close(GBK, conn); 
        	return false;
        }  
    }  
         
	public boolean existURL(String url) {
		Collection result = query(getActTable(), "url", "url", url);
		return (result.size() > 0);
	}
	
    public Collection query(String table, String col, String item, String where) {  
    	Connection conn;
    	PreparedStatement st;
    	int length = actParser.getActivityItems().length;
    	Collection actList = new ArrayList(3);
  	
    	int GBK = 1;
    	if (col != null) 
    		if (col.equals("image"))
    			GBK = 0;
    	conn = getConnectionFromPool(GBK);
        try {  
            String sql = "SELECT * FROM " + table;    
            if (where != null) {
            	sql += " WHERE " + item + "=?";
            }
            st = conn.prepareStatement(sql);    
            if (where != null) st.setString(1, where);
            ResultSet rs = st.executeQuery();   
 //           log("[Succeed] " + source, "Database query " + col + " from Table " + table + "successfully.");  
            while (rs.next()) { 
            	String value;
            	if (col == null) {               	  
            		String[] content = new String[length];
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
            close(GBK, conn); 
              
        } catch (SQLException e) {  

        	failedlog(source, "Database query " + col + " from Table " + table + "unsuccessfully.", true); 
            close(GBK, conn);
        }  
        return actList;
    }  
      
    private static Connection getConnection(int GBK) {  
    	try {
    		Connection conn = crawlerParser.getConnection(GBK);
 //   		if(!conn.isClosed())
 //   			log("[Succeed] Database", "Connecting to the database:" + conn.getMetaData().getURL());
    		return conn;
    	} catch(ClassNotFoundException e) {   
    		failedlog("Database", "Can`t find the Driver!", true);    
    		return null;
    	} catch (SQLException e) {
    		failedlog("Database", "Sql statement error!", true); 
    		return null;
    	}
	}
    
    private static String getActURLTable(int website) {
    	if (website == -1)
    		return crawlerParser.getActURLTable();
    	else
    		return crawlerParser.getActURLTable(website);
    }
    
    private static String getActTable() {
    	return crawlerParser.getActTable();
    }
    
    private static String getPostTable() {
    	return crawlerParser.getPostTable();
    }
    
}  