package log;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

import configParser.CrawlerParser;
import editorUI.IMainUI;
import editorUI.RssMainUI;

public abstract class CrawlerLog {
	
	private static String logfile = ""; 
	private static File logf;
	private static OutputStreamWriter write;
	private static BufferedWriter writer;
	private static CrawlerParser crawlerParser = new CrawlerParser();
	protected static IMainUI mainUI;
	
	static {
		logfile = crawlerParser.getLog();
	}
	
	public CrawlerLog(IMainUI mainUI) {
		this.mainUI = mainUI;
	}
	
	public String cityInfo(String cityName, String cityURL) {
		return "city:" + cityName + " url:" + cityURL;
	}
			
	public void succeedlog(String cityName, String cityURL, String message, boolean toUI) {
		log("[Succeed] " + cityInfo(cityName, cityURL), message, toUI);
	}
	
	public void failedlog(String cityName, String cityURL, String message, boolean toUI) {
		log("[Failure] " + cityInfo(cityName, cityURL), message, toUI);
	}
	
	public void succeedlog(String tag, String message, boolean toUI) {
		log("[Succeed] " + tag, message, toUI);
	}
	
	public static void failedlog(String tag, String message, boolean toUI) {
		log("[Failure] " + tag, message, toUI);
	}
	
	public static synchronized void log(String tag, String message, boolean toUI)
	  {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		SimpleDateFormat dayf = new SimpleDateFormat("yyyy-MM-dd");
		Date nowDate = new Date();
		String msg = "("+df.format(nowDate)+")"+tag + " => " + message;
		try {
			logf = new File(logfile + "_" + dayf.format(nowDate));
			logf.createNewFile();
			write = new OutputStreamWriter(new FileOutputStream(logf, true),"UTF-8");
			writer = new BufferedWriter(write);
			writer.write(msg + "\n");
			writer.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    System.out.println(msg);
	    if (toUI) mainUI.currentLog(msg);
	  }
}
