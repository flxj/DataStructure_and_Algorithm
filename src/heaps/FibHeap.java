package heaps;
//���ԡ��㷨���İ桷,2018-5-10

import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class FibHeap<Key> implements Iterable<Key> {
	private Node head;					//��С�������˫�������ͷ���
	private Node min;					//ָ���ֵ��С�Ľڵ�
	private int size;					//�ڵ�����
	private final Comparator<Key> comp;	//�Ƚ���
	private HashMap<Integer, Node> table = new HashMap<Integer, Node>(); //�ϲ�����֮��
	
	private class Node {
		Key key;						
		int order;						//�ýڵ�Ķ�
		Node prev, next;				//�����ֵܽڵ�
		Node child;						//һ�����ӽڵ�
	}
	
	//**************���캯��********************
	public FibHeap(Comparator<Key> C) {
		comp = C;
	}
	public FibHeap() {
		comp = new MyComparator();
	}
	public FibHeap(Key[] a) {
		comp = new MyComparator();
		for (Key k : a) insert(k);
	}
	public FibHeap(Comparator<Key> C, Key[] a) {
		comp = C;
		for (Key k : a) insert(k);
	}
	
	//**************�������******************
	public boolean isEmpty() {
		return size == 0;
	}

	public int size() {
		return size;
	}

	public void insert(Key key) {
		Node x = new Node();
		x.key = key;
		size++;
		head = insert(x, head);
		if (min == null) min = head;
		else 			 min = (greater(min.key, key)) ? head : min;
	}
	public Key minKey() {
		if (isEmpty()) throw new NoSuchElementException("Heap is empty");
		return min.key;
	}

	//ɾ����С��ֵ����Ӧ�ڵ�
	public Key delMin() {
		if (isEmpty()) throw new NoSuchElementException("Heap is empty");
		head = cut(min, head);
		Node x = min.child;
		Key key = min.key;
		min.key = null;
		if (x != null) {
			head = meld(head, x);
			min.child = null;
		}
		size--;
		if (!isEmpty()) consolidate();
		return key;
	}
	//�ϲ�������
	public FibHeap<Key> union(FibHeap<Key> that) {
		this.head = meld(head, that.head);//ʵ�ʵĺϲ�����
		this.min = (greater(this.min.key, that.min.key)) ? that.min : this.min;
		this.size = this.size+that.size;
		return this;
	}
	
	//************************��������********************************
	private boolean greater(Key n, Key m) {
		if (n == null) return false;
		if (m == null) return true;
		return comp.compare(n,m) > 0;
	}
	
	//����root1�ļ�ֵ����root2�����root2��Ϊ�µĸ�
	private void link(Node root1, Node root2) {
		root2.child = insert(root1, root2.child);//���ڵ�root1���뵽root2�ĺ��ӽڵ��б�root2.child��
		root2.order++;
	}

	//Coalesce the roots, thus reshapes the tree
	private void consolidate() {
		table.clear();
		Node x = head;
		int maxOrder = 0;
		min = head;
		Node y = null; Node z = null;
		do {
			y = x;
			x = x.next;
			z = table.get(y.order);
			while (z != null) {
				table.remove(y.order);
				if (greater(y.key, z.key)) {
					link(y, z);
					y = z;
				} else {
					link(z, y);
				}
				z = table.get(y.order);
			}
			table.put(y.order, y);
			if (y.order > maxOrder) maxOrder = y.order;
		} while (x != head);
		head = null;
		for (Node n : table.values()) {
			if (n != null) {
				min = greater(min.key, n.key) ? n : min;
				head = insert(n, head);
			}
		}
	}
	
	//��head˫���б������һ���ڵ�, ����һ���µ�ͷ��㣨���²���ڵ㣩
		private Node insert(Node x, Node head) {
			if (head == null) {
				x.prev = x;
				x.next = x;
			} else {
				head.prev.next = x;
				x.next = head;
				x.prev = head.prev;
				head.prev = x;
			}
			return x;
		}
		
		//�Ƴ�һ����,Ҳ����˵�ڸ����˫���б���ɾ��һ���ڵ�x
		private Node cut(Node x, Node head) {
			if (x.next == x) {
				x.next = null;
				x.prev = null;
				return null;
			} else {
				x.next.prev = x.prev;
				x.prev.next = x.next;
				Node res = x.next;
				x.next = null;
				x.prev = null;
				if (head == x)  return res;//���ɾ������head����ô����head�ĺ�̽ڵ�
				else 			return head;//ɾ�����Ǹ���head�ڵ㣬��ô������ԭ��head
			}
		}
		
		//�鲢�������б�
		private Node meld(Node x, Node y) {
			if (x == null) return y;
			if (y == null) return x;
			x.prev.next = y.next;
			y.next.prev = x.prev;
			x.prev = y;
			y.next = x;
			return x;
		}
		
		//************************������*********************************
		public Iterator<Key> iterator() {
			return new MyIterator();
		}
		
		private class MyIterator implements Iterator<Key> {
			private FibHeap<Key> copy;
			
			
			//Constructor takes linear time
			public MyIterator() {
				copy = new FibHeap<Key>(comp);
				insertAll(head);
			}
			
			private void insertAll(Node head) {
				if (head == null) return;
				Node x = head;
				do {
					copy.insert(x.key);
					insertAll(x.child);
					x = x.next;
				} while (x != head);
			}
			
			public void remove() {
				throw new UnsupportedOperationException();
			}
			
			public boolean hasNext() {
				return !copy.isEmpty();
			}
			
			//Takes amortized logarithmic time
			public Key next() {
				if (!hasNext()) throw new NoSuchElementException();
				return copy.delMin();
			}
		}
		
		//************************Ĭ�ϱȽ���*******************************
		private class MyComparator implements Comparator<Key> {
			@Override
			public int compare(Key key1, Key key2) {
				return ((Comparable<Key>) key1).compareTo(key2);
			}
		}
	

}
