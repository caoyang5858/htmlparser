package thread;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.ArrayList;

import org.apache.commons.httpclient.NameValuePair;

import parse.HtmlParser;
import parse.Tag;

import com.DownloadFile;

import db.bean.CourseBean;
import db.dao.CourseDAO;

public class CourseAnt implements Runnable{
    private String URL="http://202.115.47.141/courseSearchAction.do";//原始页面，通过他获得第一页
    private String url="http://202.115.47.141/courseSearchAction.do?actionType=2&pageNumber=";
    private void doIt(ArrayList<CourseBean> beans,CourseDAO dao,HtmlParser parser) throws SQLException{
    	Tag tag_table=parser.findTagByLocation("0.0.1.4.0.0.0");
		ArrayList<Tag> list_trs=tag_table.children;
		int len=list_trs.size();
		for(int i=1;i<len;i++){//忽略第一行无用数据
			Tag tag_tr=list_trs.get(i);
			ArrayList<Tag> list_tds=tag_tr.children;
			CourseBean bean=new CourseBean();
		
			for(int j=0;j<16;j++)//一共16个数据
			{
				String str_data=list_tds.get(j).children.get(0).tagProp;
				str_data=str_data.replaceAll("&nbsp;","");
				str_data=str_data.replaceAll("\r\n", "");
				
				switch(j){
				 case 0:  bean.setCourseNo(str_data);   break;
				 case 1:  bean.setDepart(str_data);   break;
				 case 2:  bean.setCourseName(str_data);   break;
				 case 3:  bean.setClassNo(str_data);   break;
				 case 4:  bean.setCredit(str_data);   break;
				 case 5:  bean.setTestType(str_data);   break;
				 case 6:  bean.setTeacher(str_data);   break;
				 case 7:  bean.setWeeks(str_data);   break;
				 case 8:  bean.setDayOfWeek(str_data);   break;
				 case 9:  bean.setClassToclass(str_data);  break;
				 case 10: bean.setCampus(str_data);     break;
				 case 11: bean.setTeachBuiding(str_data); break;
				 case 12: bean.setClassRoom(str_data);   break;
				 case 13: bean.setClassVolume(str_data); break;
				 case 14: bean.setStudentNum(str_data);  break;
				 case 15: bean.setDescription(str_data); break;
				 default :break;
				}
				
			}
			
			beans.add(bean);
		}
		
		for(CourseBean b:beans){//将本页获取的课程信息存入数据库
			dao.addCourse(b);
		}
		beans.clear();//清空
    }
	public void run() {
		  DownloadFile down = new DownloadFile();
		  down.setFilePath("D:\\course\\");
		  NameValuePair[] postData=new NameValuePair[19];//分析原始网页,提供传递的数据
		  postData[0] = new NameValuePair("pageNumber", "0");
		  postData[1] = new NameValuePair("actionType", "1");
		  postData[2] = new NameValuePair("pageSize","20");//每页显示20条
		  postData[3] = new NameValuePair("showColumn","kch#课程号");
		  postData[4] = new NameValuePair("showColumn","kkxsjc#开课系");
		  postData[5] = new NameValuePair("showColumn","kcm#课程名");
		  postData[6] = new NameValuePair("showColumn","kxh#课序号");
		  postData[7] = new NameValuePair("showColumn","xf#学分");
		  postData[8] = new NameValuePair("showColumn","kslxmc#考试类型");
		  postData[9] = new NameValuePair("showColumn","skjs#教师");
		  postData[10] = new NameValuePair("showColumn","zcsm#周次");
		  postData[11] = new NameValuePair("showColumn","skxq#星期");
		  postData[12] = new NameValuePair("showColumn","skjc#节次");
		  postData[13] = new NameValuePair("showColumn","xqm#校区");
		  postData[14] = new NameValuePair("showColumn","jxlm#教学楼");
		  postData[15] = new NameValuePair("showColumn","jasm#教室");
		  postData[16] = new NameValuePair("showColumn","bkskrl#课容量");
		  postData[17] = new NameValuePair("showColumn","xss#学生数");
		  postData[18] = new NameValuePair("showColumn","xkxzsm#选课限制说明");
		  
		  HtmlParser parser=new HtmlParser();
		  String file_1=down.postDownFile(URL,postData);//下载第一页
		  int sumPage=0;//总页数
		  InputStream is_1=null;
		  try {
			is_1=new FileInputStream(file_1);
			parser.parse(is_1);
			parser.InitTagLocation();
			
			
			String str=HtmlParser.getTextOfTextTag(parser.findTagByLocation("0.0.1.6"));
			String sumPageStr=str.substring(7, 10);
			sumPage=Integer.valueOf(sumPageStr);//获得总页数
			
			ArrayList<CourseBean> beans=new ArrayList<CourseBean>();
			CourseDAO dao=new CourseDAO();
		    doIt(beans,dao,parser);//从parser提取数据并放入数据库,将第一页获取的课程信息存入数据库
			
			for(int page=2;page<sumPage;page++){//将第1到sumPage-1页获取的课程信息存入数据库
				String filePath=down.postDownFile(url+page,null);
				is_1=new FileInputStream(filePath);
				parser=new HtmlParser();
				parser.parse(is_1);
				parser.InitTagLocation();
				doIt(beans,dao,parser);
			}
			
			//处理最后一页，可能没有20项数据
			String file_end=down.postDownFile(url+sumPage, null);
			is_1=new FileInputStream(file_end);
			parser=new HtmlParser();
			parser.parse(is_1);
			parser.InitTagLocation();
			doIt(beans,dao,parser);//由于这个函数中 int len=list_trs.size();
			                       //              for(int i=1;i<len;i++){ 这样的写法,最后一页处理和其他页无异
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}
   
}
