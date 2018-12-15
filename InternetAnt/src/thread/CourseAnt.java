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
    private String URL="http://202.115.47.141/courseSearchAction.do";//ԭʼҳ�棬ͨ������õ�һҳ
    private String url="http://202.115.47.141/courseSearchAction.do?actionType=2&pageNumber=";
    private void doIt(ArrayList<CourseBean> beans,CourseDAO dao,HtmlParser parser) throws SQLException{
    	Tag tag_table=parser.findTagByLocation("0.0.1.4.0.0.0");
		ArrayList<Tag> list_trs=tag_table.children;
		int len=list_trs.size();
		for(int i=1;i<len;i++){//���Ե�һ����������
			Tag tag_tr=list_trs.get(i);
			ArrayList<Tag> list_tds=tag_tr.children;
			CourseBean bean=new CourseBean();
		
			for(int j=0;j<16;j++)//һ��16������
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
		
		for(CourseBean b:beans){//����ҳ��ȡ�Ŀγ���Ϣ�������ݿ�
			dao.addCourse(b);
		}
		beans.clear();//���
    }
	public void run() {
		  DownloadFile down = new DownloadFile();
		  down.setFilePath("D:\\course\\");
		  NameValuePair[] postData=new NameValuePair[19];//����ԭʼ��ҳ,�ṩ���ݵ�����
		  postData[0] = new NameValuePair("pageNumber", "0");
		  postData[1] = new NameValuePair("actionType", "1");
		  postData[2] = new NameValuePair("pageSize","20");//ÿҳ��ʾ20��
		  postData[3] = new NameValuePair("showColumn","kch#�γ̺�");
		  postData[4] = new NameValuePair("showColumn","kkxsjc#����ϵ");
		  postData[5] = new NameValuePair("showColumn","kcm#�γ���");
		  postData[6] = new NameValuePair("showColumn","kxh#�����");
		  postData[7] = new NameValuePair("showColumn","xf#ѧ��");
		  postData[8] = new NameValuePair("showColumn","kslxmc#��������");
		  postData[9] = new NameValuePair("showColumn","skjs#��ʦ");
		  postData[10] = new NameValuePair("showColumn","zcsm#�ܴ�");
		  postData[11] = new NameValuePair("showColumn","skxq#����");
		  postData[12] = new NameValuePair("showColumn","skjc#�ڴ�");
		  postData[13] = new NameValuePair("showColumn","xqm#У��");
		  postData[14] = new NameValuePair("showColumn","jxlm#��ѧ¥");
		  postData[15] = new NameValuePair("showColumn","jasm#����");
		  postData[16] = new NameValuePair("showColumn","bkskrl#������");
		  postData[17] = new NameValuePair("showColumn","xss#ѧ����");
		  postData[18] = new NameValuePair("showColumn","xkxzsm#ѡ������˵��");
		  
		  HtmlParser parser=new HtmlParser();
		  String file_1=down.postDownFile(URL,postData);//���ص�һҳ
		  int sumPage=0;//��ҳ��
		  InputStream is_1=null;
		  try {
			is_1=new FileInputStream(file_1);
			parser.parse(is_1);
			parser.InitTagLocation();
			
			
			String str=HtmlParser.getTextOfTextTag(parser.findTagByLocation("0.0.1.6"));
			String sumPageStr=str.substring(7, 10);
			sumPage=Integer.valueOf(sumPageStr);//�����ҳ��
			
			ArrayList<CourseBean> beans=new ArrayList<CourseBean>();
			CourseDAO dao=new CourseDAO();
		    doIt(beans,dao,parser);//��parser��ȡ���ݲ��������ݿ�,����һҳ��ȡ�Ŀγ���Ϣ�������ݿ�
			
			for(int page=2;page<sumPage;page++){//����1��sumPage-1ҳ��ȡ�Ŀγ���Ϣ�������ݿ�
				String filePath=down.postDownFile(url+page,null);
				is_1=new FileInputStream(filePath);
				parser=new HtmlParser();
				parser.parse(is_1);
				parser.InitTagLocation();
				doIt(beans,dao,parser);
			}
			
			//�������һҳ������û��20������
			String file_end=down.postDownFile(url+sumPage, null);
			is_1=new FileInputStream(file_end);
			parser=new HtmlParser();
			parser.parse(is_1);
			parser.InitTagLocation();
			doIt(beans,dao,parser);//������������� int len=list_trs.size();
			                       //              for(int i=1;i<len;i++){ ������д��,���һҳ���������ҳ����
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}
   
}
