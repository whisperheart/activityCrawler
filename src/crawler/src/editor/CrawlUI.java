package editor;

import java.awt.Choice;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Collection;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import access.Access;
import access.AccessInterface;

import constents.ActConst;
import constents.DBConst;
import crawler.*;
import filter.Filter;

public class CrawlUI extends JFrame implements ISpiderReportable{
	
	Choice choice = new Choice();
	Filter[] filter = new Filter[ActConst.WEBSITE.length];;
	
	int nowWebSite = -1;
	
	  JLabel label1 = new JLabel();
	  JButton begin = new JButton();
	  JTextField url = new JTextField();
	  JScrollPane errorScroll = new JScrollPane();
	  JTextArea errors = new JTextArea();
	  JLabel current = new JLabel();
	  JLabel goodLinksLabel = new JLabel();
	  JLabel badLinksLabel = new JLabel();
	  
	  /**
	   * The background spider thread
	   */
	  protected Thread[] backgroundThread = new Thread[ActConst.WEBSITE.length];
	  /**
	   * The spider object being used
	   */
	  protected Spider[] spider = new Spider[ActConst.WEBSITE.length];
	  /**
	   * The URL that the spider began with
	   */
	  protected URL[] base = new URL[ActConst.WEBSITE.length];
	  /**
	   * How many bad links have been found
	   */
	  protected int[] badLinksCount = new int[ActConst.WEBSITE.length];
	  /**
	   * How many good links have been found
	   */
	  protected int[] goodLinksCount = new int[ActConst.WEBSITE.length]; 
	  
	  protected String[] currentProcessing = new String[ActConst.WEBSITE.length];
	  
	  protected String[] currentError = new String[ActConst.WEBSITE.length];
	  
	  protected boolean frameSizeAdjusted = false;
	  
	  protected Access access;
	  
	  Collection[] actUrlList = new Collection[ActConst.WEBSITE.length];
	  
