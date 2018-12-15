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
				Thread.sleep(1000*60*45);//45������Ϣһ��
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			new AboutDialog(null,"��Ӧ�ó�ȥ��Ϣһ����Ŷ��",true);
		}
	}
	
	public void myInitTray(){
		if (SystemTray.isSupported()) { // �ж��Ƿ�֧��ϵͳ����   
		   
		    ImageIcon icon = new ImageIcon("src\\Learn\\tray.png"); // ʵ����ͼ�����   
		    Image image = icon.getImage(); // ���Image����   
		    TrayIcon trayIcon = new TrayIcon(image); // ��������ͼ��   
		    trayIcon.addMouseListener(new MouseAdapter() { // Ϊ����������������   
		        public void mouseClicked(MouseEvent e) { // ����¼�   
		            if (e.getClickCount() == 2) { // �ж��Ƿ�˫�������   
			        	JOptionPane.showMessageDialog(null,"��Ϣһ�°ɣ�Ϊ�����Ľ�����");  
		            }  
		        }  
		    });  
		    trayIcon.setToolTip("Relax"); // ��ӹ�����ʾ�ı�   
		    PopupMenu popupMenu = new PopupMenu(); // ���������˵�   
		    MenuItem open = new MenuItem("���ڳ���");
		    MenuItem exit = new MenuItem("�˳�����"); // �����˵���   
		    //��Ӧ����   
		    open.addActionListener(new ActionListener() {  
		        public void actionPerformed(ActionEvent e) {  
		        	JOptionPane.showMessageDialog(null,"ÿ��50������Ϣһ�°ɣ�Ϊ�����Ľ�����");  
		        }  
		      
		    });  
		    exit.addActionListener(new ActionListener() {  
		        public void actionPerformed(ActionEvent e) {  
		        	System.exit(0);             
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
}


 class AboutDialog extends JDialog {

	private static final long serialVersionUID = 1L;
	JLabel jlb=null;
	Icon ico=null;
	ImageIcon imgIcon=null;
	String text="��Ϣһ�°ɣ�";
	
	public AboutDialog(Frame owner, String title, boolean modal) {
		super(owner, title, modal);
		imgIcon = new ImageIcon("src\\Learn\\hhhh.jpg");
		
		jlb=new JLabel(text,imgIcon,JTextField.CENTER);
	      
		this.add(jlb);
	     
		int w = Toolkit.getDefaultToolkit().getScreenSize().width;//��Ļ��
		int h = Toolkit.getDefaultToolkit().getScreenSize().height;//
		this.setSize(450,350);
		 this.setLocation(w/3, h/3);
		this.setDefaultCloseOperation(HIDE_ON_CLOSE);
		this.setVisible(true);
	}
	
}
