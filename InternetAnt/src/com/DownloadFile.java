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
	
	//通过url和网页类型获得 文件名
	public String getFileNameByUrl(String url,String contentType){
		//移除http:
		url=url.substring(7);
		// text/html类型
		if(contentType.indexOf("html")!=-1){
			url = url.replaceAll("[\\?/:*<>\"]","_")+".html";
			return url;
		}
		else{
			return url.replaceAll("[\\?/:*<>\"]","_")+"."+
			        contentType.substring(contentType.indexOf("/")+1);
		}
	}

	//通过 请求返回的输入流，将网页保存到本地
	public void saveToLocal(InputStream is,String filePath) throws IOException{
		if(is!=null&&filePath!=null){
			BufferedInputStream bis = new BufferedInputStream(is);
			OutputStream os= new FileOutputStream(filePath);
			BufferedOutputStream bos  = new BufferedOutputStream(os);
			
			byte[] cache = new byte[500];
			int i=0;
			// 这儿 i表示  读取的 字节量,-1为文件结尾
			while((i=bis.read(cache))!=-1){
				bos.write(cache, 0, i);
			}
            
			bis.close();
			bos.flush();
			bos.close();
		}
	}
	
	// 下载 url指向的网页
	public String downLoadFile(String url){
		String filePath=this.getFilePath(); //网页文件存放路径
		File directory=new File(filePath);
		
		if(!directory.exists()){
			directory.mkdir();
		}
		if(httpClientDownFile==null){
			httpClientDownFile = new HttpClient();
		}
		HttpClient httpClient = httpClientDownFile;
		httpClient.getHttpConnectionManager().getParams().setConnectionTimeout(7000);//设置http 连接 超时 5s
		
		GetMethod getMethod = new GetMethod(url);
		//s设置get 方法请求超时 5s
		getMethod.getParams().setParameter(HttpMethodParams.SO_TIMEOUT, 7000);
		//设置请求重试处理
		getMethod.getParams().setParameter(HttpMethodParams.RETRY_HANDLER,new DefaultHttpMethodRetryHandler());
		
		//执行 get 请求
		try {
			int status=httpClient.executeMethod(getMethod); //执行，并返回状态码
			// 请求成功,获得响应内容
			if(status==HttpStatus.SC_OK){
				InputStream is=getMethod.getResponseBodyAsStream();
				//获得文件路径+文件名
				filePath = filePath+getFileNameByUrl(url,getMethod.getResponseHeader("Content-Type").getValue());
				saveToLocal(is,filePath);//保存网页文件
				return filePath;
			}
			//若需 转向
			else if((status==HttpStatus.SC_MOVED_TEMPORARILY) || (status==HttpStatus.SC_MOVED_PERMANENTLY)
					    || (status==HttpStatus.SC_SEE_OTHER)||(status==HttpStatus.SC_TEMPORARY_REDIRECT)){
		         //读取新的url地址
				 Header  header=getMethod.getResponseHeader("location");
				 if(header!=null){
					 String newUrl= header.getValue();
					 if(newUrl!=null&&!newUrl.equals("")){
						  PostMethod redict = new PostMethod(newUrl);
						  int statusRedict=httpClient.executeMethod(redict);
						  if(statusRedict==HttpStatus.SC_OK){
								InputStream is=redict.getResponseBodyAsStream();
								//获得文件路径+文件名
								filePath = filePath+getFileNameByUrl(url,redict.getResponseHeader("Content-Type").getValue());
								saveToLocal(is,filePath);//保存网页文件
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
			System.out.println("请检查你的http地址是否有问题!");
		} catch (IOException e) {
			System.out.println("请检查你的网络链接!");
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
			// 这儿 i表示  读取的 字节量,-1为文件结尾
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
			// 这儿 i表示  读取的 字节量,-1为文件结尾
			while((i=bis.read(cache))!=-1){
				bos.write(cache, 0, i);
			}
			
			bis.close();
			bos.close();
		}
		postMethod.releaseConnection();
	}
	
	//保持连接，会话不会中断，方便需要登录的情况抓取网页
	public String postDownFile(String url,NameValuePair[] postData){
		String filePath=this.getFilePath(); //网页文件存放路径
		File directory=new File(filePath);
		
		if(!directory.exists()){
			directory.mkdir();
		}
		if(httpClientOfme==null){//保持连接，会话不会中断
			httpClientOfme = new HttpClient();
		}
		HttpClient httpClient = httpClientOfme;
		httpClient.getHttpConnectionManager().getParams().setConnectionTimeout(7000);//设置http 连接 超时 5s
		
		PostMethod postMethod=new PostMethod(url);
		//s设置get 方法请求超时 5s
		postMethod.getParams().setParameter(HttpMethodParams.SO_TIMEOUT, 7000);
		//设置请求重试处理
		postMethod.getParams().setParameter(HttpMethodParams.RETRY_HANDLER,new DefaultHttpMethodRetryHandler());
		if(postData!=null){
			  postMethod.addParameters(postData);
			}
		//执行 post 请求
		try {
			
			int status=httpClient.executeMethod(postMethod); //执行，并返回状态码
			// 请求成功,获得响应内容
			if(status==HttpStatus.SC_OK){
				InputStream is=postMethod.getResponseBodyAsStream();
				//获得文件路径+文件名
				filePath = filePath+getFileNameByUrl(url,postMethod.getResponseHeader("Content-Type").getValue());
				saveToLocal(is,filePath);//保存网页文件
				return filePath;
			}
			else{
				System.out.println("method failed:" + postMethod.getStatusLine());
			}
		} catch (HttpException e) {
			System.out.println("请检查你的http地址是否有问题!");
		} catch (IOException e) {
			System.out.println("请检查你的网络链接!");
		}finally{
			postMethod.releaseConnection();
		}
		return null;
	}
}

