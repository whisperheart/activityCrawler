import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.net.URL;
import java.net.URLConnection;
import java.util.Date;

import javax.swing.text.MutableAttributeSet;
import javax.swing.text.html.HTML;
import javax.swing.text.html.HTMLEditorKit;


public class test {
	protected static File f = new File("E:\\", "html.txt");
	protected static FileOutputStream fos;
	protected static DataOutputStream dos;
	
	 public void processURL(URL url)
	  {
	    try {
	      // get the URL's contents
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
	 static public void main(String args[]) throws IOException
	  {
		fos = new FileOutputStream(f);
		dos = new DataOutputStream(fos);
		URL url = new URL("http://www.douban.com/event/18741328/");
		test t = new test();
		t.processURL(url);
	  }
	 protected class Parser
	  extends HTMLEditorKit.ParserCallback {
	    protected URL base;
	    int time = 0; 
	    public Parser(URL base)
	    {
	      this.base = base;
	    }
	    public void handleSimpleTag(HTML.Tag t,
                MutableAttributeSet a,int pos)
{
	    	
				if ((t == HTML.Tag.H1) && ((String)a.getAttribute("itemprop")).equals("summary")) {					
					System.out.println("######################" + "Tag:" + t.toString() + " Atr: " + a.toString());
					System.out.println(pos);
					System.out.println("######################");
					time = 1;
				}				

}
public void handleStartTag(HTML.Tag t,
               MutableAttributeSet a,int pos)
{
handleSimpleTag(t,a,pos);    // handle the same way
}
	
public void handleText(char[] data, int pos) {
	if (time == 1) {
	System.out.println(data);
	System.out.println(pos);
	try {		
		OutputStreamWriter write = new OutputStreamWriter(new FileOutputStream(f),"UTF-8");
		BufferedWriter writer = new BufferedWriter(write);
		for (int i = 0; i < data.length; i++)
		write.write(data[i]);
		writer.close();
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	time = 0;
	}
}
	  /**
	   * Called internally to log information
	   * This basic method just writes the log
	   * out to the stdout.
	   * 
	   * @param entry The information to be written to the log.
	   */
	  public void log(String entry)
	  {
	    System.out.println( (new Date()) + ":" + entry );
	  }
}
}