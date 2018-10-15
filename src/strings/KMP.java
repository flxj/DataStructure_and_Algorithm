package strings;

public class KMP {
	private final int R;
	private int[][] dfa;
	
	//模式串以两种形式给出
	private char[] pattern;
	private String pat;
	
	//相应构造函数也有两个
	public KMP(String pat){
		this.R=256;
		this.pat=pat;
		//根据pattern创建确定性有穷状态自动机DFA
		int m=pat.length();
		dfa=new int[R][m];
		dfa[pat.charAt(0)][0]=1;
		for(int x=0,j=1;j<m;j++){
			for(int c=0;c<R;c++){
				dfa[c][j]=dfa[c][x];
			}
			dfa[pat.charAt(j)][j]=j+1;
			x=dfa[pat.charAt(j)][x];
		}
	}
	public KMP(char[] pattern, int R) {
        this.R = R;
        this.pattern = new char[pattern.length];
        for (int j = 0; j < pattern.length; j++)
            this.pattern[j] = pattern[j];

        // build DFA from pattern
        int m = pattern.length;
        dfa = new int[R][m]; 
        dfa[pattern[0]][0] = 1; 
        for (int x = 0, j = 1; j < m; j++) {
            for (int c = 0; c < R; c++) 
                dfa[c][j] = dfa[c][x];     // Copy mismatch cases. 
            dfa[pattern[j]][j] = j+1;      // Set match case. 
            x = dfa[pattern[j]][x];        // Update restart state. 
        } 
    } 
	
	public int search(String text){
		int m=pat.length();
		int n=text.length();
		int i,j;
		for(i=0,j=0;i<n && j<m;i++ ){
			j=dfa[text.charAt(i)][j];
		}
		if(j==m) return i-m;
		return n;//not found
	}
	
	 public int search(char[] text) {

	        // simulate operation of DFA on text
	    int m = pattern.length;
	    int n = text.length;
	    int i, j;
	    for (i = 0, j = 0; i < n && j < m; i++) {
	        j = dfa[text[i]][j];
	    }
	    if (j == m) return i - m;    // found
	    return n;                    // not found
	}

}
