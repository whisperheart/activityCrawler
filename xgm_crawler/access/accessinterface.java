package access;

import java.util.Collection;

public interface AccessInterface {
	
	public Collection fetchActUrls(boolean[] webSiteMark);
	
	public Collection fetchActUrls(int website);  
	
	public void saveActList(String[][] actContentList);
	
	public boolean saveAct(String[] actContent);
	
	public boolean saveImage(String imgUrl, String p, boolean http);
	
	public void saveUrlList(int website, String[] urls);
	
	public void saveUrl(int website, int p, String url);
	
	public boolean saveUrlFile(String url, String p, boolean http); 
	
	public String[] fetchAct(String target);
	
	public String[][] fetchActList();
	
	public Object fetchImage(String target);
	
	public void clear(int target);
	
}