	  public CrawlUI(boolean[] website,Access access) {
		setTitle("Find Broken Links");
		getContentPane().setLayout(null);
		setSize(405,350);
		setVisible(true);
		
		this.access = access;
		choice.add("Choose");
		
		for (int i = 0; i < ActConst.WEBSITE.length; i++) {
			if (website[i]) {
				choice.add(ActConst.WEBSITE[i]);
				actUrlList[i] = new ArrayList(3);
				Object ob;
				try {
					ob = Class.forName("filter." + ActConst.WEBSITE[i]).newInstance();
					filter[i] = (Filter)ob;
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
				
			}
			goodLinksCount[i] = 0;
			badLinksCount[i] = 0;
			currentProcessing[i] = "";
			currentError[i] = "";
		}
		ItemAction itemAction = new ItemAction();
		choice.addItemListener(itemAction);
		getContentPane().add(choice);		
		choice.setBounds(12, 5, 100, 25);
		
	    label1.setText("Enter a URL:");
	    getContentPane().add(label1);
	    label1.setBounds(12,12,84,12);
	    begin.setText("Begin");
	    begin.setActionCommand("Begin");
	    getContentPane().add(begin);
	    begin.setBounds(12,36,84,24);
	    getContentPane().add(url);
	    url.setBounds(108,36,288,24);
	    errorScroll.setAutoscrolls(true);
	    errorScroll.setHorizontalScrollBarPolicy(javax.swing.
	                ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
	    errorScroll.setVerticalScrollBarPolicy(javax.swing.
	                ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
	    errorScroll.setOpaque(true);
	    getContentPane().add(errorScroll);
	    errorScroll.setBounds(12,150,384,156);
	    errors.setEditable(false);
	    errorScroll.getViewport().add(errors);
	    errors.setBounds(0,0,366,138);
	    current.setText("Currently Processing: ");
	    getContentPane().add(current);
	    current.setBounds(12,72,384,12);
	    goodLinksLabel.setText("Good Links: 0");
	    getContentPane().add(goodLinksLabel);
	    goodLinksLabel.setBounds(12,96,192,12);
	    badLinksLabel.setText("Bad Links: 0");
	    getContentPane().add(badLinksLabel);
	    badLinksLabel.setBounds(216,96,96,12);
	    
	    SymAction lSymAction = new SymAction();
	    begin.addActionListener(lSymAction);
	  }
	  
	  public void addNotify()
	  {
	    // Record the size of the window prior to calling parent's
	    // addNotify.
	    Dimension size = getSize();
	    super.addNotify();
	    if ( frameSizeAdjusted )
	      return;
	    frameSizeAdjusted = true;
	// Adjust size of frame according to the insets and menu bar
	    Insets insets = getInsets();
	    JMenuBar menuBar = getRootPane().getJMenuBar();
	    int menuBarHeight = 0;
	    if ( menuBar != null )
	      menuBarHeight = menuBar.getPreferredSize().height;
	    setSize(insets.left + insets.right + size.width, insets.top +
	                          insets.bottom + size.height + 
	                          menuBarHeight);
	  }
	  
	  class SymAction implements ActionListener {
		    public void actionPerformed(ActionEvent event)
		    {
		      Object object = event.getSource();
		      if ( object == begin )
		        begin_actionPerformed(event);
		    }
		  }
		  /**
		   * Called when the begin or cancel buttons are clicked
		   * 
		   * @param event The event associated with the button.
		   */
		  void begin_actionPerformed(ActionEvent event)
		  {
			  if (nowWebSite >= 0) {
			  if (backgroundThread[nowWebSite] == null) {
				  access.clear(nowWebSite);
				  begin.setLabel("Cancel");
				  BeginThread beginThread = new BeginThread(nowWebSite, this);
				  backgroundThread[nowWebSite] = new Thread(beginThread);
				  backgroundThread[nowWebSite].start();
				  goodLinksCount[nowWebSite] = 0;
			      badLinksCount[nowWebSite] = 0;
			  } else {
				  spider[nowWebSite].cancel();
				  actUrlList[nowWebSite].clear();
			  }
			}
		  }
		  
		  public boolean spiderFoundURL(int website, URL base,URL url)
		  {
			currentProcessing[website] = url.toString();
			if (nowWebSite == website) {
				UpdateURL cs = new UpdateURL();
				//cs.msg = url.toString();
				SwingUtilities.invokeLater(cs);
			}
		    if ( !checkLink(url) ) {
		    	if (nowWebSite == website) {	
		    		UpdateErrors err = new UpdateErrors();
		    		currentError[website] += url+"(on page " + base + ")\n";
		    		SwingUtilities.invokeLater(err);
		    	}
		      badLinksCount[website]++;
		      return false;
		    }
		    goodLinksCount[website]++;
		    return filter[website].urlFilter(url);
		  }
		  /**
		   * Called when a URL error is found
		   * 
		   * @param url The URL that resulted in an error.
		   */
		  
		  public void spiderTargetURL(int website, URL base, URL url) {
		    if (filter[website].activityFilter(url)) 
		    	if (!actUrlList[website].contains(url)) {    		
		    		actUrlList[website].add(url);
		    		access.saveUrl(website, actUrlList[website].size(), url.toString());
		    		access.log("act url", url.toString());
		    }
		   }
		   
		  public void spiderURLError(URL url)
		  {
		  }
		  /**
		   * Called internally to check whether a link is good
		   * 
		   * @param url The link that is being checked.
		   * @return True if the link was good, false otherwise.
		   */
		  protected boolean checkLink(URL url)
		  {
		    try {
		      URLConnection connection = url.openConnection();
		      connection.connect();
		      return true;
		    } catch ( IOException e ) {
		      return false;
		    }
		  }
		  /**
		   * Called when the spider finds an e-mail address
		   * 
		   * @param email The email address the spider found.
		   */
		  public void spiderFoundEMail(String email)
		  {
		  }
		  /**
		   * Internal class used to update the error information
		   * in a Thread-Safe way
		    */

		  /**
		   * Used to update the current status information
		   * in a "Thread-Safe" way
		   */	  
		  
	   class ItemAction implements ItemListener {
		   public void itemStateChanged(ItemEvent e) {
			   String name = choice.getSelectedItem();
			   int now = -1;
			   for (int i = 0; i < ActConst.WEBSITE.length; i++) 
				   if (name.equals(ActConst.WEBSITE[i])) 
				   {
					   now = i;
					   UpdateCurrentStats cs = new UpdateCurrentStats();
					   SwingUtilities.invokeLater(cs);
					   break;
				   }
			   nowWebSite = now;	
		   }
	   }
	   
	   class BeginThread implements Runnable {
		    private int website;
		    private ISpiderReportable parent;
		    
		    BeginThread(int website, ISpiderReportable parent) {
		    	this.website = website;
		    	this.parent = parent;
		    }
		    public void run()
		    {
		    	try {
				      errors.setText("");
				      spider[website] = new Spider(parent, website, filter[website], access);
				      spider[website].clear();
				      base[website] = new URL(url.getText());
				      spider[website].addURL(base[website], base[website]);
				      spider[website].begin();
				      if (website == nowWebSite) {
				      Runnable doLater = new Runnable()
				      {
				        public void run()
				        {
				          begin.setText("Begin");
				        }
				      };
				      SwingUtilities.invokeLater(doLater);
				      }
				      backgroundThread[website]=null;
				    } catch ( MalformedURLException e ) {
				      UpdateErrors err = new UpdateErrors();
				      currentError[website] += "Bad address./n";
				      SwingUtilities.invokeLater(err);
				    } catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
		    }
		  }
	   
	   class UpdateErrors implements Runnable {
		    public String msg;
		    public void run()
		    {
		    	errors.setText(currentError[nowWebSite]);
		    }
		  }
	   
	   class UpdateURL implements Runnable {
		    public String msg;
		    public void run()
		    {
		    	current.setText("Currently Processing: " + currentProcessing[nowWebSite]);
			    goodLinksLabel.setText("Good Links: " + goodLinksCount[nowWebSite]);
			    badLinksLabel.setText("Bad Links: " + badLinksCount[nowWebSite]);
		    }
		  }
	   
	   class UpdateCurrentStats implements Runnable {
		    public void run()
		    {
		    	if (nowWebSite >= 0) {
		    	if (backgroundThread[nowWebSite] == null)
		    		begin.setText("Begin");
		    	else 
		    		begin.setText("Cancel");
		      current.setText("Currently Processing: " + currentProcessing[nowWebSite]);
		      goodLinksLabel.setText("Good Links: " + goodLinksCount[nowWebSite]);
		      badLinksLabel.setText("Bad Links: " + badLinksCount[nowWebSite]);
		      errors.setText(currentError[nowWebSite]);
		      url.setText(ActConst.WebEntry[nowWebSite]);
		    	} else {
		    		begin.setText("Begin");
		    		current.setText("Currently Processing: ");
		    		goodLinksLabel.setText("Good Links: ");
		    		badLinksLabel.setText("Bad Links: ");
		    		url.setText("");
		    	}
		    }
		  }
	   
}
