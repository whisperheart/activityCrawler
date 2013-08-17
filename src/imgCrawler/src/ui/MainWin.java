package ui;

import java.awt.BorderLayout; 
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.List;

import javax.swing.JButton; 
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.ScrollPaneLayout;

import core.Controller; 
import core.data.impl.ControllerImpl;

/** 
 * 界面实现
 * @author xgx
 *
 */
public class MainWin extends JFrame implements ActionListener, MouseListener {
    private JTextField imgFiled;
    private JTextField urlFiled;
    private JTextArea messageArea;
    private JPopupMenu memu;

    public MainWin(String name) {
        super(name);
        init();

    }

    public JTextArea getMessageArea() {
        return messageArea;
    }

    public void setMessageArea(JTextArea messageArea) {
        this.messageArea = messageArea;
    }

    private void init() {
        setSize(500, 400);
        Dimension size = Toolkit.getDefaultToolkit().getScreenSize();

        // 屏幕位置
        int locationX = ((int) size.getWidth() / 2) - getWidth() / 2;
        int locationY = ((int) size.getHeight() / 2) - getHeight() / 2;
        ;
        setLocation(locationX, locationY);

        setDefaultCloseOperation(EXIT_ON_CLOSE);
        getContentPane().setLayout(new BorderLayout());

        // 顶部面板
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new GridLayout(2, 1));

        JPanel top1 = new JPanel();
        JLabel urlLabel = new JLabel("请输入网址:");
        urlFiled = new JTextField();
        urlFiled.setColumns(30);
        urlFiled.addMouseListener(this);

        memu = new JPopupMenu();
        // 粘贴菜单
        final JMenuItem paste = new JMenuItem("粘贴网址");
        paste.setAccelerator(KeyStroke.getKeyStroke('V', InputEvent.CTRL_MASK));
        paste.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                 Clipboard clipboard = getToolkit().getSystemClipboard();
                 
                   Transferable content = clipboard.getContents(this);
                   Object s;
                try {
                    s = content.getTransferData(DataFlavor.stringFlavor);
                    if (s instanceof String) {
                        urlFiled.setText(s.toString());
                        }
                } catch (UnsupportedFlavorException e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                } catch (IOException e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                }
            }
        });
        memu.add(paste);

        JButton fetch = new JButton("抓取");

        // 向top1面板添加元素
        top1.add(urlLabel);
        top1.add(urlFiled);
        top1.add(fetch);

        final JPanel top2 = new JPanel();
        JLabel imgLabel = new JLabel("图片存储目录:");
        imgFiled = new JTextField();
        imgFiled.setEditable(false);
        imgFiled.setColumns(29);
        JButton choose = new JButton("选择");

        // 向top2面板添加元素
        top2.add(imgLabel);
        top2.add(imgFiled);
        top2.add(choose);

        // 向顶部面板添加元素
        topPanel.add(top2);
        topPanel.add(top1);

        // 中部面板
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BorderLayout());
        messageArea = new JTextArea();
        messageArea.setLineWrap(true);
        messageArea.setEditable(false);
        messageArea.setText("欢迎使用");

        final JScrollPane textPane = new JScrollPane(messageArea);
        textPane.setLayout(new ScrollPaneLayout());
        textPane.setAutoscrolls(true);

        centerPanel.add(textPane, "Center");

        add(topPanel, "North");
        add(centerPanel, "Center");

        // 设置选择目录监听器
        choose.addActionListener(this);

        // 设置抓取按钮监听器
        fetch.addActionListener(this);

        setResizable(false);
        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String s = e.getActionCommand();

        // “选择”按钮被按下
        if (s.equals("选择")) {
            JFileChooser chooser = new JFileChooser();
            chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            int result = chooser.showOpenDialog(this);
            if (result == JFileChooser.APPROVE_OPTION) {
                imgFiled.setText(chooser.getSelectedFile().getPath());
            }

        } else if (s.equals("抓取")) {// “抓取”按钮被按下
            if (imgFiled.getText().length() < 1) {
                messageArea.setText("请选择图片保存目录！");
            } else if (urlFiled.getText().length() < 1) {
                messageArea.setText("请输入网页地址！");
            }

            else {
                messageArea.setText("正解析网址...");
                this.update(getGraphics());
                try {
                    Controller controller = new ControllerImpl(this);
                    List<File> fileList = controller.fetchImages(urlFiled
                            .getText(), imgFiled.getText());

                } catch (NullPointerException e1) {
                    e1.printStackTrace();
                    messageArea.setText("网址错误,正确格式:http://www.lovej.tk");
                } catch (MalformedURLException e1) {
                    e1.printStackTrace();
                    messageArea.setText("网址错误,正确格式:http://www.lovej.tk");
                } catch (IOException e1) {
                    e1.printStackTrace();
                    messageArea.setText("获取失败");
                }
            }
        }

    }

    @Override
    public void mouseClicked(MouseEvent e) {
        // TODO Auto-generated method stub

    }

    @Override
    public void mouseEntered(MouseEvent e) {
        // TODO Auto-generated method stub

    }

    @Override
    public void mouseExited(MouseEvent e) {
        // TODO Auto-generated method stub

    }

    @Override
    public void mousePressed(MouseEvent e) {
        if (e.getButton() == MouseEvent.BUTTON3) {
            this.memu.show(this, urlFiled.getLocation().x, urlFiled.getLocation().y);
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        // TODO Auto-generated method stub

    }
}
