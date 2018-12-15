package parse;



import java.io.FileInputStream;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import java.io.OutputStream;

import java.util.ArrayList;
import java.util.HashMap;




public class HtmlParser {
	 private MyStack stack = new MyStack();
	 private HashMap<Integer,ArrayList<Tag>> map = new HashMap<Integer,ArrayList<Tag>>();
	 private int index=0;//记录 正在读的字符的序号
	 private String encoding="gb2312"; 
	 private int ch;
	 
	 public  HtmlParser(){
		 for(int t=0;t<30;t++){
			 map.put(t, new ArrayList<Tag>());//初始化10组
		 }
		 
	 }
	 
	 public void setEncoding(String encoding){
		 this.encoding=encoding;
	 }
	 
	 /**
	  * 获得tag的 第一层儿子中 名叫sonName的儿子  的个数
	  * @param tag
	  * @return
	  */
	 public int getFirstFloorSonNum(Tag tag,String sonName){
		 int n=0;
		 ArrayList<Tag> sons=tag.children;
		 for(Tag s:sons){
			 if(s.tagName.equals(sonName)){
				 ++n;
			 }
		 }
		 return n;
	 }
	 /**
	  * 获取tag的儿子中所有textTag的文本(tagProp)
	  * @param tag
	  * @return
	  */
	 public static String getTextOfTextTag(Tag tag){
		 String text="";
		 if(tag.tagName.equals("textTag")){
			 return tag.tagProp;
		 }
		 
		 ArrayList<Tag> list=tag.children;
		 if(list!=null){
			 for(int i=0;i<list.size();i++){
				text+=getTextOfTextTag(list.get(i));
			 }
		 }
		 
		 return text;
		 
	 }
	 
	 /**
	  * 将网页中的unicode表示的中文 "&#27901;&#19996;&#24605;&#24819;&#21644;"转化为中文;
	  * @param a
	  * @return
	  */
	 private String unicode10ToChi(String a){
		 
		   String str = a.replaceAll("&#",",").replaceAll(";","");
	       String [] s2 = str.split(",");
	       String s1 ="";
	       System.out.println(s2.length);
	       for (int i=1;i<s2.length;i++){
	           int v = Integer.parseInt(s2[i],10);
	           s1=s1+(char)v;
	       }
	       return s1;
	 }
	 
	 private int len(byte[] str){
		 int i=0;
		 while(str[i]!=0){
			 i++;
		 }
		 return i;
	 }
	 private void copy(byte[] des,byte[] src){
		 int i=0;
		 int j=0;
		 while(i<des.length&&j<src.length&&src[j]!=0){
			 des[i++]=src[j++];
		 }
	 }
	 private String getToken(InputStream is) throws IOException{
		 String token=null;
		 byte[] str=new byte[20];
		 int t=0;
		
		 while((ch<='z'&&ch>='a') || (ch<='Z'&&ch>='A')){
			 str[t++]=(byte) ch;
			 ch=(int) read(is);
		 }
	    byte[] bytes=new byte[len(str)];
	    copy(bytes,str);
		token=new String(bytes);
			 
		 
		 return token;
	 }
	 private String getProp(InputStream is) throws IOException{
		 String prop=null;
		 byte[] str=new byte[40000];
		 int t=0;
		 while((ch==' '|| ch=='\r' || ch=='\n' || ch=='\t')){
			 ch=read(is);
		 }
		 while(ch!='>'&&ch!=-1){
			 str[t++]=(byte) ch;
			 ch=(int) read(is);
		 }
		 if(str[0]!=0){
			    byte[] bytes=new byte[len(str)];
			    copy(bytes,str);
				prop=new String(bytes,encoding);
		 }
		 return prop;
	 }
	 private int read(InputStream is) throws IOException{
		 ++index;
		 return is.read();
	 }
	 
