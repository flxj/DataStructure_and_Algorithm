package strings;

public class Tire {
	private final int SIZE=26;
	private Node root;
	
	private class Node{
		boolean isStr;
		Node[] next;
		Node(){
			this.isStr=false;
			this.next=new Node[SIZE];
		}
	}
	
	Tire(){
		this.root=new Node();
	}
	public void insert(String s){
		Node location=root;
		for(int i=0;i<s.length();i++){
			if(location.next[s.charAt(i)-'a']==null){
				Node tmp=new Node();
				location.next[s.charAt(i)-'a']=tmp;
			}
			location=location.next[s.charAt(i)-'a'];
		}
		location.isStr=true;
		
	}
	public boolean search(String s){
		Node location=root;
		int i=0;
		while(i<s.length() && location!=null){
			location=location.next[s.charAt(i)-'a'];
			i++;
		}
		return (location!=null && location.isStr==true);
		
	}
	
		
	
	

}
