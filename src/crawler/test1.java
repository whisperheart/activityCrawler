package crawler;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.regex.*;


public class test1 {
	 static public void main(String args[]) throws IOException 
	  {
		URL url;
//		try {
//			url = new URL("http://www.douban.com/location");
//			URLConnection connection = null;
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
//			  String path = url.getPath();
			  
	//		  System.out.println(path.matches("/location.{0,}"));
	//		  String dataSaveDir = "/Users/kanglianghuan/workplace/APPproject/data/";
	//		  File saveDir = new File(dataSaveDir + String.valueOf(1));
	//		  saveDir.mkdir();
	//		  File outf = new File(dataSaveDir + String.valueOf(1) + "/" + "test.txt");
	//		  outf.createNewFile();
//				outf.createNewFile();
//				write = new OutputStreamWriter(new FileOutputStream(outf),"UTF-8");
//				writer = new BufferedWriter(write);
//				writer.write(Num + "\n");
			 try {
				Thread.sleep(10000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			 System.out.print("up!");
//		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}	  
	  }
}
