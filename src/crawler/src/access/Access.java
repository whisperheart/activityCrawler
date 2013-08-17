package access;

import java.util.Collection;

public class Access extends AbstractAccess {
	private boolean savedb;
	private boolean savefile;
	
	private DBAccess accessdb = new DBAccess();
	private FileAccess accessfile = new FileAccess();
	
	public Access() {
		savedb = false;
		savefile = false;
	}
	
	public Access(boolean savedb, boolean savefile) {
		this.savedb = savedb;
		this.savefile = savefile;
	}
	
	public void changeState(boolean savedb, boolean savefile) {
		this.savedb = savedb;
		this.savefile = savefile;
	}
	
	public Collection fetchActUrls(boolean[] webSiteMark) {
		if (savedb) 
			return accessdb.fetchActUrls(webSiteMark);
		else 
		if (savefile) 
			return accessfile.fetchActUrls(webSiteMark);
		else 
			return null;
	}
	
	public Collection fetchActUrls(int website) {
		if (savedb) 
			return accessdb.fetchActUrls(website);
		else 
		if (savefile) 
			return accessfile.fetchActUrls(website);
		else 
			return null;
	}
	
	public void saveActList(String[][] actContentList) {
		if (savedb) 
			accessdb.saveActList(actContentList);
		if (savefile) 
			accessfile.saveActList(actContentList);
	}
	
	public void saveAct(String[] actContent){
		if (savedb) 
			accessdb.saveAct(actContent);
		if (savefile) 
			accessfile.saveAct(actContent);
	}
	
	public void saveImage(String imgUrl, String p, boolean http){
		if (savedb) 
			accessdb.saveImage(imgUrl, p, http);
		if (savefile) 
			accessfile.saveImage(imgUrl, p, http);
	}
	
	public void saveUrlList(int website, String[] urls) {
		if (savedb) 
			accessdb.saveUrlList(website, urls);
		if (savefile) 
			accessfile.saveUrlList(website, urls);
	}
	
	public void saveUrl(int website, int p, String url) {
		if (savedb) 
			accessdb.saveUrl(website,p, url);
		if (savefile) 
			accessfile.saveUrl(website, p, url);
	}
	
	public void saveUrlFile(String url, String p, boolean http) {
			if (savedb) 
				accessdb.saveUrlFile(url, p, http);
			if (savefile) 
				accessfile.saveUrlFile(url, p, http);
	}
	
	public String[] fetchAct(String target) {
		if (savedb) 
			return accessdb.fetchAct(target);
		else 
		if (savefile) 
			return accessfile.fetchAct(target);
		else 
			return null;
	}
	
	public String[][] fetchActList() {
		if (savedb) 
			return accessdb.fetchActList();
		else 
		if (savefile) 
			return accessfile.fetchActList();
		else 
			return null;
	}
	
	public Object fetchImage(String target) {
		if (savedb) 
			return accessdb.fetchImage(target);
		else 
		if (savefile) 
			return accessfile.fetchImage(target);
		else 
			return null;
	}
	
	public void clear(int target) {
		if (savedb) 
			accessdb.clear(target);
		if (savefile) 
			accessfile.clear(target);
	}
}
