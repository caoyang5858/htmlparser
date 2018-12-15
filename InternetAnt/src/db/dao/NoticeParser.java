package db.dao;

import java.sql.ResultSet;
import java.sql.SQLException;

import db.bean.NoticeBean;
import db.util.RowParser;

public class NoticeParser implements RowParser{


	public Object convertToModel(ResultSet rs) {
		NoticeBean bean = new NoticeBean();
		try {
			bean.setNum(rs.getInt("num"));
			bean.setType(rs.getString("type"));
			bean.setTime(rs.getString("time"));
			bean.setTitle(rs.getString("title"));
			bean.setAbout(rs.getString("about"));
			bean.setDetail(rs.getString("detail"));
			bean.setLink(rs.getString("link"));
			return bean;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

}
