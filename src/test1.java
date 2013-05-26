import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.regex.*;


public class test1 {
	 static public void main(String args[]) throws IOException 
	  {
		URL url;
		try {
			url = new URL("http://www.douban.com/location");
			URLConnection connection = null;
//			try {
//				connection = url.openConnection();
//			} catch (IOException e) {
				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//			  File f = new File("E:\\","output.txt");
//			  f.createNewFile();
//			  FileOutputStream fos = new FileOutputStream(f);
//		  DataOutputStream dos = new DataOutputStream(fos);
//			  dos.writeChars(connection.getContent().toString());
			  String path = url.getPath();
			  
			  System.out.println(path.matches("/location.{0,}"));
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	  
	  }
}
