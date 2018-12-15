package db.dao;

import java.sql.ResultSet;
import java.sql.SQLException;

import db.bean.LectureBean;
import db.util.RowParser;

public class LectureParser implements RowParser{

	public Object convertToModel(ResultSet rs) {
		LectureBean bean=new LectureBean();
		try {
			bean.setNum(rs.getInt("num"));
			bean.setType(rs.getString("type"));
			bean.setTime(rs.getString("time"));
			bean.setLocation(rs.getString("location"));
			bean.setTopic(rs.getString("topic"));
			bean.setAbout(rs.getString("about"));
			bean.setOrganizer(rs.getString("organizer"));
			return bean;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

}
