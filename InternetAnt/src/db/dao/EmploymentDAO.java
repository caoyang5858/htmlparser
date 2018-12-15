package db.dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import com.DateUtil;

import db.bean.EmploymentBean;
import db.util.DBUtil;

public class EmploymentDAO {
 private DBUtil dbUtil;
 public EmploymentDAO(){
	 dbUtil=DBUtil.getDBUtil();
 }
 public boolean addEmployment(EmploymentBean emp) throws SQLException{
	 String sql="insert into employment values(?,?,?,?,?,?,?)";
	 int n=0;
	 Object[] objs=new Object[]{
		emp.getPublish_time(),
		emp.getEmploy_time(),
		emp.getEmploy_address(),
		emp.getTitle(),
		emp.getAbout(),
		emp.getDetail(),
		emp.getLink()
	 };
	 n=dbUtil.update(sql, objs);
	 dbUtil.commit();
	 if(n>0){
		 return true;
	 }
	 return false;
 }
 
 
 public EmploymentBean searchByTitle(String title){ 
	 String sql="select * from employment where title=?";
	 Object[] objs=new Object[]{
				title 
		 };
	Object obj= dbUtil.unique(sql, new EmploymentParser(), objs);
	if(obj!=null){
		return (EmploymentBean)obj;
	}
	 return null;
	 
 }
 
 public void delEmploymentByTitle(String title) throws SQLException{
	 String sql="delete from employment where title=?";
	 Object[] objs=new Object[]{
			title 
	 };
	 dbUtil.update(sql, objs);
	 dbUtil.commit();
 }
 /**
  * �г�֪ͨʱ����dateʱ��֮��� ��¼
  * @param date
  * @return
  */
 public List<EmploymentBean> listnew(Date date){
	 List<EmploymentBean> re=new ArrayList<EmploymentBean>();
	 List<EmploymentBean> all = this.listAll();
	 DateUtil.setFormat("yyyy-MM-dd- HH:mm:ss");
	 if(all!=null&&all.size()>0){
		    
		 
		 for(EmploymentBean b:all){
			 
			 
			 if(DateUtil.strToDate(b.getPublish_time()).after(date)){
				 re.add(b);
			 }
		 }
		 
		 all=null;
		 return re;
	 }
	 else{
		 re=null;
		 all=null;
	 }
	 return null;
 }
 
 /**
  * �г���Ƹʱ����dateʱ��֮��� ��¼
  * @param date
  * @return
  */
 public List<EmploymentBean> listAfterToday(Date date){
	 List<EmploymentBean> re=new ArrayList<EmploymentBean>();
	 List<EmploymentBean> all = this.listAll();
	 DateUtil.setFormat("yyyy��MM��dd��HH��mm��");
	 if(all!=null&&all.size()>0){
		 
		 for(EmploymentBean b:all){
			 if(DateUtil.strToDate(b.getEmploy_time()).after(date)){
				 re.add(b);
			 }
		 }
		 
		 all=null;
		 return re;
	 }
	 else{
		 re=null;
		 all=null;
	 }
	 return null;
 }
 /**
  * �г����м�¼
  * @return
  */
 public List<EmploymentBean> listAll(){
	 String sql="select * from employment";
	 List<Object> list=dbUtil.select(sql, new EmploymentParser());
	 List<EmploymentBean> re=new ArrayList<EmploymentBean>();
	 if(list!=null && list.size()>0){
		 
		 
		 for(Object obj:list){
			 EmploymentBean bean=(EmploymentBean)obj;
			 re.add(bean);
		 }
		 list=null;
		 return re;
	 }
	 else{
		 list=null;
		 re=null;
	 }
	 return null;
 }
 
 public static void main(String args[]){
//	    Date date=new Date();//ȡʱ��
//		Calendar calendar = new GregorianCalendar();
//		 calendar.setTime(date);
//		 calendar.add(Calendar.DATE,-20);//��������������һ��.����������,������ǰ�ƶ�
//		 date=calendar.getTime(); //���� 
//		 
//		 EmploymentDAO dao  = new EmploymentDAO();
//		 List list=dao.listnew(date);
 }
}
