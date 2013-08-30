package configParser;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

public abstract class XMLConfigParser {

	public final static String configFile = "D:/xampp/htdocs/manager/config/ActConfig.xml";
	
	protected Element getRootElement() {		
		File conf = new File(configFile);
		FileInputStream input;
		try {
			input = new FileInputStream(conf);
		
	    InputStreamReader reader = new InputStreamReader(input, "UTF-8");
	    SAXReader saxReader = new SAXReader();
	    //saxReader.setEncoding("UTF-8"); 
	    Document document = saxReader.read(reader);
	    Element root = document.getRootElement();
	    return root;
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
	
	protected abstract Element getMainElement();
	
	public String getDefaultValue(String valueName) {
		Element main = getMainElement();
		Element defaultEle = main.element("default");
		String value = defaultEle.elementText(valueName);
		return value;
	}
	
	public String getLog() {
		Element main = getMainElement();
		String log = main.elementText("log");
		return log;
	}
	
}
