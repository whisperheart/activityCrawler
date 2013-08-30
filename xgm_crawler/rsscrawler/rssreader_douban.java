package rssCrawler;

import java.io.ByteArrayInputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Iterator;
import java.util.Locale;

import log.CrawlerLog;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.select.Elements;

import editorUI.IMainUI;


public class RssReader_Douban extends RssReader{
	
	public RssReader_Douban(IMainUI mainUI) {
		super(mainUI);
	}
	
	public void parse(String cityURL, String cityName) {
        String id = null;
        String title = null;
        boolean update = false;
        try
        {
        	String xmlString = getXMLString(cityURL);
            SAXReader saxReader = new SAXReader();
            saxReader.setEncoding("UTF-8"); 
            Document document = saxReader.read(new ByteArrayInputStream(xmlString.getBytes("UTF-8")));
            Element channel = (Element) document.getRootElement().element("channel");
            
            for (Iterator i = channel.elementIterator("item"); i.hasNext()&&!cancel;)
            {
            	try {
                Element element = (Element) i.next();
   
                //发布时间
                String pubDateStr = element.elementText("pubDate");
                DateFormat Gmt = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss z",Locale.ENGLISH);  
                long nowPubDate = Gmt.parse(pubDateStr).getTime();  
                
                //活动主题
                title= element.elementText("title"); 
                
                //活动链接
                String link = element.elementText("link");                
                
                if (access.existURL(link)) continue;
                
                mainUI.currentProcess(" (" + cityName + ") " + cityURL, link + " => " + title);
                
                //活动描述
                String descrition = element.elementText("description");
                org.jsoup.nodes.Document doc = Jsoup.parseBodyFragment(descrition);
                org.jsoup.nodes.Element body = doc.body();
                Elements img = body.getElementsByTag("img");
                
                //活动海报
                String imgURL = img.attr("src");
                
                Elements other = body.getElementsByTag("p");
                org.jsoup.nodes.Element ele = other.get(0);
                
                //活动地点
                TextNode placeNode = (TextNode)ele.childNode(4); 
                String place = placeNode.text();
                place = place.substring(place.indexOf("：") + 1);
                
                //活动详情
                String content = "";
                String text = element.elementText("encoded");               
                doc = Jsoup.parseBodyFragment(text);
                body = doc.body();
                Node node;
                org.jsoup.nodes.Element child;
                
                for (int j = 0; j < body.childNodeSize(); j++){
                	node = body.childNode(j);
                	if (node instanceof TextNode) 
                		content += ((TextNode)node).text();
                	else 
                	if (node instanceof org.jsoup.nodes.Element) {
                		child = (org.jsoup.nodes.Element)node;
                		if (child.tagName().equals("br")) 
                			content += "\n";
                	}                		 
                }

                String html = getURLFileContent(link);
                doc = Jsoup.parseBodyFragment(html);
                
                //活动时间
                Elements timeEle = doc.select("li.calendar-str-item ");
                String time = timeEle.get(0).text();
                
                //开始时间
                Elements starttimeEle = doc.select("time[itemprop=startDate]");
                String startTime = starttimeEle.get(0).attr("datetime");
                startTime = startTime.replace('T', ' ');            
                
                //结束时间
                Elements endtimeEle = doc.select("time[itemprop=endDate]");
                String endTime = endtimeEle.get(0).attr("datetime");
                endTime = endTime.replace('T', ' ');                   
                
                //活动类型
                Elements typeEle = doc.select("a[itemprop=eventType]");
                String type = typeEle.get(0).text();
                
                //活动主办方
                Elements div = doc.select("div.event-detail");
                String host = null;
                for(Iterator j = div.iterator(); j.hasNext();)
                {               	
            		org.jsoup.nodes.Element divEle = (org.jsoup.nodes.Element)j.next();	
                    Elements span = divEle.select("span.pl");
                    for(Iterator k = span.iterator(); k.hasNext();)
                    { 
                    	org.jsoup.nodes.Element spanEle = (org.jsoup.nodes.Element)k.next();                   	
                    	if (spanEle.text().equals("主办方:")) {
                    		org.jsoup.nodes.Element hostEle = divEle.getElementsByTag("a").get(0);
                    		host = hostEle.text();
                    		break;
                    	}
                    }
                    if (host != null) break;
            	}
                if (host == null) host = actParser.getDefaultValue("host");
                id = "Douban-" + nowPubDate;
                
                actContent[0] = id;
                actContent[1] = link;
                actContent[2] = title;
                actContent[3] = time;
                actContent[4] = startTime;
                actContent[5] = endTime;
                actContent[6] = cityName;
                actContent[7] = "0";
                actContent[8] = place;
                actContent[9] = type;
                actContent[10] = "0";
                actContent[11] = host;
                actContent[12] = imgURL;
                actContent[13] = content;
                actContent[14] = "0";
                actContent[15] = "2";
                if (access.saveAct(actContent)) 
                	if (access.saveImage(actContent[imgNum], actContent[0], true)) {
                		mainUI.succeedActCount();
                		update = true;
                	} else 
                		mainUI.failedActCount();
                else mainUI.failedActCount();               
            } catch (Exception e) {
            	String msg = "Exception: " + e.toString();
            	failedlog(cityName, cityURL, msg, true);
            	mainUI.failedActCount();
            } 
         }
         if (update) mainUI.succeedRssCount();             
        } catch (Exception e) {
			// TODO Auto-generated catch block
        	String msg = "id<"+ id + "> " + title +" Exception: " + e.toString();
        	failedlog(cityName, cityURL, msg, true);
        	mainUI.failedRssCount();
		}
    }
	
}