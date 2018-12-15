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
	 private int index=0;//��¼ ���ڶ����ַ������
	 private String encoding="gb2312"; 
	 private int ch;
	 
	 public  HtmlParser(){
		 for(int t=0;t<30;t++){
			 map.put(t, new ArrayList<Tag>());//��ʼ��10��
		 }
		 
	 }
	 
	 public void setEncoding(String encoding){
		 this.encoding=encoding;
	 }
	 
	 /**
	  * ���tag�� ��һ������� ����sonName�Ķ���  �ĸ���
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
	  * ��ȡtag�Ķ���������textTag���ı�(tagProp)
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
	  * ����ҳ�е�unicode��ʾ������ "&#27901;&#19996;&#24605;&#24819;&#21644;"ת��Ϊ����;
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
	  * �γ�html��
	  * @param is
	  * @throws IOException
	  */
	 public  void getTag(InputStream is) throws IOException {
		 
		 while((ch==' '|| ch=='\r' || ch=='\n' || ch=='\t')&&ch!=-1){
			 ch=read(is);
		 }//ȥ�ո�
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
                     if(ch=='>'){//Tag��ջ,
                    	 
                    	 int i=stack.searchTagByName(tagName);
                    	 if(i!=-1){//��ƥ���tag
                    		 if(stack.isTop(i)){//���tag��ջ��
                    			 Tag tag=stack.pop();
                    			 ArrayList<Tag> list=map.get(i+1);//��HashMap[iLevel+1]�е�����Ԫ��ȡ����Ϊ��Tag���ӽڵ�
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
                    			
                    			
                    			 
                    			//��HashMap[i+1~1+top]�е�����Ԫ��ȡ����Ϊ��Tag���ӽڵ㡣
                    			 for(int tmp=i+1;tmp<=top+1;tmp++){
                    				 ArrayList<Tag> list=map.get(tmp);
                    				 for(int t=0;t<list.size();t++){

                    					 childrens.add(list.get(t));
                    					 
                    				 }
                    				 list.clear();//���map[i+1+����Tag��]
                    			 }
                    			 
                    			 for(int t=stack.topIndex();t>i;t--){//����ƥ���tagջ֮�ϵ�tags������Ϊtag�Ķ���
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
				 else if(ch=='!'){//�������ע��
					 while(ch!='>'){
						 ch=read(is);
						 
					 }
				 }
				 else if((ch<='z'&&ch>='a') || (ch<='Z'&&ch>='A')){
					 int beginpos=index;//tag ��ʼ��λ��
					 String tagName=getToken(is);
					 String tagProp=getProp(is);
					
                     if(ch=='>'){
                    
                    	 Tag tag=new Tag();
                    	 tag.setBeginPos(beginpos);
                    	 tag.setTagName(tagName);
                    	 tag.setTagProp(tagProp);
                    	 stack.push(tag); //����ǩ��ջ
                     }
                     
				 }
				 else{//ȥ�ո�
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
			 text=new String(bytes,encoding);//����ҳ��text���빹��string,��ֹ�е�ҳ��Ϊgb23112,�е�Ϊutf-8
			 tag.setBeginPos(beginpos);
			 tag.setTagName("textTag");
			 tag.setTagProp(text);
			 //stack.push(tag); //��TextTag��ջ
			 int i=stack.topIndex()+1;//��TextTag ���浽map[stackTop+1]
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
	 private void printout(Tag tag) throws IOException{//���html����غ���
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
	  * �γ�Html������
	  * @param is ������
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
	  * ��ʼ��ÿ��Tag��λ��
	  */
	 public void InitTagLocation(){ //  0.1 ��ȡ ���� map[0].get[1]  
		 InitS(s);                                                 //  1.1.2       map[0].get[1]��>children.get[2]  
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
			 level++;//������μ�1
			 searchTag(list.get(i).children);
			 s[level]=-1;//�����һ��ı�ʶ
			 level--;//�ص�����
		 }
	 }
	 
	 
	 private ArrayList<Tag> findList=null;
	 private ArrayList<Tag> findList_2=null;
	 /**
	  * ����tag���֣��ҵ���ͬ����tag���б���tag�Ķ��ӿ�ʼ��
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
	 public void find_2(ArrayList<Tag> list,String tagName){//������ȱ���
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
	  * ����tag���֣��ҵ���ͬ����tag���б���html������ʼ��
	  * @param tagName
	  * @return
	  */
	 public ArrayList<Tag> findTagsByName(String tagName){ //  0.1 ��ȡ ���� map[0].get[1]
		                                               //  1.1.2       map[0].get[1]��>children.get[2]
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
	 public void find(ArrayList<Tag> list,String tagName){//������ȱ���
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
	  * ����λ���ҵ�Tag
	  * @param location
	  * @return
	  */
	 public Tag findTagByLocation(String location){
//		 int len=location.length();
//		 ArrayList<Tag> list=map.get(location.charAt(0)-'0');
//		 Tag tag=null;
//		 for(int i=2;i<len;i++){
//			 if(location.charAt(i)!='.'){
//			  tag = list.get(location.charAt(i)-'0'); /*������ 0.12.10.3��������� ,��������9����������쳣*/
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


