package editor;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;

import access.Access;
import access.AccessInterface;
import access.DBAccess;
import access.FileAccess;

import constents.ActConst;
import crawler.ContentParser;

public class MainUI extends JFrame{

	private final int DEFAULT_WIDTH = 540;
	private final int DEFAULT_HEIGHT = 260;
	private final int DEFAULT_X = 160;
	private final int DEFAULT_Y = 90;
	
	  JButton crawl = new JButton();
	  JButton parse = new JButton();
	  JButton edit = new JButton();
	
	  boolean[] webSiteMark = new boolean[ActConst.WEBSITE.length];
	  boolean savedb;
	  boolean savefile;
	  
	  JCheckBoxMenuItem[] checkbox = new JCheckBoxMenuItem[ActConst.WEBSITE.length];
	  JCheckBoxMenuItem database;
	  JCheckBoxMenuItem filesystem;
	  
	  JCheckBoxMenuItem actConfig;
	  JCheckBoxMenuItem dbConfig;
	  JCheckBoxMenuItem fileConfig = new JCheckBoxMenuItem("Filesystem");
	  
	  Access access = new Access();

	  public MainUI()  {
	        setTitle("MenuFrame");
	        setBounds(DEFAULT_X,DEFAULT_Y,DEFAULT_WIDTH, DEFAULT_HEIGHT);
	        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

	        
	        //MenuBar
	        JMenuBar fileMenuBar = new JMenuBar();
	        setJMenuBar(fileMenuBar);
	        
	        //File Menu
	        JMenu fileMenu = new JMenu("File");
	        
	        fileMenuBar.add(fileMenu);
		    JMenuItem exitItem = new JMenuItem("Exit");  
		    fileMenu.add(exitItem);
		    ExitAction exitAction = new ExitAction();
		    exitItem.addActionListener(exitAction); 

		    //WebSite Menu
		    JMenu webSiteMenu = new JMenu("WebSite");
		    fileMenuBar.add(webSiteMenu);
		    CheckAction checkaction = new CheckAction();
		    
		    for (int i = 0; i < ActConst.WEBSITE.length; i++)
		    {
		    	 checkbox[i] = new JCheckBoxMenuItem(ActConst.WEBSITE[i]);
		    	 webSiteMenu.add(checkbox[i]);		    	 
		    	 checkbox[i].addActionListener(checkaction);
		    }
		    
		    //Save Mode
		    JMenu saveMode = new JMenu("SaveMode");
		    fileMenuBar.add(saveMode);
		    database = new JCheckBoxMenuItem("Database");
		    database.addActionListener(checkaction);
		    saveMode.add(database);
		    filesystem = new JCheckBoxMenuItem("FileSystem");
		    filesystem.addActionListener(checkaction);
		    saveMode.add(filesystem);
		    
		  //Save Mode
		    JMenu configMode = new JMenu("Config");
		    fileMenuBar.add(configMode);
		    
		    actConfig = new JCheckBoxMenuItem("Activity");
//		    actConfig.addActionListener(checkaction);
		    configMode.add(actConfig);
		    
		    dbConfig = new JCheckBoxMenuItem("Database");
//		    dbConfig.addActionListener(checkaction);		    
		    configMode.add(dbConfig);
		    
		    fileConfig = new JCheckBoxMenuItem("FileSystem");
//		    fileConfig.addActionListener(checkaction);
		    configMode.add(fileConfig);
		    
		    //Buttons
		    JPanel pan=new JPanel();
		    getContentPane().add(pan);
		    pan.setBounds(100, 100, 300, 300);
		    
		    ButtonAction buttonAction = new ButtonAction();
		    
		    crawl.setText("Crawl");
		    crawl.setActionCommand("Crawl");
		    crawl.setBounds(52,56,84,24);
		    crawl.addActionListener(buttonAction);
		    pan.add(crawl);
		    
		    parse.setText("Parse");
		    parse.setActionCommand("Parse");
		    parse.setBounds(52,80,84,24);
		    parse.addActionListener(buttonAction);
		    pan.add(parse);
		    
		    edit.setText("Edit");
		    edit.setActionCommand("Edit");
		    edit.setBounds(52,120,84,24);
		    edit.addActionListener(buttonAction);
		    pan.add(edit);
		   
		    
	    }
	
	class ExitAction implements ActionListener {
        @Override  
          public void actionPerformed(ActionEvent e) {  
              // TODO Auto-generated method stub  
              System.exit(0);  
          }  
	}
        
    class ButtonAction implements ActionListener {
       public void actionPerformed(ActionEvent event) {
              Object object = event.getSource();
              if ( object == crawl ) {
                (new CrawlUI(webSiteMark, access)).setVisible(true);
            } else
                if ( object == parse ) {
                  (new ParseUI(webSiteMark, access)).setVisible(true);
              } else
            	  if ( object == edit ) {        			
        			Editor editor = new Editor(access);
        			editor.setVisible(true);
                  }
          }
    }
    
    class CheckAction implements ActionListener {
    	 public void actionPerformed(ActionEvent event) {
    		 JCheckBoxMenuItem object = (JCheckBoxMenuItem)event.getSource();
    		 if (object == database) {
    			 savedb = object.isSelected();
    			 access.changeState(savedb, savefile);
    			 System.out.println("database " + savedb);
    		 } else 
    		 if (object == filesystem) {
    			savefile = object.isSelected(); 
    			access.changeState(savedb, savefile);
    			System.out.println("filesystem " + savefile);
    		 } else 
    		 for (int i = 0; i < ActConst.WEBSITE.length; i++)
    			 if (checkbox[i] == object) {
    				webSiteMark[i] = object.isSelected();
    				System.out.println(ActConst.WEBSITE[i] + webSiteMark[i]);
    				break;
    			 }
    	 }
    }

 public static void main(String[] args) {
	   (new MainUI()).setVisible(true);
 }
}
