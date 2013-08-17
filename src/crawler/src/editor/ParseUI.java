package editor;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import javax.swing.text.MutableAttributeSet;
import javax.swing.text.html.HTML;
import javax.swing.text.html.HTMLEditorKit;

import access.Access;
import access.AccessInterface;
import constents.ActConst;
import crawler.ContentParser;
import crawler.HTMLParse;
import crawler.IParserReportable;
import filter.Filter;

public class ParseUI extends JFrame implements IParserReportable, Runnable{
	protected String nowPath;
	protected String imgSaveDir;
		
	protected String[] actContent = new String[ActConst.ACTITEM.length];
	protected int website;
	
	protected Access access;
	protected boolean[] webSiteMark;
	protected boolean[] selectWebsite = new boolean[ActConst.WEBSITE.length];
	protected Filter[] filter = new Filter[ActConst.WEBSITE.length]; 
	
	protected JLabel label = new JLabel();
	protected JLabel label2 = new JLabel();
	protected JButton begin = new JButton();
	
	protected JScrollPane messageScroll = new JScrollPane();
	protected JTextArea message = new JTextArea();
	protected JCheckBox[] checkbox = new JCheckBox[ActConst.WEBSITE.length];
	protected JPanel panel = new JPanel();
	
	protected Thread backgroundThread;
	protected ContentParser parser;
	
