package com;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.ArrayList;

import org.apache.commons.httpclient.NameValuePair;

import db.bean.CourseBean;
import db.dao.CourseDAO;

import parse.HtmlParser;
import parse.Tag;
import thread.CourseAnt;
import thread.EmploymentAnt;
import thread.LectureAnt;
import thread.NoticeAnt;

public class TestTest {
  public static void main(String args[]) throws IOException, SQLException{
	  DownloadFile down = new DownloadFile();
	   String a=down.downLoadFile("http://tuanwei.scu.edu.cn/tw/articleshow.php?type=inform&id=27373");
      System.out.println(a);
      InputStream is=new FileInputStream(a);
	   HtmlParser parser=new HtmlParser();
	   //parser.setEncoding("utf-8");
	   parser.parse(is);
	   parser.InitTagLocation();
	   parser.show();


	   Thread thread_emp=new Thread(new EmploymentAnt());
	   thread_emp.start();
   
	   Thread th=new Thread(new LectureAnt());
	   th.start();
	   
	   Thread th3=new Thread(new NoticeAnt());
	   th3.run();
	   
	   Thread th4=new Thread(new CourseAnt());
	   th4.run();

  }
}