	 /**
	  * 形成html树
	  * @param is
	  * @throws IOException
	  */
	 public  void getTag(InputStream is) throws IOException {
		 
		 while((ch==' '|| ch=='\r' || ch=='\n' || ch=='\t')&&ch!=-1){
			 ch=read(is);
		 }//去空格
		 if(ch=='<'){
			 ch=read(is);
			 while(ch!='>'&&ch!=-1){
				 if(ch=='/'){
					 ch=read(is);
					 while(!(ch<='z'&&ch>='a')&&!(ch<='Z'&&ch>='A')&&ch!=-1){
						 ch=read(is);
					 }
					 String tagName=getToken(is);
					 while(ch!='>'&&ch!=-1){
						 ch=read(is);
					 }
                     if(ch=='>'){//Tag出栈,
                    	 
                    	 int i=stack.searchTagByName(tagName);
                    	 if(i!=-1){//有匹配的tag
                    		 if(stack.isTop(i)){//如果tag在栈顶
                    			 Tag tag=stack.pop();
                    			 ArrayList<Tag> list=map.get(i+1);//将HashMap[iLevel+1]中的所有元素取出作为此Tag的子节点
                    			 if(!list.isEmpty()){
                    				 ArrayList<Tag> childrens = new ArrayList<Tag>();
                    				 for(int t=0;t<list.size();t++){
                    					 childrens.add(list.get(t));
                    				 }
                    				 list.clear();
                    				 tag.setChildren(childrens);
                    			 }
                    			 map.get(i).add(tag);
                    		 }
                    		 else{
                    			 ArrayList<Tag> childrens=new ArrayList<Tag>();
                    			 int top=stack.topIndex();
                    			
                    			
                    			 
                    			//将HashMap[i+1~1+top]中的所有元素取出作为此Tag的子节点。
                    			 for(int tmp=i+1;tmp<=top+1;tmp++){
                    				 ArrayList<Tag> list=map.get(tmp);
                    				 for(int t=0;t<list.size();t++){

                    					 childrens.add(list.get(t));
                    					 
                    				 }
                    				 list.clear();//清空map[i+1+独立Tag数]
                    			 }
                    			 
                    			 for(int t=stack.topIndex();t>i;t--){//将在匹配的tag栈之上的tags弹出作为tag的儿子
                    				 Tag child=stack.pop();
                    				 childrens.add(child);
                    				 
                    			 }
                    			 
                    			 Tag tag = stack.pop();
                    			 
                    			 tag.setChildren(childrens);
                    			 map.get(i).add(tag);
                    			
                    		 }
                    	 }
                     }
				 }
				 else if(ch=='!'){//如果遇到注释
					 while(ch!='>'){
						 ch=read(is);
						 
					 }
				 }
				 else if((ch<='z'&&ch>='a') || (ch<='Z'&&ch>='A')){
					 int beginpos=index;//tag 开始的位置
					 String tagName=getToken(is);
					 String tagProp=getProp(is);
					
                     if(ch=='>'){
                    
                    	 Tag tag=new Tag();
                    	 tag.setBeginPos(beginpos);
                    	 tag.setTagName(tagName);
                    	 tag.setTagProp(tagProp);
                    	 stack.push(tag); //将标签入栈
                     }
                     
				 }
				 else{//去空格
				   ch=read(is);
				 }
			 }
			 if(ch!=-1){
				 ch=read(is);
			 }
		 }
		 else{
			 String text=null;
			 byte[] str=new byte[6000];
			 int t=0;
			 int beginpos=index;
			 Tag tag=new Tag();
			 while(ch!='<'&&ch!=-1){
				 str[t++]=(byte) ch;
				 ch=read(is);
			 }
			 byte[] bytes=new byte[len(str)];
			 copy(bytes,str);
			 text=new String(bytes,encoding);//根据页面text编码构造string,防止有的页面为gb23112,有的为utf-8
			 tag.setBeginPos(beginpos);
			 tag.setTagName("textTag");
			 tag.setTagProp(text);
			 //stack.push(tag); //将TextTag入栈
			 int i=stack.topIndex()+1;//将TextTag 保存到map[stackTop+1]
			 map.get(i).add(tag);
		 }
		
	 }
    
	 
	 
