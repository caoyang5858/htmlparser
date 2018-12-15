package ui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Vector;

import javax.swing.DefaultCellEditor;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;

import ui.NoticeXuegongPanel.MyButtonEditor;

import db.bean.EmploymentBean;
import db.dao.EmploymentDAO;

public class EmploymentPanel extends JPanel implements ActionListener,MouseMotionListener{


	private static final long serialVersionUID = 1L;
	JTable jt=null;
	JScrollPane jsp;
	Vector<String> columnNames;
	Vector<Vector<String>> rowData;
	
	JPanel jp=null;
	JButton jbt_up=null;//��һҳ
	JButton jbt_down=null;
	
	JPanel jp2=null;
	JButton jbt_new=null;//���·���
	JButton jbt_jin=null;//�����Ƹ
	JButton jbt_all=null;//������Ƹ
	
	EmploymentDAO dao=null;
	List<EmploymentBean> list=null;
	DefaultTableModel dataModel=null;
	
	Font f1=new Font("����",Font.PLAIN,14);
	Font f2=new Font("����",Font.BOLD,14);
	Font f3=new Font("����",Font.PLAIN,12);
	
	int pageSize=15;//ÿҳ��ʾ20����¼
	int index=0;//ָ��list�ĵڼ���
	
	
	 int w = Toolkit.getDefaultToolkit().getScreenSize().width;//��Ļ��
	 int h = Toolkit.getDefaultToolkit().getScreenSize().height;//
	public EmploymentPanel(){
		init();
	}
	public void initData(Vector<Vector<String>> rowData){
		dao=new EmploymentDAO();
		if(list==null){
		  list=dao.listAll();
		}
		int len=list.size();
		if(len>pageSize){
			jp=new JPanel();
			jbt_up=new JButton("��һҳ");
			jbt_down=new JButton("��һҳ");
			jbt_up.setFont(f3);
			jbt_down.setFont(f3);
			jbt_up.addActionListener(this);
			jbt_down.addActionListener(this);
			jp.add(jbt_up);
			jp.add(jbt_down);
			
			jp2=new JPanel();
			jbt_new = new JButton("���·���");
			jbt_jin = new JButton("�����Ƹ");
			jbt_all = new JButton("������Ƹ");
			jbt_new.setFont(f1);
			jbt_jin.setFont(f1);
			jbt_all.setFont(f1);
			jbt_new.addActionListener(this);
			jbt_jin.addActionListener(this);
			jbt_all.addActionListener(this);
			jbt_new.setForeground(Color.MAGENTA);
			jbt_jin.setForeground(Color.MAGENTA);
			jbt_all.setForeground(Color.MAGENTA);
			jp2.add(jbt_new);
			jp2.add(jbt_jin);
			jp2.add(jbt_all);
		}
		for(int i=index,j=0;j<pageSize&&i<len;i++,j++){
			Vector<String> hang = new Vector<String>();
			EmploymentBean bean=list.get(i);//���һ�м�¼
			hang.add(bean.getEmploy_time());
			hang.add(bean.getEmploy_address());
			hang.add(bean.getTitle());
			hang.add(bean.getAbout());
			hang.add(bean.getDetail());
			hang.add(bean.getLink());
			rowData.add(hang);
		}
		
	}
	
	class TableCellTextAreaRenderer extends JTextArea implements TableCellRenderer {  

		private static final long serialVersionUID = 1L;
		public TableCellTextAreaRenderer() {       
			setLineWrap(true);       
			setWrapStyleWord(true);   
		}   
		public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {      
			// ���㵱���е���Ѹ߶�        
			int maxPreferredHeight = 0;       
			for (int i = 0; i < table.getColumnCount(); i++) {     
				setText("" + table.getValueAt(row, i));          
				setSize(table.getColumnModel().getColumn(column).getWidth(), 0);     
				maxPreferredHeight = Math.max(maxPreferredHeight, getPreferredSize().height);     
			}    
			if (table.getRowHeight(row) != maxPreferredHeight) // ��������������Ϲæ        
				table.setRowHeight(row, maxPreferredHeight);    
			if(row%2==1){
				this.setBackground(Color.GREEN);
			}
			else{
			    this.setBackground(Color.CYAN);
			}
				setText(value == null ? "" : value.toString());     
				return this;   
			
		}

	}
	
	
	class MyButtonEditor extends DefaultCellEditor  
	{  
	  
