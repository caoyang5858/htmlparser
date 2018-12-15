package thread;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.ArrayList;

import parse.HtmlParser;
import parse.Tag;

import com.DownloadFile;

import db.bean.EmploymentBean;
import db.dao.EmploymentDAO;

public class EmploymentAnt implements Runnable{
   
	private String url_1="http://jy.scu.edu.cn/jiuye/news.php?type_id=4";
	private String url_2="http://jy.scu.edu.cn/jiuye/news.php?start=22&type_id=4";
    public EmploymentAnt(String url1,String url2){
    	url_1=url1;
    	url_2=url2;
    }
    public EmploymentAnt(){
    	
    }
	public void run() {
		DownloadFile down = new DownloadFile();
		HtmlParser parser=new HtmlParser();
		ArrayList<String> urls=new ArrayList<String>();//存放招聘的url
		String filePath_1=down.downLoadFile(url_1);//下载html文档
		String filePath_2=down.downLoadFile(url_2);
		InputStream is_1=null;
		InputStream is_2=null;
		try {
			is_1=new FileInputStream(filePath_1);
			parser.parse(is_1);
			parser.InitTagLocation();

			Tag tag=parser.findTagByLocation("1.0.1.2.0.1.0.0.0.0");
			
			ArrayList<Tag> lista=parser.findTagsByNameFromTag("a", tag);
			for(Tag taga:lista){
				String str=taga.tagProp;
				str=str.substring(str.indexOf("href=")+6);
				str=str.substring(0, str.indexOf('\''));
				String url="http://jy.scu.edu.cn/jiuye/"+str;
				urls.add(url);
			}
			//处理第二页招聘信息
			parser=null;
			parser=new HtmlParser();
			is_2=new FileInputStream(filePath_2);
			parser.parse(is_2);
			parser.InitTagLocation();     
			tag=parser.findTagByLocation("1.0.1.2.0.1.0.0.0.0");
			lista=parser.findTagsByNameFromTag("a", tag);
			for(Tag taga:lista){
				String str=taga.tagProp;
				str=str.substring(str.indexOf("href=")+6);
				str=str.substring(0, str.indexOf('\''));
				String url="http://jy.scu.edu.cn/jiuye/"+str;
				urls.add(url);
			}
			
			
			down.setFilePath("D:\\employments\\");//设置招聘信息保存目录
			EmploymentDAO dao=new EmploymentDAO();//数据库接口
			EmploymentBean bean=new EmploymentBean();
			for(String url:urls){
				String detail=down.downLoadFile(url);//下载招聘信息,得到在本地存储的路径
				//详情
				InputStream is=new FileInputStream(detail);
				parser=new HtmlParser();
				parser.parse(is);
				parser.InitTagLocation();
				Tag tag_title=parser.findTagByLocation("1.0.1.2.2.0.1.0.0");
				String title=tag_title.tagProp;//标题
				Tag tag_pubtime=parser.findTagByLocation("1.0.1.2.2.0.4.0");
				String publish_time=tag_pubtime.tagProp;
				publish_time=publish_time.substring(3);//发布时间
				
				String about=HtmlParser.getTextOfTextTag(parser.findTagByLocation("1.0.1.2.2.2.0")); 
				about=about.replaceAll("&nbsp;", "");
				String str=about.substring(about.indexOf("招聘时间：")+5);
				String employ_time=str.substring(0, 17);//招聘时间
				String employ_address=str.substring(22);//招聘地址
				about=about.substring(0, 150);
				
				
				bean.setLink(url);
				bean.setDetail(detail);
				bean.setTitle(title);
				bean.setPublish_time(publish_time);
				bean.setEmploy_time(employ_time);
				bean.setEmploy_address(employ_address);
				bean.setAbout(about);
				if(dao.searchByTitle(bean.getTitle())==null){
					/*新的招聘*/
					dao.addEmployment(bean);
				}
				else{
					break;//当前项目已有，所以后面的url对应的项目也在数据库存在
				}
				
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
