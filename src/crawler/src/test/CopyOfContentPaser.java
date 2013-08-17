package test;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Collection;

import javax.swing.text.MutableAttributeSet;
import javax.swing.text.html.HTML;
import javax.swing.text.html.HTMLEditorKit;

import access.AccessInterface;
import access.FileAccess;
import constents.FileConst;
import crawler.HTMLParse;
import filter.Filter;

public class CopyOfContentPaser {
	protected static String nowPath;
	protected static String imgSaveDir;
		
	protected static String[][] actContentList; 
	protected static int Num;
	
	protected static AccessInterface access;
	
	public void processURL(URL url)
	  {
	    try {
	      // get the URL's contents
//	      HttpURLConnectionWrapper connection = new HttpURLConnectionWrapper(url);
	      URLConnection connection = url.openConnection();
	      if ( (connection.getContentType()!=null) &&
	           !connection.getContentType().toLowerCase().startsWith("text/") ) {
	        return;
	      }
	      
	      // read the URL
	      InputStream is = connection.getInputStream();
	      Reader r = new InputStreamReader(is,"UTF-8");
	      // parse the URL
	      HTMLEditorKit.Parser parse = new HTMLParse().getParser();
	      parse.parse(r,new Parser(url),true);
	    } catch ( IOException e ) {
	      return;
	    }
	    // mark URL as complete
	  }
	 
	 public void getActContentList() throws MalformedURLException {
		Collection actList = access.fetchActUrls(null);
		Object[] arr = actList.toArray();
		actContentList = new String[arr.length][9];
		for(Num = 0; Num < arr.length; Num++){

			actContentList[Num][0] = String.valueOf(Num);
			actContentList[Num][1] = (String)arr[Num];
			actContentList[Num][8] = "";
			URL url = new URL((String)arr[Num]);
			actContentList[Num][2] = "";
			processURL(url);
	
			if (actContentList[Num][2] == "") {
				try {
						Thread.sleep(10000);
						access.log((String)arr[Num], "Request failed! Process again.");
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
			processURL(url);
			}
			int length = actContentList[Num][8].length();
			if (length >= 4){
				char[] charlist = actContentList[Num][8].toCharArray();
			if (charlist[length - 4] == 'Ç') {
				actContentList[Num][8] = actContentList[Num][8].substring(0, length - 4);
			}
			access.log((String)arr[Num], "finish processing.");
			}
		}
	 }
	 
	 public void parseUrlList() throws IOException {
		 getActContentList();
		 access.saveActList(actContentList);
	 }
	 
	 protected class Parser
	  extends HTMLEditorKit.ParserCallback {
	    protected URL base;
	    boolean city = false;
	    boolean title = false;
	    boolean time = false; 
	    boolean place = false;
	    boolean img = false;
	    boolean type = false;
	    boolean content = false;
	    boolean br = false;
	    
	   public Parser(URL base)
	    {
	      this.base = base;
	    }
	  public void handleSimpleTag(HTML.Tag t,
			  
               MutableAttributeSet a,int pos) {	
		  /*
		city = (Filter.contentFilter(t, a, pos, 0));	
		title = (Filter.contentFilter(t, a, pos, 1));		
		time = (Filter.contentFilter(t, a, pos, 2));
		place = (Filter.contentFilter(t, a, pos, 3));
		img = (Filter.contentFilter(t, a, pos, 4));
		type = (Filter.contentFilter(t, a, pos, 5));
	*/
		if (img) {
			String url = (String)a.getAttribute(HTML.Attribute.SRC);
			actContentList[Num][7] = url;
		}
		if (content && (t == HTML.Tag.BR)) {
			actContentList[Num][8] += "\n";
		}
}
public void handleStartTag(HTML.Tag t,
              MutableAttributeSet a,int pos) {
//	if (Filter.contentFilter(t, a, pos, 6)) {
		content = true;
//	}
	handleSimpleTag(t,a,pos);    // handle the same way
}

public void handleEndTag(HTML.Tag t, int pos){
//	if (Filter.contentFilter(t, null, pos, 7)) {
		content = false; 
//	}
}
	
public void handleText(char[] data, int pos) {
	/*
	if (city) {	
			city = false;
			actContentList[Num][4] = String.valueOf(data);
		} 
	if (title) {	
		title = false;
		actContentList[Num][2] = String.valueOf(data);
	} 
	if (time) {	
		time = false;
		actContentList[Num][3] = String.valueOf(data);
	}
	if (place) {	
		place = false;
		actContentList[Num][5] = String.valueOf(data);
	}
	if (type) {
		type = false;
		actContentList[Num][6] = String.valueOf(data);
	}
	if (content){
		actContentList[Num][8] += String.valueOf(data);
	}
	*/
	}

}	  
}
