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

import db.bean.LectureBean;
import db.dao.LectureDAO;

public class LectureAnt implements Runnable{
	private String url_1="http://www.scu.edu.cn/portal/xskb/H6013index_1.htm";//学术讲座
	private String url_2="http://tuanwei.scu.edu.cn/tw/jcygshow.php?type=jcyg";//活动讲座
    public LectureAnt(String url1,String url2){
    	this.url_1=url1;
    	this.url_2=url2;
    }
    public LectureAnt(){
    	;
    }
    private ArrayList<LectureBean> trToBean(ArrayList<Tag> list_tr){
    	ArrayList<LectureBean> re=null;
		if(list_tr!=null){
			re=new ArrayList<LectureBean>();
			for(Tag tr:list_tr){
				 LectureBean bean=new LectureBean();
				 bean.setType("academic");
				 for(int i=0;i<tr.children.size();i++){//tr.children 是td的list
					 switch(i){
					   case 0:
						   bean.setTime(tr.children.get(i).children.get(0).tagProp);
						  break;
					   case 1:
						   bean.setLocation(tr.children.get(i).children.get(0).tagProp);
						   break;

					   case 2:
						   bean.setTopic(tr.children.get(i).children.get(0).tagProp);
						  break;
					   case 3:
						   bean.setAbout(tr.children.get(i).children.get(0).tagProp);
						   break;

					   case 4:
						   bean.setOrganizer(tr.children.get(i).children.get(0).tagProp);
						  break;
					   default:
						  break;
					 }
				 }
				 re.add(bean);
			}
		}
		return re;
    }
    private ArrayList<LectureBean> trToBean_2(ArrayList<Tag> list_tr){
    	ArrayList<LectureBean> re=null;
    	if(list_tr!=null){
    		re=new ArrayList<LectureBean>();
    		for(int t=1;t<list_tr.size();t++){//忽略第一个tr ,因为不是内容
    			 Tag tr=list_tr.get(t);
    			 
    			 LectureBean bean=new LectureBean();
				 bean.setType("activity");
				 for(int i=0;i<tr.children.size();i++){
					 switch(i){
					   case 0:
						   bean.setTime(tr.children.get(i).children.get(0).tagProp);
						  break;
					   case 1:
						   bean.setLocation(tr.children.get(i).children.get(0).tagProp);
						   break;
					   case 2:
						   bean.setTopic(tr.children.get(i).children.get(0).tagProp);
						  break;
					   case 3:
						   String str=HtmlParser.getTextOfTextTag(tr.children.get(i));//文本处理
						   str=str.trim();
						   str=str.replaceFirst("&nbsp;", "");
						   str=str.trim();
						   str=str.replaceAll("&nbsp;", " ");
						   bean.setAbout(str);
						   break;
					   case 4:
						   bean.setOrganizer(tr.children.get(i).children.get(0).tagProp);
						  break;
					   default:
						  break;
					 }
				 }
				 re.add(bean);
    		}
    	}
    	
    	return re;
    }
	public void run() {
		DownloadFile down = new DownloadFile();
		HtmlParser parser=new HtmlParser();
		String filePath_1=down.downLoadFile(url_1);
		String filePath_2=down.downLoadFile(url_2);
		InputStream is_1=null;
		InputStream is_2=null;
		try {
			is_1 = new FileInputStream(filePath_1);
			parser.parse(is_1); //在Parser里面形成html分析树
			parser.InitTagLocation();
			Tag tag=parser.findTagByLocation("0.0.1.11.0.0.1.2.0.0.1.2");
			ArrayList<Tag> list_tr=parser.findTagsByNameFromTag("TR", tag);
			ArrayList<LectureBean> beans=trToBean(list_tr);
			LectureDAO dao=new LectureDAO();
			for(LectureBean bean:beans){ //将数据库中不存在的项目 存入数据库
				if(dao.searchByTopic(bean.getTopic())==null){
					/*这儿是新项目，可以弹出通知*/
					dao.addLecture(bean);
				}else{
					break;//说明当前 的数据在数据库中有，以下的项目无需判断也在数据库
				}
			}
			parser=null;// 放弃url_1对应的html树
			
			//处理活动讲座
			is_2=new FileInputStream(filePath_2);
			parser=new HtmlParser();
			parser.parse(is_2);
			parser.InitTagLocation();//初始化每个tag的位置
			tag=parser.findTagByLocation("0.0.1.5.0");
			list_tr=tag.children;//tr 标签的list
			beans=trToBean_2(list_tr);//根据每个tr产生一个bean
			for(LectureBean bean:beans){ //将数据库中不存在的项目 存入数据库
				if(dao.searchByTopic(bean.getTopic())==null){
					/*这儿是新项目，可以弹出通知*/
					dao.addLecture(bean);
				}else{
					break;
				}
			}
			parser=null;
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
