package db.dao;

import java.sql.ResultSet;
import java.sql.SQLException;

import db.bean.EmploymentBean;
import db.util.RowParser;

public class EmploymentParser implements RowParser{

	public Object convertToModel(ResultSet rs) {
		EmploymentBean emp=new EmploymentBean();
		try {
			emp.setNum(rs.getInt("num"));
			emp.setPublish_time(rs.getString("publish_time"));
			emp.setEmploy_time(rs.getString("employ_time"));
			emp.setEmploy_address(rs.getString("employ_address"));
			emp.setTitle(rs.getString("title"));
			emp.setAbout(rs.getString("about"));
			emp.setDetail(rs.getString("detail"));
			emp.setLink(rs.getString("link"));
			return emp;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

}
