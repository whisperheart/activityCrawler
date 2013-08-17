package crawler;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collection;

import access.Access;
import access.AccessInterface;
import constents.ActConst;

public class ContentParser {		
	protected String[] actContent; 
	
	protected Access access;
	protected boolean[] webSiteMark;
	protected boolean cancel;
	protected IParserReportable report;
	
	public ContentParser(boolean[] webSiteMark, Access access, IParserReportable report) {
		this.webSiteMark = webSiteMark;
		this.access = access;
		this.report = report; 
	}
 
	 public void begin() {
		 cancel = false;
		 for (int website = 0; website < ActConst.WEBSITE.length&&(!cancel); website++) 
			 if (webSiteMark[website]) {
			 
				 Collection actList = access.fetchActUrls(website);		 
				 Object[] arr = actList.toArray();

				 for(int num = 0; num < arr.length&&(!cancel); num++){
					 URL url;
					 System.out.println("num: " + num);
					 try {
						url = new URL((String)arr[num]);
						report.processURL(website, num, url);
					} catch (MalformedURLException e) {
						// TODO Auto-generated catch block
						System.out.println("Wrong url: " + num + "->" + arr[num]);
						e.printStackTrace();
					} catch (IOException e) {
						System.out.println("fail to process url: " + num + "->" + arr[num]);
						e.printStackTrace();
						try {
							Thread.sleep(600000);
							url = new URL((String)arr[num]);
							report.processURL(website, num, url);
						} catch (InterruptedException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						} catch (IOException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
					}
					 		
				 }
			 }
	 }
	 
  public void cancel()
	  {
	    cancel = true;
	  }
}
	
