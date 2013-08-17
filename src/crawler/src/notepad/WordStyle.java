package notepad;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GraphicsEnvironment;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JColorChooser;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

public class WordStyle extends JFrame implements ActionListener{
 private JComboBox wordname,wordstyle,wordsize;    //组合框
 private JRadioButton fontleft,fontcenter,fontright;   //单选按钮
 private JTextField showfrecolor,showbackcolor;   //显示当前选择的前景色背景色预浏
 private JButton frecolorchoose,backcolorchoose; 
 private JTextPane text,present;
 private JCheckBox underline;
 public WordStyle()
 {
  super("字体");
  this.setBounds(200,200,500,500);
  this.setLayout(new GridLayout(4,1));
  this.setDefaultCloseOperation(EXIT_ON_CLOSE);
  
  JPanel panel1=new JPanel();
  this.getContentPane().add(panel1);
  panel1.setSize(500,200);
  panel1.add(new JLabel("字体")); 
  
  GraphicsEnvironment ge=GraphicsEnvironment.getLocalGraphicsEnvironment();  
  String[] fontName=ge.getAvailableFontFamilyNames();   //获取系统字体
  wordname=new JComboBox(fontName);
  wordname.addActionListener((ActionListener) this);
  panel1.add(wordname);
  panel1.add(new JLabel("   字形"));
  String[] fontstyle={"常规","加粗","倾斜","加粗 倾斜"};
  wordstyle=new JComboBox(fontstyle);
  wordstyle.addActionListener(this);
  panel1.add(wordstyle);
  panel1.add(new JLabel("   字号"));  //本想加数字,但不知道怎么弄
  String[] fontsize={"5","5.5","6.5","7.5","8","9","10","11","12","13","14","15","16","17","18","19","20",
    "24","28","30","40","50","60","70","80","90","100"};
  wordsize=new JComboBox(fontsize);
  wordsize.setSelectedItem("16");
  wordsize.addActionListener(this);
  panel1.add(wordsize);
  
  JPanel panel2=new JPanel();
  this.getContentPane().add(panel2);
  panel2.setLayout(new GridLayout(1,2));
  panel2.setSize(500,100);
  JPanel panel21=new JPanel();      //前景颜色选择面板
  panel2.add(panel21);
  panel21.setLayout(new FlowLayout());
  frecolorchoose = new JButton("前景色");
  frecolorchoose.addActionListener(this);
  panel21.add(frecolorchoose);
  showfrecolor=new JTextField(10);
  showfrecolor.setBackground(Color.black);
  panel21.add(showfrecolor);
 
    
  JPanel panel22=new JPanel();   /////背景字体对齐面板
  panel2.add(panel22);
  panel22.setLayout(new FlowLayout());
  backcolorchoose = new JButton("背景色");
  backcolorchoose.addActionListener(this);
  panel22.add(backcolorchoose);
  showbackcolor=new JTextField(10);
  panel22.add(showbackcolor);
  
  
  JPanel panel3 =new JPanel();
  this.getContentPane().add(panel3);
  panel3.setSize(400,100);
  ButtonGroup group=new ButtonGroup();
     fontleft =new JRadioButton("左对齐",true);
  group.add(fontleft);
  panel3.add(fontleft);
  fontleft.addActionListener(this); 
  fontcenter =new JRadioButton("居中");
  group.add(fontcenter);
  panel3.add(fontcenter);
  fontcenter.addActionListener(this);  
  fontright =new JRadioButton("右对齐");
  group.add(fontright);
  panel3.add(fontright);
  fontright.addActionListener(this);
  
  underline=new JCheckBox("下划线"); 
  panel3.add(underline);
  underline.addActionListener(this);
  
  
  JPanel panel4 =new JPanel();
  this.getContentPane().add(panel4);
  panel4.setSize(400,200);
  present=new JTextPane();
  present.setText("yaing's present");
  panel4.add(present);
  present.setEditable(false);
  
 
  this.setResizable(false);
  this.setVisible(true);
  
 }
 public WordStyle(JTextPane text)
 {
  this();
  this.text=text;
 }
 
