package avlTree;

//节点数据对象
/*
class Data<KeyType,ValueType>{
	public KeyType key;
	public ValueType value;
	Data(KeyType k,ValueType v){
		key=k;
		value=v;
	}
}
*/

class AVLTreeNode<T extends Comparable<?>,U>{
	public Data data;
	public AVLTreeNode<T,U> left;
	public AVLTreeNode<T,U> right;
	public int height;
	//内部类<键值，数据>
	class Data{
		public T key;
		public U value;
		Data(T k,U v){
			key=k;
			value=v;
		}
	}
	
	AVLTreeNode(T k,U v){
		this.data=new Data(k,v);
		this.left=null;
		this.right=null;
		this.height=0;
		
	}
	AVLTreeNode(T k,U v,AVLTreeNode<T,U> left,AVLTreeNode<T,U> right){
		this.data=new Data(k,v);
		this.left=left;
		this.right=right;
		this.height=0;
	}	
}


public class AVLTree<T extends Comparable,U> {
	private AVLTreeNode<T,U> root;
	
	AVLTree(){
		root=null;
	}
	public void insert(T key,U val){
		root=insert(root,key,val);
	}
	public void remove(T key){
		AVLTreeNode<T,U> z=search(root,key);
		if(z!=null){
			root=remove(root,z);
		}
		
	}
	public AVLTreeNode<T,U> find(T key){
		return search(root,key);
	}
	public void destroy(){
		destroy(root);
	}
	public AVLTreeNode<T,U> findMin(){
		return findMin(root);
		
	}
	public AVLTreeNode<T,U> findMax(){
		return findMax(root);
		
	}
	public int height(){
		return Height(root);
	}
	//以上public方法的具体实现
	//插入元素实现
	private AVLTreeNode<T,U> insert(AVLTreeNode<T,U> t,T key,U val){
		if(t==null){
			t=new AVLTreeNode<T,U>(key,val);
			/*
			if(t==null){
				throw new Exception("Create Node Failed");
			}
			*/
		}else if(key.compareTo(t.data.key)<0){
			t.left=insert(t.left,key,val);
			if(Height(t.left)-Height(t.right)==2){
				if(key.compareTo(t.left.data.key)<0){
					t=RightRotate(t);
				}else{
					t=LeftRightRotate(t);
				}
			}
		}else{
			t.right=insert(t.right,key,val);
			if(Height(t.right)-Height(t.left)==2){
				if(key.compareTo(t.right.data.key)>0){
					t=LeftRotate(t);
				}else{
					t=RightLeftRotate(t);
				}
			}
		}
		t.height=Integer.max(Height(t.left),Height(t.right))+1;
		return t;
		
	}
	//删除节点，z为待删除节点
	private AVLTreeNode<T,U> remove(AVLTreeNode<T,U> t,AVLTreeNode<T,U> z){
		if(t==null || z==null) return null;
		int cmp=z.data.key.compareTo(t.data.key);
		if(cmp<0){                                    //待删除节点在左子树
			t.left=remove(t.left,z);
			if(Height(t.right)-Height(t.left)==2){    //若删除节点后造成不平衡，应该调节
				AVLTreeNode<T,U> r=t.right;
				if(Height(r.left)>Height(r.right)){
					t=RightLeftRotate(t);
				}else{
					t=RightRotate(t);
				}
			}
		}else if(cmp>0){                               //待删除节点在右子树
			t.right=remove(t.right,z);
			if(Height(t.left)-Height(t.right)==2){
				AVLTreeNode<T,U> r=t.left;
				if(Height(r.right)>Height(r.left)){
					t=LeftRightRotate(t);
				}else{
					t=LeftRotate(t);
				}
			}
		}else{                                          //待删除节点正为当前节点t
			if(t.left!=null && t.right!=null){          //t的左右子树均不为空
				if(Height(t.left)>Height(t.right)){     //左子树偏高些
					AVLTreeNode<T,U> h=t.left;         //找出t的左子树中的最小节点
					while(h.right!=null){
						h=h.right;
					}
					t.data=h.data;
					t.left=remove(t.left,h);
				}else{                                //右子树偏高些,或等高
					AVLTreeNode<T,U> h=t.right;
					while(h.left!=null){
						h=h.left;
					}
					t.data=h.data;
					t.right=remove(t.left,h);
				}
				
			}else{
				AVLTreeNode<T,U> tmp=t;
				t=(t.left!=null)?t.left:t.right;
				tmp=null;
			}
		}
		return t;
	}
	//递归查找节点
	private AVLTreeNode<T,U> search(AVLTreeNode<T,U> t,T key){
		if(t==null) return t;
		int cmp=key.compareTo(t.data.key);
		if(cmp<0){
			return search(t.left,key);
			
		}else if(cmp>0){
			return search(t.right,key);
		}else{
			return t;
		}
	}
	//非递归查找节点
	private AVLTreeNode<T,U> search2(AVLTreeNode<T,U> t,T key){
		while(t!=null){
			int cmp=t.data.key.compareTo(key);
			if(cmp<0){
				t=t.right;
			}else if(cmp>0){
				t=t.left;
			}else{
				return t;
			}
		}
		return t;
	}
	//获取key最大最小节点
	private AVLTreeNode<T,U> findMin(AVLTreeNode<T,U> t){
		if(t==null) return null;
		while(t.left!=null){
			t=t.left;
		}
		return t;
		
	}
	private AVLTreeNode<T,U> findMax(AVLTreeNode<T,U> t){
		if(t==null) return null;
		while(t.right!=null){
			t=t.right;
		}
		return t;
		
	}
	//销毁AVLTree
	private void destroy(AVLTreeNode<T,U> t){
		if(t==null) return ;
		if(t.left!=null){
			destroy(t.left);
		}
		if(t.right!=null){
			destroy(t.right);
		}
		t=null;
	}
	private int Height(AVLTreeNode<T,U> x){
		return x!=null?x.height:0;
		
	}
	//各种旋转操作
	//右旋
	private AVLTreeNode<T,U> RightRotate(AVLTreeNode<T,U> a){
		AVLTreeNode<T,U> b=a.left;
		a.left=b.right;
		b.right=a;
		a.height=Integer.max(Height(a.left), Height(a.right));
		b.height=Integer.max(Height(b.left),Height(b.right));
		return b;
	}
	//左旋
	private AVLTreeNode<T,U> LeftRotate(AVLTreeNode<T,U> a){
		AVLTreeNode<T,U> b=a.right;
		a.right=b.left;
		b.left=a;
		a.height=Integer.max(Height(a.left), Height(a.right));
		b.height=Integer.max(Height(b.left),Height(b.right));
		return b;
	}
	//先左旋再右旋
	private AVLTreeNode<T,U> LeftRightRotate(AVLTreeNode<T,U> a){
		a.left=LeftRotate(a.left);
		return RightRotate(a);
	}
	//先右旋再左旋
	private AVLTreeNode<T,U> RightLeftRotate(AVLTreeNode<T,U> a){
		a.right=RightRotate(a.right);
		return LeftRotate(a);
	}
	//中序输出，只是为了测试之用
	private void inOrder(AVLTreeNode<T,U> t){
		if(t!=null){
			inOrder(t.left);
			System.out.println("("+t.data.key+":"+t.data.value+")");
			inOrder(t.right);
		}
	}
	public void inOrder(){
		inOrder(root);
	}
    
}
