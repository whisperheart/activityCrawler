package rssCrawler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.Date;
import java.util.HashSet;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.swing.SwingUtilities;

import log.CrawlerLog;
import access.DBAccess;
import configParser.ActParser;
import configParser.CrawlerParser;
import editorUI.IMainUI;

public abstract class RssReader extends CrawlerLog{
	
	protected String[] actContent;
	protected int imgNum;
	protected DBAccess access;
	protected String cityURL;
	protected String cityName;
	protected static RssCrawler rssCrawler;
	protected static final ActParser actParser = new ActParser();
	protected static final CrawlerParser crawlerParser = new CrawlerParser();
	protected boolean cancel = false;
	protected String website;
	private int number;
	
	protected static ExecutorService pool;
	private static final HashSet<Character> hashSet; 
	public static final char substitute = '\uFFFD'; 
	
	static { 
	        final String escapeString = "\u0000\u0001\u0002\u0003\u0004\u0005" + 
	            "\u0006\u0007\u0008\u000B\u000C\u000E\u000F\u0010\u0011\u0012" + 
	            "\u0013\u0014\u0015\u0016\u0017\u0018\u0019\u001A\u001B\u001C" + 
	            "\u001D\u001E\u001F\uFFFE\uFFFF";

	        hashSet = new HashSet<Character>(); 
	        for (int i = 0; i < escapeString.length(); i++) { 
	         hashSet.add(escapeString.charAt(i)); 
	        } 
	        int size = crawlerParser.getThreadPoolSize();
	        pool = Executors.newFixedThreadPool(size);
	        
	}  
	
	private boolean isIllegal(char c) { 
		return hashSet.contains(c); 
	}

	private String escapeCharacters(String string) {
		char[] copy = null; 
		boolean copied = false; 
		for (int i = 0; i < string.length(); i++) { 
		    if (isIllegal(string.charAt(i))) { 
		       if (!copied) { 
		          copy = string.toCharArray(); 
		          copied = true; 
		        } 
		     copy[i] = substitute; 
		     } 
		} 
		return copied ? new String(copy) : string; 
	}
	
	public RssReader(IMainUI mainUI) {
		super(mainUI);
	   	String [] actItems = actParser.getActivityItems();
	   	imgNum = ActParser.imgNum;
    	actContent = new String[actItems.length];
	}
	
	public void init(DBAccess access, String cityURL, String cityName) {		
		this.access = access;
		this.cityURL = cityURL;
		this.cityName = cityName;
	}
	
	public String getXMLString(String urlStr) throws IOException {		
		InputStreamReader streamReader = null;
		BufferedReader reader = null;
        String xmlString = "";
        URL url = new URL(urlStr);
        URLConnection uc = url.openConnection();
	    InputStream input = uc.getInputStream();
	    streamReader = new InputStreamReader(input, "UTF-8");	     
	    reader = new BufferedReader(streamReader);
	    String tempString = null;
	    while ((tempString = reader.readLine()) != null) {
	        xmlString += escapeCharacters(tempString);
	    }
	    reader.close();
	    return xmlString;
	}
	
	public abstract void parse(String cityURL, String city);
	
	public String getURLFileContent(String strURL) throws IOException {      	   
        URL url = new URL(strURL);   
        BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream(), "UTF-8"));  
        String s = "";    
        StringBuffer sb = new StringBuffer("");   
        while ((s = br.readLine()) != null) {   
            sb.append(s + "/r/n");   
        }    
        br.close();   
        return sb.toString();    
}  
	
	public void cancel() {
		cancel = true;
	}
	
	public void setRssCrawler(RssCrawler rssCrawler, int number) {
		this.rssCrawler = rssCrawler;
		this.number = number;
	}
	
	public String getCityURL() {
		return cityURL;
	}
	
	public String getCityName() {
		return cityName;
	}
	
	public int getNumber() {
		return number;
	}
	
class RssThread implements Runnable {
	
	public void run(){
		if (!cancel) {
		parse(cityURL, cityName);
		Runnable doLater = new Runnable()
	      {
	        public void run()
	        {
	  		  Date d = new Date();
			  long time = d.getTime();
	          rssCrawler.lastModifyTime(cityURL, time);
	          rssCrawler.removeRssReader(cityURL);
	        }
	      };
		SwingUtilities.invokeLater(doLater);		
	}  
	}
}

	public void startRssThread() {      		
		RssThread rssThread = new RssThread();
		pool.execute(rssThread);
	}
	
}
