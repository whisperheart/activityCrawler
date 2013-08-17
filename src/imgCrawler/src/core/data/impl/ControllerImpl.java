package core.data.impl;

import java.io.File; 
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JTextArea; 

import ui.MainWin; 

import core.Controller; 
import core.data.DataFetcher;
import core.data.DataHandler;
import core.data.impl.DataFetcherImpl;
import core.data.impl.DataHandlerImpl;

public class ControllerImpl implements Controller { 

    private MainWin mainWin;
    private JTextArea messageArea;
    private DataFetcher fetcher = new DataFetcherImpl();
    private DataHandler hander = new DataHandlerImpl();

    public ControllerImpl(MainWin mainWin) {
        this.mainWin = mainWin;
        this.messageArea = mainWin.getMessageArea();
    }

    @Override
    public List<File> fetchImages(String pageUrl, String imgSaveDir)
            throws MalformedURLException, IOException {
        
        // 获取html页面
        StringBuffer page = fetcher.fetchHtml(pageUrl);

        // 获取页面中的地址
        List<String> imgUrls = hander.getImageUrls(page);

        // 保存图片，返回文件列表
        List<File> fileList = new ArrayList<File>();
        int i = 1;
        for (String url : imgUrls) {
            File file = fetcher.fecthFile(url, imgSaveDir + "//" + i + ".jpg");
            System.out.println(file.getPath()
                    + " 下载完成！");
            messageArea.setText(
                    messageArea.getText() + "/n" + file.getPath()
                            + " 下载完成！");
            mainWin.update(mainWin.getGraphics());
            fileList.add(file);
            i++;
        }
        
        messageArea.setText(
                messageArea.getText() + "/n" + " 任务完成，共下载"+fileList.size()+"个图片!");
        
        return fileList;
    }

}