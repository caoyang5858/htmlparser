package ui;

import java.awt.Frame;
import java.awt.Toolkit;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JTextField;

public class AboutDialog extends JDialog {

	private static final long serialVersionUID = 1L;
	JLabel jlb=null;
	Icon ico=null;
	ImageIcon imgIcon=null;
	String text="创建具有指定文本、图像和水平对齐方式的 JLabel 实例。";
	
	public AboutDialog(Frame owner, String title, boolean modal) {
		super(owner, title, modal);
		imgIcon = new ImageIcon("src\\ui\\aboutDlg.jpg");
		
		jlb=new JLabel(text,imgIcon,JTextField.CENTER);
	      
		this.add(jlb);
	     
		int w = Toolkit.getDefaultToolkit().getScreenSize().width;//屏幕宽
		int h = Toolkit.getDefaultToolkit().getScreenSize().height;//
		this.setSize(400,400);
		 this.setLocation(w/3*2, h/3);
		this.setDefaultCloseOperation(HIDE_ON_CLOSE);
		this.setVisible(true);
	}
	
}
