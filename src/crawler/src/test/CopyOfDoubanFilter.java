package test;

import java.net.URL;

import javax.swing.text.MutableAttributeSet;
import javax.swing.text.html.HTML;

class CopyOfDoubanFilter {

	public static boolean URLFilter(URL url) {
		String host = url.getHost();
		String path = url.getPath();
		return (host.matches("\\w{1,}.douban.com") && 
				(path.equalsIgnoreCase("") || path.matches("/location") || path.matches("/location/.{0,}") ));
	}
	public static boolean activityFilter(URL url) {
		String path = url.getPath();
		return path.matches("/event/\\d{1,}/");
	}
	public static boolean contentFilter(HTML.Tag t,
							MutableAttributeSet a,int pos, int type){
		try {
		switch(type) {
		case 0: return ((t == HTML.Tag.A) && ((String)a.getAttribute(HTML.Attribute.CLASS)).equals("label"));
		case 1: return ((t == HTML.Tag.H1) && ((String)a.getAttribute("itemprop")).equals("summary"));
		case 2: return ((t == HTML.Tag.LI) && ((String)a.getAttribute(HTML.Attribute.CLASS)).equals("calendar-str-item "));
		case 3: return ((t == HTML.Tag.SPAN) && ((String)a.getAttribute("itemprop")).equals("street-address"));
		case 4: return ((t == HTML.Tag.IMG) && ((String)a.getAttribute(HTML.Attribute.ID)).equals("poster_img") 
											&& ((String)a.getAttribute("itemprop")).equals("image"));
		case 5: return ((t == HTML.Tag.A) && ((String)a.getAttribute("itemprop")).equals("eventType"));
		case 6: return ((t == HTML.Tag.DIV) && ((String)a.getAttribute(HTML.Attribute.CLASS)).equals("wr") 
			&& (((String)a.getAttribute(HTML.Attribute.ID) == null) || (((String)a.getAttribute(HTML.Attribute.ID)).equals("edesc_f"))));
		case 7: return (t == HTML.Tag.DIV);
		
		default: return false;
		}
		} catch (NullPointerException e) {
			return false;
		}
	}
}
