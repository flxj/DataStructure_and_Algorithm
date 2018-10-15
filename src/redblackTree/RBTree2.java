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
	//***************************�������******************************************
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
		if(comp){       //˵��Ӧ�ò��뵽y�����
			if(j==begin()){ //����ڵ�ĸ��ڵ��ǵ�ǰ������С�ڵ�
				return insert(x,y,key,val);
			}else{
				//�˻�j������ǰ��
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
		if(key.compareTo(j.key)!=0){ //���ִ�Key����ͻ
			return insert(x,y,key,val);
		}
		return null;
	}
	//�����Ĳ������
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
	//���ݼ�ֵ�����ҵ��Ľڵ�
	private Node search(Node x,Key key){
		//�ݹ�
		if(x==null) return null;
		if(key.compareTo(x.key)<0){
			return search(x.left,key);
		}else if(key.compareTo(x.key)>0){
			return search(x.right,key);
			
		}else{
			return x;
		}
		//����
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
	//ɾ���ڵ㣬����������ɾ��
	private boolean remove(Key key){
		Node z=search(root(),key);
		if(z==null || rebalance_for_erase(z,header.parent,header.left,header.right)==null) return false;
		return true;
		
	}
	//***************************��ת��ƽ�����***********************************************
	private void rb_tree_rebalance(Node x,Node root){
		x.color=RED;
		while(x!=root && x.parent.color==RED){
			if(x.parent==x.parent.parent.left){
				Node y=x.parent.parent.right;
				if(y!=null && y.color==RED){  //��XYr"�ͣ���ɫ�任
					x.parent.color=BLACK;
					y.color=BLACK;
					x.parent.parent.color=RED;
					x=x.parent.parent;  //��ɫ�任��ƽ��������ϴ���
				}else{      //y�����ڻ���yΪ��ɫ����XYb"�ͣ���Ҫ��ת
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
		y.parent=x.parent;  //��ΪyҪȡ��x�����yҪȫ�̡��̳С�y��y�ĸ��ڵ�����ӹ�ϵ
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
	//ɾ���ڵ㲢���µ���ƽ�⣬
	private Node rebalance_for_erase(Node z,Node root,Node leftmost,Node rightmost){
		Node y=z;
		Node x=null;
		Node x_parent=null;
		//zΪ��ɾ���ڵ㣬�ҵ�z����������ڵ�y����yȡ��z,xȡ��y
		if(y.left==null){
			x=y.right;//z��������Ϊ�գ����������⣬����������������Ϊ�յ������
		}else{
			if(y.right==null){
				x=y.left;//z���������ǿգ�������Ϊ��
			}else{
				y=y.right;//z��������������Ϊ��,Ѱ��z��������ڵ�
				while(y.left!=null){
					y=y.left;
				}
				x=y.right;//�ҵ���y��z��������������ڵ㣨�϶�û�����ӽڵ㣩����xָ�����ӽڵ㣨xҲ����Ϊnull��
			}
		}
		//��yȡ��z
		if(y!=z){ //z������������Ϊ��
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
					boolean col=y.color;//Ϊ�˱���ԭz����ƽ�⣬Ӧ�ý���y��z����ɫ
					y.color=z.color;
					z.color=col;
					y=z;//y����ָ��Ӧ������ɾ���ķ����ڵ�z
				}
			}
		}else{ //zֻ��һ����������zΪҶ�ڵ�
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
		//���ǽ�y����ȥȡ��z�����yԭ���Ǻ�ڵ㣬��ô��������ʧ�⣻
		//��yԭ��Ϊ�ڽڵ㣬��ô����y�󣬻���ԭ������y��·������ɺڽڵ����һ,�����Ҫ������
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