	 public void skipSpace(InputStream is) throws IOException{
		 ch=read(is);
		 while((ch==' '|| ch=='\r' || ch=='\n' || ch=='\t')&&ch!=-1){
			 ch=read(is);
		 }
	 }
	 
	 public boolean notEnd(){
		 if(ch!=-1){
			 return true;
		 }
		 return false;
	 }
	 
	 private static int printTab=0;
	 private OutputStream os =null;
	 private void printout(Tag tag) throws IOException{//输出html树相关函数
         if(os==null){
        	os = new FileOutputStream("D:\\parse.txt");
         }
         for(int i=0;i<printTab;i++){
        	 os.write(' ');
         }
         if(tag.tagName!=null)
		   os.write(tag.tagName.getBytes());
         os.write(" ".getBytes());
         if(tag.tagProp!=null)
		   os.write(tag.tagProp.getBytes());
         os.write(" loc:".getBytes());
         if(tag.location!=null)
           os.write(tag.location.getBytes());
        
        os.write("\r\n".getBytes());
	 }
	 public void print(ArrayList<Tag> list) throws IOException{
		 for(int i=0;i<list.size();i++){
			 if(list.get(i)!=null){
			   printout(list.get(i));
			 }
			 if(list.get(i).children!=null){
				 printTab+=5;
				 print(list.get(i).children);
				 printTab-=5;
			 }
		 }
		
	 }
	 public void show() throws IOException{
		 for(int t=0;t<20;t++){
			 if(!map.get(t).isEmpty()){
				 print(map.get(t));
			 }
		 }
	 }
	 /**
	  * 形成Html分析树
	  * @param is 输入流
	  * @throws IOException
	  */
	 public void parse(InputStream is) throws IOException{
		 if(is!=null){
			 this.skipSpace(is);
			 while(this.notEnd()){
				 this.getTag(is);
			 }
		 }
	 }
	 
	 
	 
	 
	 private void InitS(int s[]){
		 int i=0;
		 while(s.length>i){
			 s[i++]=-1;
		 }
	 }
	 private String myStringLocation(int s[]){
		 String loc="";
		 for(int i=0;i<s.length&&s[i]!=-1;i++){
			 if(s[i+1]!=-1)
			    loc+=s[i]+".";
			 else
				loc+=s[i];
		 }
		 
		 return loc;
	 }
	 private int s[]=new int[40];
	 private int level=0;
	 /**
	  * 初始化每个Tag的位置
	  */
	 public void InitTagLocation(){ //  0.1 索取 代表 map[0].get[1]  
		 InitS(s);                                                 //  1.1.2       map[0].get[1]—>children.get[2]  
		 for(int t=0;t<20;t++){
			 if(!map.get(t).isEmpty()){
				 s[level]=t;
				 level++;
				 searchTag(map.get(t));
				 s[level]=-1;
				 level--;
			 }
		 }
		
	 }
	 private void searchTag(ArrayList<Tag> list){
		 if(list==null||list.size()==0){
			 return;
		 }
		 for(int i=0;i<list.size();i++){
			 s[level]=i;
			 list.get(i).setLocation(myStringLocation(s));
			 level++;//遍历层次加1
			 searchTag(list.get(i).children);
			 s[level]=-1;//清空上一层的标识
			 level--;//回到本层
		 }
	 }
	 
	 
	 private ArrayList<Tag> findList=null;
	 private ArrayList<Tag> findList_2=null;
	 /**
	  * 根据tag名字，找到相同名字tag的列表，从tag的儿子开始找
	  * @param tag
	  * @return
	  */
	 public ArrayList<Tag> findTagsByNameFromTag(String tagName,Tag tag){
		 if(findList_2==null){
			 findList_2=new ArrayList<Tag>();
		 }
		 find_2(tag.children,tagName);
		 return findList_2;
	 }
	 public void find_2(ArrayList<Tag> list,String tagName){//深度优先遍历
		 if(list==null||list.size()==0){
			 return;
		 }
		 for(int i=0;i<list.size();i++){
			 
			 if(list.get(i).getTagName().equals(tagName)){
				 findList_2.add(list.get(i));
			 }
			 find_2(list.get(i).children,tagName);
			 
		 }
	 }
	 /**
	  * 根据tag名字，找到相同名字tag的列表，从html树根开始找
	  * @param tagName
	  * @return
	  */
	 public ArrayList<Tag> findTagsByName(String tagName){ //  0.1 索取 代表 map[0].get[1]
		                                               //  1.1.2       map[0].get[1]—>children.get[2]
		 if(findList==null){
			 findList=new ArrayList<Tag>();
		 }
		 for(int t=0;t<20;t++){
			 if(!map.get(t).isEmpty()){
				 find(map.get(t),tagName);
			 }
		 }
		 return findList;
	 }
	 public void find(ArrayList<Tag> list,String tagName){//深度优先遍历
		 if(list==null||list.size()==0){
			 return;
		 }
		 for(int i=0;i<list.size();i++){
			 
			 if(list.get(i).getTagName().equals(tagName)){
				 findList.add(list.get(i));
			 }
			 find(list.get(i).children,tagName);
			 
		 }
	 }
	 
