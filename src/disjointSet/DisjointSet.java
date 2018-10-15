package disjointSet;

public class DisjointSet
{
	int[] rank,parent;//parent[i]��ʾ��i��Ԫ�صĸ��ڵ�
	int n;
	
	public DisjointSet(int n){
		rank=new int[n];
		parent=new int[n];
		this.n=n;
		for(int i=0;i<n;i++){
			parent[i]=i;
		}
	}
	//����Ԫ��k����set�Ĵ���Ԫ
	public int find(int k){
		/*
		if(k>n || k<0) return Integer.MIN_VALUE-1;
		if(parent[k]==k){//k�ĸ��ڵ�������Լ���˵��k�������Լ����ڼ��ϵĴ���Ԫ
			return k;
		}else{
			int res=find(parent[k]);//�Ե����ϵݹ����
			parent[k]=res;//��kֱ�ӹҵ����ڵ���
			return res;
		}
		*/
		if(parent[k]!=k){
			parent[k]=find(parent[k]);//·��ѹ��
		}
		return parent[k];
	}
	//������x��y���������ϰ��Ⱥϲ�
	public void union(int x,int y){
		int xrep=find(x);
		int yrep=find(y);
		if(xrep==yrep) return ;
		if(rank[xrep]<rank[yrep]){
			parent[xrep]=yrep;
		}else if(rank[xrep]>rank[yrep]){
			parent[yrep]=xrep;
		}else{
			parent[yrep]=xrep;
			rank[xrep]+=1;
		}
	}
	//
	
	//

}
