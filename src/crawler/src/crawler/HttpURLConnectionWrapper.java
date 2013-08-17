package crawler;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

public class HttpURLConnectionWrapper extends HttpURLConnection {  
      
    HttpURLConnection httpURLConnection;  

    CookieManager cookieManager = CookieManager.getInstance();  
      
    public HttpURLConnectionWrapper(URL u) throws IOException {  
        super(u);  
        httpURLConnection = (HttpURLConnection) u.openConnection();  
        setFollowRedirects(false);  
        fillRequestHeadField();  
    }  
 
    private void fillRequestHeadField(){  
        httpURLConnection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 5.1; rv:5.0) Gecko/20100101 Firefox/5.0");  
        httpURLConnection.setRequestProperty("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");  
        httpURLConnection.setRequestProperty("Accept-Language", "zh-cn,zh;q=0.5");  
        httpURLConnection.setRequestProperty("Accept-Encoding", "GB2312,utf-8;q=0.7,*;q=0.7");  
        httpURLConnection.setRequestProperty("Referer", "http://movie.douban.com/");  
        httpURLConnection.setRequestProperty("Cache-Control", "max-age=0");  
        httpURLConnection.setRequestProperty("Cookie", cookieManager.getCookies(url.getHost()));  
          
    }  
  
    @Override  
    public InputStream getInputStream() throws IOException {  
        InputStream is = httpURLConnection.getInputStream();  
    
        resolveCookies();  
        int responseCode = getResponseCode();  
        if(responseCode != 200 && responseCode != 404 ){  
           
            CookieManager.getInstance().removeCookies(url.getHost());  
            try{  
                httpURLConnection.disconnect();  
                is.close();  
            }catch (Exception e) {  
            }  
            httpURLConnection = (HttpURLConnection) this.getURL().openConnection();  
            setFollowRedirects(false);  
            fillRequestHeadField();  
            is = httpURLConnection.getInputStream();  
        }  
        return is;  
    }  
    private void resolveCookies(){  
        List<String> setCookies = getHeaderFields().get("Set-Cookie");  
        if(setCookies != null && !setCookies.isEmpty()){  
            for (String setCookie : setCookies) {  
                cookieManager.setCookies(this.url.getHost(), setCookie);  
            }     
        }  
    }
	@Override
	public void disconnect() {
		// TODO Auto-generated method stub
		
	}
	@Override
	public boolean usingProxy() {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public void connect() throws IOException {
		// TODO Auto-generated method stub
		
	}  

}  