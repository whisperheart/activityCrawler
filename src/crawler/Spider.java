package crawler;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import javax.swing.text.MutableAttributeSet;
import javax.swing.text.html.HTML;
import javax.swing.text.html.HTMLEditorKit;

import access.Access;
import access.AccessInterface;
import access.DBAccess;
import access.FileAccess;
import filter.Filter;
/**
 * That class implements a reusable spider
 */
public class Spider {
  /**
   * A collection of URLs that resulted in an error
   */
  protected Collection workloadError = new ArrayList(3);
  /**
   * A collection of URLs that are waiting to be processed
   */
  protected Collection workloadWaiting = new ArrayList(3);
  /**
   * A collection of URLs that were processed
   */
  protected Collection workloadProcessed = new ArrayList(3);
  /**
   * The class that the spider should report its URLs to
   */
  protected ISpiderReportable report;
  /**
   * A flag that indicates whether this process
   * should be canceled
   */
  protected boolean cancel = false;
  
  protected int website;
  
  protected Filter filter;
  
  protected Access access;
  
  public Spider(ISpiderReportable report, int website, Filter filter, Access access) throws IOException
  {
    this.report = report;
    this.website = website;
    this.filter = filter;
    this.access = access;
  }
  /**
   * Get the URLs that resulted in an error.
   * 
   * @return A collection of URL's.
   */
  public Collection getWorkloadError()
  {
    return workloadError;
  }
  /**
   * Get the URLs that were waiting to be processed.
   * You should add one URL to this collection to
   * begin the spider.
   * 
   * @return A collection of URLs.
   */
  public Collection getWorkloadWaiting()
  {
    return workloadWaiting;
  }
  /**
   * Get the URLs that were processed by this spider.
   * 
   * @return A collection of URLs.
   */
  public Collection getWorkloadProcessed()
  {
    return workloadProcessed;
  }    
  /**
   * Clear all of the workloads.
   */
  public void clear()
  {
    getWorkloadError().clear();
    getWorkloadWaiting().clear();
    getWorkloadProcessed().clear();
  }
  /**
   * Set a flag that will cause the begin
   * method to return before it is done.
   */
  public void cancel()
  {
    cancel = true;
  }
  /**
   * Add a URL for processing.
   * 
   * @param url
 * @throws IOException 
   */
  public void addURL(URL base, URL url) throws IOException
  {
    if ( getWorkloadWaiting().contains(url) )
      return;
    if ( getWorkloadError().contains(url) )
      return;
    if ( getWorkloadProcessed().contains(url) )
      return;
    log("Adding to workload: " + base + " -> " + url );
    getWorkloadWaiting().add(url);
  }
  /**
   * Called internally to process a URL
   * 
   * @param url The URL to be processed.
 * @throws IOException 
   */
  public void processURL(URL url) throws IOException
  {
    try {
      log("Processing: " + url );
      // get the URL's contents
      URLConnection connection = url.openConnection();
      if ( (connection.getContentType()!=null) &&
           !connection.getContentType().toLowerCase().startsWith("text/") ) {
        getWorkloadWaiting().remove(url);
        getWorkloadProcessed().add(url);
        log("Not processing because content type is: " +
             connection.getContentType() );
        return;
      }
      
      // read the URL
      InputStream is = connection.getInputStream();
      Reader r = new InputStreamReader(is);
      // parse the URL
      HTMLEditorKit.Parser parse = new HTMLParse().getParser();
      parse.parse(r,new Parser(url),true);
    } catch ( IOException e ) {
      getWorkloadWaiting().remove(url);
      getWorkloadError().add(url);
      log("Error: " + url );
      report.spiderURLError(url);
      return;
    }
    // mark URL as complete
    getWorkloadWaiting().remove(url);
    getWorkloadProcessed().add(url);
    log("Complete: " + url );
 
  }
  /**
   * Called to start the spider
 * @throws IOException 
   */
  public void begin() throws IOException
  {
    cancel = false;
    while ( !getWorkloadWaiting().isEmpty() && !cancel ) {
      Object list[] = getWorkloadWaiting().toArray();
      for ( int i=0;(i<list.length)&&!cancel;i++ )
        processURL((URL)list[i]);
    }
    log("Cancel: " + cancel);
    
    /*
    Object list[] = getWorkloadProcessed().toArray();
    
    Collection urlList = new ArrayList(3);
    for ( int i=0;i<list.length; i++) 
    	if (filter.activityFilter((URL)list[i])) 
    		urlList.add((String)list[i]);
    
    String[] urls;
    if (urlList.toArray().length > 0)
    	urls = (String[])urlList.toArray();
    else 
    	urls = null;

     access.saveURLs(website, urls);
    */
  }
  
  protected class Parser
  extends HTMLEditorKit.ParserCallback {
    protected URL base;
    public Parser(URL base)
    {
      this.base = base;
    }
    public void handleSimpleTag(HTML.Tag t,
                                MutableAttributeSet a,int pos)
    {
      String href = (String)a.getAttribute(HTML.Attribute.HREF);
 //     System.out.println(href);
      
      if( (href==null) && (t==HTML.Tag.FRAME) )
        href = (String)a.getAttribute(HTML.Attribute.SRC);
        
      if ( href==null )
        return;
      int i = href.indexOf('#');
      if ( i!=-1 )
        href = href.substring(0,i);
      
      if ( href.toLowerCase().startsWith("mailto:") ) {
        report.spiderFoundEMail(href);
        return;
      }
      try {
		handleLink(base,href);
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
    }
    public void handleStartTag(HTML.Tag t,
                               MutableAttributeSet a,int pos)
    {
      handleSimpleTag(t,a,pos);    // handle the same way
    }
    protected void handleLink(URL base,String str) throws IOException
    {
      try {
  //  	  System.out.println("base:" + base + " str:" + str);
    	  if (str != null) 
    		  if (str.length() > 0)
 			 if (str.charAt(0) == '?') 
 			 { 
 				 String baseStr = base.toString();
 				int i = baseStr.indexOf('?');
 			      if ( i!=-1 )
 			       baseStr = baseStr.substring(0,i);
 				 str = baseStr + str;
 				 
 			 }
        URL url = new URL(base,str);
 //      System.out.println("url:" + url);
        if ( report.spiderFoundURL(website, base,url) )
          addURL(base, url);
        report.spiderTargetURL(website, base, url);
        	
      } catch ( MalformedURLException e ) {
        log("Found malformed URL: " + str );
      }
    }
  }

  /**
   * Called internally to log information
   * This basic method just writes the log
   * out to the stdout.
   * 
   * @param entry The information to be written to the log.
 * @throws IOException 
   */
  public void log(String entry) throws IOException
  {
    System.out.println( (new Date()) + ":" + entry );
  }
}
