package strings;

public class KMP2 {
	//接受两种形式的模式串
	private char[] pattern;
	private String pat;
	
	private int[] next;
	
	public KMP2(String pat){
		this.pat=pat;
		
		int n=pat.length();
		next=new int[n];
		int j=0,k=-1;
		next[0]=-1;
		while(j<n){
			if(k==-1 || pat.charAt(j)==pat.charAt(k)){
				j++;
				k++;
				next[j]=k;
			}else{
				k=next[k];
			}
		}
	}
	public KMP2(char[] pattern){
		this.pattern = new char[pattern.length];
        for (int j = 0; j < pattern.length; j++)
            this.pattern[j] = pattern[j];
        
        int n=pattern.length;
        int j=0,k=-1;
		next[0]=-1;
		while(j<n){
			if(k==-1 || pattern[j]==pattern[k]){
				j++;
				k++;
				next[j]=k;
			}else{
				k=next[k];
			}
		}
	}
	
	public int search(String text){
		int pos=0;
		int posT=0;
		int m=pat.length();
		int n=text.length();
		while(pos<m && posT<n){
			if(pos==-1 || pat.charAt(pos)==text.charAt(posT)) {
				pos++;
				posT++;
			}
			else  pos=next[pos];
		}
		if(pos<m) return -1;
		return posT-m;
		
		
	}
	public int serrch(char[] text){
		int pos=0;
		int posT=0;
		int m = pattern.length;
	    int n = text.length;
	    while(pos<m && posT<n){
			if(pos==-1 || pattern[pos]==text[posT]) {
				pos++;
				posT++;
			}	
			else  pos=next[pos];
		}
		if(pos<m) return -1;
		return posT-m;
		
	}
	public int search(String text,int start){
		//从text下标start开始匹配
		return 0;
		
	}
	

}
