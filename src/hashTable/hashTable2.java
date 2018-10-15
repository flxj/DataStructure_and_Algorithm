package hashTable;

import java.util.AbstractCollection;
import java.util.Collection;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Set;
//2018-4-16
//���ԡ����ݽṹ���������Java����������---����̽�ⷨ
public class hashTable2<AnyType> extends AbstractCollection<AnyType> implements Set<AnyType> {
	private int curSize=0;
	private int count=0;
	private int modCount=0;
	private Entry[] table;
	
	//�û�����Ϊ�����ṩһ�����ʵ�hashCode()����
	private static class Entry implements java.io.Serializable{
		public Object element;
		public boolean isActive;
		Entry(Object e){
			this.element=e;
			this.isActive=true;
		}
		Entry(Object e,boolean i){
			this.element=e;
			this.isActive=i;
		}
	}
	private class HashTableIterator implements Iterator<AnyType>{
		private int expectedModCount=modCount;
		private int curPos=-1;
		private int visited=0;
		
		public boolean hasNext(){
			if(expectedModCount!=modCount){
				throw new ConcurrentModificationException();
			}
			return visited!=size();
			
		}
		public AnyType next(){
			if(!hasNext()){
				throw new NoSuchElementException();
			}
			do{
				curPos++;
			}while(curPos<table.length && !isActive(table,curPos));
			visited++;
			return (AnyType) table[curPos].element;
		}
		public void remove(){
			if(expectedModCount!=modCount){
				throw new ConcurrentModificationException();
			}
			if(curPos==-1 || !isActive(table,curPos)){
				throw new IllegalStateException();
			}
			table[curPos].isActive=false;
			curSize--;
			visited--;
			modCount++;
			expectedModCount++;
		}
		
	}
	private static final int DEFAULT_TABLE_SIZE=101;
	//******************************************************
	hashTable2(){
		allocateArray(DEFAULT_TABLE_SIZE);
		clear();
	}
	public hashTable2(Collection<? extends AnyType> other){
		allocateArray(nextPrime(other.size()*2));
		clear();
		for(AnyType val:other){
			add(val);
		}
	}
	//*******************************************************
	public int size(){
		return curSize;
	}
	public Iterator iterator(){
		return new HashTableIterator();
	}
	public void clear(){
		curSize=count=0;
		modCount++;
		for(int i=0;i<table.length;i++){
			table[i]=null;
		}
		
	}
	public boolean add(AnyType x){
		int curPos=findPos(x);
		if(isActive(table,curPos)){
			return false;
		}
		count++;
		//if
		table[curPos]=new Entry(x,true);
		curSize++;
		modCount++;
		if(count>table.length/2){
			rehash();
		}
		return true;
	}
	public AnyType getMatch(AnyType x){
		int curPos=findPos(x);
		if(isActive(table,curPos)){
			return (AnyType)table[curPos].element;
		}
		return null;
		
	}
	public boolean contains(Object x){
		return isActive(table,findPos(x));
	}
	//�Ƴ�����--αɾ��
	public boolean remove(Object x){
		int curPos=findPos(x);
		if(!isActive(table,curPos)){
			return false;
		}
		table[curPos].isActive=false;
		curSize--;
		modCount++;
		if(curSize<table.length/8){
			rehash();
		}
		return true;
	}
	//*******************************************************
	private boolean isActive(Entry[] tab,int pos){
		return tab[pos]!=null && tab[pos].isActive;
	}
	private void allocateArray(int size){
		table=new Entry[size];
	}
	private static int nextPrime(int n){
		return 0;
	}
	//����̽��
	private int findPos(Object x){
		int offset=1;
		int curPos=(x==null)?0:Math.abs(x.hashCode()%table.length);
		while(table[curPos]!=null){
			if(x==null){
				if(table[curPos].element==null){
					break;
				}
			}else if(x.equals(table[curPos].element)){
				break;
			}
			curPos+=offset;
			offset+=2;
			if(curPos>=table.length){
				curPos-=table.length;
			}
		}
		return curPos;
	}
	//��������---�ع�ϣ
	private void rehash(){
		Entry[] oldtable=table;
		allocateArray(nextPrime(4*size()));
		curSize=0;
		count=0;
		for(int i=0;i<oldtable.length;i++){
			if(isActive(oldtable,i)){
				add((AnyType)oldtable[i].element);
			}
		}
	}

}
