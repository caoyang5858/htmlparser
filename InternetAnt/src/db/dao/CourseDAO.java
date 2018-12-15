package db.dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import db.bean.CourseBean;

import db.util.DBUtil;

public class CourseDAO {
	 private DBUtil dbUtil;
	 public CourseDAO(){
		 dbUtil=DBUtil.getDBUtil();
	 }
	 
	 public boolean addCourse(CourseBean bean) throws SQLException{
		 String sql="insert into course values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		 Object[] objs=new Object[]{
		   bean.getDepart(),
		   bean.getCourseNo(),
		   bean.getCourseName(),
		   bean.getClassNo(),
		   bean.getCredit(),
		   bean.getTestType(),
		   bean.getTeacher(),
		   bean.getWeeks(),
		   bean.getDayOfWeek(),
		   bean.getClassToclass(),
		   bean.getCampus(),
		   bean.getTeachBuiding(),
		   bean.getClassRoom(),
		   bean.getClassVolume(),
		   bean.getStudentNum(),
		   bean.getDescription()
		 };
		 
		 
		 int n=dbUtil.update(sql, objs);
		 dbUtil.commit();
		 if(n>0){
			 return true;
		 }
		 return false;
	 }
	 
	 public List<CourseBean> listAllComputerScience(){
		   String sql="select * from course where depart='计算机学院'";
		   List<Object>  list=dbUtil.select(sql, new CourseParser());
		   List<CourseBean> re=new ArrayList<CourseBean>();
		   if(list!=null&&list.size()>0){
			   for(Object obj:list){
				   CourseBean bean=(CourseBean)obj;
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
