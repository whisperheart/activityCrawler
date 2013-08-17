package notepad;

import java.awt.*;
import java.awt.event.*;  //Action
import javax.swing.*;
import javax.swing.event.*;  //Caret

public class ReplaceJFrame extends JFrame implements ActionListener,CaretListener{
 
 private static final long serialVersionUID = 1L;
 private JTextPane text;
 private JTextField text_find,text_replace;
 private JButton btn_findnext , btn_replace,btn_replaceall;
 FindJFrame findjframe ;
 public ReplaceJFrame()
 {
  super("替换");
  this.setBounds(200,200,400,200);
 // this.setLayout(new GridLayout(5,1));
  this.setDefaultCloseOperation(EXIT_ON_CLOSE);
  this.setLayout(new FlowLayout());
  
  Container container=this.getContentPane();
  
     JPanel panel1=new JPanel();   //Panel默认情况下是FlowLayout
  container.add(panel1);
  panel1.add(new JLabel("查找内容（N）： "));
  text_find=new JTextField("",20);
  panel1.add(text_find);
  
  JPanel panel2=new JPanel();
  container.add(panel2);
  btn_findnext=new JButton("查找下一个");
  btn_findnext.addActionListener(this);
  panel2.add(btn_findnext);
  JPanel panel3=new JPanel();
  container.add(panel3); 
  panel3.add(new JLabel("替换为（P):    "));
  text_replace=new JTextField("",20);
  panel3.add(text_replace);
  
  JPanel panel4=new JPanel();
  container.add(panel4);
  btn_replace=new JButton("替换");
  btn_replace.addActionListener(this);
  panel4.add(btn_replace);
  
  JPanel panel5=new JPanel();
  container.add(panel5);
  btn_replaceall=new JButton("全部替换（A）");
  btn_replaceall.addActionListener(this);
  panel5.add(btn_replaceall);
  
  this.setResizable(false);
  this.setVisible(true);
 }
 public ReplaceJFrame(JTextPane text)
 {
  this();
  this.text=text; 
  findjframe = new FindJFrame(this.text);
  findjframe.setVisible(false);
  findjframe.setStart(0);
  findjframe.setEnd(text.getText().length()); 
  
 }
 public void caretUpdate(CaretEvent e)
 {
  findjframe.setfindstr(text_find.getText());
 }
 public void actionPerformed(ActionEvent e) {
 { 
  findjframe.setfindstr(text_find.getText());
  if(e.getSource()==btn_findnext)
  {  
   findjframe.find();
   
   if(!findjframe.isFind())
    JOptionPane.showMessageDialog(this,"没有找到"+text_find.getText());
  }
  else if(e.getSource()==btn_replace)  
  {
   if(text_replace.getText()=="")
   {
    JOptionPane.showMessageDialog(this,"请输入要替换的内容");
    return ;
   }
   if(findjframe.isFind())      //调用find后,会把找到的字符串选中
   {
    String tempstr=text.getText().substring(text.getSelectionStart(), text.getSelectionEnd());
    
    if(tempstr.equals(text_find.getText()))  //找到了 用text_replace替换
     text.replaceSelection(text_replace.getText());   
   }
  }
  
  else if(e.getSource()==btn_replaceall)   //从文本开头,替换所有
  {
   if(text_replace.getText()=="")
   {
    JOptionPane.showMessageDialog(this,"请输入要替换的内容");
    return ;
   }
   int len=text.getText().length();
   findjframe.setStart(text.getSelectionStart());
   findjframe.setEnd(len);
   findjframe.find();
   while(findjframe.isFind())
   {   
    String tempstr=text.getText().substring(text.getSelectionStart(), text.getSelectionEnd());
    if(tempstr.equals(text_find.getText()))
    {
     text.replaceSelection(text_replace.getText());
    }
    findjframe.find();
   }
  }
 }
 }
}

