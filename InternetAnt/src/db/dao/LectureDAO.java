package db.dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.DateUtil;

import db.bean.LectureBean;
import db.util.DBUtil;

public class LectureDAO {
	private DBUtil dbUtil;
	public LectureDAO(){
			dbUtil=DBUtil.getDBUtil();
		
	}
   public void addLecture(LectureBean bean) throws SQLException{
	   String sql="insert into lecture values(?,?,?,?,?,?)";
	   Object[] objs=new Object[]{
			bean.getType(),
			bean.getTime(),
			bean.getLocation(),
			bean.getTopic(),
			bean.getAbout(),
			bean.getOrganizer()
	   };
	   dbUtil.update(sql, objs);
	   dbUtil.commit();
   }
   
   public LectureBean searchByTopic(String topic){
	   String sql="select * from lecture where topic=?";
	   Object[] objs=new Object[]{
			   topic
	   };
	  Object obj= dbUtil.unique(sql, new LectureParser(), objs);
	  if(obj!=null){
		  return (LectureBean)obj;
	  }
	  return null;
   }
   
   public void delByTopic(String topic) throws SQLException{
	   String sql="delete from lecture where topic=?";
	   Object[] objs=new Object[]{
			   topic
	   };
	   dbUtil.update(sql, objs);
	   dbUtil.commit();
   }
   
   /**
    * 列出在time之后的讲座
    * @param time
    * @return
    */
   public List<LectureBean> listAfterTime(Date time){
	   List<LectureBean> all=this.listAll();
	   List<LectureBean> re=new ArrayList<LectureBean>();
	   if(all!=null&&all.size()>0){
		   for(LectureBean b:all){
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
   public List<LectureBean> listAll(){
	   String sql="select * from lecture";
	   List<Object>  list=dbUtil.select(sql, new LectureParser());
	   List<LectureBean> re=new ArrayList<LectureBean>();
	   if(list!=null&&list.size()>0){
		   for(Object obj:list){
			   LectureBean bean=(LectureBean)obj;
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
    * 列出所有学术讲座
    * @return
    */
   public List<LectureBean> listAllAcdemic(){
	   String sql="select * from lecture where type='academic'";
	   List<Object>  list=dbUtil.select(sql, new LectureParser());
	   List<LectureBean> re=new ArrayList<LectureBean>();
	   if(list!=null&&list.size()>0){
		   for(Object obj:list){
			   LectureBean bean=(LectureBean)obj;
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
    * 列出所有活动讲座
    * @return
    */
   public List<LectureBean> listAllActivity(){
	   String sql="select * from lecture where type='activity'";
	   List<Object>  list=dbUtil.select(sql, new LectureParser());
	   List<LectureBean> re=new ArrayList<LectureBean>();
	   if(list!=null&&list.size()>0){
		   for(Object obj:list){
			   LectureBean bean=(LectureBean)obj;
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
