package filter;
import java.net.URL;

import javax.swing.text.MutableAttributeSet;
import javax.swing.text.html.HTML;

import constents.ActConst;

public class Damai implements Filter{

	//Judge whether the url will be further parsed
	public boolean urlFilter(URL url) {
		String host = url.getHost();
		String path = url.getPath();
//		System.out.println(url);
//		System.out.println("host:" + host.matches("\\w{1,}.douban.com"));
//		System.out.println((path.matches("/events/today-all") || path.matches("/events/today-all/.{0,}") ));
//		return (host.matches("\\w{1,}.douban.com") && 
//				(path.equalsIgnoreCase("") || path.matches("/location") || path.matches("/location/.{0,}") ));
		return (host.matches("\\w{1,}.douban.com") && 
				(path.matches("/events/today-all") || path.matches("/events/today-all/.{0,}") ));
	}
	
	//Judge whether the url is an actitivity url
	public boolean activityFilter(URL url) {
		String path = url.getPath();
		return path.matches("/event/\\d{1,}/");
	}
	
	//Filter the valuable content from each Web Page 
	public boolean contentFilter(HTML.Tag t,
							MutableAttributeSet a,int pos, int type){
		try {
		switch(type) {
		case 2: return ((t == HTML.Tag.H1) && ((String)a.getAttribute("itemprop")).equals("summary"));
		case 3: return ((t == HTML.Tag.LI) && ((String)a.getAttribute(HTML.Attribute.CLASS)).equals("calendar-str-item "));
		case 4: return ((t == HTML.Tag.A) && ((String)a.getAttribute(HTML.Attribute.CLASS)).equals("label"));
		case 5: return ((t == HTML.Tag.SPAN) && ((String)a.getAttribute("itemprop")).equals("street-address"));
		case 6: return ((t == HTML.Tag.A) && ((String)a.getAttribute("itemprop")).equals("eventType"));
		case 7: return ((t == HTML.Tag.IMG) && ((String)a.getAttribute(HTML.Attribute.ID)).equals("poster_img") 
											&& ((String)a.getAttribute("itemprop")).equals("image"));
//		case 6: return ((t == HTML.Tag.DIV) && ((String)a.getAttribute(HTML.Attribute.CLASS)).equals("wr") 
//			&& (((String)a.getAttribute(HTML.Attribute.ID) == null) || (((String)a.getAttribute(HTML.Attribute.ID)).equals("edesc_f"))));
//		case 7: return (t == HTML.Tag.DIV);
		
		default: return false;
		}
		} catch (NullPointerException e) {
			return false;
		}
	}
	
	public boolean isContent(int p) {
		return (p == 8);
	}
	
	public boolean isContent(String item) {
		return (item.equals("content"));
	}
	
	public boolean contentStart(HTML.Tag t, MutableAttributeSet a) {
		if (a == null) return false;
		if (a.getAttribute(HTML.Attribute.CLASS) == null) return false;
		boolean ok = (t == HTML.Tag.DIV) && ((String)a.getAttribute(HTML.Attribute.CLASS)).equals("wr");
		if (a.getAttribute(HTML.Attribute.ID) == null) return ok;
		else return ((String)a.getAttribute(HTML.Attribute.ID)).equals("edesc_f") && ok;
	}
	
	public boolean contentEnd(HTML.Tag t, MutableAttributeSet a) {
		return  (t == HTML.Tag.DIV);
	}
	
	public String[] repairContent(String[] actContent) {
		 for (int i = 0; i < ActConst.ACTITEM.length; i++)
			 if (isContent(i)) {
				 int length = actContent[i].length();
				 if (length >= 4){
					 char[] charlist = actContent[i].toCharArray();
//					 System.out.println(charlist[length - 4]);
					 if ((charlist[length - 4] == '?') || (charlist[length - 4] == 'Ç')) {
						 actContent[i] = actContent[i].substring(0, length - 4);							
					 }
				 }
		}
		 return actContent;
	}
	
	public boolean getSrc(int p) {
		return (p == 7);
	}
	
	public boolean getSrc(String item) {
		return (item.equals("img"));
	}
}
