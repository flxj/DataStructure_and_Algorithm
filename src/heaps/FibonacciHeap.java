package heaps;
//2018-5-11
import java.util.Comparator;
import java.util.HashMap;
import java.util.NoSuchElementException;

public class FibonacciHeap<Key>{
	private int size;//堆中节点总数
	private Node head;//各个最小树根链表头结点
	private Node min;
	private final Comparator<Key> comp;
	private HashMap<Integer, Node> table = new HashMap<Integer, Node>(); //合并操作之用
	
	private class Node{
		Key key;
		int degree;//节点的度
		Node left,right;//节点的左右兄弟节点
		Node child;//节点的第一个孩子节点
		Node parent;//节点的父节点
		boolean childCut;//childCut标记的作用就是用来标记"该节点的子节点是否有被删除过"，它的作用是来实现级联剪切。
		
		Node(Key key){
			this.key=key;
			this.degree=0;
			this.childCut=false;
			this.left=this;
			this.right=this;
			this.parent=null;
			this.child=null;
		}
	}
	//**********************构造函数*******************************
	public FibonacciHeap(Comparator<Key> C) {
		comp = C;
	}
	public FibonacciHeap() {
		comp = new MyComparator();
	}
	public FibonacciHeap(Key[] a) {
		comp = new MyComparator();
		for (Key k : a) insert(k);
	}
	public FibonacciHeap(Comparator<Key> C, Key[] a) {
		comp = C;
		for (Key k : a) insert(k);
	}
	//************************************************常规操作****************************************************
	public boolean isEmpty() {
		return size == 0;
	}

	public int size() {
		return size;
	}
	public void insert(Key key) {
		Node x = new Node(key);
		size++;
		head = insert(x, head);
		if (min == null) min = head;
		else 			 min = (greater(min.key, key)) ? head : min;
	}
	public Key minKey() {
		if (isEmpty()) throw new NoSuchElementException("Heap is empty");
		return min.key;
	}
	//将另一个堆并入当前堆中
	public FibonacciHeap<Key> union(FibonacciHeap<Key> that) {
		this.head = meld(head, that.head);//实际的合并函数
		this.min = (greater(this.min.key, that.min.key)) ? that.min : this.min;
		this.size = this.size+that.size;
		return this;
	}
	//************************************************删除最小节点******************************************************
	//删除最小结点操作
	//(1）将要抽取最小结点的子树都直接串联在根表中；
	//(2）合并所有degree相等的树，直到没有相等的degree的树
	public Key delMin() {
		if (isEmpty()) throw new NoSuchElementException("Heap is empty");
		head = cut(min, head);//将最小节点min从head列表中切除，返回新的节点(min的后继节点)作为head
		Node x = min.child;//x表示最小节点的孩子们，因为我们只是要删除最小节点，因此它的孩子要归并到head
		Key key = min.key;
		min.key = null;
		if (x != null) {
			head = meld(head, x);//将最小节点min的孩子列表并入根层次列表head
			//x.parent=null;
			min.child = null;//将min的孩子设置为空，此时min成为了一个孤立节点，可以删除掉了
		}
		size--;
		if (!isEmpty()) consolidate();//将最小节点的孩子插入根层次列表head后，需要合并具有相同度的最小树
		return key;
	}
	//将根节点为x的最小树插入到根层次列表
	private Node insert(Node x, Node head) {
			if (head == null) {
				x.left = x;
				x.right = x;
			} else {
				head.left.right = x;
				x.right = head;
				x.left= head.left;
				head.left = x;
			}
			return x;
	}
	//归并两个根列表
	private Node meld(Node x, Node y) {
		if (x == null) return y;
		if (y == null) return x;
		x.left.right = y.right;
		y.right.left= x.left;
		x.left = y;
		y.right = x;
		return x;
	}
	//移除一棵树,也就是说在根层次双向列表head中删除一个节点x
	private Node cut(Node x, Node head) {
		if (x.right == x) {
			x.right = null;
			x.left = null;
			return null;
		} else {
			x.right.left = x.left;
			x.left.right = x.right;
			Node res = x.right;
			x.right= null;
			x.left= null;
			//x.parent=null
			if (head == x)  return res;//如果删除的是head，那么返回head的后继节点
			else 			return head;//删除的是个非head节点，那么还返回原来head
		}
	}
	//合并根层次列表中具有相同度的树
	private void consolidate() {
			table.clear();
			Node x = head;
			int maxDegree = 0;
			min = head;
			Node y = null; Node z = null;
			do {
				y = x;
				x = x.right;
				z = table.get(y.degree);
				while (z != null) {
					table.remove(y.degree);
					if (greater(y.key, z.key)) {
						link(y, z);
						y = z;
					} else {
						link(z, y);
					}
					z = table.get(y.degree);
				}
				table.put(y.degree, y);
				if (y.degree > maxDegree) maxDegree = y.degree;
			} while (x != head);
			head = null;
			for (Node n : table.values()) {
				if (n != null) {
					min = greater(min.key, n.key) ? n : min;
					head = insert(n, head);
				}
			}
		}
	//假设root1的键值大于root2，因此root2变为新的根
	private void link(Node root1, Node root2) {
			root2.child = insert(root1, root2.child);//将节点root1插入到root2的孩子节点列表root2.child中
			root1.parent=root2;
			root2.degree++;
			root1.childCut=false;
	}
	//*****************************************************减小键值*******************************************************
	//减小节点键值，如果减少节点后破坏了"最小堆"性质，要进行调整:
	//(1) 首先，将"被减小节点"从"它所在的最小堆"剥离出来；然后将"该节点"关联到"根链表"中。 倘若被减小的节点不是单独一个节点，而是包含子树的树根。
	//    则是将以"被减小节点"为根的子树从"最小堆"中剥离出来，然后将该树关联到根链表中。
	//(2) 接着，对"被减少节点"的原父节点进行"级联剪切"。所谓"级联剪切"，就是在被减小节点破坏了最小堆性质，并被切下来之后；再从"它的父节点"进行递归级联剪切操作。
	//    而级联操作的具体动作则是：若父节点(被减小节点的父节点)的childCut标记为false，则将其设为true，然后退出。
	//    否则，将父节点从最小堆中切下来(方式和"切被减小节点的方式"一样)；然后递归对祖父节点进行"级联剪切"。
	//    而级联剪切的真正目的是为了防止"最小堆"由二叉树演化成链表。
	
