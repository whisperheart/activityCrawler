package editor;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Collection;

import constents.FileConst;

public class FileWR {
	
	public static  File outf;
	public static  OutputStreamWriter write;
	public static  BufferedWriter writer;
	
	public static Collection readFileByLines(File file) {
		Collection actList = new ArrayList(3);
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(file));
            String tempString = null;
            int line = 1;
            while ((tempString = reader.readLine()) != null) {
                if (!actList.contains(tempString))
                  	actList.add(tempString);           
                line++;
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e1) {
                }
            }
        }
       return actList;
    }
	
	public static String[] readFileAsAct(File file) {
		String[] actList = new String[9];
		actList[8] = "";
		InputStreamReader streamReader = null;
		BufferedReader reader = null;
	    try {
	         streamReader = new InputStreamReader(new FileInputStream(file), "UTF-8");
	         reader = new BufferedReader(streamReader);
	         String tempString = null;
	         int line = 0;
	         while ((tempString = reader.readLine()) != null) {
	        	if (line < 8) {
	        		actList[line] = tempString;  
	        		line ++;
	        	}
	        	else actList[line] += tempString + "\n";
	            }
	            reader.close();
	        } catch (IOException e) {
	            e.printStackTrace();
	        } finally {
	            if (reader != null) {
	                try {
	                    reader.close();
	                } catch (IOException e1) {
	                }
	            }
	        }
	   return actList;
	}
	
	static public void saveAsFileListByType(String[][] actContentList, int length) throws IOException{
		
		for(int i = 0; i < length; i++){
			saveAsFileByType(actContentList[i]);
		 }
	}
	
	static public void saveAsFileByType(String[] actContent) throws IOException{
		 
			String nowTypePath = FileConst.getTypeDir(actContent[6]);
			File saveTypeDir = new File(nowTypePath);
			saveTypeDir.mkdir();
			String nowPath = FileConst.getTypeContDir(actContent[6], actContent[0]);
			File saveDir = new File(nowPath);
			saveDir.mkdir();
			String filename = FileConst.getTypeContFile(actContent[6], actContent[0]);
			outf = new File(filename);
			outf.createNewFile();
			write = new OutputStreamWriter(new FileOutputStream(outf),"UTF-8");
			writer = new BufferedWriter(write);
			
			for (int j = 0; j < 9; j++) {
				writer.write(actContent[j] + "\n");
			}
			try {
				fecthUrlFile(actContent[7], FileConst.getTypeImgFile(actContent[6], actContent[0]), true);
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			writer.close();
	 }
	  
	 static public void saveAsFileList(String[][] actContentList, int length) throws IOException{
		 for(int i = 0; i < length; i++){
			 saveAsFile(actContentList[i]);
			 saveAsImg(actContentList[i][7], i, true);
		 }
	 }
	 
	 static public void saveAsFile(String[] actContent) throws IOException{
		 
			String nowPath = FileConst.getContDir(actContent[0]);
			File saveDir = new File(nowPath);
			saveDir.mkdir();
			outf = new File(FileConst.getContFile(actContent[0]));
			outf.createNewFile();
			write = new OutputStreamWriter(new FileOutputStream(outf),"UTF-8");
			writer = new BufferedWriter(write);
			
			for (int j = 0; j < 9; j++) {
				writer.write(actContent[j] + "\n");
			}
			
			writer.close();
			log(actContent[1], " : Activity has been saved in file.");
	 }
	 
	 static public void saveAsImg(String imgUrl, int p, boolean http) throws IOException{
		 String imgFile = FileConst.getImgFile(String.valueOf(p));
		 fecthUrlFile(imgUrl, imgFile, http);
	 }
	 
	  static private InputStream getInputStream(String httpUrl) throws IOException {

	        URL url = new URL(httpUrl);
	        URLConnection uc = url.openConnection();
	        uc.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");
	        return uc.getInputStream();
	    }
	  
	  static public File fecthUrlFile(String url, String filename, boolean http) throws IOException {
		  File imgf = new File(filename);
		  	if (!imgf.exists()) {
		  		imgf.createNewFile();
		  	}
		  	
		  	InputStream input;
		  	if (http)  
		  		input = getInputStream(url);
		  	else 
		  		input = new FileInputStream(new File(url));
		  	
		  	BufferedInputStream in = new BufferedInputStream(input);

		  	FileOutputStream out = new FileOutputStream(imgf);

		  	byte[] buff = new byte[1];

		  	while (in.read(buff) > 0) {
		  		out.write(buff);
		  	}

		  	out.flush();
		  	out.close();
		  	in.close();
		  	return imgf;
	  }
	 
    public static void log(String tag, String entry)
	  {
	    System.out.println(tag + " :" + entry);
	  }
}
