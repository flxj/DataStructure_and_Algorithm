
//普通堆实现优先队列
package binaryHeap;

import java.util.AbstractCollection;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Queue;

public class PriorityQueue<T> extends AbstractCollection<T> implements Queue<T> {
	
	private int curSize;
	private T[] array;
	private Comparator<? super T> cmp;
	private static final int defaultSize=10;
	
	
	//构造函数
	public PriorityQueue(){
		curSize=0;
		cmp=null;
		array=(T[])new Object[defaultSize+1];
		
	}
	//构造函数之二
	public PriorityQueue(Comparator<? super T> c){
		curSize=0;
		cmp=c;
		array=(T[])new Object[defaultSize+1];
	}
	//
	public PriorityQueue(Collection<? extends T> coll){
		cmp=null;
		curSize=coll.size();
		array=(T[])new Object[(curSize+2)*11/10];
		int i=0;
		for(T item:coll){
			array[i++]=item;
		}
		buildHeap();
	}

	@Override
	public boolean addAll(Collection<? extends T> arg0) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void clear() {
		curSize=0;
		
	}

	@Override
	public boolean contains(Object arg0) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean containsAll(Collection<?> arg0) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isEmpty() {
		return curSize==0;
	}

	@Override
	public Iterator<T> iterator() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean remove(Object arg0) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean removeAll(Collection<?> arg0) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean retainAll(Collection<?> arg0) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public int size() {
		return curSize;
	}

	@Override
	public Object[] toArray() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <T> T[] toArray(T[] arg0) {
		// TODO Auto-generated method stub
		return null;
	}
    //插入新元素，自底向上调整堆
	@Override
	public boolean add(T x) {
		if(curSize+1==array.length)
			doubleArray();
		int hole=++curSize;
		array[0]=x;
		for(;compare(x,array[hole/2])<0;hole/=2){
			array[hole]=array[hole/2];
		}
		array[hole]=x;
		return true;
	}

	@Override
	public T element() {
		if(isEmpty()){
			return null;
		}
		return array[1];
	}

	@Override
	public boolean offer(T arg0) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public T peek() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public T poll() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public T remove() {
		T minItem=element();
		array[1]=array[curSize--];
		percolateDown(1);
		return minItem;
	}
    private void doubleArray(){
		
	}
	private int compare(T lhs,T rhs){
		return 0;
	}
	//向下过滤过程,自顶向下调整堆
	private void percolateDown(int hole){
		int child;
		T tmp=array[hole];
		for(;hole*2<=curSize;hole=child){
			child=hole*2;
			if(child!=curSize && compare(array[child+1],array[child])<0){
				child++;
			}
			if(compare(array[child],tmp)<0){
				array[hole]=array[child];
			}else{
				break;
			}
		}
		array[hole]=tmp;
	}
	//构造堆
	private void buildHeap(){
		for(int i=curSize/2;i>0;i--){
			percolateDown(i);
		}
	}

}
