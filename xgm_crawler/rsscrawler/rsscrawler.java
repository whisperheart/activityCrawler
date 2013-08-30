package rssCrawler;

import java.lang.reflect.Constructor;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;

import javax.swing.SwingUtilities;

import log.CrawlerLog;
import access.DBAccess;
import configParser.CrawlerParser;
import editorUI.IMainUI;

public class RssCrawler extends CrawlerLog{ 
	
	private HashMap<String, Long> lastModify = new HashMap<String, Long>();
	
	protected int totalThread = 0;
	private final CrawlerParser crawlerParser = new CrawlerParser();
	private boolean cancel = false;
	private ArrayList<RssReader> rssReaderList = new ArrayList<RssReader>();
	
	private long minSleepTime;
	
	public RssCrawler(IMainUI mainUI) {
		super(mainUI); 
	}
	
	public void cancel() {
		cancel = true;
	}
	
	public void begin() {
		while (!cancel) {
			crawling();
		}
		for (Iterator i = rssReaderList.iterator(); i.hasNext();){
				RssReader rssReader = (RssReader)i.next();
				rssReader.cancel();
		}
	}
	
	public synchronized RssReader findRssReader(String cityURL) {
		for (Iterator i = rssReaderList.iterator(); i.hasNext();) {
			RssReader rssReader = (RssReader)i.next();
			if (rssReader.getCityURL().equals(cityURL)) 
				return rssReader;
		}
		return null;
	}
	
	public synchronized void removeRssReader(String cityURL) {
		RssReader rssReader = findRssReader(cityURL);
		rssReaderList.remove(rssReader);
		int totalThread = rssReader.getNumber();
		String cityName = rssReader.getCityName();
		log("[Finish RssCrawler] RssCrawlerThread "+totalThread,"finish to crawl city: " + cityName + " url: " + cityURL, true);
	}
	
	public synchronized void lastModifyTime(String cityURL, long time) {
		lastModify.put(cityURL, time);
	}
	
	public void crawling() {	
		String[][] rssCity = crawlerParser.getRssCity();
		minSleepTime = Long.MAX_VALUE;
		for (int i = 0; i < rssCity.length && !cancel;i++) {
			String[] cityInfo = rssCity[i];
	    	String cityURL = cityInfo[2];
	    
	    	if (findRssReader(cityURL) != null) continue;	    	
//	    	System.out.println("rssReaderList:" + rssReaderList.size() + " lastModify:" + lastModify.size());
	    	
	    	if (lastModify.containsKey(cityURL)) {
	    		long lastModifyTime = lastModify.get(cityURL);
		  		Date d = new Date();
				long time = d.getTime();
				long scanfreq = crawlerParser.getScanFreq() * 1000;
				long detTime = time - lastModifyTime;
				if (detTime < scanfreq) {
					if ((scanfreq - detTime) < minSleepTime)
						minSleepTime = scanfreq - detTime;
					continue;
				}
	    	}
	    	
	    	String cityName = cityInfo[1];
			String crawlerName = "rssCrawler.RssReader_" + cityInfo[0];	
	    	try {
	    		Class ob = Class.forName(crawlerName);
	    		Constructor con = ob.getConstructor(IMainUI.class);	    		
	    		RssReader rssReader = (RssReader)con.newInstance(mainUI);
	    		rssReaderList.add(rssReader);
	    		totalThread++;
	    		rssReader.setRssCrawler(this, totalThread);
	    		DBAccess access = new DBAccess(mainUI, cityInfo(cityName, cityURL));
	    		rssReader.init(access, cityURL, cityName);
	    		rssReader.startRssThread();
	    		log("[New RssCrawler] RssCrawlerThread "+totalThread,"start to crawl city: " + cityName + " url: " + cityURL, true);
	    	} catch (Exception e) {
	    		// TODO Auto-generated catch block
	    		e.printStackTrace();
	    	} 	
		}
		if (minSleepTime < Long.MAX_VALUE) {				
			long hour = minSleepTime / 3600000;
			long minite = (minSleepTime / 1000 % 3600) / 60;
			long second = (minSleepTime / 1000 % 3600) % 60;
			long scanFreq = crawlerParser.getScanFreq() * 1000;
			log("[MainCrawler sleeping] scan frequency is "+scanFreq+" s ",
				 "The MainCrawler sleeps for " + hour + ":"+minite+":"+second, true); 					
		try {
			Thread.sleep(minSleepTime);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		}
	}	
}
