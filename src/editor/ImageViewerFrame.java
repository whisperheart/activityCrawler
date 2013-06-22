package editor;

import java.awt.event.ActionEvent;  
import java.awt.event.ActionListener;  
import java.io.File;  
  
import javax.swing.ImageIcon;  
import javax.swing.JFileChooser;  
import javax.swing.JFrame;  
import javax.swing.JLabel;  
import javax.swing.JMenu;  
import javax.swing.JMenuBar;  
import javax.swing.JMenuItem;  
  
  
public class ImageViewerFrame extends JFrame {  
    private JLabel label;  
    private JFileChooser chooser;  
    private static final int DEFAULT_WIDTH = 500;  
    private static final int DEFAULT_HEIGHT = 700;  
  
    public ImageViewerFrame() {  
        super();  
        setTitle("ImageViewer-lihm");  
        setSize(this.DEFAULT_WIDTH,this.DEFAULT_HEIGHT);  
          
        label = new JLabel();  
        add(label);  
          
        this.chooser = new JFileChooser(); 
        chooser.setCurrentDirectory(new File("."));
          
        JMenuBar menuBar = new JMenuBar();  
        setJMenuBar(menuBar);  
          
        JMenu menu = new JMenu("File");  
        menuBar.add(menu);  
          
        JMenuItem openItem = new JMenuItem("OpenImag");  
        menu.add(openItem);  
          
        openItem.addActionListener(new ActionListener(){  
  
            @Override  
            public void actionPerformed(ActionEvent e) {  
                // TODO Auto-generated method stub  
                int result = chooser.showOpenDialog(null);  
                if(result==JFileChooser.APPROVE_OPTION){  
                    String name = chooser.getSelectedFile().getPath();  
                    label.setIcon(new ImageIcon(name));  
                }     
            }  
        });  
          
        JMenuItem exitItem = new JMenuItem("Exit");  
        menu.add(exitItem);  
        exitItem.addActionListener(new ActionListener(){  
  
            @Override  
            public void actionPerformed(ActionEvent e) {  
                // TODO Auto-generated method stub  
                System.exit(0);  
            }  
              
        });  
    }  
  
  
}