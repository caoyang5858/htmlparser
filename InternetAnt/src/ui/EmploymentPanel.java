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
	JButton jbt_up=null;//上一页
	JButton jbt_down=null;
	
	JPanel jp2=null;
	JButton jbt_new=null;//最新发布
	JButton jbt_jin=null;//最近招聘
	JButton jbt_all=null;//所有招聘
	
	EmploymentDAO dao=null;
	List<EmploymentBean> list=null;
	DefaultTableModel dataModel=null;
	
	Font f1=new Font("宋体",Font.PLAIN,14);
	Font f2=new Font("宋体",Font.BOLD,14);
	Font f3=new Font("宋体",Font.PLAIN,12);
	
	int pageSize=15;//每页显示20条记录
	int index=0;//指向list的第几个
	
	
	 int w = Toolkit.getDefaultToolkit().getScreenSize().width;//屏幕宽
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
			jbt_up=new JButton("上一页");
			jbt_down=new JButton("下一页");
			jbt_up.setFont(f3);
			jbt_down.setFont(f3);
			jbt_up.addActionListener(this);
			jbt_down.addActionListener(this);
			jp.add(jbt_up);
			jp.add(jbt_down);
			
			jp2=new JPanel();
			jbt_new = new JButton("最新发布");
			jbt_jin = new JButton("最近招聘");
			jbt_all = new JButton("所有招聘");
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
			EmploymentBean bean=list.get(i);//获得一行记录
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
			// 计算当下行的最佳高度        
			int maxPreferredHeight = 0;       
			for (int i = 0; i < table.getColumnCount(); i++) {     
				setText("" + table.getValueAt(row, i));          
				setSize(table.getColumnModel().getColumn(column).getWidth(), 0);     
				maxPreferredHeight = Math.max(maxPreferredHeight, getPreferredSize().height);     
			}    
			if (table.getRowHeight(row) != maxPreferredHeight) // 少了这行则处理器瞎忙        
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
	        // DefautlCellEditor有此构造器，需要传入一个，但这个不会使用到，直接new一个即可。   
	        super(new JTextField());  
	  
	        // 设置点击几次激活编辑。   
	        this.setClickCountToStart(1);  
	  
	        this.initButton();  
	  
	  
	    }  
	  
	    private void initButton()  
	    {  
	        this.button = new JButton();  
	        button.setCursor(NoticeTuanweiPanel.mycursor);
	        
	        // 为按钮添加事件。这里只能添加ActionListner事件，Mouse事件无效。   
	      
	        this.button.addActionListener(new ActionListener()  
	        {  
	            public void actionPerformed(ActionEvent e)  
	            {  
	                // 触发取消编辑的事件，不会调用tableModel的setValue方法。   
	                MyButtonEditor.this.fireEditingCanceled();  
	                String cmd = "rundll32 url.dll,FileProtocolHandler "+button.getText();   
	                try {
	                    Runtime.getRuntime().exec(cmd);
	                } catch (IOException e1) {
	                 e1.printStackTrace();
	                }
	                // 这里可以做其它操作。   
	                // 可以将table传入，通过getSelectedRow,getSelectColumn方法获取到当前选择的行和列及其它操作等。   
	            }  
	        });  
	  
	    }  
	  
	  
	    /** 
	     * 这里重写父类的编辑方法，返回一个JPanel对象即可（也可以直接返回一个Button对象，但是那样会填充满整个单元格） 
	     */  
	    @Override  
	    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column)  
	    {  
	        // 只为按钮赋值即可。也可以作其它操作。   
	        this.button.setText(value == null ? "" : String.valueOf(value));  
	  
	        return this.button;  
	    }  
	
	  
	} 
	
	
	public void init(){
		columnNames = new Vector<String>();
		columnNames.add("招聘时间");
		columnNames.add("招聘地址");
		columnNames.add("标题");
		columnNames.add("概要");
		columnNames.add("详情");
		columnNames.add("链接");
		
		rowData=new Vector<Vector<String>>();
		initData(rowData);//初始化数据
		//初始化JTable
		dataModel=new DefaultTableModel(rowData,columnNames);
	    jt=new JTable();
	    jt.setModel(dataModel);//设置数据模型，设计模型的更新会实时的显示到界面上
	   // jt.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
	   // adjustTableColumnWidths(jt);
	    jt.setFont(f1);
	    jt.setDefaultRenderer(Object.class, new TableCellTextAreaRenderer());//设置单元格的格式
	    jt.getColumnModel().getColumn(4).setCellEditor(new MyButtonEditor());
	    jt.getColumnModel().getColumn(5).setCellEditor(new MyButtonEditor());

	    jt.addMouseMotionListener(this);
	    //初始化 jsp JscorllPane
	   
	    
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
				EmploymentBean bean=list.get(i);//获得一行记录
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
			 
			 changePage();//换页
             
			 jt.invalidate();
			}
			else{
				JOptionPane.showMessageDialog( null,"已经是第一页 !");
				System.out.println(index);
			}
		}
		if(e.getSource()==jbt_down){
			
			if(index<list.size()-pageSize){
				index+=pageSize;
				
				jt.removeAll();//删除所有数据
				 changePage();
				jt.invalidate();
			}
			else{
				JOptionPane.showMessageDialog( null,"已经是最后一页 !");
				System.out.println(index);
			}
		}
		
		if(e.getSource()==jbt_new){
			list.clear();
			rowData.clear();
			index=0;
			
			Date date=new Date();//取时间
			Calendar calendar = new GregorianCalendar();
			 calendar.setTime(date);
			 calendar.add(Calendar.DATE,-8);//把日期往后增加一天.整数往后推,负数往前移动
			 date=calendar.getTime(); //8天之内发布 
			list=dao.listnew(date);
			
			int len=list.size();
			for(int i=index,j=0;j<pageSize&&i<len;i++,j++){
				Vector<String> hang = new Vector<String>();
				EmploymentBean bean=list.get(i);//获得一行记录
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
			
			Date date=new Date();//取时间
			Calendar calendar = new GregorianCalendar();
			 calendar.setTime(date);
			 calendar.add(Calendar.DATE,-2);//把日期往后增加一天.整数往后推,负数往前移动
			 date=calendar.getTime(); //前天 
			list=dao.listAfterToday(date);
			
			int len=list.size();
			for(int i=index,j=0;j<pageSize&&i<len;i++,j++){
				Vector<String> hang = new Vector<String>();
				EmploymentBean bean=list.get(i);//获得一行记录
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
				EmploymentBean bean=list.get(i);//获得一行记录
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
		int col= jt.columnAtPoint(e.getPoint());//知道鼠标悬停在哪一列
		   if(col==4){
				 jt.setCursor(NoticeTuanweiPanel.mycursor);
		   }
		   else{
			 jt.setCursor(NoticeTuanweiPanel.arrowcursor);
		   }
	}
}


