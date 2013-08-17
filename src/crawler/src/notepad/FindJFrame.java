package notepad;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
public class FindJFrame extends JFrame implements ActionListener{
 private JTextPane text;
 private JTextField findstr;
 private JButton btn_findnext,btn_cannel;
 private int start=0,end=0;
 private final int FIND=1;   //找到标志
 private int flag=0;    //标志是否找到
 public FindJFrame()
 {
  super("查找");
  this.setBounds(200,300,200,200);
  this.setLayout(new GridLayout(2,1));
  this.setDefaultCloseOperation(EXIT_ON_CLOSE);
  
  Panel panel1=new Panel();
  panel1.setLayout(new FlowLayout());
  this.getContentPane().add(panel1);
  
  panel1.add(new Label("查找内容"));
  findstr=new JTextField(15);
  panel1.add(findstr);
  btn_findnext=new JButton("查找下一个");
  btn_findnext.addActionListener(this);
  panel1.add(btn_findnext);
  
  Panel panel2=new Panel();
  this.getContentPane().add(panel2);
 // panel2.add(new JLabel("方向"));
  //JButton
  this.setResizable(false);
  this.setVisible(true);////////////////////////////
  
 }
 public FindJFrame(JTextPane text)
 {
  this();
  this.text=text;
  end=text.getText().length();
 }
 
 public void setfindstr(String str)  //设置要查找的字符串
 {
  this.findstr.setText(str);
 }
 public int getStart()   //获得选中文本的起始位置
 {
  return start;
 }
 public int getEnd()   //选中文本的终止位置
 {
  return end;
 }
 public void setTitle(String name)   //设置文本标题
 {
  super.setTitle(name);
 }
 public void setStart(int start)  //设置鼠标起始位置
 {
  if(start>=0)
  this.start=start;
  else start=0;
 }
 public void setEnd(int end)      //设置鼠标终止位置
 {
  if(end>=0)
   this.end=end;
  else end=text.getText().length();
 }
 public boolean isFind()     //判断是否找到
 {
  if(flag==FIND)
   return true;
  else return false;
 }
 public void find()
 {
  flag=0;
  if(start<0)
  {
   JOptionPane.showMessageDialog(this,"位置超出范围");
   return ;
  }
   String str=text.getText().substring(start,end); 
   int findstart=str.indexOf(findstr.getText());  //没有找到findstr的话返加-1
   if(findstart!=-1)//找到
    flag=FIND;
   else
   {
    text.setSelectionStart(end);
    text.setSelectionEnd(end);
    return ;
   }
   System.out.println("findstart= "+findstart);
   text.setSelectionStart(start+findstart);
   int findend=start+findstart+findstr.getText().length();
   text.setSelectionEnd(findend); 
   start=findend;
 }
 public void actionPerformed(ActionEvent e)   //点击"查找下一个"
 {
  if(e.getSource()==btn_findnext)
  {
   find();
   if(!isFind())
    JOptionPane.showMessageDialog(this,"没有找到");
  }
 }
}