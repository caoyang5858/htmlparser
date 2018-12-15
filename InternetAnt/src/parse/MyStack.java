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
	 * 返回位于栈顶的元素，但是并不在堆栈中删除它。
	 * @return
	 */
	public Tag peek(){
		return list.get(list.size()-1);
	}

	/**
	 * 返回位于栈顶的元素，并在进程中删除它。
	 * @return
	 */
	public Tag pop(){
		int topIndex = list.size()-1;
		Tag tmp = list.get(topIndex);
		list.remove(topIndex);
		return tmp;
	}

	/**
	 * 将element压入堆栈，同时也返回element
	 * @param element
	 * @return
	 */
	public Tag push(Tag element){
		list.add(element);
		return element;
	}

	/**
	 * 在堆栈中搜索element，如果发现了，则返回它相对于栈底的偏移量。否则，返回-1。
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
	 * 在堆栈中搜索element，如果发现了，则返回它相对于栈底的偏移量。否则，返回-1。
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
     * 相对于栈底的偏移量i对应的元素是否在栈顶
     */
	public boolean isTop(int i){
		return i==(list.size()-1);
	}
	/**
	 * 
	 * @return 栈顶的下标
	 */
	public int topIndex(){
		return list.size()-1;
	}

}