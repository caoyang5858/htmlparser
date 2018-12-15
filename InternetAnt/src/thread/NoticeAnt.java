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

import db.bean.NoticeBean;
import db.dao.NoticeDAO;

public class NoticeAnt implements Runnable{
    private String url="http://tuanwei.scu.edu.cn/tw/article_list.php?pagename=&type=inform&page=1";
    private String url_2="http://xsc.scu.edu.cn/news_list.aspx?leixin=43";
    public NoticeAnt(String url){
    	this.url=url;
    }
    public NoticeAnt(){
    	
    }
	public void run() {
		DownloadFile down = new DownloadFile();
		HtmlParser parser= new HtmlParser();
		String path_notice = down.downLoadFile(url);
		ArrayList<String> urls=new ArrayList<String>();
		try {
			InputStream is = new FileInputStream(path_notice);
			parser.parse(is);
			parser.InitTagLocation();
			Tag tag_table=parser.findTagByLocation("0.0.1.0");
			ArrayList<Tag> lista=parser.findTagsByNameFromTag("a", tag_table);
			for(int i=0;i<15;i++){//取前15个连接，因为页面有效连接为前15
				Tag tag_a=lista.get(i);
				String str=tag_a.tagProp;
				str=str.substring(str.indexOf("href=\"")+6);
				String URL="http://tuanwei.scu.edu.cn/tw/"+str.substring(0, str.indexOf("\""));
				urls.add(URL);
			}
			
			down.setFilePath("D:\\notice\\");
			NoticeDAO dao=new NoticeDAO();
			NoticeBean bean = new NoticeBean();
			for(String URL:urls){
				String filepath=down.downLoadFile(URL);
				parser=new HtmlParser();
			    is=new FileInputStream(filepath);
			    parser.parse(is);
			    parser.InitTagLocation();
			    String title=parser.findTagByLocation("0.0.1.4.0.0.0.0").tagProp;
			    String time=parser.findTagByLocation("0.0.1.4.0.1.0.0").tagProp;
			    String about=HtmlParser.getTextOfTextTag(parser.findTagByLocation("0.0.1.4.0.4.0.0"));
			    about=about.replaceAll("&nbsp;", "");
			    about=about.substring(0, 120);
			    bean.setAbout(about);
			    bean.setDetail(filepath);
			    bean.setLink(URL);
			    bean.setTime(time);
			    bean.setTitle(title);
			    bean.setType("tuanwei");
			    if(dao.searchNoticeByTitle(title)==null){
			    	/*新的公告，通知用户*/
			    	dao.addNotice(bean);
			    }
			    else{
			    	break;//说明接下来的项目在数据库也有了
			    }
			}
			
			
			//处理学工部公告
			parser=new HtmlParser();
			down.setFilePath("D:\\");
			path_notice=down.downLoadFile(url_2);
			is = new FileInputStream(path_notice);
			parser.setEncoding("utf-8");
			parser.parse(is);
			parser.InitTagLocation();
			Tag tagtable=parser.findTagByLocation("0.0.1.0.2.0.0.1.0.3.0.0.0.0.0.1.1.0.0.0.0.2.0");
			lista=parser.findTagsByNameFromTag("a", tagtable);
			urls.clear();
		    for(int i=0;i<20;i++){
				   Tag tag_a=lista.get(i);
				   String str=tag_a.tagProp;
				   str=str.substring(6);
				   String Url="http://xsc.scu.edu.cn/"+str.substring(0, str.indexOf('\''));
				   urls.add(Url);
			}
			down.setFilePath("D:\\notice\\");
		    for(String URL:urls){
		    	String filePath=down.downLoadFile(URL);
		    	is=new FileInputStream(filePath);
		    	parser=new HtmlParser();
		    	parser.setEncoding("utf-8");
		    	parser.parse(is);
		    	parser.InitTagLocation();
		    	String strprop=parser.findTagByLocation("0.0.1.0.0.0.0.1.0.3.0.0.0.0.0.3.0.0.0").tagProp;
		    	String time=strprop.substring(strprop.length()-10);//时间
		    	strprop=parser.findTagByLocation("0.0.1.0.0.0.0.1.0.3.0.0.0.0.0.1.0.0.0.1.0.0.0.0.0").tagProp;
		    	String title=strprop;
		    	Tag tag_div=parser.findTagByLocation("0.0.1.0.0.0.0.1.0.3.0.0.0.0.0.5.0.0");
		    	String about=HtmlParser.getTextOfTextTag(tag_div);
		    	about=about.replaceAll("&nbsp;", "");
		    	if(about.length()>121){
		    	 about=about.substring(0, 120);
		    	}
		    	bean.setAbout(about);
		    	bean.setDetail(filePath);
		    	bean.setLink(URL);
		    	bean.setTime(time);
		    	bean.setTitle(title);
		    	bean.setType("xuegong");
		    	if(dao.searchNoticeByTitle(title)==null){
		    		/*新的学工部通知*/
		    		dao.addNotice(bean);
		    	}
		    	else{
		    		break;//说明后面的URL对应的通知已在数据库
		    	}
		    }
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