	public ParseUI(boolean[] webSiteMark, Access access) {
		this.webSiteMark = webSiteMark;
		this.access = access;
		
	    setTitle("Parse Website Links");
	    getContentPane().setLayout(null);
	    setSize(505,410);
	    setVisible(true);
		
		label.setText("Choose websites:");
		getContentPane().add(label);
		label.setBounds(12,12,150,20);
		
		getContentPane().add(panel);
		panel.setBounds(12, 30, 200, 30);
		
		CheckAction checkAction = new CheckAction();
		
		try {
		for (int i = 0; i < ActConst.WEBSITE.length; i++) {
				selectWebsite[i] = false;
				if (webSiteMark[i]) {
					checkbox[i] = new JCheckBox(ActConst.WEBSITE[i]);
					panel.add(checkbox[i]);
					checkbox[i].setBounds((i + 1) * 50, 0, 80, 20);
					checkbox[i].addActionListener(checkAction);
					filter[i] = (Filter)Class.forName("filter." + ActConst.WEBSITE[i]).newInstance();
				}
		}
		} catch (InstantiationException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
		} catch (IllegalAccessException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
		} catch (ClassNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
		}
		  
		label2.setText("Parse messages: ");
		getContentPane().add(label2);
		label2.setBounds(12,90,150,20);
		
		messageScroll.setAutoscrolls(true);
		messageScroll.setHorizontalScrollBarPolicy(javax.swing.
	                ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
		messageScroll.setVerticalScrollBarPolicy(javax.swing.
	                ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		messageScroll.setOpaque(true);
	    getContentPane().add(messageScroll);
	    messageScroll.setBounds(12,120,430,216);
	    message.setEditable(false);
	    messageScroll.getViewport().add(message);
	    message.setBounds(0,0,366,138);
		
	    begin.setText("Begin");
	    begin.setActionCommand("Begin");
	    getContentPane().add(begin);
	    begin.setBounds(12,350,84,24);
	    BeginAction beginAction = new BeginAction();
	    begin.addActionListener(beginAction);
	}
	
	public void processURL(int website, int num, URL url) throws IOException {
	      // get the URL's contents
//	      HttpURLConnectionWrapper connection = new HttpURLConnectionWrapper(url);
	      URLConnection connection = url.openConnection();
	      if ( (connection.getContentType()!=null) &&
	           !connection.getContentType().toLowerCase().startsWith("text/") ) {
	        return;
	      }
	      
	      // read the URL
	      InputStream is = connection.getInputStream();
	      Reader r = new InputStreamReader(is,"UTF-8");
	      // parse the URL
	      HTMLEditorKit.Parser parse = new HTMLParse().getParser();
	      for (int i = 0; i < ActConst.ACTITEM.length; i++) 
	    	  actContent[i] = "";
	      actContent[0] = ActConst.WEBSITE[website] + String.valueOf(num);
		  actContent[1] = url.toString();
	      parse.parse(r,new Parser(filter[website], url),true);

/*
	      if (actContent[2] == "") {
				 try {
					 Thread.sleep(10000);
					 access.log(url.toString(), "Request failed! Process again.");
				 } catch (InterruptedException e) {
					 // TODO Auto-generated catch block
					 e.printStackTrace();
				 }
				 processURL(website, num, url);
			 }
*/
			actContent = filter[website].repairContent(actContent); 
			log(url.toString() + ": finish processing. \n");
			access.saveAct(actContent);
			access.saveImage(actContent[ActConst.imgNum], actContent[0], true);

	    }
	    // mark URL as complete
	 
	 public void run()
	  {
	      parser = new ContentParser(selectWebsite, access, this);
	      parser.begin();
	      Runnable doLater = new Runnable()
	      {
	        public void run()
	        {
	          begin.setText("Begin \n");
	        }
	      };
	      SwingUtilities.invokeLater(doLater);
	      backgroundThread=null;
	  }
	 
	class BeginAction implements ActionListener {
		    public void actionPerformed(ActionEvent event)
		    {
		      Object object = event.getSource();
		      if ( object == begin )
		        begin_actionPerformed(event);
		    }
		  }
	 
	void begin_actionPerformed(java.awt.event.ActionEvent event)
	  {
	    if ( backgroundThread==null ) {
	      access.clear(-1);
	      begin.setLabel("Cancel");
	      backgroundThread = new Thread(this);
	      backgroundThread.start();
	    } else {
	      parser.cancel();
	    }
	  }
	
	   class CheckAction implements ActionListener {
	    	 public void actionPerformed(ActionEvent event) {
	    		 JCheckBox object = (JCheckBox)event.getSource();
	    		
	    		 for (int i = 0; i < ActConst.WEBSITE.length; i++)
	    			 if (checkbox[i] == object) {
	    				selectWebsite[i] = object.isSelected();
	    				System.out.println(ActConst.WEBSITE[i] + selectWebsite[i]);
	    				break;
	    			 }
	    	 }
	    }
	   
	   protected class Parser
		  extends HTMLEditorKit.ParserCallback {
		    protected URL base;
		    protected Filter filter;
		    boolean[] itemMark = new boolean[ActConst.ACTITEM.length];
		    boolean br = false;
		    
		   public Parser(Filter filter, URL base)
		    {
		      this.base = base;
		      this.filter = filter;
		      for (int i = 0; i < ActConst.ACTITEM.length; i++)
		    	  itemMark[i] = false;
		    }
		  public void handleSimpleTag(HTML.Tag t,
	               MutableAttributeSet a,int pos) {	
			for (int i = 0; i < ActConst.ACTITEM.length; i ++) 
				if (!filter.isContent(i)) {
				itemMark[i] = filter.contentFilter(t, a, pos, i);
				if (filter.getSrc(i) && (itemMark[i])) 
					actContent[i] = (String)a.getAttribute(HTML.Attribute.SRC);			
				} else
					if (itemMark[i]) 
						if (t == HTML.Tag.BR) 
							actContent[i] += "\n";
	}
	public void handleStartTag(HTML.Tag t,
	              MutableAttributeSet a,int pos) {
		if (filter.contentStart(t, a)) {
			for (int i = 0; i < ActConst.ACTITEM.length; i ++) 
				if (filter.isContent(i))
					itemMark[i] = true;	
		}
		handleSimpleTag(t,a,pos);    // handle the same way
	}

	public void handleEndTag(HTML.Tag t, int pos){
		if (filter.contentEnd(t, null))
			for (int i = 0; i < ActConst.ACTITEM.length; i ++) 
				if (filter.isContent(i))
					itemMark[i] = false; 
		}
		
	public void handleText(char[] data, int pos) {
		for (int i = 0; i < ActConst.ACTITEM.length; i ++)
			if (itemMark[i]) {
				if (filter.isContent(i)) {				
					actContent[i] += String.valueOf(data);
					System.out.println(data);
				}
				else { 
					actContent[i] = String.valueOf(data);
					itemMark[i] = false;
				}
			}
		}
		}
	   
	   class UpdateMsg implements Runnable {
		  
		   String msg;
		    public void run()
		    {
		    	message.append(msg);
		    }
	   }
	   
	   private void log(String message) {
		   UpdateMsg msg = new UpdateMsg();
		   msg.msg = message;
		   SwingUtilities.invokeLater(msg);
	   }
}
	
