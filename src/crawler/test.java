package crawler;
import java.io.BufferedInputStream;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Date;

import javax.swing.text.MutableAttributeSet;
import javax.swing.text.html.HTML;
import javax.swing.text.html.HTMLEditorKit;


public class test {
	protected static File f = new File("/Users/kanglianghuan/workplace/APPproject/html.txt");
	protected static String imgSaveDir = new String("/Users/kanglianghuan/workplace/APPproject/");
	protected static OutputStreamWriter write;
	protected static BufferedWriter writer;
	
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
	 
	  static private InputStream getInputStream(String httpUrl) throws IOException {
	        // 网页Url
		   System.out.println("http: " + httpUrl);
	        URL url = new URL(httpUrl);
	        URLConnection uc = url.openConnection();
	        uc.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");
	        return uc.getInputStream();
	    }
	  
	  static public File fecthFile(String httpUrl, String filename)
	  								throws MalformedURLException, IOException {
		    File imgf = new File(filename);
		  	if (!imgf.exists()) {
		  		imgf.createNewFile();
		  	}

  // 打开输入流
		  	BufferedInputStream in = new BufferedInputStream(
		  			getInputStream(httpUrl));

  // 打开输出流
		  	FileOutputStream out = new FileOutputStream(imgf);

		  	byte[] buff = new byte[1];
  // 读取数据
		  	while (in.read(buff) > 0) {
		  		out.write(buff);
		  	}

		  	out.flush();
		  	out.close();
		  	in.close();
		  	return imgf;
	  }
	  
	 static public void main(String args[]) throws IOException
	  {
		f.createNewFile();
		write = new OutputStreamWriter(new FileOutputStream(f),"UTF-8");
		writer = new BufferedWriter(write);
		URL url = new URL("http://www.douban.com/event/18627581/");
		test t = new test();
		t.processURL(url);
		writer.close();
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
	    
	    public Parser(URL base)
	    {
	      this.base = base;
	    }
	  public void handleSimpleTag(HTML.Tag t,
                MutableAttributeSet a,int pos) {	
		city = (Filter.contentFilter(t, a, pos, 0));	
		title = (Filter.contentFilter(t, a, pos, 1));		
		time = (Filter.contentFilter(t, a, pos, 2));
		place = (Filter.contentFilter(t, a, pos, 3));
		img = (Filter.contentFilter(t, a, pos, 4));
		type = (Filter.contentFilter(t, a, pos, 5));
		if (img) {
			System.out.println("img!");
			String url = (String)a.getAttribute(HTML.Attribute.SRC);
			System.out.println(url);
	/*		try {
				test.fecthFile(url, );
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			*/
		}
}
public void handleStartTag(HTML.Tag t,
               MutableAttributeSet a,int pos)
{
	handleSimpleTag(t,a,pos);    // handle the same way
}
	
public void handleText(char[] data, int pos) {
	try {
	if (city) {	
			write.write("city:");
			for (int i = 0; i < data.length; i++)
			write.write(data[i]);
			write.write("\n");
			city = false;
		}
	if (title) {	
		write.write("activity:");
		for (int i = 0; i < data.length; i++)
		write.write(data[i]);
		write.write("\n");
		title = false;
	}
	if (time) {	
		write.write("time:");
		for (int i = 0; i < data.length; i++)
		write.write(data[i]);
		write.write("\n");
		time = false;
	}
	if (place) {	
		write.write("place:");
		for (int i = 0; i < data.length; i++)
		write.write(data[i]);
		write.write("\n");
		place = false;
	}
	if (type) {
		write.write("type:");
		for (int i = 0; i < data.length; i++)
			write.write(data[i]);
			write.write("\n");
		type = false;
	}
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
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