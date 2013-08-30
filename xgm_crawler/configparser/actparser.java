package configParser;

import java.util.ArrayList;
import java.util.Iterator;

import org.dom4j.Element;

public class ActParser extends XMLConfigParser {
	
//	public final static String[] WEBSITE = {"Douban", "Damai"};
	
//	public final static String[] WebEntry = {"http://beijing.douban.com/events/today-all", "http://www.douban.com/location/world/"};
	
//	public final static String[] ACTITEM = {"activity", "url", "title", "time", "starttime", "endtime", "city", "place", "type", "img", "content","edit","recent"};
	
//	public final static String[][] CITY = {{"beijing","北京"}, {"shanghai","上海"},  {"guangzhou","广州"},  {"shenzhen","深圳"},  {"changsha","长沙"}};

	public static int imgNum = -1;
	
	public Element getMainElement() {
		Element root = getRootElement();
		if (root == null) return null;
		Element activity = root.element("activity");
		return activity;
	}
	
	public String[] getActivityItems() {
		
		ArrayList<String> arr = new ArrayList<String>();
		Element activity = getMainElement();
		if (activity==null) return null;
		Element content = activity.element("content");
		int count = 0;
		for (Iterator i = content.elementIterator("item"); i.hasNext();)
		    {
		        Element itemEle = (Element) i.next();	
		        if (itemEle.attributeValue("type") != null)
		        	if (itemEle.attributeValue("type").equals("image")) imgNum = count;
		        count++;
		        String itemStr = itemEle.getText();
		        arr.add(itemStr);
		    }
		String[] items = new String[arr.size()];
		arr.toArray(items);
		return items;
	}

    public static void main(String[] args) {
    	String host = (new ActParser()).getDefaultValue("host");
    	System.out.println(host);
    }
}
