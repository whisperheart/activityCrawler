package filter;

import java.net.URL;

import javax.swing.text.MutableAttributeSet;
import javax.swing.text.html.HTML;

public interface Filter {
	public boolean urlFilter(URL url);
	public boolean activityFilter(URL url);
	public boolean contentFilter(HTML.Tag t,
			MutableAttributeSet a,int pos, int type);
	
	public boolean isContent(int p);
	public boolean isContent(String item);
	public String[] repairContent(String[] actContent);
	public boolean contentStart(HTML.Tag t, MutableAttributeSet a);
	public boolean contentEnd(HTML.Tag t, MutableAttributeSet a);
	public boolean getSrc(int p);
	public boolean getSrc(String item);
}
