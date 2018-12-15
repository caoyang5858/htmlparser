package ui;

import java.awt.Font;
import java.awt.Image;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.Toolkit;
import java.awt.TrayIcon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.util.Vector;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;

import com.Tool;


public class MainFrame extends JFrame implements ActionListener{
	
	private static final long serialVersionUID = 1L;
	
	JTable jt=null;
	JScrollPane jsp;
	
	
	
	AboutDialog aboutDlg=null;
	
	JMenuBar jmb=null;
	JMenu jm1,jm2;
	JMenuItem jm1_1,jm1_2,jm2_1;
	
	//字体
	Font f1=new Font("宋体",Font.PLAIN,14);
	
	
	JTabbedPane tp=null;
	
	boolean cho[];
	String path_CJ,path_KB;
	String strHtmlCJ,strHtmlKC;
	public MainFrame(boolean cho[]){
		this.cho=cho;//传递关注的信息种类
		init();
	}
	public MainFrame(boolean cho[],String chengjiPath,String kechengbiaoPath){
		this.cho=cho;//传递关注的信息种类
		path_CJ=chengjiPath;
		path_KB=kechengbiaoPath;
		try {
			dododo();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		init();
	}
	public MainFrame(){
		test();
	}
	
	public void dododo() throws IOException{//处理课程表和本学期成绩的html字符串
		strHtmlCJ=Tool.readFileContent(path_CJ);
		strHtmlCJ.trim();
		int i=strHtmlCJ.indexOf("<table width=\"100%\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" class=\"titleTop2\">",100);
		strHtmlCJ=strHtmlCJ.substring(i+107);
		i=strHtmlCJ.indexOf("<div align=\"right\"><table width=\"100%\" border=\"0\" cellpadding=\"0\"");
		strHtmlCJ=strHtmlCJ.substring(0, i);
		
		
		String str1="<html><head><style type=\"text/css\">th{background-color:#CCCCCC;font-size:11px;}td{background-color:#99CCCC;font-size:10px;}table{border:1px;border-color:#999999;}</style></head><body>";
		String str2="</body></html>";
		strHtmlCJ=str1+strHtmlCJ+str2;
		strHtmlKC=Tool.readFileContent(path_KB);
		strHtmlKC.trim();
		i=strHtmlKC.indexOf("css\"><table width=\"100%\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" class=\"titleTop2\">");
		strHtmlKC=strHtmlKC.substring(i+107);
		i=strHtmlKC.indexOf("</table>");
		strHtmlKC=strHtmlKC.substring(5,i+8);
		strHtmlKC=str1+strHtmlKC+str2;
	
	}
	
	
	
	public void init(){
		 ImageIcon imgIcon = new ImageIcon("src\\ui\\8.png");
		   // ImageIcon imgIcon3 = new ImageIcon("src\\ui\\8.png");
		    jm1=new JMenu("菜单");
		    jm1.setFont(f1);
		  //  jm1.setIcon(imgIcon3);
		    jm1_1=new JMenuItem("about",imgIcon);
		    jm1_1.setActionCommand("about");
		    jm1_1.addActionListener(this);
		    jm1_1.setFont(f1);
		    ;
		    jm1.add(jm1_1);
		  
		    jmb=new JMenuBar();
		    jmb.add(jm1);
		  
		    
		  
		    
		    tp=new JTabbedPane(JTabbedPane.TOP,JTabbedPane.SCROLL_TAB_LAYOUT);
		    tp.setFont(f1);
		    
		    if(cho[6]){
		    	 tp.add("本期成绩",new GradePanel(strHtmlCJ));
		    	 tp.add("课程表",new GradePanel(strHtmlKC));
		    }
		    
		    if(cho[0])
		    tp.add("招聘信息",new EmploymentPanel());
		    if(cho[1])
		    tp.add("学术讲座",new LecturePanel());
		    if(cho[2])
		    tp.add("活动讲座",new Lecture2Panel());
		    if(cho[3])
		    tp.add("课程信息",new CoursePanel());
		    if(cho[4])
		    tp.add("团委公告",new NoticeTuanweiPanel());
		    if(cho[5])
		    tp.add("学工部公告",new NoticeXuegongPanel());
		    int w = Toolkit.getDefaultToolkit().getScreenSize().width;//屏幕宽
		    int h = Toolkit.getDefaultToolkit().getScreenSize().height;//
		    
		    myInitTray();//初始化托盘
		    this.setJMenuBar(jmb);//添加菜单
		    this.getContentPane().add(tp);
		    this.setLocation(w/3, h/3);
		    this.setSize(w*2/4, h*5/7);
		    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		    this.setTitle("校园消息便捷查询");
		    this.setVisible(true);
		    this.addWindowListener(new WindowAdapter() {
				public void windowClosing(WindowEvent e) {
					   onCloseWindow();
				}
				public void windowIconified(WindowEvent e){
					   hideFrame();	  
				}
			});
	}
	public void test(){
		
		
	    
	    ImageIcon imgIcon = new ImageIcon("src\\ui\\2.png");
	   // ImageIcon imgIcon3 = new ImageIcon("src\\ui\\8.png");
	    jm1=new JMenu("菜单");
	    jm1.setFont(f1);
	  //  jm1.setIcon(imgIcon3);
	    jm1_1=new JMenuItem("about",imgIcon);
	    jm1_1.setActionCommand("about");
	    jm1_1.addActionListener(this);
	    jm1_1.setFont(f1);
	    ;
	    jm1.add(jm1_1);
	  
	    jmb=new JMenuBar();
	    jmb.add(jm1);
	  
	    
	  
	    
	    tp=new JTabbedPane(JTabbedPane.TOP,JTabbedPane.SCROLL_TAB_LAYOUT);
	    tp.setFont(f1);
	    
	    tp.add("招聘信息",new EmploymentPanel());
	    tp.add("学术讲座",new LecturePanel());
	    tp.add("活动讲座",new Lecture2Panel());
	    tp.add("课程信息",new CoursePanel());
	    tp.add("团委公告",new NoticeTuanweiPanel());
	    tp.add("学工部公告",new NoticeXuegongPanel());
	    int w = Toolkit.getDefaultToolkit().getScreenSize().width;//屏幕宽
	    int h = Toolkit.getDefaultToolkit().getScreenSize().height;//
	    
	    this.setJMenuBar(jmb);//添加菜单
	    this.getContentPane().add(tp);
	    this.setLocation(w/3, h/3);
	    this.setSize(w*2/4, h*5/7);
	    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    this.setVisible(true);
	}
	
	public static void main(String args[]){
		MainFrame frm=new MainFrame();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		if(e.getActionCommand()=="test"){
			
		}
		else if(e.getActionCommand()=="about"){
			if(aboutDlg==null){
				aboutDlg=new AboutDialog(this,"About this program...",true);
			}
			else{
				aboutDlg.setVisible(true);
			}
		}
	}

	public void showFrame(){
		int w = (Toolkit.getDefaultToolkit().getScreenSize().width)  -300;
        int h = (Toolkit.getDefaultToolkit().getScreenSize().height) / 9;
		this.setLocation(w, h);
		this.setVisible(true);
		this.setExtendedState(JFrame.NORMAL);
		this.toFront();
	}
	public void myInitTray(){
		if (SystemTray.isSupported()) { // 判断是否支持系统托盘   
		   
		    ImageIcon icon = new ImageIcon("src\\ui\\8.png"); // 实例化图像对象   
		    Image image = icon.getImage(); // 获得Image对象   
		    TrayIcon trayIcon = new TrayIcon(image); // 创建托盘图标   
		    trayIcon.addMouseListener(new MouseAdapter() { // 为托盘添加鼠标适配器   
		        public void mouseClicked(MouseEvent e) { // 鼠标事件   
		            if (e.getClickCount() == 2) { // 判断是否双击了鼠标   
		                showFrame(); // 调用方法显示窗体   
		            }  
		        }  
		    });  
		    trayIcon.setToolTip("川大消息便捷查询"); // 添加工具提示文本   
		    PopupMenu popupMenu = new PopupMenu(); // 创建弹出菜单   
		    MenuItem open = new MenuItem("打开面板");
		    MenuItem exit = new MenuItem("退出程序"); // 创建菜单项   
		    //响应方法   
		    open.addActionListener(new ActionListener() {  
		        public void actionPerformed(ActionEvent e) {  
		        	showFrame();     
		        }  
		      
		    });  
		    exit.addActionListener(new ActionListener() {  
		        public void actionPerformed(ActionEvent e) {  
		        	onCloseWindow();             
		        }  
		      
		    });  
		    
		    
		    popupMenu.add(open);
		    popupMenu.add(exit); // 为弹出菜单添加菜单项   
		    trayIcon.setPopupMenu(popupMenu); // 为托盘图标加弹出菜弹   
		    SystemTray systemTray = SystemTray.getSystemTray(); // 获得系统托盘对象   
		    try {  
		        systemTray.add(trayIcon); // 为系统托盘加托盘图标   
		    } catch (Exception e) {  
		        e.printStackTrace();  
		    }  
		              
		}  
		else{
			JOptionPane.showMessageDialog(null,"notray");
		}

	}
	public void hideFrame(){
	    this.repaint();
		this.setVisible(false);
		
		
	}
	public void onCloseWindow(){
		System.exit(0);
	}

}
