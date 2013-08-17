package access;

import java.util.ArrayList;
import java.util.Collection;

import constents.ActConst;

public abstract class AbstractAccess implements AccessInterface {
	
	public Collection fetchActUrls(boolean[] webSiteMark) {
		Collection actList = new ArrayList(3);
		for (int i = 0; i < ActConst.WEBSITE.length; i++) 
		if (webSiteMark[i]) 
			actList.addAll(fetchActUrls(i));
		return actList;
	}
	
	public abstract Collection fetchActUrls(int website);  
	
	public abstract void saveActList(String[][] actContentList);
	
	public abstract void saveAct(String[] actContent);
	
	public abstract void saveImage(String imgUrl, String p, boolean http);
	
	public abstract void saveUrlList(int website, String[] urls);
	
	public abstract void saveUrl(int website, int p, String url);
	
	public abstract void saveUrlFile(String url, String p, boolean http); 
	
	public abstract String[] fetchAct(String target);
	
	public abstract String[][] fetchActList();
	
	public abstract Object fetchImage(String target);
	
	public abstract void clear(int target);
	
	public void log(String tag, String entry)
	  {
	    System.out.println(tag + " :" + entry);
	  }
	
}