 public void actionPerformed(ActionEvent e)
 {
  int size=0;
  if(e.getSource()==underline)
  {
   SimpleAttributeSet bSet = new SimpleAttributeSet(); 
   StyleConstants.setUnderline(bSet, true);    //设置下划线
   StyledDocument doc = text.getStyledDocument();
   doc.setParagraphAttributes(text.getSelectionStart(), text.getSelectionEnd()-text.getSelectionStart(), bSet, false);
  }
  if(e.getSource() instanceof JComboBox)
  {
   try
   {
    String name=(String)wordname.getSelectedItem();
    SimpleAttributeSet aSet = new SimpleAttributeSet(); 
    //public class StyleConstantsextends Object  一个已知的或常见的属性键和方法的集合
    StyleConstants.setFontFamily(aSet, name);
    
    int style=this.text.getFont().getStyle();
 
    if(style==0)  //常规
    {
     StyleConstants.setBold(aSet,false);
     StyleConstants.setItalic(aSet,false);
    }
    else if(style==1)  //粗体
    {
     StyleConstants.setBold(aSet,true);
    }
    else if(style==2)  //斜体
    {
     StyleConstants.setItalic(aSet, false);
    }
    else if(style==3)  //粗斜
    {
     StyleConstants.setBold(aSet,true);
     StyleConstants.setItalic(aSet, true);
    }
    size=Integer.parseInt((String)(wordsize.getSelectedItem()));
    if(size<5||size>100)
     throw new Exception("the size is error");
    StyleConstants.setFontSize(aSet, size);
    StyledDocument doc = text.getStyledDocument();
    doc.setCharacterAttributes(text.getSelectionStart(), text.getSelectionEnd()-text.getSelectionStart(), aSet, false);
      
   }
   catch(NumberFormatException nfe)
   {
    JOptionPane.showMessageDialog(this,(String)wordsize.getSelectedItem()+"不能转化成整数");
   }
   catch(Exception ec)
   {
    if(ec.getMessage()=="fontsizeException")
     JOptionPane.showMessageDialog(this,size+" 字号不合适(5,100)");
   }
  }
  if(e.getSource() instanceof JRadioButton)
  {
  
          SimpleAttributeSet bSet = new SimpleAttributeSet(); 
          if(e.getSource()==fontleft)   //左对齐
          StyleConstants.setAlignment(bSet, StyleConstants.ALIGN_LEFT); 
          if(e.getSource()==fontcenter)   //居中
           StyleConstants.setAlignment(bSet, StyleConstants.ALIGN_CENTER);
          if(e.getSource()==fontright)   //右对齐
           StyleConstants.setAlignment(bSet, StyleConstants.ALIGN_RIGHT);      
  
          StyledDocument doc = text.getStyledDocument();         
          doc.setParagraphAttributes(text.getSelectionStart(), text.getSelectionEnd()-text.getSelectionStart(), bSet, false); 
  }
     if(e.getSource() instanceof JButton)
  {
   Color color;
   if(e.getSource()==frecolorchoose)
   {
    color=JColorChooser.showDialog(this,"前景色选择",Color.black); 
    showfrecolor.setBackground(color);
    this.present.setForeground(color);
   
     SimpleAttributeSet aSet = new SimpleAttributeSet(); 
     StyleConstants.setForeground(aSet, color);
     StyledDocument doc=text.getStyledDocument();    
     doc.setCharacterAttributes(text.getSelectionStart(),text.getSelectionEnd()-text.getSelectionStart(), aSet, false);    
            }
   else
   {
    color=JColorChooser.showDialog(this,"背景色选择",Color.white);
    showbackcolor.setBackground(color);
    this.present.setBackground(color);
 
     SimpleAttributeSet aSet = new SimpleAttributeSet(); 
     StyleConstants.setBackground(aSet, color);
     StyledDocument doc=text.getStyledDocument();    
     doc.setCharacterAttributes(text.getSelectionStart(),text.getSelectionEnd()-text.getSelectionStart(), aSet, false);
   }   
  }
 }
 
}