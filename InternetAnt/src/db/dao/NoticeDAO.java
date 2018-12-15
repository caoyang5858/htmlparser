package db.dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.DateUtil;

import db.bean.NoticeBean;
import db.util.DBUtil;

public class NoticeDAO {
   private DBUtil dbUtil;
   public NoticeDAO(){
	   
	   dbUtil=DBUtil.getDBUtil();
	   
   }
   public void addNotice(NoticeBean bean) throws SQLException{
	   String sql="insert into notice values(?,?,?,?,?,?)";
	   Object[] objs=new Object[]{
		  bean.getType(),
		  bean.getTime(),
		  bean.getTitle(),
		  bean.getAbout(),
		  bean.getDetail(),
		  bean.getLink()
	   };
	   dbUtil.update(sql, objs);
	   dbUtil.commit();
   }
   
   public void delNoticeByTitle(String title) throws SQLException{
	   String sql="delete from notice where title=?";
	   Object[] objs=new Object[]{
		 title	   
	   };
	   dbUtil.update(sql, objs);
	   dbUtil.commit();
   }
   
   public NoticeBean searchNoticeByTitle(String title){
	   String sql="select * from notice where title=?";
	   Object[] objs=new Object[]{
		title	   
	   };
	   Object obj=dbUtil.unique(sql, new NoticeParser(), objs);
	   if(obj!=null){
		   return (NoticeBean)obj;
	   }
	   return null;
   }
   
   /**
    * 找出时间在tiem之后的所有公告
    * @param time
    * @return
    */
   public List<NoticeBean> listAfterTime(Date time){
	   List<NoticeBean> all=this.listAll();
	   List<NoticeBean> re=new ArrayList<NoticeBean>();
	   if(all!=null&&all.size()>0){
		   for(NoticeBean b:all){
			   if(DateUtil.strToDate(b.getTime()).after(time)){
				   re.add(b);
			   }
		   }
		   all=null;
		   return re;
	   }
	   else{
		   all=null;
		   re=null;
	   }
	   return null;
   }
   
   public List<NoticeBean> listAll(){
	   String sql="select * from notice";
	   List<Object> list=dbUtil.select(sql, new NoticeParser());
	   List<NoticeBean> re=new ArrayList<NoticeBean>();
	   if(list!=null&&list.size()>0){
		   for(Object obj:list){
			   NoticeBean bean = (NoticeBean)obj;
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
   
   /**
    * 列出所有团委公告
    */
   public List<NoticeBean> listAllTuanwei(){
	   String sql="select * from notice where type='tuanwei'";
	   List<Object> list=dbUtil.select(sql, new NoticeParser());
	   List<NoticeBean> re=new ArrayList<NoticeBean>();
	   if(list!=null&&list.size()>0){
		   for(Object obj:list){
			   NoticeBean bean = (NoticeBean)obj;
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
   /**
    * 
    * @return
    */
   public List<NoticeBean> listAllXuegong(){
	   String sql="select * from notice where type='xuegong'";
	   List<Object> list=dbUtil.select(sql, new NoticeParser());
	   List<NoticeBean> re=new ArrayList<NoticeBean>();
	   if(list!=null&&list.size()>0){
		   for(Object obj:list){
			   NoticeBean bean = (NoticeBean)obj;
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
}
