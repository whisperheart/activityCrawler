package editor;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingUtilities;

import access.Access;
import access.AccessInterface;
import constents.ActConst;
import constents.FileConst;

public class Editor extends javax.swing.JFrame 
					implements Runnable {
	  protected Thread backgroundThread;
	
	  protected JLabel[] label = new JLabel[9];

	  protected JButton submit = new JButton();
	  
	  protected JButton prev = new JButton();
	  
	  protected JButton next = new JButton();

	  protected JTextField[] text = new JTextField[8];
	  
	  protected JTextPane contentText = new JTextPane();

	  protected JScrollPane scroll = new JScrollPane(contentText);
	  
	  protected JPanel panel = new JPanel();
	  
	  protected String[][] actContentList;
	  	  
	  protected int length;
	  
	  private int actPoint = 0;
	  
	  private JFileChooser chooser; 
	  
	  private JLabel imglabel; 
	  
	  protected Access access;
	  
	public Editor(Access access)
	  {
		this.access = access;
		actContentList = access.fetchActList();
		if (actContentList == null)
			length = 0;
		else
			length = actContentList.length;
	    setTitle("Edit Activity Content");
	    getContentPane().setLayout(null);
	    
	    Dimension dim = getToolkit().getScreenSize(); 
	    int x0 = dim.width/4, y0 = dim.height/4, w = dim.width/2, h = dim.height;
	    this.setBounds(x0, y0, w, h);
	    setVisible(true);
	    
	    int labelx = 20;
	    int imgx = w / 3, imgw = w / 3, imgh = h * 35 / 100;
	    int labelw = w / 10, labelh = 20;
	    int textx = w / 8;
	    int textw = w * 3 / 4, texth = 20, texthc = h / 5;
	    int subx = w / 2, subw = 84, subh = 24;
	    int prevx = subx - w / 8 , prevw = 84, prevh = 24;
	    int nextx = subx + w / 8, nextw = 84, nexth = 24;
	    int y = 0, dy = 24;
	    
        imglabel = new JLabel();  
        getContentPane().add(imglabel); 
        imglabel.setBounds(imgx, y, imgw, imgh);
	    
        y += imgh + 10;
        
	    for (int i = 0; i < 8; i ++) {
	    	label[i] = new JLabel(ActConst.ACTITEM[i]);
	    	getContentPane().add(label[i]);
		    label[i].setBounds(labelx, y, labelw, labelh);
		    if (actContentList == null) 
		    	text[i] = new JTextField("");
		    else
		    	text[i] =  new JTextField(actContentList[0][i]);
	    	getContentPane().add(text[i]);
		    text[i].setBounds(textx, y, textw, texth);
		    y += dy;
	    }
	       
	    label[8] = new JLabel(ActConst.ACTITEM[8]);
	    getContentPane().add(label[8]);
	    label[8].setBounds(labelx, y + texthc / 2, labelw, labelh);
	    
	    if (actContentList == null) 
	    	contentText.setText("");
	    else
	    	contentText.setText(actContentList[0][8]);
	    scroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
	    scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS); 

	    scroll.setBounds(textx, y, textw, texthc);
	    getContentPane().add(scroll);
	    
	    y += texthc + 5;
	    
	    submit.setText("Submit");
	    submit.setActionCommand("Submit");
	    getContentPane().add(submit);
	    submit.setBounds(subx, y,subw, subh);
	    
	    prev.setText("Prev");
	    prev.setActionCommand("Prev");
	    getContentPane().add(prev);
	    prev.setBounds(prevx, y ,prevw, prevh);
	    
	    next.setText("Next");
	    next.setActionCommand("Next");
	    getContentPane().add(next);
	    next.setBounds(nextx, y, nextw, nexth);
	    
	    SubAction lSubAction = new SubAction();
	    submit.addActionListener(lSubAction);
	    
	    PrevAction lPrevAction = new PrevAction();
	    prev.addActionListener(lPrevAction);
	    
	    NextAction lNextAction = new NextAction();
	    next.addActionListener(lNextAction);
        
	    if (actContentList != null) {
	    Object image = access.fetchImage(actContentList[0][0]);
	    if (image instanceof byte[])
	    	imglabel.setIcon(new ImageIcon((byte[])image));
	    else 
	    	imglabel.setIcon(new ImageIcon((String)image));
	    }
	    
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
	  }
	
	
	class SubAction implements ActionListener {
	    public void actionPerformed(java.awt.event.ActionEvent event)
	    {
	      Object object = event.getSource();
	      if ( object == submit )
	        begin_actionPerformed(event);
	    }
	  }

	class PrevAction implements ActionListener {
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
	
	class NextAction implements ActionListener {
	    public void actionPerformed(ActionEvent event)
	    {
	      Object object = event.getSource();
	      if ( object == next )
		    if (actPoint < length - 1) {
		        actPoint += 1;
		        flushEditor();
		    }
	    }
	  }
	
	class ImgAction implements ActionListener {   	 
        @Override  
        public void actionPerformed(ActionEvent e) {  
                // TODO Auto-generated method stub  
                int result = chooser.showOpenDialog(null);  
                if(result==JFileChooser.APPROVE_OPTION){  
                    String posterFile = chooser.getSelectedFile().getPath();             
                    imglabel.setIcon(new ImageIcon(posterFile)); 
					access.saveImage(posterFile, actContentList[actPoint][0], false);
                }     
            }  
	}
	
	class ExitAction implements ActionListener {
	          @Override  
	            public void actionPerformed(ActionEvent e) {  
	                // TODO Auto-generated method stub  
	                System.exit(0);  
	            }  
	  }
	
	  void begin_actionPerformed(ActionEvent event)
	  {
		String[] actContent = new String[9]; 
		for (int i = 0; i < 8; i ++){
			actContent[i] = text[i].getText();
		}
		
		actContent[8] = contentText.getText();
		access.saveAct(actContent);
		access.log("Submit successes.", "");
	  }

	  public void run()
	  {
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
		  for (int i = 0; i < 8; i ++) 
			  text[i].setText(actContentList[actPoint][i]);
		  contentText.setText(actContentList[actPoint][8]);
	        Object image = access.fetchImage(actContentList[actPoint][0]);
		    if (image instanceof byte[])
		    	imglabel.setIcon(new ImageIcon((byte[])image));
		    else 
		    	imglabel.setIcon(new ImageIcon((String)image));
	  }

}
