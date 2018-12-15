package ui;

import java.awt.Dimension;
import java.awt.Toolkit;

import javax.swing.JEditorPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;


public class GradePanel extends JPanel{
	private static final long serialVersionUID = 1L;
	JEditorPane jep=null;
	JScrollPane jsp;
	 int w = Toolkit.getDefaultToolkit().getScreenSize().width;//ÆÁÄ»¿í
	  int h = Toolkit.getDefaultToolkit().getScreenSize().height;//
	  
	public GradePanel(String strHtml){
		    jep=new JEditorPane("text/html",strHtml);//ÏÔÊ¾Html 
		    jsp=new JScrollPane(jep);
		    jsp.setPreferredSize(new Dimension(w/2-25,h*5/7-150));
		    jep.setEditable(false);
		    this.add(jsp);
    }
    public static void main(){
    	
    }
}
