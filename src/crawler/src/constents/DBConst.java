package constents;

public class DBConst {
	
	
	public final static String DRIVER = "com.mysql.jdbc.Driver";
	
	public final static String URL = "jdbc:mysql://127.0.0.1:3306/";
	
	public final static String SCHEMA = "test";
	
	public final static String GBK = "?useUnicode=true&characterEncoding=GBK";
	
	public final static String USER = "root";
	
	public final static String PWD = "";
	
	public final static String ACTURLS = "ACTURLS";
	
	public final static String ACTTABLE = "ACTTABLE";
	
	public final static String POSTERTABLE = "POSTTABLE";
	
	public final static String getSchemaUrl() {
		return URL + SCHEMA;
	}
	
	public final static String getSchemaUrlGBK() {
		return URL + SCHEMA + GBK;
	}
	
	public final static String getActUrlsTable(int website) {
		return ACTURLS + "_" + ActConst.WEBSITE[website];
	}
}
