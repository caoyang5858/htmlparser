package ui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.io.IOException;
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

import db.bean.NoticeBean;
import db.dao.NoticeDAO;

public class NoticeTuanweiPanel extends JPanel implements ActionListener,MouseMotionListener{


	private static final long serialVersionUID = 1L;
	public  static Cursor mycursor=new Cursor(Cursor.HAND_CURSOR);
	public  static Cursor arrowcursor=new Cursor(Cursor.DEFAULT_CURSOR);

	JTable jt=null;
	JScrollPane jsp;
	Vector<String> columnNames;
	Vector<Vector<String>> rowData;
	
	JPanel jp=null;
	JButton jbt_up=null;//上一页
	JButton jbt_down=null;
	
	NoticeDAO dao=null;
	List<NoticeBean> list=null;
	DefaultTableModel dataModel=null;
	
	Font f1=new Font("宋体",Font.PLAIN,14);
	Font f2=new Font("宋体",Font.BOLD,14);
	
	int pageSize=15;//每页显示20条记录
	int index=0;//指向list的第几个
	
	
	 int w = Toolkit.getDefaultToolkit().getScreenSize().width;//屏幕宽
	 int h = Toolkit.getDefaultToolkit().getScreenSize().height;//
	public NoticeTuanweiPanel(){
		init();
	}
	public void initData(Vector<Vector<String>> rowData){
		dao=new NoticeDAO();
		if(list==null){
		  list=dao.listAllTuanwei();
		}
		int len=list.size();
		if(len>pageSize){
			jp=new JPanel();
			jbt_up=new JButton("上一页");
			jbt_down=new JButton("下一页");
			jbt_up.setFont(f1);
			jbt_down.setFont(f1);
			jbt_up.addActionListener(this);
			jbt_down.addActionListener(this);
			jp.add(jbt_up);
			jp.add(jbt_down);
		}
		for(int i=index,j=0;j<pageSize&&i<len;i++,j++){
			Vector<String> hang = new Vector<String>();
			NoticeBean bean=list.get(i);//获得一行记录
			hang.add(bean.getTime());
			hang.add(bean.getTitle());
			hang.add(bean.getAbout());
			hang.add(bean.getDetail());
			hang.add(bean.getLink());
			rowData.add(hang);
		}
		
	}
	
	
	class TableCellTextAreaRenderer extends JTextArea implements TableCellRenderer {  

		private static final long serialVersionUID = 1L;
	    private JButton button;
	    
        
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
			
			if(column==3){
				button=new JButton();
				
				button.setBackground(getBackground());
				button.setCursor(NoticeTuanweiPanel.mycursor);
				this.button.setText(value == null ? "" : String.valueOf(value));
				return this.button;
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
		columnNames.add("时间");
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
	    jt.getColumnModel().getColumn(3).setCellEditor(new MyButtonEditor());
	    jt.getColumnModel().getColumn(4).setCellEditor(new MyButtonEditor());
	    jt.addMouseMotionListener(this);

	    //初始化 jsp JscorllPane
	    
	    
	    jsp=new JScrollPane(jt);
	   
	    jsp.setPreferredSize(new Dimension(w/2-25,h*5/7-150));
	    this.add(jsp);
	    if(jp!=null){
	    	this.add(jp);
	    }
	}
	
	
	
	private void changePage(){
		 rowData.clear();
		 int len=list.size();
		 for(int i=index,j=0;j<pageSize&&i<len;i++,j++){
				Vector<String> hang = new Vector<String>();
				NoticeBean bean=list.get(i);//获得一行记录
				hang.add(bean.getTime());
				hang.add(bean.getTitle());
				hang.add(bean.getAbout());
				hang.add(bean.getDetail());
				hang.add(bean.getLink());
				rowData.add(hang);
			}
         dataModel=new DefaultTableModel(rowData,columnNames);
		 jt.setModel(dataModel);
		 jt.getColumnModel().getColumn(3).setCellEditor(new MyButtonEditor());
		    jt.getColumnModel().getColumn(4).setCellEditor(new MyButtonEditor());
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
	}
	
	@Override

	public void mouseDragged(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void mouseMoved(MouseEvent e) {
		int col= jt.columnAtPoint(e.getPoint());//知道鼠标悬停在哪一列
		   if(col==3){
				 jt.setCursor(mycursor);
		   }
		   else{
			 jt.setCursor(arrowcursor);
		   }
	}
}


