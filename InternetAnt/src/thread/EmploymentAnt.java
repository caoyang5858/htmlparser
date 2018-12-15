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
		ArrayList<String> urls=new ArrayList<String>();//�����Ƹ��url
		String filePath_1=down.downLoadFile(url_1);//����html�ĵ�
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
			//����ڶ�ҳ��Ƹ��Ϣ
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
			
			
			down.setFilePath("D:\\employments\\");//������Ƹ��Ϣ����Ŀ¼
			EmploymentDAO dao=new EmploymentDAO();//���ݿ�ӿ�
			EmploymentBean bean=new EmploymentBean();
			for(String url:urls){
				String detail=down.downLoadFile(url);//������Ƹ��Ϣ,�õ��ڱ��ش洢��·��
				//����
				InputStream is=new FileInputStream(detail);
				parser=new HtmlParser();
				parser.parse(is);
				parser.InitTagLocation();
				Tag tag_title=parser.findTagByLocation("1.0.1.2.2.0.1.0.0");
				String title=tag_title.tagProp;//����
				Tag tag_pubtime=parser.findTagByLocation("1.0.1.2.2.0.4.0");
				String publish_time=tag_pubtime.tagProp;
				publish_time=publish_time.substring(3);//����ʱ��
				
				String about=HtmlParser.getTextOfTextTag(parser.findTagByLocation("1.0.1.2.2.2.0")); 
				about=about.replaceAll("&nbsp;", "");
				String str=about.substring(about.indexOf("��Ƹʱ�䣺")+5);
				String employ_time=str.substring(0, 17);//��Ƹʱ��
				String employ_address=str.substring(22);//��Ƹ��ַ
				about=about.substring(0, 150);
				
				
				bean.setLink(url);
				bean.setDetail(detail);
				bean.setTitle(title);
				bean.setPublish_time(publish_time);
				bean.setEmploy_time(employ_time);
				bean.setEmploy_address(employ_address);
				bean.setAbout(about);
				if(dao.searchByTitle(bean.getTitle())==null){
					/*�µ���Ƹ*/
					dao.addEmployment(bean);
				}
				else{
					break;//��ǰ��Ŀ���У����Ժ����url��Ӧ����ĿҲ�����ݿ����
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