	    /** 
	     * serialVersionUID 
	     */  
	    private static final long serialVersionUID = -6546334664166791132L;  
	  
	  
	    private JButton button;  
	  
	    public MyButtonEditor()  
	    {  
	        // DefautlCellEditor�д˹���������Ҫ����һ�������������ʹ�õ���ֱ��newһ�����ɡ�   
	        super(new JTextField());  
	  
	        // ���õ�����μ���༭��   
	        this.setClickCountToStart(1);  
	  
	        this.initButton();  
	  
	  
	    }  
	  
	    private void initButton()  
	    {  
	        this.button = new JButton();  
	        button.setCursor(NoticeTuanweiPanel.mycursor);
	        
	        // Ϊ��ť����¼�������ֻ�����ActionListner�¼���Mouse�¼���Ч��   
	      
	        this.button.addActionListener(new ActionListener()  
	        {  
	            public void actionPerformed(ActionEvent e)  
	            {  
	                // ����ȡ���༭���¼����������tableModel��setValue������   
	                MyButtonEditor.this.fireEditingCanceled();  
	                String cmd = "rundll32 url.dll,FileProtocolHandler "+button.getText();   
	                try {
	                    Runtime.getRuntime().exec(cmd);
	                } catch (IOException e1) {
	                 e1.printStackTrace();
	                }
	                // �������������������   
	                // ���Խ�table���룬ͨ��getSelectedRow,getSelectColumn������ȡ����ǰѡ����к��м����������ȡ�   
	            }  
	        });  
	  
	    }  
	  
	  
	    /** 
	     * ������д����ı༭����������һ��JPanel���󼴿ɣ�Ҳ����ֱ�ӷ���һ��Button���󣬵��������������������Ԫ�� 
	     */  
	    @Override  
	    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column)  
	    {  
	        // ֻΪ��ť��ֵ���ɡ�Ҳ����������������   
	        this.button.setText(value == null ? "" : String.valueOf(value));  
	  
	        return this.button;  
	    }  
	
	  
	} 
	
	
	public void init(){
		columnNames = new Vector<String>();
		columnNames.add("��Ƹʱ��");
		columnNames.add("��Ƹ��ַ");
		columnNames.add("����");
		columnNames.add("��Ҫ");
		columnNames.add("����");
		columnNames.add("����");
		
		rowData=new Vector<Vector<String>>();
		initData(rowData);//��ʼ������
		//��ʼ��JTable
		dataModel=new DefaultTableModel(rowData,columnNames);
	    jt=new JTable();
	    jt.setModel(dataModel);//��������ģ�ͣ����ģ�͵ĸ��»�ʵʱ����ʾ��������
	   // jt.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
	   // adjustTableColumnWidths(jt);
	    jt.setFont(f1);
	    jt.setDefaultRenderer(Object.class, new TableCellTextAreaRenderer());//���õ�Ԫ��ĸ�ʽ
	    jt.getColumnModel().getColumn(4).setCellEditor(new MyButtonEditor());
	    jt.getColumnModel().getColumn(5).setCellEditor(new MyButtonEditor());

	    jt.addMouseMotionListener(this);
	    //��ʼ�� jsp JscorllPane
	   
	    
	    jsp=new JScrollPane(jt);
	   
	    jsp.setPreferredSize(new Dimension(w/2-25,h*5/7-150));
	    this.add(jsp);
	    if(jp!=null){
	    	this.add(jp);
	    	this.add(jp2);
	    }
	}
	
	
	
	private void changePage(){
		 rowData.clear();
		 int len=list.size();
		 for(int i=index,j=0;j<pageSize&&i<len;i++,j++){
				Vector<String> hang = new Vector<String>();
				EmploymentBean bean=list.get(i);//���һ�м�¼
				hang.add(bean.getEmploy_time());
				hang.add(bean.getEmploy_address());
				hang.add(bean.getTitle());
				hang.add(bean.getAbout());
				hang.add(bean.getDetail());
				hang.add(bean.getLink());
				rowData.add(hang);
			}
         dataModel=new DefaultTableModel(rowData,columnNames);
		 jt.setModel(dataModel);
		 jt.getColumnModel().getColumn(4).setCellEditor(new MyButtonEditor());
		 jt.getColumnModel().getColumn(5).setCellEditor(new MyButtonEditor());
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource()==jbt_up){
			if(index>pageSize-1){
			 index-=pageSize;
			 jt.removeAll();
			 
			 changePage();//��ҳ
             
			 jt.invalidate();
			}
			else{
				JOptionPane.showMessageDialog( null,"�Ѿ��ǵ�һҳ !");
				System.out.println(index);
			}
		}
		if(e.getSource()==jbt_down){
			
			if(index<list.size()-pageSize){
				index+=pageSize;
				
				jt.removeAll();//ɾ����������
				 changePage();
				jt.invalidate();
			}
			else{
				JOptionPane.showMessageDialog( null,"�Ѿ������һҳ !");
				System.out.println(index);
			}
		}
		
		if(e.getSource()==jbt_new){
			list.clear();
			rowData.clear();
			index=0;
			
			Date date=new Date();//ȡʱ��
			Calendar calendar = new GregorianCalendar();
			 calendar.setTime(date);
			 calendar.add(Calendar.DATE,-8);//��������������һ��.����������,������ǰ�ƶ�
			 date=calendar.getTime(); //8��֮�ڷ��� 
			list=dao.listnew(date);
			
			int len=list.size();
			for(int i=index,j=0;j<pageSize&&i<len;i++,j++){
				Vector<String> hang = new Vector<String>();
				EmploymentBean bean=list.get(i);//���һ�м�¼
				hang.add(bean.getEmploy_time());
				hang.add(bean.getEmploy_address());
				hang.add(bean.getTitle());
				hang.add(bean.getAbout());
				hang.add(bean.getDetail());
				hang.add(bean.getLink());
				rowData.add(hang);
			}
		   dataModel=new DefaultTableModel(rowData,columnNames);
		   jt.setModel(dataModel);
		   jt.getColumnModel().getColumn(4).setCellEditor(new MyButtonEditor());
			 jt.getColumnModel().getColumn(5).setCellEditor(new MyButtonEditor());
			
		}
		if(e.getSource()==jbt_jin){
			list.clear();
			rowData.clear();
			index=0;
			
			Date date=new Date();//ȡʱ��
			Calendar calendar = new GregorianCalendar();
			 calendar.setTime(date);
			 calendar.add(Calendar.DATE,-2);//��������������һ��.����������,������ǰ�ƶ�
			 date=calendar.getTime(); //ǰ�� 
			list=dao.listAfterToday(date);
			
			int len=list.size();
			for(int i=index,j=0;j<pageSize&&i<len;i++,j++){
				Vector<String> hang = new Vector<String>();
				EmploymentBean bean=list.get(i);//���һ�м�¼
				hang.add(bean.getEmploy_time());
				hang.add(bean.getEmploy_address());
				hang.add(bean.getTitle());
				hang.add(bean.getAbout());
				hang.add(bean.getDetail());
				hang.add(bean.getLink());
				rowData.add(hang);
			}
		   dataModel=new DefaultTableModel(rowData,columnNames);
		   jt.setModel(dataModel);
		   jt.getColumnModel().getColumn(4).setCellEditor(new MyButtonEditor());
			 jt.getColumnModel().getColumn(5).setCellEditor(new MyButtonEditor());
			
		}
		if(e.getSource()==jbt_all){
			list.clear();
			rowData.clear();
			index=0;
			list=dao.listAll();
			int len=list.size();
			for(int i=index,j=0;j<pageSize&&i<len;i++,j++){
				Vector<String> hang = new Vector<String>();
				EmploymentBean bean=list.get(i);//���һ�м�¼
				hang.add(bean.getEmploy_time());
				hang.add(bean.getEmploy_address());
				hang.add(bean.getTitle());
				hang.add(bean.getAbout());
				hang.add(bean.getDetail());
				hang.add(bean.getLink());
				rowData.add(hang);
			}
		   dataModel=new DefaultTableModel(rowData,columnNames);
		   jt.setModel(dataModel);
		   jt.getColumnModel().getColumn(4).setCellEditor(new MyButtonEditor());
			 jt.getColumnModel().getColumn(5).setCellEditor(new MyButtonEditor());
		}
	}
	@Override
	public void mouseDragged(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void mouseMoved(MouseEvent e) {
		// TODO Auto-generated method stub
		int col= jt.columnAtPoint(e.getPoint());//֪�������ͣ����һ��
		   if(col==4){
				 jt.setCursor(NoticeTuanweiPanel.mycursor);
		   }
		   else{
			 jt.setCursor(NoticeTuanweiPanel.arrowcursor);
		   }
	}
}


