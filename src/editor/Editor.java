package editor;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.SwingUtilities;

import constents.FileConst;

import crawler.CheckLinks;

public class Editor extends javax.swing.JFrame 
					implements Runnable {
	  protected Thread backgroundThread;
	
	  protected javax.swing.JLabel[] label = new javax.swing.JLabel[9];

	  protected javax.swing.JButton submit = new javax.swing.JButton();
	  
	  protected javax.swing.JButton prev = new javax.swing.JButton();
	  
	  protected javax.swing.JButton next = new javax.swing.JButton();

	  protected javax.swing.JTextArea[] text = new javax.swing.JTextArea[9];

	  protected javax.swing.JScrollPane errorScroll = new javax.swing.JScrollPane();
	  
	  protected String[][] actContentList;
	  
	  protected String postFile;
	  
//	  protected static int Num;
	  
	  private int actPoint = 0;
	  
	  private JFileChooser chooser; 
	  
	  private JLabel imglabel; 
	  
	public Editor(String[][] actContentList)
	  {
	    //{{INIT_CONTROLS
		this.actContentList = actContentList;
//		this.Num = Num;
	    setTitle("Edit Activity Content");
	    getContentPane().setLayout(null);
	    setSize(500,788);
	    setVisible(true);
	    
	    int labelx = 12;
	    int imgx = 120, imgl = 250, imgw = 280;
	    int textx = 68;
	    int labell = 84, labelw = 20;
	    int textl = 388, textw = 20, textwc = 156;
	    int subx = 200, subl = 84, subw = 24;
	    int prevx = 100, prevl = 84, prevw = 24;
	    int nextx = 300, nextl = 84, nextw = 24;
	    int y = 12, dy = 24;
	    
        imglabel = new JLabel();  
        add(imglabel); 
        imglabel.setBounds(imgx, y, imgl, imgw);
	    
        y += imgw + 10;
        
	    for (int i = 0; i < 9; i ++) {
	    	label[i] = new javax.swing.JLabel(FileConst.LABEL[i]);
	    	getContentPane().add(label[i]);
		    label[i].setBounds(labelx, y, labell, labelw);
	    	text[i] =  new javax.swing.JTextArea(actContentList[0][i]);
	    	getContentPane().add(text[i]);
		    text[i].setBounds(textx, y, textl, textw);
		    y += dy;
	    }
	       
	    text[8].setBounds(textx, y - dy, textl, textwc);
//	    text[8].setToolTipText(actContentList[0][8]);
	    y += textwc;
	    
	    submit.setText("Submit");
	    submit.setActionCommand("Submit");
	    getContentPane().add(submit);
	    submit.setBounds(subx, y,subl, subw);
	    
	    prev.setText("Prev");
	    prev.setActionCommand("Prev");
	    getContentPane().add(prev);
	    prev.setBounds(prevx, y ,prevl, prevw);
	    
	    next.setText("Next");
	    next.setActionCommand("Next");
	    getContentPane().add(next);
	    next.setBounds(nextx, y, nextl, nextw);
	    
	    SubAction lSubAction = new SubAction();
	    submit.addActionListener(lSubAction);
	    
	    PrevAction lPrevAction = new PrevAction();
	    prev.addActionListener(lPrevAction);
	    
	    NextAction lNextAction = new NextAction();
	    next.addActionListener(lNextAction);
        
        postFile = FileConst.getImgFile("0");
        imglabel.setIcon(new ImageIcon(postFile));

	    this.chooser = new JFileChooser(); 
	    chooser.setCurrentDirectory(new File("."));
	          
	    JMenuBar menuBar = new JMenuBar();  
	    setJMenuBar(menuBar);  
	          
	    JMenu menu = new JMenu("File");  
	    menuBar.add(menu);  
	          
	    JMenuItem openItem = new JMenuItem("OpenImag");  
	    menu.add(openItem);  
	    
	    ImgAction imgAction = new ImgAction();
	    openItem.addActionListener(imgAction);  
	          
	    JMenuItem exitItem = new JMenuItem("Exit");  
	    menu.add(exitItem);
	    ExitAction exitAction = new ExitAction();
	    exitItem.addActionListener(exitAction);  
	    //}}
	  }
	
	
	class SubAction implements java.awt.event.ActionListener {
	    public void actionPerformed(java.awt.event.ActionEvent event)
	    {
	      Object object = event.getSource();
	      if ( object == submit )
	        begin_actionPerformed(event);
	    }
	  }

	class PrevAction implements java.awt.event.ActionListener {
	    public void actionPerformed(java.awt.event.ActionEvent event)
	    {
	      Object object = event.getSource();
	      if ( object == prev )
	        if (actPoint > 0) {
	        	actPoint -= 1;
	        	flushEditor();
	        }
	    }
	  }
	
	class NextAction implements java.awt.event.ActionListener {
	    public void actionPerformed(java.awt.event.ActionEvent event)
	    {
	      Object object = event.getSource();
	      if ( object == next )
		    if (actPoint < actContentList.length) {
		        actPoint += 1;
		        flushEditor();
		    }
	    }
	  }
	
	class ImgAction implements java.awt.event.ActionListener {   	 
        @Override  
        public void actionPerformed(ActionEvent e) {  
                // TODO Auto-generated method stub  
                int result = chooser.showOpenDialog(null);  
                if(result==JFileChooser.APPROVE_OPTION){  
                    postFile = chooser.getSelectedFile().getPath();             
                    imglabel.setIcon(new ImageIcon(postFile));  
                }     
            }  
	}
	
	class ExitAction implements java.awt.event.ActionListener {
	          @Override  
	            public void actionPerformed(ActionEvent e) {  
	                // TODO Auto-generated method stub  
	                System.exit(0);  
	            }  
	  }
	
	  void begin_actionPerformed(java.awt.event.ActionEvent event)
	  {
//	    if ( backgroundThread==null ) {
//	      submit.setLabel("Cancel");
//	      backgroundThread = new Thread(this);
//	      backgroundThread.start();

//	    } else {

//	    }
		String[] actContent = new String[9]; 
		for (int i = 0; i < 9; i ++){
			actContent[i] = text[i].getText();
		}
		
		try {
			FileWR.saveAsFile(actContent);
			FileWR.saveAsImg(postFile, actPoint, false);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		FileWR.log("Submit successes.", "");
	  }

	  public void run()
	  {
//	      errors.setText("");
	      Runnable doLater = new Runnable()
	      {
	        public void run()
	        {
	          submit.setText("Begin");
	        }
	      };
	      SwingUtilities.invokeLater(doLater);
	      backgroundThread=null;
	  }
	  
	  void flushEditor() {
		  for (int i = 0; i < 9; i ++) {
		    	label[i].setText(FileConst.LABEL[i]);
		    	text[i].setText(actContentList[actPoint][i]);
		    }
	        imglabel.setIcon(new ImageIcon(FileConst.getImgFile(String.valueOf(actPoint))));
	  }

/*	  
	  static public void main(String args[])
	  {
		  
		ActFetcher fetcher = new ActFetcher(Num);
		actContentList = fetcher.fetchFromFile();
	    (new Editor(actContentList)).setVisible(true);
	  }
*/

}
