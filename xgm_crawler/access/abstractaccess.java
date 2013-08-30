package access;

import java.util.ArrayList;
import java.util.Collection;

import log.CrawlerLog;
import configParser.CrawlerParser;
import editorUI.IMainUI;


public abstract class AbstractAccess extends CrawlerLog implements AccessInterface {
	
	public AbstractAccess(IMainUI mainUI) {
		super(mainUI);
		// TODO Auto-generated constructor stub
	}

	protected CrawlerParser crawlerParser = new CrawlerParser();
	
	public Collection fetchActUrls(boolean[] webSiteMark) {
		Collection actList = new ArrayList(3);
		for (int i = 0; i < crawlerParser.getWebsite().length; i++) 
		if (webSiteMark[i]) 
			actList.addAll(fetchActUrls(i));
		return actList;
	}
	
	public abstract Collection fetchActUrls(int website);  
	
	public abstract void saveActList(String[][] actContentList);
	
	public abstract boolean saveAct(String[] actContent);
	
	public abstract boolean saveImage(String imgUrl, String p, boolean http);
	
	public abstract void saveUrlList(int website, String[] urls);
	
	public abstract void saveUrl(int website, int p, String url);
	
	public abstract boolean saveUrlFile(String url, String p, boolean http); 
	
	public abstract String[] fetchAct(String target);
	
	public abstract String[][] fetchActList();
	
	public abstract Object fetchImage(String target);
	
	public abstract void clear(int target);
	
}
