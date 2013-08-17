package notepad;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.print.PrinterJob;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Calendar;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.JToolBar;
import javax.swing.KeyStroke;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.text.BadLocationException;
import javax.swing.undo.UndoManager;

public class Notepad extends JFrame implements ActionListener ,MouseListener,CaretListener
{
 
 private static final long serialVersionUID = 1L;
 private File file;
 private JTextPane text;
 private JMenu menu[];
 private JFileChooser fchooser;
 private PrinterJob prtMe;
 private JCheckBoxMenuItem checklinewrapmenuitem;
 private JPopupMenu popupmenu;
 private UndoManager myundo = new UndoManager();
 //UndoManager 管理 UndoableEdit 列表，提供撤消或恢复适当编辑的方法。
 //FindJFrame findnext;
 //Clipboard clipbd=getToolkit().getSystemClipboard();
    private JPanel panelstatus;
    private JTextField text_status;
    private JCheckBoxMenuItem checkstatusmenuitem;
 
 public Notepad()
 {
  super("Notepad");
  Dimension dim = getToolkit().getScreenSize();    //获得屏幕分辨率
  this.setBounds(dim.width/4,dim.height/4,dim.width/2,dim.height/2);//窗口大小一半、居中
     this.setDefaultCloseOperation(EXIT_ON_CLOSE);  
        this.setLayout(new BorderLayout());
    
     text = new JTextPane();
     text.getDocument().addUndoableEditListener(myundo);
     text.addCaretListener(this);
    
     this.getContentPane().add(new JScrollPane(text));  //JTextField加滚动条
    
     JMenuBar menubar = new JMenuBar();    //加菜单栏
     this.setJMenuBar(menubar);
     String[] menustr = {"文件","编辑","格式","查看","帮助"};
     menu = new JMenu[menustr.length];
     for(int i=0;i<menustr.length;i++)
     {
      menu[i] = new JMenu(menustr[i]);
      menubar.add(menu[i]);
     }
    
     Filemenu();
     Editmenu();
     Formatmenu();
     watchmenu();
     helpmenu();
    
     //加弹出菜单
     text.addMouseListener(this);    //JTextField中加鼠标监听器
     mypopupmenu();
     text.add(popupmenu);  //把弹出菜单加在JTextField中   
    
     MyToolbar();  //加工具栏
    
  MyStatusbar();  //添加装态栏
    
  this.setVisible(true);
    
     this.file=null;
     this.fchooser=new JFileChooser(new File(".",""));     //文件对话框的初始路径是当前路径
//     this.fchooser.setFileFilter(new FileFilter("文本文件(*.txt)","txt")); //设置文件过滤器
    
     prtMe = PrinterJob.getPrinterJob();
     //PrinterJob 类是控制打印的主要类。应用程序调用此类中的方法设置作业、（可选地）调用与用户的打印对话框，然后打印作业的页面。
  
    
            
 }
 public Notepad(File file) 
 {
  this();
  if(file!=null)
  {
   this.file=file;
   this.text.setText(this.file.getName());
   this.setTitle(this.file.getName());
  }
 }
 public Notepad(String filename)
 {
  this(new File(filename));
 }
 private void MyStatusbar()   //状态栏
 {
  panelstatus=new JPanel();
  this.getContentPane().add(panelstatus,"South");
  panelstatus.setLayout(new FlowLayout());
  text_status=new JTextField(20);
  text_status.setEnabled(false);
  panelstatus.add(text_status);
  panelstatus.setVisible(false);
 }
 private void MyToolbar()
 {
  JToolBar toolbar=new JToolBar();
     this.getContentPane().add(toolbar,"North");
     JButton bcreatfile =new JButton("新建");
     bcreatfile.addActionListener(this);
     toolbar.add(bcreatfile);
     JButton bopen=new JButton("打开",new ImageIcon("open.gif"));
     bopen.addActionListener(this);
     toolbar.add(bopen);
     JButton bsave=new JButton("保存",new ImageIcon("save.gif"));
     bsave.addActionListener(this);
     toolbar.add(bsave);
     JButton bundo=new JButton("撤消");
     bundo.addActionListener(this);
     toolbar.add(bundo);
     JButton bredo=new JButton("恢复");
     bredo.addActionListener(this);
     toolbar.add(bredo);
    
  JPanel pnlpicture = new JPanel();    //实现插入图片功能
  toolbar.add(pnlpicture);
  pnlpicture.add(new JLabel("insert condition"));
        final JTextField txt = new JTextField("0",6);
        pnlpicture.add(txt);
        JButton btn = new JButton("插入图标");
        pnlpicture.add(btn);
        btn.addActionListener  //监听接口在(　）内实现
        ( new ActionListener()
         {
          public void actionPerformed(ActionEvent event)
          {
           int position = text.getCaretPosition();
           int insert = 0;
          // System.out.print("dow");
           try
           {
            insert = Integer.parseInt(txt.getText());
            text.setCaretPosition(insert);
            //public ImageIcon(String filename) 根据指定的文件创建一个 ImageIcon
            ImageIcon icon = new ImageIcon("30.jpg");
            text.insertIcon(icon);
            text.setCaretPosition(position >= insert ? position + 1 : position);
           // text.requestFocus();   //请求获取焦点
           }
           catch (Exception e)
           {
            text.setCaretPosition(position);
            return;
           }
          }
         }
        );
 }
    private void mypopupmenu()   //弹出菜单
 {
  popupmenu=new JPopupMenu();
  String menuitemstr[]={"撤消","恢复","剪切","复制","粘贴","全选","字体"};
  JMenuItem popupmenuitem[]=new JMenuItem[menuitemstr.length];
  for(int i=0;i<menuitemstr.length;i++)
  {
   popupmenuitem[i]=new JMenuItem(menuitemstr[i]);
   if(i==1||i==4||i==5)
    popupmenu.addSeparator();
   popupmenu.add(popupmenuitem[i]);
   popupmenuitem[i].addActionListener(this);   
  }
  popupmenuitem[2].setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_X,InputEvent.CTRL_MASK)); //剪切快捷方式ctrl+X
  popupmenuitem[3].setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C,InputEvent.CTRL_MASK));
  popupmenuitem[4].setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_V,InputEvent.CTRL_MASK));
  popupmenuitem[5].setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_A,InputEvent.CTRL_MASK));
 }
 private void Filemenu()
 {
  String menuitemstr[] = {"新建" ,"打开","保存","另存为","页面设置","打印","退出"};
  JMenuItem menuitem[]=new JMenuItem[menuitemstr.length];
  for(int i=0;i<menuitemstr.length;i++)
  {
   menuitem[i]= new JMenuItem(menuitemstr[i]);
   menuitem[i].addActionListener(this);
   menu[0].add(menuitem[i]);
   if(i==3||i==5)
    menu[0].addSeparator();  //加菜单分隔符
  } 
 }
 private void Editmenu()
 {
  String menuitemstr[]={"撤消","恢复","剪切","复制","粘贴","删除","查找","查找下一个","替换","转到","全选","时间/日期"};
  JMenuItem menuitem[]=new JMenuItem[menuitemstr.length];
  for(int i=0;i<menuitemstr.length;i++)
  {
   menuitem[i]=new JMenuItem(menuitemstr[i]);
   menuitem[i].addActionListener(this);
   menu[1].add(menuitem[i]);
   if(i==1||i==5||i==9)
    menu[1].addSeparator();
  }
  menuitem[9].setEnabled(false);//////"转到"为阴影,没有作用
  menuitem[9].removeActionListener(this);
 }
 private void Formatmenu()
 {
  checklinewrapmenuitem = new JCheckBoxMenuItem("自动换行",true);  //true表示默认状态可以换行
     checklinewrapmenuitem.setEnabled(false);
  menu[2].add(checklinewrapmenuitem);
     JMenuItem menuitem = new JMenuItem("字体");
     menuitem.addActionListener(this);
     menu[2].add(menuitem);
 }
 private void watchmenu()
 {
  checkstatusmenuitem = new JCheckBoxMenuItem("查看状态栏");
  menu[3].add(checkstatusmenuitem);
  checkstatusmenuitem.addActionListener(this);//状态栏监听器
 }
 private void helpmenu()
 {
  JMenuItem menuitem1 = new JMenuItem("帮助主题");
     menuitem1.addActionListener(this);
  menu[4].add(menuitem1);
  menu[4].addSeparator();
  JMenuItem menuitem2 = new JMenuItem("关于记事本");
  menuitem2.addActionListener(this);
  menu[4].add(menuitem2);  
 }
 public void writeToFile(String lines)    //将字符串lines写入到当件文本文件中
 {
  try
  {
   FileWriter fout=new FileWriter(this.file);
   fout.write(lines+"\n");
   fout.close();
  }
  catch(IOException ioex)
  {
   JOptionPane.showMessageDialog(this,"有IO错，写入"+file.getName()+"文件不成功");
  }
 }
 public String readFromFile()    //将当前文本文件中的内容读出，以lines返回
 {
  char lines[]=null;
  try
  {
   FileReader fin=new FileReader(this.file);
   lines=new char[(int)this.file.length()];
   fin.read(lines);
   fin.close();
  }
  catch(FileNotFoundException fe)
  {
   JOptionPane.showMessageDialog(this,"" + this.file.getName() + "文件不存在");
     
  }
  catch(IOException ioex)
  {
   JOptionPane.showMessageDialog(this,"IO错，读入"+file.getName()+"文件不成功");  
  }
  finally
  {
   return new String(lines);
  }
 }
 public void actionPerformed(ActionEvent e) 
 {
 
  if(e.getActionCommand()=="新建")
  {
   this.file=null;
   this.setTitle("未命名");
   this.text.setText("");
  }
  else if(e.getActionCommand()=="打开"&&fchooser.showOpenDialog(this)==JFileChooser.APPROVE_OPTION)
  //点击打开，显示打开对话框，选中文件后选择确定(确定以JFileChooser.APPROVE_OPTION(int类型)返回
  {
   this.file=fchooser.getSelectedFile();
   this.setTitle(this.file.getName());
   this.text.setText(this.readFromFile());
  }
  else if(e.getActionCommand()=="保存"&&this.file!=null)  //保存非空文件，不显示保存文件对话框
         this.writeToFile(this.text.getText());
  else if((e.getActionCommand()=="保存"&&file==null||e.getActionCommand()=="另存为")
   &&fchooser.showSaveDialog(this)==JFileChooser.APPROVE_OPTION) 
   //显示保存（空文件）或点击另存为按扭，出现保存对话框，点击确定（点击确定后会返回JFileChooser.APPROVE_OPTION
   {
    this.file=fchooser.getSelectedFile();  //file为当前选中的文件
    if(!file.getName().endsWith(".txt"));
    this.file=new File(file.getAbsolutePath()+".txt");
    this.writeToFile(this.text.getText());
    this.setTitle(this.file.getName());
   }
  else if(e.getActionCommand()=="页面设置")
   prtMe.printDialog();   //显示找印对话框，供用户更改打印属性
  else if(e.getActionCommand()=="打印")
  {
   
   try{
    //  text.print(null);
     prtMe.print();//打印一组页面
    }
      catch(Exception ew)
      {
       JOptionPane.showMessageDialog(this, "找不到打印机");
      }
  
  }
  else if(e.getActionCommand()=="退出")
  {
        if(JOptionPane.showConfirmDialog(this,"终止当前程序运行?")==0)
         System.exit(0);
  }
  else if(e.getActionCommand()=="撤消"||e.getActionCommand()=="恢复")
  {
   if(e.getActionCommand()=="撤消")
   {
    if(myundo.canUndo())
     myundo.undo();      
    else JOptionPane.showMessageDialog(this,"无法撤消");
   }
   else
   {
    if(myundo.canRedo())
     myundo.redo();      
    else JOptionPane.showMessageDialog(this,"无法恢得");
   }
   
  }
  else if(e.getActionCommand()=="剪切")
  {
       text.cut();
  }
  else if(e.getActionCommand()=="粘贴")
  {
       text.paste();     
  }
  
  else if(e.getActionCommand()=="删除")   //replaceRange(String,start,end)用指定替换文本
       text.replaceSelection("");//以空字符串代串选中的范围
      
  else if(e.getActionCommand()=="查找")
  {  
   new FindJFrame(text);
  }
  else if(e.getActionCommand()=="查找下一个")
  {
   FindJFrame findnext=new FindJFrame(text);
   findnext.setTitle("查找下一个");
   findnext.setStart(text.getSelectionStart());
   findnext.setEnd(text.getSelectionEnd()); 
  }
  else if(e.getActionCommand()=="替换")
  {
   new ReplaceJFrame(text);
  }
  else if(e.getActionCommand()=="全选")
   text.selectAll();
  else if(e.getActionCommand()=="时间/日期")   //在JTextArea后追加显示系统当前时间
  {
   Calendar time =Calendar.getInstance();
   String str="    "+time.HOUR_OF_DAY+":"+time.MINUTE+"  "+time.YEAR+"-"+time.MONTH+"-"+time.DAY_OF_MONTH+"\r";
   //public Document getDocument()获取与编辑器关联的模型。
   javax.swing.text.Document doc = text.getDocument();
         if (doc != null)
         {
             try
             {
            // void insertString(int offset,  String str, AttributeSet a)throws BadLocationException插入内容字符串。
                doc.insertString(doc.getLength(), str, null);
             }
             catch (BadLocationException e1)
             {
             }
         }
  }
  else if(e.getActionCommand()=="字体")
  {
   new WordStyle(text);
  }
  else if(e.getActionCommand()=="查看状态栏")
  { 
   panelstatus.setVisible(checkstatusmenuitem.isSelected());
  }
  else if(e.getActionCommand()=="帮助主题")
  {
   JOptionPane.showMessageDialog(this,"To do by yourself,don't to depend on others\n你被耍了,hahaha……");
  }
  else if(e.getActionCommand()=="关于记事本")
  {
   JOptionPane.showMessageDialog(this,"this is made by yaing,liujian and liuli,2010-6-13");
  }
 }
 public void caretUpdate(CaretEvent e)  //状态栏上显示光标所在的行号和列号
 {
  try
  {
   int pos = text.getCaretPosition();    
   int posLine;
      int y = 0;
            int X=0;
      try
      {
       //public Rectangle modelToView(int pos)
              //  throws BadLocationException将模型中给定位置转换为视图坐标系统中的位置。
        Rectangle caretCoords = text.modelToView(pos);
        y = (int) caretCoords.getY();
        X = (int) caretCoords.getX();
      }
      catch (BadLocationException ex)
      {
      }
            //public FontMetrics getFontMetrics(Font font)
      //FontMetrics 类定义字体规格对象
      //int getHeight()       获取此 Font 中文本行的标准 height
      int lineHeight = text.getFontMetrics(text.getFont()).getHeight();
      posLine = (y / lineHeight) + 1;
      int col=text.viewToModel(new Point(X,lineHeight));
      text_status.setText("字付总数:"+pos+"  当前位置  Line:"+posLine+"  Column:"+col);
  }
  catch(Exception ex)
  {
   text_status.setText( " 无法获得当前光标位置 ");
  }
}
 public void mouseClicked(MouseEvent mec)
 {
  if(mec.getModifiers()==MouseEvent.BUTTON3_MASK)
   popupmenu.show(text,mec.getX(),mec.getY());    //在鼠标点击的地方显示弹出菜单
 }
 public void mousePressed(MouseEvent mep) {}
 public void mouseReleased(MouseEvent mre) {}
 public void mouseEntered(MouseEvent med) {}
 public void mouseExited(MouseEvent me) {}
 public void mouseDragged(MouseEvent mdg) {}
 
}