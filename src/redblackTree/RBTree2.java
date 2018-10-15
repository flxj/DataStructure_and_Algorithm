package redblackTree;

//2018-4-11
public class RBTree2<Key extends Comparable<Key>,Value> {
	private static final boolean RED=true;
	private static final boolean BLACK=false;
	
    private Node header;
    private int node_count;
	
	class Node{
		private Key key;
		private Value val;
		private Node left,right,parent;
		private boolean color;
		
		Node(Key key,Value val,boolean color){
			this.key=key;
			this.val=val;
			this.color=color;
		}
		public Node getMin(Node x){
			if(x==null) return null;
			while(x.left!=null){
				x=x.left;
			}
			return x;
		}
		public Node getMax(Node x){
			if(x==null) return null;
			while(x.right!=null){
				x=x.right;
			}
			return x;
		}
	}
	RBTree2(){
		header=new Node(null,null,RED);
		header.parent=null;
		header.left=header;
		header.right=header;
		node_count=0;
	}	
		
	//******************************************************
	public boolean isEmpty(){
		return node_count==0;
	}
	public int size(){
		return node_count;
	}
	public boolean insert_equal(Key key,Value val){
		if(insert_equ(key,val)!=null) return true;
		return false;
		
	}
	public boolean insert_unique(Key key,Value val){
		if(insert_uniq(key,val)!=null) return true;
		return false;
		
	}
	public Value get(Key key){
		Node r=search(root(),key);
		if(r!=null) return r.val;
		return null;
	}
	public boolean delete(Key key){
		return remove(key);
	}
	//*****************************************************
	private Node root(){
		return header.parent;	
	}
	private  Node leftmost(){
		return header.left;
	}
	private Node rightmost(){
		return header.right;
	}
	private Node left(Node x){
		return x.left;
	}
	private Node right(Node x){
		return x.right;
	}
	private Node parent(Node x){
		return x.parent;
	}
	private Value value(Node x){
		return x.val;
	}
	private Key key(Node x){
		return x.key;
	}
	private boolean color(Node x){
		return x.color;
	}
	//***********************************************************
	private Node getMin(Node x){
		return x.getMin(x);
	}
	private Node getMax(Node x){
		return x.getMax(x);
	}
	//***************************************************************
	private Node begin(){
		return leftmost();
	}
	private Node end(){
		return rightmost();
	}
	//***************************插入操作******************************************
	private Node insert_equ(Key key,Value val){
		Node y=header;
		Node x=root();
		while(x!=null){
			y=x;
			x=key.compareTo(x.key)<0?left(x):right(x);
		}
		return insert(x,y,key,val);
	}
	private Node insert_uniq(Key key,Value val){
		Node y=header;
		Node x=root();
		boolean comp=true;
		while(x!=null){
			y=x;
			comp=key.compareTo(x.key)<0;
			x=comp?left(x):right(x);
		}
		Node j=y;
		if(comp){       //说明应该插入到y的左侧
			if(j==begin()){ //插入节点的父节点是当前树的最小节点
				return insert(x,y,key,val);
			}else{
				//退回j的中序前驱
				if(j.color==RED && j.parent.parent==j){
					j=j.right;
				}else if(j.left!=null){
					Node p=j.left;
					while(p.right!=null){
						p=p.right;
					}
					j=p;
				}else{
					Node p=j.parent;
					while(j==p.right){
						j=p;
						p=p.parent;
					}
					j=p;
				}
			}
		}
		if(key.compareTo(j.key)!=0){ //与现存Key不冲突
			return insert(x,y,key,val);
		}
		return null;
	}
	//真正的插入程序
	private Node insert(Node _x,Node _y,Key key,Value val){
		Node x=_x;
		Node y=_y;
		Node z;
		if(y==header|| x!=null || key.compareTo(y.key)<0){
			z=new Node(key,val,RED);
			y.left=z;
			if(y==header){
				y.parent=z;
				y.right=z;
			}else if(y==leftmost()){
				header.left=z;
			}
		}else{
			z=new Node(key,val,RED);
			y.right=z;
			if(y==rightmost()){
				header.right=z;
			}
		}
		z.parent=y;
		z.left=null;
		z.right=null;
		rb_tree_rebalance(z,header.parent);
		node_count++;

		return z;
	}
	//根据键值返回找到的节点
	private Node search(Node x,Key key){
		//递归
		if(x==null) return null;
		if(key.compareTo(x.key)<0){
			return search(x.left,key);
		}else if(key.compareTo(x.key)>0){
			return search(x.right,key);
			
		}else{
			return x;
		}
		//迭代
		/*
		while(x!=null){
			int cmp=key.compareTo(x.key);
			if(cmp<0){
				x=x.left;
			}else if(cmp>0){
				x=x.right;
			}else{
				return x;
			}
		}
		return null;
		*/
	}
	//删除节点，先搜索，再删除
	private boolean remove(Key key){
		Node z=search(root(),key);
		if(z==null || rebalance_for_erase(z,header.parent,header.left,header.right)==null) return false;
		return true;
		
	}
	//***************************旋转与平衡操作***********************************************
	private void rb_tree_rebalance(Node x,Node root){
		x.color=RED;
		while(x!=root && x.parent.color==RED){
			if(x.parent==x.parent.parent.left){
				Node y=x.parent.parent.right;
				if(y!=null && y.color==RED){  //“XYr"型，颜色变换
					x.parent.color=BLACK;
					y.color=BLACK;
					x.parent.parent.color=RED;
					x=x.parent.parent;  //颜色变换后不平衡可能向上传递
				}else{      //y不存在或者y为黑色，“XYb"型，需要旋转
					if(x==x.parent.right){
						x=x.parent;
						rotate_left(x,root);
					}
					x.parent.color=BLACK;
					x.parent.parent.color=RED;
					rotate_right(x.parent.parent,root);
				}
				
			}else{   //
				Node y=x.parent.parent.left;
				if(y!=null && y.color==RED){
					x.parent.color=BLACK;
					y.color=BLACK;
					x.parent.parent.color=RED;
					x=x.parent.parent;
				}else{
					if(x==x.parent.left){
						x=x.parent;
						rotate_right(x,root);
					}
					x.parent.color=BLACK;
					x.parent.parent.color=RED;
					rotate_left(x.parent.parent,root);
				}
				
			}
		}
		root.color=BLACK;
	}
	private void rotate_left(Node x,Node root){
		Node y=x.right;
		x.right=y.left;
		if(y.left!=null){
			y.left.parent=x;
		}
		y.parent=x.parent;  //因为y要取代x，因此y要全盘“继承”y与y的父节点的链接关系
		if(x==root){
			root=y;
		}else if(x==x.parent.left){
			x.parent.left=y;
		}else{
			x.parent.right=y;
		}
		y.left=x;
		x.parent=y;		
	}
	private void rotate_right(Node x,Node root){
		Node y=x.left;
		x.left=y.right;
		if(y.right!=null){
			y.right.parent=x;
		}
		y.parent=x.parent;
		if(x==root){
			root=y;
		}else if(x==x.parent.right){
			x.parent.right=y;
		}else{
			x.parent.left=y;
		}
		y.right=x;
		x.parent=y;
	}
	//删除节点并重新调整平衡，
	private Node rebalance_for_erase(Node z,Node root,Node leftmost,Node rightmost){
		Node y=z;
		Node x=null;
		Node x_parent=null;
		//z为待删除节点，找到z的中序后续节点y，用y取代z,x取代y
		if(y.left==null){
			x=y.right;//z的左子树为空（右子树随意，包含了左右子树均为空的情况）
		}else{
			if(y.right==null){
				x=y.left;//z的左子树非空，右子树为空
			}else{
				y=y.right;//z的左右子树均不为空,寻找z中序后续节点
				while(y.left!=null){
					y=y.left;
				}
				x=y.right;//找到的y是z的右子树的最左节点（肯定没有左子节点），另x指其右子节点（x也可能为null）
			}
		}
		//用y取代z
		if(y!=z){ //z左右子树均不为空
			z.left.parent=y;
			y.left=z.left;
			if(y!=z.right){
				x.parent=y.parent;
				if(x!=null) x.parent=y.parent;
				y.parent.left=x;
				y.right=z.right;
				z.right.parent=y;
			}else{
				x_parent=y;
				if(root==z){
					root=y;
				}else if(z.parent.left==z){
					z.parent.left=y;
				}else{
					z.parent.right=y;
					y.parent=z.parent;
					boolean col=y.color;//为了保持原z处的平衡，应该交换y与z的颜色
					y.color=z.color;
					z.color=col;
					y=z;//y现在指向应该物理删除的废弃节点z
				}
			}
		}else{ //z只有一个子树，或z为叶节点
			x_parent=y.parent;
			if(x!=null) x.parent=y.parent;
			//
			if(root==z){
				root=x;
			}else{
				if(z.parent.left==z){
					z.parent.left=x;
				}else{
					z.parent.right=x;
				}
			}
			//
			if(leftmost==z){
				if(z.right==null) leftmost=z.parent;
				else  leftmost=getMin(x);
			}
			if(rightmost==z){
				if(z.left==null) rightmost=z.parent;
				else rightmost=getMax(x);
			}
		}
		//我们将y提上去取代z，如果y原来是红节点，那么不会引发失衡；
		//若y原来为黑节点，那么提走y后，会在原来经过y的路径上造成黑节点减少一,这就需要调整了
		if(y.color!=RED){
			while(x!=root && (x==null || x.color==BLACK)){
				if(x==x_parent.left){
					Node w=x_parent.right;
					if(w.color==RED){
						w.color=BLACK;
						x_parent.color=RED;
						rotate_left(x_parent,root);
						w=x.parent.right;
					}
					if((w.left==null || w.left.color==BLACK) && (w.right==null || w.right.color==BLACK)){
						w.color=RED;
						x=x_parent;
						x_parent=x_parent.parent;
					}else{
						if(w.right==null || w.right.color==BLACK){
							if(w.left!=null) w.left.color=BLACK;
							w.color=RED;
							rotate_right(w,root);
							w=x_parent.right;
						}
						x.color=x_parent.color;
						x_parent.color=BLACK;
						if(w.right!=null) w.right.color=BLACK;
						rotate_left(x_parent,root);
						break;
					}
				}else{
					Node w=x_parent.left;
					if(w.color==RED){
						w.color=BLACK;
						x_parent.color=RED;
						rotate_right(x_parent,root);
						w=x_parent.left;
					}
					if((w.right==null || w.right.color==BLACK) && (w.left==null || w.left.color==BLACK)){
						w.color=RED;
						x=x_parent;
						x_parent=x_parent.parent;
					}else{
						if(w.right==null || w.right.color==BLACK){
							if(w.right!=null) w.right.color=BLACK;
							w.color=RED;
							rotate_left(w,root);
							w=x_parent.left;
						}
						w.color=x_parent.color;
						x_parent.color=BLACK;
						if(w.left!=null) w.left.color=BLACK;
						rotate_right(x_parent,root);
						break;
					}
				}
				if(x!=null) x.color=BLACK;
			}
			//if(x!=null) x.color=BLACK;
		}
		return y;
	}
	//*****************************************************************************
	
}
