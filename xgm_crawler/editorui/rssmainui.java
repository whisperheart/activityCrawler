package editorUI;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Calendar;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;

import rssCrawler.RssCrawler;

public class RssMainUI extends JFrame implements IMainUI{
	
	private static final long serialVersionUID = 1L;
	private final int DEFAULT_WIDTH = 900;
	private final int DEFAULT_HEIGHT = 560;
	private final int DEFAULT_X = 160;
	private final int DEFAULT_Y = 90;
	
	private final JButton crawl = new JButton();
	private final JScrollPane logScroll = new JScrollPane();
	private final JTextArea log = new JTextArea();
	private final JLabel lcurrentRss = new JLabel();
	private final JLabel lcurrentAct = new JLabel();
	private final JLabel goodCrawlLabel = new JLabel();
	private final JLabel badCrawlLabel = new JLabel();
	
	protected Thread backgroundThread = null;
	protected RssCrawler rssCrawler;
	
	protected int badCrawlRssCount = 0;
	protected int badCrawlActCount = 0;
	protected int goodCrawlRssCount = 0; 
	protected int goodCrawlActCount = 0; 
	protected String currentRss = "";
	protected String currentAct = "";
	protected String currentLog = "";
	private int oldtime = 0;
	
	public RssMainUI()  {
	        setTitle("想干嘛活动爬虫");
	        setBounds(DEFAULT_X,DEFAULT_Y,DEFAULT_WIDTH, DEFAULT_HEIGHT);
	        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			getContentPane().setLayout(null);
			setVisible(true);
	        
	        //MenuBar
	        final JMenuBar fileMenuBar = new JMenuBar();
	        setJMenuBar(fileMenuBar);
	        
	        //File Menu
	        final JMenu fileMenu = new JMenu("File");
	        
	        fileMenuBar.add(fileMenu);
		    final JMenuItem exitItem = new JMenuItem("Exit");  
		    fileMenu.add(exitItem);
		    final ExitAction exitAction = new ExitAction();
		    exitItem.addActionListener(exitAction); 
		    
		    //Buttons
		    
		    final ButtonAction buttonAction = new ButtonAction();
		    
		    crawl.setText("Crawl");
		    crawl.setActionCommand("Crawl");
		    crawl.setBounds(760,470,84,24);
		    crawl.addActionListener(buttonAction);
		    getContentPane().add(crawl);
		    
		    logScroll.setAutoscrolls(true);
		    logScroll.setHorizontalScrollBarPolicy(javax.swing.
		                ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
		    logScroll.setVerticalScrollBarPolicy(javax.swing.
		                ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		    logScroll.setOpaque(true);
		    getContentPane().add(logScroll);
		    logScroll.setBounds(26,100,850,356);
		    log.setEditable(false);
		    logScroll.getViewport().add(log);
		    log.setBounds(0,0,850,138);
		    lcurrentRss.setText("Currently Processing Rss: ");
		    getContentPane().add(lcurrentRss);
		    lcurrentRss.setBounds(26,18,800,20);
		    lcurrentAct.setText("Currently Processing Activity: ");
		    getContentPane().add(lcurrentAct);
		    lcurrentAct.setBounds(26,39,800,20);
		    goodCrawlLabel.setText("Succeed Activity(Rss) Crawling: 0(0)");
		    getContentPane().add(goodCrawlLabel);
		    goodCrawlLabel.setBounds(26,60,300,20);
		    badCrawlLabel.setText("Failed Activity(Rss) Crawling: 0(0)");
		    getContentPane().add(badCrawlLabel);
		    badCrawlLabel.setBounds(320,60,400,20);		    
	}
	  
	class ExitAction implements ActionListener {
	        @Override  
	          public void actionPerformed(final ActionEvent e) {  
	              // TODO Auto-generated method stub  
	              System.exit(0);  
	          }  
	}
	
	class ButtonAction implements ActionListener {
	       
		public void actionPerformed(final ActionEvent event) {
	        final Object object = event.getSource();
	        if ( object == crawl ) 	            	
	       		 begin_actionPerformed(event);
	    }
	}
	     
	void begin_actionPerformed(final ActionEvent event) {

	   if (backgroundThread == null) {	       			
		   crawl.setText("Cancel");
		   final BeginThread beginThread = new BeginThread(this);
	       backgroundThread = new Thread(beginThread);
	       backgroundThread.start();
	       goodCrawlActCount = 0;
	       goodCrawlRssCount = 0;
	       badCrawlActCount = 0;
	       badCrawlRssCount = 0;
	    } else {
	       rssCrawler.cancel();
	    }
	}
	
	class BeginThread implements Runnable {
		private final IMainUI parent;
		BeginThread(final IMainUI parent) {
			this.parent = parent;
		}
		    
		public void run(){			      
			rssCrawler = new RssCrawler(parent);
			rssCrawler.begin();
		    crawl.setText("Crawl");
		    backgroundThread = null;
		}
	}

	class UpdateCurrentProcess implements Runnable {
		public void run(){
			lcurrentRss.setText("Currently Processing Rss: " + currentRss);
	    	lcurrentAct.setText("Currently Processing Activity: " + currentAct);
	    }
	}
	
	class UpdateCount implements Runnable {
		public void run(){
			goodCrawlLabel.setText("Succeed Activity(Rss) Crawling: "+goodCrawlActCount+"("+goodCrawlRssCount+")");
			badCrawlLabel.setText("Failed Activity(Rss) Crawling: "+badCrawlActCount+"("+badCrawlRssCount+")");
		}
	}
	
	class UpdateText implements Runnable {
		public void run() {		    	
		    log.setText(currentLog);
		}
	}
	 
	public void currentProcess(String rssMSG, String actMSG) {
		currentRss = rssMSG;
		currentAct = actMSG;
		final UpdateCurrentProcess cs = new UpdateCurrentProcess();
		SwingUtilities.invokeLater(cs);
	}
	
	public synchronized void succeedActCount() {
		goodCrawlActCount++;
		UpdateCount count = new UpdateCount();
		SwingUtilities.invokeLater(count);
	}
	
	public synchronized void succeedRssCount() {
		goodCrawlRssCount++;
		final UpdateCount count = new UpdateCount();
		SwingUtilities.invokeLater(count);
	}
	
	public synchronized void failedActCount() {
		badCrawlActCount++;
		final UpdateCount count = new UpdateCount();
		SwingUtilities.invokeLater(count);
	}
	
	public synchronized void failedRssCount() {
		badCrawlRssCount++;
		final UpdateCount count = new UpdateCount();
		SwingUtilities.invokeLater(count);
	}
		
	public synchronized void currentLog(final String msg) {
		final UpdateText text = new UpdateText();
		Calendar cal = Calendar.getInstance();
	    int month = cal.get(Calendar.MONTH);
	    if (month != oldtime){
	    	currentLog = "";
	    	oldtime = month;
	    };
		currentLog += msg + "\n";
		SwingUtilities.invokeLater(text);
	}
	   
	public static void main(final String[] args) {
		   (new RssMainUI()).setVisible(true);
	 }

}
