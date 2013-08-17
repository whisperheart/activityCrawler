package crawler;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.Iterator;

import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.select.Elements;


public class RssReader {
	
    public static void parse() {
        try
        {
            URL url = new URL("http://www.douban.com/location/beijing/events/feed/weekly");
 //           URL url = new URL("http://www.douban.com/location/shanghai/events/feed/weekly");
            URLConnection uc = url.openConnection();
//	        uc.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");
	        InputStream input = uc.getInputStream();

	        InputStreamReader reader = new InputStreamReader(input, "UTF-8");
//	        XMLReader readerXML = new XMLReader(reader);
            SAXReader saxReader = new SAXReader();
            saxReader.setEncoding("UTF-8"); 
            Document document = saxReader.read(reader);

            Element channel = (Element) document.getRootElement().element("channel");
            for (Iterator i = channel.elementIterator("item"); i.hasNext();)
            {
                Element element = (Element) i.next();
                String title= element.elementText("title"); 
                String link = element.elementText("link");
                String pubDate = element.elementText("pubDate");
                String descrition = element.elementText("description");
                org.jsoup.nodes.Document doc = Jsoup.parseBodyFragment(descrition);
                org.jsoup.nodes.Element body = doc.body();
                Elements img = body.getElementsByTag("img");
                String str = img.attr("src");
                
                Elements other = body.getElementsByTag("p");
                org.jsoup.nodes.Element ele = other.get(0);
                
                //开始时间
                TextNode startNode = (TextNode)ele.childNode(0); 
                String startTime = startNode.text();
                startTime = startTime.substring(startTime.indexOf("：") + 1);
                
                //结束时间
                TextNode endNode = (TextNode)ele.childNode(2); 
                String endTime = endNode.text();
                endTime = endTime.substring(endTime.indexOf("：") + 1);
                
                //地点
                TextNode placeNode = (TextNode)ele.childNode(4); 
                String place = placeNode.text();
                place = place.substring(place.indexOf("：") + 1);
                
                String content = element.elementText("content:encoded");
                System.out.println("description:" + str);
 //               System.out.println("title: " + title);
 //              System.out.println("link: " + element.elementText("link"));
 //               System.out.println("pubDate: " + element.elementText("pubDate"));

 //               Element source = element.element("link");
  //              Attribute attribute = source.attribute("url");
  //              System.out.println("source content: " + source.getStringValue());
   //             System.out.println("source url: " + attribute.getValue());
                System.out.println();
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    
    public static void main(String[] args) {
    	(new RssReader()).parse();
  }
}