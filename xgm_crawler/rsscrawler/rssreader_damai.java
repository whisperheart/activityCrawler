package rssCrawler;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import log.CrawlerLog;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.select.Elements;

import editorUI.IMainUI;


public class RssReader_Damai extends RssReader{
	
	public RssReader_Damai(IMainUI mainUI) {
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
            int num = 0;
            
            for (Iterator i = channel.elementIterator("item"); i.hasNext()&&!cancel;) {
            	try {
                Element element = (Element) i.next();
                
                //发布时间
                String pubDateStr = element.elementText("context");
                DateFormat Gmt = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss z",Locale.ENGLISH);  
                long nowPubDate = Gmt.parse(pubDateStr).getTime();  
                
                //活动主题
                title= element.elementText("title"); 
                
                //活动链接
                String link = element.elementText("link");                
                if (access.existURL(link)) continue;                            
                
                mainUI.currentProcess("(" + cityName + ")" + cityURL, link + "=>" + title);
                
                //活动描述
                String descrition = element.elementText("description");
                org.jsoup.nodes.Document doc = Jsoup.parseBodyFragment(descrition);
                org.jsoup.nodes.Element body = doc.body();
                                          
                //活动地点
                org.jsoup.nodes.Element placeEle = (org.jsoup.nodes.Element)body.childNode(3); 
                String place = placeEle.text();           

                //解析活动主页
                String html = getURLFileContent(link);
                doc = Jsoup.parseBodyFragment(html);
                
                //活动时间
                String time = "";
                Elements performEles = doc.select("div.ri-t");
                if (performEles.size() > 0) 
                for(Iterator j = performEles.iterator(); j.hasNext();)
                {               	
            		org.jsoup.nodes.Element divEle = (org.jsoup.nodes.Element)j.next();	           		
            		Elements timeEles = divEle.getElementsByTag("time");
            		if (timeEles.size() > 0) {           			
            			org.jsoup.nodes.Element timeEle = timeEles.get(0);
            			time = timeEle.attr("datetime");
            			time = time.replace('T', ' ');
            			break;
            		}
            	} else {
            		performEles = doc.select("dd[id=perform]");                
            		if (performEles.size() > 0) {
            			org.jsoup.nodes.Element timeEle = performEles.get(0);
            			try {
            				TextNode timeNode = (TextNode)timeEle.childNode(2);
            				time = timeNode.text();
            			} catch (java.lang.ClassCastException e) {
            				e.printStackTrace();
            				System.out.println(link);
            			}     
            		}
            	}
                
                //开始时间
                String startTime = time;             
                
                //结束时间
                String endTime = time;
                   
                //活动类型
                Elements hereEles = doc.select("div.here");
                if (hereEles.size() == 0)
                	System.out.println(link);
                org.jsoup.nodes.Element hereEle = hereEles.get(0);
                org.jsoup.nodes.Element typeEle = (org.jsoup.nodes.Element) hereEle.childNode(4);
                String type = typeEle.text();
                
                //活动详情
                String content = "";
                Elements contentEles = doc.select("div[class=tab-txt-inner clear c1]");
                org.jsoup.nodes.Element contentEle = (org.jsoup.nodes.Element) contentEles.get(0);
                content = getActContent(contentEle);
                
                //活动海报
                Elements divEles = doc.select("div.goods-basic-poster");
                org.jsoup.nodes.Element divEle = divEles.get(0);
                Elements imgEles = divEle.getElementsByTag("img");
                org.jsoup.nodes.Element imgEle = imgEles.get(0);
                String imgSrc = imgEle.attr("src");
                id = "Damai-" + nowPubDate + "_" + num++;
                
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
                actContent[11] = actParser.getDefaultValue("host");
                actContent[12] = imgSrc;
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
        }  catch (Exception e) {
			// TODO Auto-generated catch block
        	String msg = "";
        	if (id != null) 
        		msg = "id<"+ id + "> ";
        	if (title != null)
        		msg += title + " ";
        	msg += "Exception: " + e.toString();
        	failedlog(cityName, cityURL, msg, true);
        	mainUI.failedRssCount();
		}
    }
    
	protected String getActContent(org.jsoup.nodes.Element ele) {
		String text = "";
		if (ele != null) {
			 List<Node> children = ele.childNodes();
             org.jsoup.nodes.Element child;
             for (int j = 0; j < children.size(); j++) {
             	Node node = children.get(j);
             	if (node instanceof TextNode) {
             		String str = ((TextNode)node).text();
             		str = str.replaceAll("/r/n", "\n"); 
             		text += str;
             	} else 
             		if (node instanceof org.jsoup.nodes.Element) {
                 		child = (org.jsoup.nodes.Element)node;
                 		if (child.tagName().equals("br")) 
                 			text += "\n";
                 		else text += getActContent(child);
             		}
             }
		}
		return text;		
	}
	
}