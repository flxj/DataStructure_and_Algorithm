package strings;
//BoyerMoore,2018-5-15

public class BM {
	private final int R;
	private int[] right;//坏字符规则
	
	private char[] pattern;
	private String pat;
	
	BM(String pat){
		this.R=256;
		this.pat=pat;
		//
		right=new int[R];
		for(int c=0;c<R;c++){
			right[c]=-1;
		}
		for(int j=0;j<pat.length();j++){
			right[pat.charAt(j)]=j;//pat.charAt(j)转换成int了，就是ASCII码
		}
	}
     BM(char[] pattern, int R) {
        this.R = R;
        this.pattern = new char[pattern.length];
        for (int j = 0; j < pattern.length; j++)
            this.pattern[j] = pattern[j];

        // position of rightmost occurrence of c in the pattern
        right = new int[R];
        for (int c = 0; c < R; c++)
            right[c] = -1;
        for (int j = 0; j < pattern.length; j++)
            right[pattern[j]] = j;
    }
     public int search(String txt){
    	 int m=pat.length();
    	 int n=txt.length();
    	 int skip;
    	 for(int i=0;i<n-m;i+=skip){
    		 skip=0;
    		 for(int j=m-1;j>=0;j--){
    			 if(pat.charAt(j)==txt.charAt(i+j)){
    				 skip=Math.max(1, j-right[txt.charAt(i+j)]);
    				 break;
    			 }
    		 }
    		 if(skip==0) return i;
    	 }
    	 return -1;
     }
     public int search(char[] text) {
         int m = pattern.length;
         int n = text.length;
         int skip;
         for (int i = 0; i <= n - m; i += skip) {
             skip = 0;
             for (int j = m-1; j >= 0; j--) {
                 if (pattern[j] != text[i+j]) {
                     skip = Math.max(1, j - right[text[i+j]]);
                     break;
                 }
             }
             if (skip == 0) return i;    // found
         }
         return -1;                       // not found
     }

}
