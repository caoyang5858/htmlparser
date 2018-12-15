package parse;

import java.util.ArrayList;



public class Tag{
	public String tagName=null;//��ǩ��
	public String tagProp=null;//��ǩ����
	public int beginPos=0;// �� <a ..>ָ�� a�� html�ļ��е�λ�� ����1��ʼ��
	public ArrayList<Tag> children=null;// Tag �Ķ��Ӷ���
	public String location=null;//��ʾ Tag��Html�������е�λ��  0.2.3����  map[0].[2].[3]
	
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
