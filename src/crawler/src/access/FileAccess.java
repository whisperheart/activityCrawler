package access;

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

import constents.ActConst;
import constents.FileConst;

public class FileAccess extends AbstractAccess{
	
	public  File outf;
	public  OutputStreamWriter write;
	public  BufferedWriter writer;
	
	public Collection fetchActUrls(int website) {
		File file =  new File(FileConst.getUrlsFile(website));

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
	
	public String[] fetchAct(String target) {
		File file = new File(FileConst.getContFile(target));
		if (! file.exists()) return null;
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
	
	public void saveActListByType(String[][] actContentList, int length) throws IOException{
		
		for(int i = 0; i < length; i++){
			saveActByType(actContentList[i]);
		 }
	}
	
	public void saveActByType(String[] actContent) throws IOException{
		 
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
			saveUrlFile(actContent[7], FileConst.getTypeImgFile(actContent[6], actContent[0]), true);
			writer.close();
	 }
	  
	 public void saveActList(String[][] actContentList) {
		 for(int i = 0; i < actContentList.length; i++){
			 saveAct(actContentList[i]);
			 saveImage(actContentList[i][7], actContentList[i][0], true);
		 }
	 }
	
	 public void saveAct(String[] actContent) {
		 
			String nowPath = FileConst.getContDir(actContent[0]);
			File saveDir = new File(nowPath);
			saveDir.mkdir();
			
			try {
			outf = new File(FileConst.getContFile(actContent[0]));
			outf.createNewFile();
			write = new OutputStreamWriter(new FileOutputStream(outf),"UTF-8");
			writer = new BufferedWriter(write);
			
			for (int j = 0; j < 9; j++) {
				writer.write(actContent[j] + "\n");
			}
			
			writer.close();
			log("Act actContent[0]", "Activity has been saved in file.");
			} catch (IOException e) {
				log("Act actContent[0]", "Activity save fails.");
				e.printStackTrace();
			}
	 }
	 
	 public void saveImage(String imgUrl, String p, boolean http) {
		 String imgFile = FileConst.getImgFile(String.valueOf(p));
		 saveUrlFile(imgUrl, imgFile, http);
	 }
	 
	 public void saveUrl(int website, int p, String url) {
		 try {
				outf = new File(FileConst.getUrlsFile(website));
				outf.createNewFile();
				write = new OutputStreamWriter(new FileOutputStream(outf));
				writer = new BufferedWriter(write);
				writer.write(url + "\n");
				
				writer.close();
				log("Act Url", "has been saved in file:" + FileConst.getUrlsFile(website));
				} catch (IOException e) {
					log("Act Url", "save fails.");
					e.printStackTrace();
				}
	 }
	 
	 public void saveUrlList(int website, String[] urls) {	
		 if (urls != null) 
		try {
			outf = new File(FileConst.getUrlsFile(website));
			outf.createNewFile();
			write = new OutputStreamWriter(new FileOutputStream(outf));
			writer = new BufferedWriter(write);
			
			for (int i = 0; i < urls.length; i++) {
				writer.write(urls[i] + "\n");
			}
			
			writer.close();
			log("Act Urls", "has been saved in file:" + FileConst.getUrlsFile(website));
			} catch (IOException e) {
				log("Act Urls", "save fails.");
				e.printStackTrace();
			}
	 }
	 
	  private InputStream getInputStream(String httpUrl) throws IOException {

	        URL url = new URL(httpUrl);
	        URLConnection uc = url.openConnection();
	        uc.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");
	        return uc.getInputStream();
	    }
	  
	  public void saveUrlFile(String url, String filename, boolean http) {
		  File imgf = new File(filename);
		  try {
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

		  	input.close();
		  	out.flush();
		  	out.close();
		  	in.close();
		  	} catch (IOException e) {
		  		log("Act " + url , "fetch poster fails.");
		  		e.printStackTrace();
		  	}
	  }
	 
	public String[][] fetchActList() {	
		Collection list = new ArrayList(3);
		String[] content;
		int website = 0;
		int i = 0;
		while ((fetchAct(ActConst.WEBSITE[website] + "0") == null) && (website < ActConst.WEBSITE.length))
				website ++;
			
		while (website < ActConst.WEBSITE.length)  { 
			 content = fetchAct(ActConst.WEBSITE[website] + i);
			 list.add(content);
			 log("Activity " + ActConst.WEBSITE[website] + i, " has been fetched");
			 
			 i ++;
			 if (fetchAct(ActConst.WEBSITE[website] + i) == null) {
				 i = 0;
				 while ((fetchAct(ActConst.WEBSITE[website] + "0") == null) && (website < ActConst.WEBSITE.length))
				website ++;
			 }
			 
			 
//			 System.out.println(actContentList[i][8]);
		 }
		
		String[][] actContentList = (String[][])list.toArray();
		return actContentList;
	}
	  
	public String fetchImage(String target) {
		return FileConst.getImgFile(target);
	}
	
	public void clear(int dir) {
		
	}
}
