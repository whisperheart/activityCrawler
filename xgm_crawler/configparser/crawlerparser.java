package configParser;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.dom4j.Element;

public class CrawlerParser extends XMLConfigParser{
	
/*	
	public final static String DRIVER = "com.mysql.jdbc.Driver";
	
	public final static String URL = "jdbc:mysql://127.0.0.1:3306/";
	
	public final static String SCHEMA = "test";
	
	public final static String GBK = "?useUnicode=true&characterEncoding=GBK";
	
	public final static String USER = "root";
	
	public final static String PWD = "";
	
	public final static String ACTURLS = "ACTURLS";
	
	public final static String ACTTABLE = "ACTTABLE";
	
	public final static String POSTERTABLE = "POSTTABLE";
*/
	public final static String GBKURL = "?useUnicode=true&characterEncoding=GBK";
	
	public Element getMainElement() {
		Element root = getRootElement();
		if (root == null) return null;
		Element crawler = root.element("crawler");
		return crawler;
	}
	
	public int getThreadPoolSize() {
		Element crawler = getMainElement();
		if (crawler==null) return 1;
		Element rssEle = crawler.element("rss");
		int threadpool = 1;
		try {
			threadpool = Integer.parseInt(rssEle.elementText("threadpool"));
		} catch (Exception e) {
		}
		return threadpool;
	}
	
	public long getScanFreq() {
		Element crawler = getMainElement();
		if (crawler==null) return 0;
		Element rssEle = crawler.element("rss");
		long scanfreq = 0;
		try {
			scanfreq = Long.parseLong(rssEle.elementText("scanfreq"));
		} catch (Exception e) {
		}
		return scanfreq;
	}
	
	public String[][] getWebsite() {
		ArrayList<String[]> arr = new ArrayList<String[]>();
		Element crawler = getMainElement();
		if (crawler==null) return null;
		Element websiteEle = crawler.element("website");
		for (Iterator i = websiteEle.elementIterator("web"); i.hasNext();)
		    {
		        Element webEle = (Element) i.next();		        
		        String[] webStr = new String[2];
		        String webName = webEle.attributeValue("name");
		        String webEntry = webEle.attributeValue("entry");
		        webStr[0] = webName;
		        webStr[1] = webEntry;
		        arr.add(webStr);
		    }
		String[][] websites = new String[arr.size()][];
		arr.toArray(websites);
		return websites;
	}
	
	public String[][] getRssCity() {		
		Element crawler = getMainElement();
		if (crawler==null) return null;
		Element rssEle = crawler.element("rss");
        ArrayList<String[]> arr = new ArrayList<String[]>();
		for (Iterator i = rssEle.elementIterator("website"); i.hasNext();)
		    {
		        Element websiteEle = (Element) i.next();		        		        
		        String webName = websiteEle.attributeValue("name");
		        for (Iterator j = websiteEle.elementIterator("url"); j.hasNext();){
		        	Element urlEle = (Element) j.next();
		        	String cityName = urlEle.attributeValue("city");
		        	String urlStr = urlEle.getText();
		        	String[] rssStrs = new String[3];
		        	rssStrs[0] = webName;
		        	rssStrs[1] = cityName;
		        	rssStrs[2] = urlStr;
		        	arr.add(rssStrs);
		        };
		    }
		String[][] ans = new String[arr.size()][];
		arr.toArray(ans);
		return ans;
	}
	
	protected Element getDatabase() {
		Element root = getMainElement();
		if (root == null) return null;
		Element database = root.element("database");
		return database;
	}
	
	public Element getConnectionSize(String name) {
		Element database = getDatabase();
		Element connectPool = database.element("connectpool");
		List<Element> sizeList = connectPool.elements("size");
		for (Element size : sizeList) {
			String connName = size.attributeValue("name");
			if (connName.equals(name))
				return size;
		}
		return null;
	}
	public int getConnectionPoolMax(String name) {
		Element size = getConnectionSize(name);
		String maxStr = size.elementText("max");
		int max = Integer.parseInt(maxStr);
		return max;
	}
	
	public int getConnectionPoolMin(String name) {
		Element size = getConnectionSize(name);
		String minStr = size.elementText("min");
		int min = Integer.parseInt(minStr);
		return min;
	}
	
	public String getActURLTable() {
		Element database = getDatabase();
		Element table = database.element("table");
		String acturl = table.elementText("acturl");
		return acturl;
	}
		
	public String getActURLTable(int website) {
		return getActURLTable() + "_" + getWebsite()[website][0];
	}
	
	public String getActTable() {
		Element database = getDatabase();
		Element table = database.element("table");
		String acttable = table.elementText("act");
		return acttable;
	}
	
	public String getPostTable() {
		Element database = getDatabase();
		Element table = database.element("table");
		String posttable = table.elementText("poster");
		return posttable;
	}	
	
	public Connection getConnection(int GBK) throws ClassNotFoundException, SQLException {
		Element database = getDatabase();
		if (database==null) return null;
		String driver = database.elementText("driver");
		String url = database.elementText("dburl");
		String schema = database.elementText("schema");
		String user = database.elementText("user");
		String pwd = database.elementText("pwd");
		
	   	Connection conn =null;

    	if (GBK == 0) url = url + schema;
    	else url = url + schema + GBKURL;

    	Class.forName(driver);
    	conn = DriverManager.getConnection(url, user, pwd);
		return conn; 
    }	
}
