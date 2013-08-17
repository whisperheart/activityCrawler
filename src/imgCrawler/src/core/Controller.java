package core;

import java.io.File; 
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.List;

public interface Controller { 
    
    /**
     * 处理图片抓取
     * @param pageUrl 要抓取图片的网页地址
     * @return
     * @throws IOException 
     * @throws MalformedURLException 
     */
    public List<File> fetchImages(String pageUrl,String imgSaveDir) throws MalformedURLException, IOException;
}