	 /**
	  * 根据位置找到Tag
	  * @param location
	  * @return
	  */
	 public Tag findTagByLocation(String location){
//		 int len=location.length();
//		 ArrayList<Tag> list=map.get(location.charAt(0)-'0');
//		 Tag tag=null;
//		 for(int i=2;i<len;i++){
//			 if(location.charAt(i)!='.'){
//			  tag = list.get(location.charAt(i)-'0'); /*当出现 0.12.10.3这样的情况 ,索引大于9的情况会有异常*/
//			 }
//			 else{// location[i]=='.'
//				 list=tag.children;    
//			 }
//		 }
//		 return tag;
		 
		 
		 int[] loc=myStringToInt(location);
		
		 ArrayList<Tag> list=map.get(loc[0]);
		 Tag tag=null;
		 for(int i=1;i<loc.length;i++){
			 tag=list.get(loc[i]);
			 list=tag.children;
		 }
		 return tag;
		
	 }
	 private int floors(String loc){
		 int i=0;
		 int len=loc.length();
		 for( int t=0;t<len;t++){
			 if(loc.charAt(t)=='.'){
				 i++;
			 }
		 }
		 return i+1;
	 }
	 private int[] myStringToInt(String location){
		 int len=floors(location);
		 int[] re=new int[len];
		 int i=0;
		for(int j=0;j<location.length();j++){ //0.0.1.10.0

			int sum=0;
			sum+=(location.charAt(j)-'0');
			j++;
			if(j<location.length()){
				while(location.charAt(j)!='.'){
					sum*=10;
					sum+=(location.charAt(j)-'0');
					j++;
				}
			}
			
			re[i++]=sum;
		}
		 
		 return re;
	 }
	 
	 public static void main(String args[]) throws IOException{
        InputStream is = new FileInputStream("D:\\a.htm");
        HtmlParser parser = new HtmlParser();
	      parser.setEncoding("utf-8");
	      parser.parse(is);
	      parser.InitTagLocation();
	      parser.show();
	      
	    
	     
	   //  System.out.println(HtmlParser.getTextOfTextTag(parser.findTagByLocation("0.0.1.5.0.2.3.1.0.0.0")));
//	      int[] s=new int[20];
//	      parser.InitS(s);
//          s[0]=2;
//          s[1]=3;
//          
//	      System.out.println(parser.myStringLocation(s));

	 }
}


