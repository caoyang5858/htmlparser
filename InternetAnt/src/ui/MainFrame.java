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
	
	//����
	Font f1=new Font("����",Font.PLAIN,14);
	
	
	JTabbedPane tp=null;
	
	boolean cho[];
	String path_CJ,path_KB;
	String strHtmlCJ,strHtmlKC;
	public MainFrame(boolean cho[]){
		this.cho=cho;//���ݹ�ע����Ϣ����
		init();
	}
	public MainFrame(boolean cho[],String chengjiPath,String kechengbiaoPath){
		this.cho=cho;//���ݹ�ע����Ϣ����
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
	
	public void dododo() throws IOException{//����γ̱�ͱ�ѧ�ڳɼ���html�ַ���
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
		    jm1=new JMenu("�˵�");
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
		    	 tp.add("���ڳɼ�",new GradePanel(strHtmlCJ));
		    	 tp.add("�γ̱�",new GradePanel(strHtmlKC));
		    }
		    
		    if(cho[0])
		    tp.add("��Ƹ��Ϣ",new EmploymentPanel());
		    if(cho[1])
		    tp.add("ѧ������",new LecturePanel());
		    if(cho[2])
		    tp.add("�����",new Lecture2Panel());
		    if(cho[3])
		    tp.add("�γ���Ϣ",new CoursePanel());
		    if(cho[4])
		    tp.add("��ί����",new NoticeTuanweiPanel());
		    if(cho[5])
		    tp.add("ѧ��������",new NoticeXuegongPanel());
		    int w = Toolkit.getDefaultToolkit().getScreenSize().width;//��Ļ��
		    int h = Toolkit.getDefaultToolkit().getScreenSize().height;//
		    
		    myInitTray();//��ʼ������
		    this.setJMenuBar(jmb);//��Ӳ˵�
		    this.getContentPane().add(tp);
		    this.setLocation(w/3, h/3);
		    this.setSize(w*2/4, h*5/7);
		    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		    this.setTitle("У԰��Ϣ��ݲ�ѯ");
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
	    jm1=new JMenu("�˵�");
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
	    
	    tp.add("��Ƹ��Ϣ",new EmploymentPanel());
	    tp.add("ѧ������",new LecturePanel());
	    tp.add("�����",new Lecture2Panel());
	    tp.add("�γ���Ϣ",new CoursePanel());
	    tp.add("��ί����",new NoticeTuanweiPanel());
	    tp.add("ѧ��������",new NoticeXuegongPanel());
	    int w = Toolkit.getDefaultToolkit().getScreenSize().width;//��Ļ��
	    int h = Toolkit.getDefaultToolkit().getScreenSize().height;//
	    
	    this.setJMenuBar(jmb);//��Ӳ˵�
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
		if (SystemTray.isSupported()) { // �ж��Ƿ�֧��ϵͳ����   
		   
		    ImageIcon icon = new ImageIcon("src\\ui\\8.png"); // ʵ����ͼ�����   
		    Image image = icon.getImage(); // ���Image����   
		    TrayIcon trayIcon = new TrayIcon(image); // ��������ͼ��   
		    trayIcon.addMouseListener(new MouseAdapter() { // Ϊ����������������   
		        public void mouseClicked(MouseEvent e) { // ����¼�   
		            if (e.getClickCount() == 2) { // �ж��Ƿ�˫�������   
		                showFrame(); // ���÷�����ʾ����   
		            }  
		        }  
		    });  
		    trayIcon.setToolTip("������Ϣ��ݲ�ѯ"); // ��ӹ�����ʾ�ı�   
		    PopupMenu popupMenu = new PopupMenu(); // ���������˵�   
		    MenuItem open = new MenuItem("�����");
		    MenuItem exit = new MenuItem("�˳�����"); // �����˵���   
		    //��Ӧ����   
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
		    popupMenu.add(exit); // Ϊ�����˵���Ӳ˵���   
		    trayIcon.setPopupMenu(popupMenu); // Ϊ����ͼ��ӵ����˵�   
		    SystemTray systemTray = SystemTray.getSystemTray(); // ���ϵͳ���̶���   
		    try {  
		        systemTray.add(trayIcon); // Ϊϵͳ���̼�����ͼ��   
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