	private void newDegree(Node parent,int degree){//修改节点度数
		parent.degree-=degree;
		if(parent.parent!=null)
			newDegree(parent.parent,degree);
	}
	private void cut2(Node x, Node parent) {
		x.right.left = x.left;
		x.left.right = x.right;
	    newDegree(parent, x.degree);
	    // x没有兄弟
	    if (x == x.right) 
	        parent.child = null;
	    else 
	        parent.child = x.right;

	    x.parent = null;
	    x.left = x.right = null;
	    x.childCut = false;
	    // 将"x所在树"添加到"根链表"中
	    head = insert(x, head);
	}
	//级联剪切
	private void cascadingCut(Node x) {
	    Node parent = x.parent;
	    if (parent != null) {
	        if (x.childCut == false) 
	            x.childCut = true;
	        else {
	            cut2(x, parent);
	            cascadingCut(parent);
	        }
	    }
	}
	private void decreaseKey(Node x, Key key) {
	    if (min==null ||x==null) 
	        return ;
	    if (greater(key, x.key)) {
	    System.out.printf("decrease failed: the new key(%d) is no smaller than current key(%d)\n", key, x.key);
	        return ;
	    }
	    Node parent = x.parent;
	    x.key = key;
	    if (parent!=null && greater(parent.key,x.key)) {
	        // 将x从父节点parent中剥离出来，并将node添加到根链表中
	        cut2(x, parent);
	        cascadingCut(parent);
	    }
	    // 更新最小节点
	    if (greater(min.key , x.key))
	        min = x;
	}
	//****************************************************增加键值********************************************************
	//(1) 将"被增加节点"的"左孩子和左孩子的所有兄弟"都链接到根链表中。
	//(2) 接下来，把"被增加节点"添加到根链表；对其进行级联剪切。
	private void increaseKey(Node x, Key key) {
	    if (min==null ||x==null) 
	        return ;

	    if (greater(x.key,key)) {
	    System.out.printf("increase failed: the new key(%d) is no greater than current key(%d)\n", key, x.key);
	        return ;
	    }

	    // 将node每一个儿子(不包括孙子,重孙,...)都添加到"斐波那契堆的根链表"中
	    while (x.child != null) {
	    	
	        Node child = x.child;
	        child.right.left = child.left;
			child.left.right = child.right;              // 将child从x的子链表中删除
			
	        if (child.right == child)
	            x.child = null;
	        else
	            x.child = child.right;

	        head=insert(child,head);      // 将child添加到根链表中
	        child.parent = null;
	    }
	    x.degree = 0;
	    x.key = key;

	    // 如果x不在根链表中，
	    //     则将x从父节点parent的子链接中剥离出来，
	    //     并使x成为"堆的根链表"中的一员，
	    //     然后进行"级联剪切"
	    // 否则，则判断是否需要更新堆的最小节点
	    Node parent = x.parent;
	    if(parent != null) {
	        cut2(x, parent);
	        cascadingCut(parent);
	    } else if(min == x) {
	        Node right = x.right;
	        while(right != x) {
	            if(greater(x.key , right.key))
	                min = right;
	            right = right.right;
	        }
	    }
	}
	//*******************************************************删除节点****************************************************
	private void remove(Node x) {
	    Key minKey = min.key;
	    delMin();
	    decreaseKey(x, minKey);  
	}
	//************************辅助函数*******************************
	//比较两个键值
	private boolean greater(Key n, Key m) {
		if (n == null) return false;
		if (m == null) return true;
		return comp.compare(n,m) > 0;
	}
	//************************默认比较器******************************
	private class MyComparator implements Comparator<Key> {
		@Override
		public int compare(Key key1, Key key2) {
			return ((Comparable<Key>) key1).compareTo(key2);
		}
	}
}
