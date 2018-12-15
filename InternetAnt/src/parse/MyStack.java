package parse;

import java.util.ArrayList;


public class MyStack {
	
	private ArrayList<Tag> list ;
	public MyStack(){
		list = new ArrayList<Tag>();
	}
	public boolean empty(){
		return list.isEmpty();
	}

	/**
	 * ����λ��ջ����Ԫ�أ����ǲ����ڶ�ջ��ɾ������
	 * @return
	 */
	public Tag peek(){
		return list.get(list.size()-1);
	}

	/**
	 * ����λ��ջ����Ԫ�أ����ڽ�����ɾ������
	 * @return
	 */
	public Tag pop(){
		int topIndex = list.size()-1;
		Tag tmp = list.get(topIndex);
		list.remove(topIndex);
		return tmp;
	}

	/**
	 * ��elementѹ���ջ��ͬʱҲ����element
	 * @param element
	 * @return
	 */
	public Tag push(Tag element){
		list.add(element);
		return element;
	}

	/**
	 * �ڶ�ջ������element����������ˣ��򷵻��������ջ�׵�ƫ���������򣬷���-1��
	 * @param element
	 * @return
	 */
	public int search(Tag element){
		for(int i=0;i<list.size();i++){
			if(element.equals(list.get(i))){
				return i;
			}
		}
		return -1;
	}
	/**
	 * �ڶ�ջ������element����������ˣ��򷵻��������ջ�׵�ƫ���������򣬷���-1��
	 * @param element
	 * @return
	 */
	public int searchTagByName(String name){
		for(int i=list.size()-1;i>=0;i-- ){
			if(list.get(i).tagName.equals(name)){
				return i;
			}
		}
		return -1;
	}
	
    /**
     * �����ջ�׵�ƫ����i��Ӧ��Ԫ���Ƿ���ջ��
     */
	public boolean isTop(int i){
		return i==(list.size()-1);
	}
	/**
	 * 
	 * @return ջ�����±�
	 */
	public int topIndex(){
		return list.size()-1;
	}

}