package access;

import java.util.Collection;

public interface AccessInterface {
	
	public Collection fetchActUrls(boolean[] webSiteMark);
	
	public Collection fetchActUrls(int website);  
	
	public void saveActList(String[][] actContentList);
	
	public void saveAct(String[] actContent);
	
	public void saveImage(String imgUrl, String p, boolean http);
	
	public void saveUrlList(int website, String[] urls);
	
	public void saveUrl(int website, int p, String url);
	
	public void saveUrlFile(String url, String p, boolean http); 
	
	public String[] fetchAct(String target);
	
	public String[][] fetchActList();
	
	public Object fetchImage(String target);
	
	public void clear(int target);
	
	public void log(String tag, String entry);
}
