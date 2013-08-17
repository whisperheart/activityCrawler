package test;
import java.io.IOException;
import java.net.URL;

import access.AccessInterface;
import crawler.ContentParser;
import editor.Editor;


public class test {
	 static public void main(String args[]) throws IOException
	  {
//			ContentParser t = new ContentParser();
//			t.parseUrlList();
			
//			AccessInterface access = new Access();
//			Editor editor = new Editor(access);
//			editor.setVisible(true);
		 String str = "?start=10";
		 URL context = new URL("http://beijing.douban.com/events/today-all");
		 if (str != null)
			 if (str.charAt(0) == '?') 
				 str = context + str;
		 URL url = new URL(context, str);
		 System.out.println(url);
	  }
}
