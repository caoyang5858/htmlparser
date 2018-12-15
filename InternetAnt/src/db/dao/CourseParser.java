package db.dao;

import java.sql.ResultSet;
import java.sql.SQLException;

import db.bean.CourseBean;
import db.util.RowParser;

public class CourseParser implements RowParser{

	@Override
	public Object convertToModel(ResultSet rs) {
		CourseBean bean=new CourseBean();
		
			try {
				bean.setDepart(rs.getString("depart"));
				bean.setCourseNo(rs.getString("courserNo"));
				bean.setCourseName(rs.getString("courseName"));
				bean.setClassNo(rs.getString("classNo"));
				bean.setCredit(rs.getString("credit"));
				bean.setTeacher(rs.getString("teacher"));
				bean.setCampus(rs.getString("campus"));
				bean.setTeachBuiding(rs.getString("teachBuiding"));
				bean.setClassRoom(rs.getString("classRoom"));
				return bean;
			} catch (SQLException e) {
				e.printStackTrace();
			}
		
		return null;
	}

}
