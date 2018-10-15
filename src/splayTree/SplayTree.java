package splayTree;


import java.util.Comparator;

//2018-5-12
public class SplayTree<Key> {
	private Node root;
	private Node nullNode;
	private Node newNode=null;
	private Node header=new Node();
	private final Comparator<Key> comp;
	
	private class Node{
		Key key;
		Node left,right;
		Node(){
			this.key=null;
			this.left=null;
			this.right=null;
		}
		Node(Key key){
			this.key=key;
			this.left=null;
			this.right=null;
		}
		
	}
	//**************************���캯��*******************************
	public SplayTree(Comparator<Key> C) {
		comp = C;
		nullNode=new Node();
		nullNode.left=nullNode.right=nullNode;
		root=nullNode;
	}
	public SplayTree() {
		comp = new MyComparator();
		nullNode=new Node();
		nullNode.left=nullNode.right=nullNode;
		root=nullNode;
	}
	public SplayTree(Key[] a) {
		comp = new MyComparator();
		for (Key k : a) insert(k);
	}
	public SplayTree(Comparator<Key> C, Key[] a) {
		comp = C;
		for (Key k : a) insert(k);
	}
	//****************************�������******************************
	public void clear(){
		root=nullNode;
	}
	public boolean isEmpty(){
		return root==nullNode;
	}
	//
	public void insert(Key key){
		if(newNode==null){
			newNode=new Node(key);
		}
		if(root==nullNode){
			newNode.left=newNode.right=nullNode;
			root=newNode;
		}else{
			//���²����keyִ����չ����
			root=splay(key,root);
			if(greater(root.key,key)){
				newNode.left=root.left;
				newNode.right=root;
				root.left=nullNode;
				root=newNode;
			}else if(greater(key,root.key)){
				newNode.right=root.right;
				newNode.left=root;
				root.right=nullNode;
				root=newNode;
				
			}else{
				//����չ������ɣ���չ���ĸ��ڵ�keyֵ�������ڵ���ͬ��˵��ԭ�����и�key���ڣ��ظ���
			}
		}
		newNode=null;	//��newNode��Ϊnull��ʹ���´β������ʱnewһ���µ�
	}
	public void remove(Key key){
		Node newTree;
		root=splay(key,root);
		if(!equel(root.key,key)){
			//û�ҵ���ɾ��Key
		}
		if(root==nullNode) newTree=root.left;
		else{
			//Ѱ���������е����ڵ㣬��չ����
			newTree=root.left;
			newTree=splay(key,newTree);
			newTree.right=root.right;
		}
		root=newTree;
	}
	public Key find(Key key){
		root=splay(key,root);
		if(isEmpty() || !equel(root.key,key)){
			return null;
		}
		return root.key;
	}
	public Key findMin( )
    {
        if( isEmpty( ) )
            return null;
        Node ptr = root;
        while( ptr.left != nullNode )
            ptr = ptr.left;

        root = splay( ptr.key, root );
        return ptr.key;
    }
	public Key findMax( )
    {
        if( isEmpty( ) )
            return null;
        Node ptr = root;
        while( ptr.right != nullNode )
            ptr = ptr.right;
        root = splay( ptr.key, root );
        return ptr.key;
    }
	//***********************�Զ�������չ***********************
	//����������������ĳһ�ڵ�Xʱ����;���Ľڵ㼰��������ԭ�����Ƴ�������Ҫά��������L,X,R.
	//��ǰ�ڵ�X���������ĸ���L���ڴ洢С��X�Ľڵ㣬R���ڴ洢����X�Ľڵ㡣��ʼʱXΪ��չ���ĸ���L��RΪ�ա�
	//�����������Ĺ����У�����ͨ����ת��·���ϵĽڵ���������Ϊ�м����ĸ���������/С�ڵĽڵ����L/R��
	//���մ������ڵ�X����Ϊ�м����ĸ���Ȼ��L��R���ӵ��м����ײ����������X��Ϊ��������չ���ĸ�����չ������ɡ�
	private Node splay(Key key,Node t){
		Node leftTreeMax,rightTreeMin;
		header.left=header.right=nullNode;
		leftTreeMax=rightTreeMin=header;
		nullNode.key=key;
		while(true){
			if(greater(t.key,key)){//����key�뵱ǰ�м������ڵ�key�Ĵ�С��ϵ�������µ�����·��
				if(greater(t.left.key,key)){//����·���ϵ�ǰ�ڵ㣬��ǰ�ڵ�ĺ��������ӽڵ�֮��Ĺ�ϵ��������ת����������
					t=rotateWithLeftChild(t);
				}	
			    if(t.left==nullNode)
			    	break;
				rightTreeMin.left=t;
				rightTreeMin=t;
				t=t.left;
			}else if(greater(key,t.key)){
				if(greater(key,t.right.key)){
					t=rotateWithRightChild(t);
				}
				if(t.right==nullNode)
					break;
				leftTreeMax.right=t;
				leftTreeMax=t;
				t=t.right;
			}else{
				break;
			}
		}
		leftTreeMax.right=t.left;
		rightTreeMin.left=t.right;
		t.left=header.right;
		t.right=header.left;
		return t;
	}
	//**************************��ת����*****************************
	//��k2�����ӽڵ�k1��Ϊ�¸����أ�k2��Ϊk1���Һ���
    private  Node rotateWithLeftChild( Node k2 )
    {
        Node k1 = k2.left;
        k2.left = k1.right;
        k1.right = k2;
        return k1;
    }
    //��k1���Һ��ӽڵ�k2��Ϊ�¸���k1��Ϊk2������
    private  Node rotateWithRightChild( Node k1 )
    {
        Node k2 = k1.right;
        k1.right = k2.left;
        k2.left = k1;
        return k2;
    }
    //k3�����ӽڵ���Һ��ӽ���Ϊ�¸�
    private  Node doubleRotateWithLeftChild( Node k3 )
    {
        k3.left = rotateWithRightChild( k3.left );
        return rotateWithLeftChild( k3 );
    }
    //k1���Һ��ӽڵ�����ӽ���Ϊ�¸�
    private  Node doubleRotateWithRightChild( Node k1 )
    {
        k1.right = rotateWithLeftChild( k1.right );
        return rotateWithRightChild( k1 );
    }
	//************************��������*******************************
	//�Ƚ�������ֵ
	private boolean greater(Key n, Key m) {
		if (n == null) return false;
		if (m == null) return true;
		return comp.compare(n,m) > 0;
	}
	private boolean equel(Key n,Key m){
		return comp.compare(n, m)==0;
	}
	//************************Ĭ�ϱȽ���******************************
	private class MyComparator implements Comparator<Key> {
		@Override
		public int compare(Key key1, Key key2) {
			return ((Comparable<Key>) key1).compareTo(key2);
		}
	}

}
