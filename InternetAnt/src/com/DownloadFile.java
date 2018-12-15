package com;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.SQLException;

import org.apache.commons.httpclient.DefaultHttpMethodRetryHandler;
import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;





public class DownloadFile
{  
	private String filePath="D:\\";
	private HttpClient httpClientOfme=null;
	private HttpClient httpClientDownFile=null;
	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}
    
	
	public static void main(String[] args) throws IOException, SQLException
	{
	
	  
		
	}
	
	//ͨ��url����ҳ���ͻ�� �ļ���
	public String getFileNameByUrl(String url,String contentType){
		//�Ƴ�http:
		url=url.substring(7);
		// text/html����
		if(contentType.indexOf("html")!=-1){
			url = url.replaceAll("[\\?/:*<>\"]","_")+".html";
			return url;
		}
		else{
			return url.replaceAll("[\\?/:*<>\"]","_")+"."+
			        contentType.substring(contentType.indexOf("/")+1);
		}
	}

	//ͨ�� ���󷵻ص�������������ҳ���浽����
	public void saveToLocal(InputStream is,String filePath) throws IOException{
		if(is!=null&&filePath!=null){
			BufferedInputStream bis = new BufferedInputStream(is);
			OutputStream os= new FileOutputStream(filePath);
			BufferedOutputStream bos  = new BufferedOutputStream(os);
			
			byte[] cache = new byte[500];
			int i=0;
			// ��� i��ʾ  ��ȡ�� �ֽ���,-1Ϊ�ļ���β
			while((i=bis.read(cache))!=-1){
				bos.write(cache, 0, i);
			}
            
			bis.close();
			bos.flush();
			bos.close();
		}
	}
	
	// ���� urlָ�����ҳ
	public String downLoadFile(String url){
		String filePath=this.getFilePath(); //��ҳ�ļ����·��
		File directory=new File(filePath);
		
		if(!directory.exists()){
			directory.mkdir();
		}
		if(httpClientDownFile==null){
			httpClientDownFile = new HttpClient();
		}
		HttpClient httpClient = httpClientDownFile;
		httpClient.getHttpConnectionManager().getParams().setConnectionTimeout(7000);//����http ���� ��ʱ 5s
		
		GetMethod getMethod = new GetMethod(url);
		//s����get ��������ʱ 5s
		getMethod.getParams().setParameter(HttpMethodParams.SO_TIMEOUT, 7000);
		//�����������Դ���
		getMethod.getParams().setParameter(HttpMethodParams.RETRY_HANDLER,new DefaultHttpMethodRetryHandler());
		
		//ִ�� get ����
		try {
			int status=httpClient.executeMethod(getMethod); //ִ�У�������״̬��
			// ����ɹ�,�����Ӧ����
			if(status==HttpStatus.SC_OK){
				InputStream is=getMethod.getResponseBodyAsStream();
				//����ļ�·��+�ļ���
				filePath = filePath+getFileNameByUrl(url,getMethod.getResponseHeader("Content-Type").getValue());
				saveToLocal(is,filePath);//������ҳ�ļ�
				return filePath;
			}
			//���� ת��
			else if((status==HttpStatus.SC_MOVED_TEMPORARILY) || (status==HttpStatus.SC_MOVED_PERMANENTLY)
					    || (status==HttpStatus.SC_SEE_OTHER)||(status==HttpStatus.SC_TEMPORARY_REDIRECT)){
		         //��ȡ�µ�url��ַ
				 Header  header=getMethod.getResponseHeader("location");
				 if(header!=null){
					 String newUrl= header.getValue();
					 if(newUrl!=null&&!newUrl.equals("")){
						  PostMethod redict = new PostMethod(newUrl);
						  int statusRedict=httpClient.executeMethod(redict);
						  if(statusRedict==HttpStatus.SC_OK){
								InputStream is=redict.getResponseBodyAsStream();
								//����ļ�·��+�ļ���
								filePath = filePath+getFileNameByUrl(url,redict.getResponseHeader("Content-Type").getValue());
								saveToLocal(is,filePath);//������ҳ�ļ�
								redict.releaseConnection();
								return filePath;
							}
					 }
				 }
			}
			else{
				System.out.println("method failed:" + getMethod.getStatusLine());
			}
		} catch (HttpException e) {
			System.out.println("�������http��ַ�Ƿ�������!");
		} catch (IOException e) {
			System.out.println("���������������!");
		}finally{
			getMethod.releaseConnection();
		}
		return null;
	}
	
	public String getHtml(String url) throws HttpException, IOException{
		int status=-1;
		String path="";
		HttpClient httpClient = new HttpClient();
		GetMethod getMethod = new GetMethod(url);
		status= httpClient.executeMethod(getMethod);
		if(status==HttpStatus.SC_OK){

			InputStream is=getMethod.getResponseBodyAsStream();
			BufferedInputStream bis = new BufferedInputStream(is);

			path = filePath+getFileNameByUrl(url,getMethod.getResponseHeader("Content-Type").getValue());
			
			OutputStream os= new FileOutputStream(path);
			BufferedOutputStream bos  = new BufferedOutputStream(os);

			byte[] cache = new byte[500];
			int i=0;
			// ��� i��ʾ  ��ȡ�� �ֽ���,-1Ϊ�ļ���β
			while((i=bis.read(cache))!=-1){
				bos.write(cache, 0, i);
			}

			bis.close();
			bos.close();
		}	
		
		getMethod.releaseConnection();
		return path;
	}
	
	public void postHtml(String url,NameValuePair[] postData) throws IOException{
		int status=-1;
		HttpClient httpClient = new HttpClient();
		PostMethod postMethod=new PostMethod(url);
		if(postData!=null){
		  postMethod.addParameters(postData);
		}
		status=httpClient.executeMethod(postMethod);
        System.out.println(status);
		if(status==HttpStatus.SC_OK){
			
			InputStream is=postMethod.getResponseBodyAsStream();
			BufferedInputStream bis = new BufferedInputStream(is);
			
			String fileName=url.substring(url.lastIndexOf('/')+1,url.lastIndexOf('.')+1)+"html";
			OutputStream os= new FileOutputStream(fileName);
			BufferedOutputStream bos  = new BufferedOutputStream(os);
			
			byte[] cache = new byte[500];
			int i=0;
			// ��� i��ʾ  ��ȡ�� �ֽ���,-1Ϊ�ļ���β
			while((i=bis.read(cache))!=-1){
				bos.write(cache, 0, i);
			}
			
			bis.close();
			bos.close();
		}
		postMethod.releaseConnection();
	}
	
	//�������ӣ��Ự�����жϣ�������Ҫ��¼�����ץȡ��ҳ
	public String postDownFile(String url,NameValuePair[] postData){
		String filePath=this.getFilePath(); //��ҳ�ļ����·��
		File directory=new File(filePath);
		
		if(!directory.exists()){
			directory.mkdir();
		}
		if(httpClientOfme==null){//�������ӣ��Ự�����ж�
			httpClientOfme = new HttpClient();
		}
		HttpClient httpClient = httpClientOfme;
		httpClient.getHttpConnectionManager().getParams().setConnectionTimeout(7000);//����http ���� ��ʱ 5s
		
		PostMethod postMethod=new PostMethod(url);
		//s����get ��������ʱ 5s
		postMethod.getParams().setParameter(HttpMethodParams.SO_TIMEOUT, 7000);
		//�����������Դ���
		postMethod.getParams().setParameter(HttpMethodParams.RETRY_HANDLER,new DefaultHttpMethodRetryHandler());
		if(postData!=null){
			  postMethod.addParameters(postData);
			}
		//ִ�� post ����
		try {
			
			int status=httpClient.executeMethod(postMethod); //ִ�У�������״̬��
			// ����ɹ�,�����Ӧ����
			if(status==HttpStatus.SC_OK){
				InputStream is=postMethod.getResponseBodyAsStream();
				//����ļ�·��+�ļ���
				filePath = filePath+getFileNameByUrl(url,postMethod.getResponseHeader("Content-Type").getValue());
				saveToLocal(is,filePath);//������ҳ�ļ�
				return filePath;
			}
			else{
				System.out.println("method failed:" + postMethod.getStatusLine());
			}
		} catch (HttpException e) {
			System.out.println("�������http��ַ�Ƿ�������!");
		} catch (IOException e) {
			System.out.println("���������������!");
		}finally{
			postMethod.releaseConnection();
		}
		return null;
	}
}

