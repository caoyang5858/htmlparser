package ui;

import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import org.apache.commons.httpclient.NameValuePair;

import thread.CourseAnt;
import thread.EmploymentAnt;
import thread.LectureAnt;
import thread.NoticeAnt;

import com.DownloadFile;
import com.Tool;

public class LoginFrame extends JFrame implements ActionListener{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	JButton jbt_login;
	JButton jbt_skipLogin;
	JLabel jlb_userName;
	JLabel jlb_pwd;
	JTextField userName;
	JPasswordField pwd;
	JPanel jp1;
	
	JCheckBox jck_employ;
	JCheckBox jck_course;
	JCheckBox jck_huodongJZ;//�����
	JCheckBox jck_xueshuJZ;
	JCheckBox jck_tuanwei;
	JCheckBox jck_xuegong;
	JLabel jlb_tell;
	JPanel jp2;
	
	Font f1=new Font("����",Font.PLAIN,14);
	  int w = Toolkit.getDefaultToolkit().getScreenSize().width;//��Ļ��
	  int h = Toolkit.getDefaultToolkit().getScreenSize().height;//
	  
	boolean cho[]=new boolean[7];  
	public LoginFrame(){
		initFrame();
		this.setLayout(new GridLayout(2,1));
		this.add(jp2);
		this.add(jp1);
	    this.setLocation(w/3, h/3);
	    this.setSize(w*3/8, h*2/5);
	    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    this.setTitle("У԰��Ϣ��ݲ�ѯ");
	    this.setVisible(true);
	}

	public void initFrame(){
		jbt_login=new JButton("��½");
		jbt_login.setFont(f1);
		jbt_login.addActionListener(this);
		jbt_skipLogin=new JButton("������½");
		jbt_skipLogin.setFont(f1);
		jbt_skipLogin.addActionListener(this);
		jlb_userName=new JLabel("�û���:");
		jlb_userName.setFont(f1);
		jlb_pwd=new JLabel("����:");
		jlb_pwd.setFont(f1);
		userName=new JTextField(15);
		pwd= new JPasswordField(15);
		jp1=new JPanel();
		jp1.add(jlb_userName);
		jp1.add(userName);
		jp1.add(jlb_pwd);
		jp1.add(pwd);
		jp1.add(jbt_login);
		jp1.add(jbt_skipLogin);
		
		
		jlb_tell=new JLabel("��ѡ�����ע����Ϣ:", new ImageIcon("src\\ui\\8.png"), SwingConstants.LEFT);
		jlb_tell.setFont(f1);
		jck_employ=new JCheckBox("��Ƹ��Ϣ");
		jck_employ.setFont(f1);
		jck_employ.setSelected(true);
		jck_course=new JCheckBox("�γ���Ϣ");
		jck_course.setFont(f1);
		jck_course.setSelected(true);
		jck_huodongJZ=new JCheckBox("�����");
		jck_huodongJZ.setFont(f1);
		jck_huodongJZ.setSelected(true);
		jck_xueshuJZ=new JCheckBox("ѧ������");
		jck_xueshuJZ.setFont(f1);
		jck_xueshuJZ.setSelected(true);
		jck_tuanwei=new JCheckBox("��ί����");
		jck_tuanwei.setFont(f1);
		jck_tuanwei.setSelected(true);
		jck_xuegong=new JCheckBox("ѧ��������");
		jck_xuegong.setFont(f1);
		jck_xuegong.setSelected(true);
		jp2=new JPanel();
		jp2.add(jlb_tell);
		
		jp2.add(jck_employ);
		jp2.add(jck_course);
		jp2.add(jck_huodongJZ);
		jp2.add(jck_huodongJZ);
		jp2.add(jck_xueshuJZ);
		jp2.add(jck_tuanwei);
		jp2.add(jck_xuegong);


	}
	
	public static void main(String args[]){
		LoginFrame a=new LoginFrame();
		 Thread thread_emp=new Thread(new EmploymentAnt());
		   thread_emp.start();
	   
		   Thread th=new Thread(new LectureAnt());
		   th.start();
		   
		   Thread th3=new Thread(new NoticeAnt());
		   th3.run();
		   
		   Thread th4=new Thread(new CourseAnt());
		   th4.run();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		if(e.getSource()==jbt_login){
			if(jck_employ.isSelected()){
				cho[0]=true;
			}
			if(jck_xueshuJZ.isSelected()){
				cho[1]=true;
			}
			if(jck_huodongJZ.isSelected()){
				cho[2]=true;
			}
			if(jck_course.isSelected()){
				cho[3]=true;
			}
			if(jck_tuanwei.isSelected()){
				cho[4]=true;
			}
			if(jck_xuegong.isSelected()){
				cho[5]=true;
			}
			if(userName.getText().equals("")||pwd.getText().equals("")){
				JOptionPane.showMessageDialog( null,"�û��������벻��Ϊ�� !");
			}
			else{
				NameValuePair[] postData=new NameValuePair[2];//����ԭʼ��ҳ,�ṩ���ݵ�����
				postData[0]=new NameValuePair("zjh", userName.getText());
				postData[1]=new NameValuePair("mm",pwd.getText());
				DownloadFile down=new DownloadFile();
				down.setFilePath("D:\\other\\");
				String filepath=down.postDownFile("http://202.115.47.141/loginAction.do", postData);//ģ����ҳ��½
				String strHtml="";
				try {
					strHtml = Tool.readFileContent(filepath);
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				strHtml.trim();
				if(!strHtml.startsWith("<html><head><title>ѧ�����ۺϽ���</title>"))
				{
					JOptionPane.showMessageDialog(null,"��½ʧ�� !");
				}
				else{//��½�ɹ�
					String filePath2=down.postDownFile("http://202.115.47.141/bxqcjcxAction.do", null);//��ѧ�ڳɼ�
					String filePath3=down.postDownFile("http://202.115.47.141/xkAction.do?actionType=6", null);//�α�
					
					cho[6]=true;//��ʾҪ���� �ɼ��� �α����ʾ
					new MainFrame(cho,filePath2,filePath3);
					this.dispose();
				}
			}
		}
        if(e.getSource()==jbt_skipLogin){
        	if(jck_employ.isSelected()){
				cho[0]=true;
			}
			if(jck_xueshuJZ.isSelected()){
				cho[1]=true;
			}
			if(jck_huodongJZ.isSelected()){
				cho[2]=true;
			}
			if(jck_course.isSelected()){
				cho[3]=true;
			}
			if(jck_tuanwei.isSelected()){
				cho[4]=true;
			}
			if(jck_xuegong.isSelected()){
				cho[5]=true;
			}
			new MainFrame(cho);
			this.dispose();
		}
	}

	
}
