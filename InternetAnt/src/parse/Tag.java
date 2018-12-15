package parse;

import java.util.ArrayList;



public class Tag{
	public String tagName=null;//标签名
	public String tagProp=null;//标签属性
	public int beginPos=0;// 如 <a ..>指的 a在 html文件中的位置 （从1开始）
	public ArrayList<Tag> children=null;// Tag 的儿子队列
	public String location=null;//表示 Tag在Html分析树中的位置  0.2.3代表  map[0].[2].[3]
	
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	public Tag(){
		children=null;
	}
	public String getTagName() {
		return tagName;
	}

	public void setTagName(String tagName) {
		this.tagName = tagName;
	}

	public String getTagProp() {
		return tagProp;
	}

	public void setTagProp(String tagProp) {
		this.tagProp = tagProp;
	}

	public int getBeginPos() {
		return beginPos;
	}

	public void setBeginPos(int beginPos) {
		this.beginPos = beginPos;
	}

	public ArrayList<Tag> getChildren() {
		return children;
	}

	public void setChildren(ArrayList<Tag> children) {
		this.children = children;
	}

	public boolean equals(String name){
		return tagName.equals(name);
	}
}
