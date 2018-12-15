package Learn;

import java.awt.Frame;

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

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;


public class Relax {
	
	public static void main(String args[]){
		new Relax();
	}
    
	public Relax(){
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (UnsupportedLookAndFeelException e) {
			e.printStackTrace();
		}

		myInitTray();
		
		run();
	}
	
	public void run(){
		while(true){
			try {
				Thread.sleep(1000*60*45);//45分钟休息一下
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			new AboutDialog(null,"您应该出去休息一下了哦！",true);
		}
	}
	
	public void myInitTray(){
		if (SystemTray.isSupported()) { // 判断是否支持系统托盘   
		   
		    ImageIcon icon = new ImageIcon("src\\Learn\\tray.png"); // 实例化图像对象   
		    Image image = icon.getImage(); // 获得Image对象   
		    TrayIcon trayIcon = new TrayIcon(image); // 创建托盘图标   
		    trayIcon.addMouseListener(new MouseAdapter() { // 为托盘添加鼠标适配器   
		        public void mouseClicked(MouseEvent e) { // 鼠标事件   
		            if (e.getClickCount() == 2) { // 判断是否双击了鼠标   
			        	JOptionPane.showMessageDialog(null,"休息一下吧，为了您的健康！");  
		            }  
		        }  
		    });  
		    trayIcon.setToolTip("Relax"); // 添加工具提示文本   
		    PopupMenu popupMenu = new PopupMenu(); // 创建弹出菜单   
		    MenuItem open = new MenuItem("关于程序");
		    MenuItem exit = new MenuItem("退出程序"); // 创建菜单项   
		    //响应方法   
		    open.addActionListener(new ActionListener() {  
		        public void actionPerformed(ActionEvent e) {  
		        	JOptionPane.showMessageDialog(null,"每隔50分钟休息一下吧，为了您的健康！");  
		        }  
		      
		    });  
		    exit.addActionListener(new ActionListener() {  
		        public void actionPerformed(ActionEvent e) {  
		        	System.exit(0);             
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
}


 class AboutDialog extends JDialog {

	private static final long serialVersionUID = 1L;
	JLabel jlb=null;
	Icon ico=null;
	ImageIcon imgIcon=null;
	String text="休息一下吧！";
	
	public AboutDialog(Frame owner, String title, boolean modal) {
		super(owner, title, modal);
		imgIcon = new ImageIcon("src\\Learn\\hhhh.jpg");
		
		jlb=new JLabel(text,imgIcon,JTextField.CENTER);
	      
		this.add(jlb);
	     
		int w = Toolkit.getDefaultToolkit().getScreenSize().width;//屏幕宽
		int h = Toolkit.getDefaultToolkit().getScreenSize().height;//
		this.setSize(450,350);
		 this.setLocation(w/3, h/3);
		this.setDefaultCloseOperation(HIDE_ON_CLOSE);
		this.setVisible(true);
	}
	
}
