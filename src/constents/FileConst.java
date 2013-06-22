package constents;

public class FileConst {
	
	public final static String DATASAVEDIR = "/Users/kanglianghuan/workplace/APPproject/data1/";
	
	public final static String TYPESAVEDIR = "/Users/kanglianghuan/workplace/APPproject/data0/";
	
	public final static String URLFILENAME = "/Users/kanglianghuan/workplace/APPproject/urls.txt";
	
	public final static String FILENAME = "content.txt";
	
	public final static String IMGNAME = "post.jpg";
	
	public final static String[] LABEL = {"activity", "url", "title", "time", "city", "place", "type", "img", "content"};
	
	public final static String DELIMITER = "/";
	
	public static String getContFile(String p) {
		return DATASAVEDIR + DELIMITER + p + DELIMITER + FILENAME;
	}
	
	public static String getTypeContFile(String type, String p) {
		return getTypeContDir(type, p) + DELIMITER + FILENAME;
	}
	
	public static String getImgFile(String p) {
		return DATASAVEDIR + DELIMITER + p + DELIMITER + IMGNAME;
	}
	
	public static String getTypeImgFile(String type, String p){
		return getTypeContDir(type, p) + DELIMITER + IMGNAME; 
	}
	
	public static String getTypeDir(String type) {
		return TYPESAVEDIR + DELIMITER + type;
	}
	
	public static String getTypeContDir(String type, String p){
		return getTypeDir(type) + DELIMITER + p;
	}
	
	public static String getContDir(String p) {
		return DATASAVEDIR + DELIMITER + p;
	}
}
