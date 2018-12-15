package db.util;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;






public class DBUtil {
	
	private static DBUtil dbUtil = new DBUtil();
	private final String url;
	private final String user;
	private final String pwd;
	private final ThreadLocal<Connection> th = new ThreadLocal<Connection>(); //线程变量，确保多线程下访问数据库安全
	static {
		try {
			Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
		} catch (ClassNotFoundException e) {
			System.out.println("jdbc注册失败");
			e.printStackTrace();
		}
	}
	private DBUtil(){
		Properties prop = new Properties();
		try {
			prop.load(DBUtil.class.getResourceAsStream("jdbc.properties"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		this.url = prop.getProperty("url");
		this.user = prop.getProperty("user");
		this.pwd = prop.getProperty("pwd");
		
	}
	
	public static DBUtil getDBUtil(){
		if(dbUtil==null){
			dbUtil = new DBUtil();
		}
		
		return dbUtil;
	}
    
	//获取连接
	public Connection getConnection(){
		Connection con = th.get();
		if(con==null){
			try {
				con = DriverManager.getConnection(url,user,pwd);
				con.setAutoCommit(false);
				th.set(con);
			} catch (SQLException e) {
				System.out.println("获取连接失败");
				e.printStackTrace();
			}
		}
		return con;
	}
	
	//关闭连接
	public void closeConnection() throws SQLException{
		
		Connection con = th.get();
		if(con!=null){
				th.remove();
				con.commit();
				con.close();
		}
		
	}
	
	
	public void commit() throws SQLException{
		Connection con = th.get();
		if(con!=null){
			con.commit();
		}
	}
	
	
	public void rollback() throws SQLException{
		Connection con = th.get();
		if(con!=null){
			con.rollback();
		}
	}
	
	/**
	 *  这个函数默认不提交,需要调用commit提交才会生效，为了事物处理
	 * @param sql 准备语句
	 * @param objs  对象数组
	 * @return 更新的行数
	 */
	public int update(String sql,Object objs[]){
		Connection con = this.getConnection();
		int row=-1;
	    try {
			PreparedStatement ps = con.prepareStatement(sql);
			if(objs!=null){
				for(int i=0;i<objs.length;i++){
					ps.setObject(i+1, objs[i]);
				}
			}
			row=ps.executeUpdate();
			ps.close();
			return row;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return row;
	}
	
	public int update(String sql){
		return update(sql,null);
	}
	
	
	/**
	 * 这里的parser一定要和查询出来的对象对应,实现RowParer接口
	 * @param sql
	 * @param parser
	 * @param objs
	 * @return
	 */
	 public List<Object> select(String sql,RowParser parser,Object objs[]){
		 List<Object> list = new ArrayList<Object>();
		 Connection con = this.getConnection();
		 try {
			PreparedStatement ps = con.prepareStatement(sql);
			if(objs!=null){
				for(int i=0;i<objs.length;i++){
					ps.setObject(i+1, objs[i]);
				}
			}
			
			ResultSet rs = ps.executeQuery();
			while(rs.next()){
			  Object obj = parser.convertToModel(rs);
			  if(obj!=null){
			     list.add(obj);
			  }
			}
			rs.close();
			ps.close();
		} catch (SQLException e) {
			
			e.printStackTrace();
		}
		 
	  return list;	
	}
	 
	  public List<Object> select(String sql,RowParser parser){
		  return select(sql,parser,null);
	  }
	  /**
		  * 这个函数用于查询一条记录，eg.(根据ID查询)
		  * @param sql
		  * @param parser
		  * @param objs
		  * @return
		  */
	public Object unique(String sql,RowParser parser,Object objs[]){
		Object obj=null;
		
		Connection con = this.getConnection();
		try {
			PreparedStatement ps = con.prepareStatement(sql);
			if(objs!=null){
				for(int i=0;i<objs.length;i++){
					ps.setObject(1+i, objs[i]);
				}
			}
			ResultSet rs = ps.executeQuery();
			if(rs.next()){
			  obj=parser.convertToModel(rs);
			}
			rs.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	   return obj;
	}
	
	
	public Object unique(String sql,RowParser parser){
		return unique(sql,parser,null);
	}
	
	
	public static void main(String[] args) throws SQLException {
//		EmploymentDAO dao=new EmploymentDAO();
//		System.out.println(dao.searchByTitle("a").getAbout());
	    
	}
}
