package crawler;

import java.io.IOException;
import java.net.URL;

public interface IParserReportable {
	public void processURL(int website, int num, URL url) throws IOException;
	
}
