package ui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

import db.bean.LectureBean;
import db.dao.LectureDAO;

public class LecturePanel extends JPanel implements ActionListener{


	private static final long serialVersionUID = 1L;
	JTable jt=null;
	JScrollPane jsp;
	Vector<String> columnNames;
	Vector<Vector<String>> rowData;
	
	JPanel jp=null;
	JButton jbt_up=null;//上一页
	JButton jbt_down=null;
	
	LectureDAO dao=null;
	List<LectureBean> list=null;
	DefaultTableModel dataModel=null;
	
	Font f1=new Font("宋体",Font.PLAIN,14);
	Font f2=new Font("宋体",Font.BOLD,14);
	
	int pageSize=15;//每页显示20条记录
	int index=0;//指向list的第几个
	
	
	 int w = Toolkit.getDefaultToolkit().getScreenSize().width;//屏幕宽
	 int h = Toolkit.getDefaultToolkit().getScreenSize().height;//
	public LecturePanel(){
		init();
	}
	public void initData(Vector<Vector<String>> rowData){
		dao=new LectureDAO();
		if(list==null){
		  list=dao.listAllAcdemic();
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
			LectureBean bean=list.get(i);//获得一行记录
			hang.add(bean.getTime());
			hang.add(bean.getLocation());
			hang.add(bean.getTopic());
			hang.add(bean.getAbout());
			hang.add(bean.getOrganizer());
			rowData.add(hang);
		}
		
	}
	
	
	
	public void init(){
		columnNames = new Vector<String>();
		columnNames.add("讲座时间");
		columnNames.add("讲座地点");
		columnNames.add("讲座题目");
		columnNames.add("关于讲座");
		columnNames.add("主办方");
		
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
	    //初始化 jsp JscorllPane
	   
	    
	    jsp=new JScrollPane(jt);
	   
	    jsp.setPreferredSize(new Dimension(w/2-25,h*5/7-150));
	    this.add(jsp);
	    if(jp!=null){
	    	this.add(jp);
	    }
	}
	
	
	
	private  void adjustTableColumnWidths(JTable table) 
	{
		JTableHeader header = table.getTableHeader();
		header.setFont(f2);
		int rowCount = table.getRowCount();
		TableColumnModel cm = table.getColumnModel();

		for (int i = 0; i < cm.getColumnCount(); i++) {
			TableColumn column = cm.getColumn(i);
			int width = (int)header.getDefaultRenderer()
			.getTableCellRendererComponent(table, column.getIdentifier()
					, false, false, -1, i).getPreferredSize().getWidth();
			for(int row = 0; row<rowCount; row++){
				int preferedWidth = (int)table.getCellRenderer(row, i).getTableCellRendererComponent(table,
						table.getValueAt(row, i), false, false, row, i).getPreferredSize().getWidth();
				width = Math.max(width, preferedWidth);
			}
			column.setPreferredWidth(width+table.getIntercellSpacing().width);
		}

		table.doLayout();
	}
	
	private void changePage(){
		 rowData.clear();
		 int len=list.size();
		 for(int i=index,j=0;j<pageSize&&i<len;i++,j++){
				Vector<String> hang = new Vector<String>();
				LectureBean bean=list.get(i);//获得一行记录
				hang.add(bean.getTime());
				hang.add(bean.getLocation());
				hang.add(bean.getTopic());
				hang.add(bean.getAbout());
				hang.add(bean.getOrganizer());
				rowData.add(hang);
			}
         dataModel=new DefaultTableModel(rowData,columnNames);
		 jt.setModel(dataModel);
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

