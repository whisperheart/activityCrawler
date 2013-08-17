package editor;

import java.awt.EventQueue;  
import javax.swing.JFrame;  
  
public class ImageViewer {  
  
    /** 
     * @param args 
     */  
    public static void main(String[] args) {  
        // TODO Auto-generated method stub   
        EventQueue.invokeLater(new Runnable(){ 
            @Override  
            public void run() {  
                // TODO Auto-generated method stub  
                JFrame frame = new ImageViewerFrame();  
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);  
                frame.setVisible(true);  
                  
            }  
              
        });  
  
    }  
  
} 