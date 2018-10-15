package lazyBalanceTrees;
import java.util.ArrayList;

public class ScapegoatTree2<K extends Comparable<K>,V> {
	private static final double threshold=0.75;
	
	private class Node{
		K key;
		V val;
		Node left,right;
		int realsize,size;
		boolean del;
		Node(K key,V val){
			this.key=key;
			this.val=val;
			this.left=this.right=null;
			this.realsize=this.size=1;
			this.del=false;
		}
		public void pushup(){
			this.realsize=left.realsize+right.realsize+(!del? 1:0);
			this.size=left.size+right.size+1;	
		}
		public boolean isbad(){
			return (left.size>this.size*threshold)|(right.size>this.size*threshold);
		}
	}
	
	private Node root;
	//
	public ScapegoatTree2(){
		root=null;
	}
	public ScapegoatTree2(K key,V val){
		root=new Node(key,val);
	}
	//
	public void insert(K key,V val){
		Node res=insert(root,key,val);
		if(res!=null) rebuildTree(res);
	}
	public int rank(K key){
		Node x=root;
		int ans=1;
		while(x!=null){
			if(key.compareTo(x.key)<=0){
				x=x.left;
			}else{
				ans+=x.left.realsize+(!x.del?1:0);
				x=x.right;
			}
		}
		return ans;
	}
	public void remove(K key){
		erase(root,rank(key));
		if(root.realsize<root.size*threshold) rebuildTree(root);
	}
	public void removeKth(int k){
		erase(root,k);
		if(root.realsize<root.size*threshold) rebuildTree(root);
	}
	//
	private void sortedNodes(Node x,ArrayList<Node> a){
		if(x!=null){
			if(x.left!=null){sortedNodes(x.left,a);}
			if(!x.del) a.add(x);
			if(x.right!=null){sortedNodes(x.right,a);}
		}
		else{ return;}
	}
	private Node buildTreeFromNodesArray(ArrayList<Node> a,int l,int r){
		if(r>=l) return null;
		int mid=(l+r)>>1;
		Node x=a.get(mid);
		x.left=buildTreeFromNodesArray(a,l,mid);
		x.right=buildTreeFromNodesArray(a,mid+1,r);
		x.pushup();
		return x;
	}
	private void rebuildTree(Node x){
		ArrayList<Node> a=new ArrayList<Node>();
		sortedNodes(x,a);
		x=buildTreeFromNodesArray(a,0,a.size());
	}
	private Node insert(Node x,K key,V val){
		if(x==null) {
			x=new Node(key,val);
			return null;
		}else{
			x.realsize+=1;
			x.size+=1;
			Node res=null;
			if(key.compareTo(x.key)<0){
				res=insert(x.left,key,val);
			}else if(key.compareTo(x.key)>0){
				res=insert(x.right,key,val);
			}else{
				x.del=false;
				x.val=val;
				x.size-=1;
			}
			if(x.isbad()) res=x;
			return res;
		}
	}
	private void erase(Node x,int k){//删除第k大节点
		x.realsize-=1;
		int offset=x.left.realsize+(!x.del?1:0);
		if(!x.del && offset==k){
			x.del=true;
		}else{
			if(k<=offset) erase(x.left,k);
			else erase(x.right,k-offset);
		}
	}

}
