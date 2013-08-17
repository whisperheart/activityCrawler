package crawler;
import java.io.Serializable;  
import java.util.Map;  
import java.util.concurrent.ConcurrentHashMap;  
 
class CookieManager implements Serializable{  
    private static final long serialVersionUID = 292218695837624307L;  
    private static CookieManager cookieManager = new CookieManager();  
    private Map<String,Map<String,String>> cookies = new ConcurrentHashMap<String, Map<String,String>>();  
      
    private CookieManager(){}  
 
    public String getCookies(String domain){  
        Map<String, String> domainCookies = cookies.get(getTopLevelDomain(domain));  
        if(domainCookies != null){  
            StringBuilder sb = new StringBuilder();  
            boolean isFirst = true;  
            for(Map.Entry<String, String> cookieEntry : domainCookies.entrySet()){  
                if(!isFirst){  
                    sb.append("; ");  
                }else{  
                    isFirst = false;  
                }  
                sb.append(cookieEntry.getKey())  
                  .append("=")  
                  .append(cookieEntry.getValue());  
            }  
            return sb.toString();  
        }  
        return "";  
    }  
       
    public void setCookies(String domain,String cookiesString){  
        Map<String, String> domainCookies = cookies.get(getTopLevelDomain(domain));  
        if(domainCookies == null){  
            domainCookies = new ConcurrentHashMap<String, String>();  
            cookies.put(getTopLevelDomain(domain), domainCookies);  
        }  
        String[] cookies = cookiesString.split("; ");  
        for (String cookie : cookies) {  
            if(cookie != null && !cookie.trim().isEmpty()  
                    && cookie.indexOf("=") > 0){  
                int equalMarkIndex = cookie.indexOf("=");  
                String key = cookie.substring(0,equalMarkIndex);  
                String value = cookie.substring(equalMarkIndex+1);  
                domainCookies.put(key, value);  
            }  
        }  
    }  
 
    public void removeCookies(String domain){  
        cookies.remove(getTopLevelDomain(domain));  
    }  
      

    public static CookieManager getInstance(){  
        return cookieManager;  
    }  
      

    public String getTopLevelDomain(String domain){  
        if(domain == null){  
            return null;  
        }  
        if(!domainToTopLevelDomainMap.containsKey(domain)){  
            String[] splits = domain.split("\\.");  
            domainToTopLevelDomainMap.put(domain, (splits[splits.length-2] + "." + splits[splits.length -1]));  
        }  
        return domainToTopLevelDomainMap.get(domain);  
    }  

    private Map<String,String> domainToTopLevelDomainMap = new ConcurrentHashMap<String, String>();  
}  