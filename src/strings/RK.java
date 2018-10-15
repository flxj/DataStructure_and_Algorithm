package strings;
//RabinKarp,2018-5-15

import java.math.BigInteger;
import java.util.Random;

public class RK {
	private String pat;    //供LasVegas算法使用
	private long patHash;  //模式串的哈希值
	private int m;         //模式串长度
	private long q;        //一个很大的素数
	private int R;         //进制基数
	private long RM;       //R^(M-1)%Q值
	
	RK(char[] pattern,int R){
		this.pat=String.valueOf(pattern);
		this.R=R;
		throw new UnsupportedOperationException("Operation not supported yet");
	}
	RK(String pat){
		this.pat=pat;
		R=256;
		m=pat.length();
		q=longRandomPrime();
		RM=1;
		for(int i=1;i<m;i++){
			RM=(R*RM)%q;
		}
		patHash=hash(pat,m);
	}
    public int search(String txt) {
        int n = txt.length(); 
        if (n < m) return n;
        long txtHash = hash(txt, m); 

        // check for match at offset 0
        if ((patHash == txtHash) && check(txt, 0))
            return 0;

        // check for hash match; if hash match, check for exact match
        for (int i = m; i < n; i++) {
            // Remove leading digit, add trailing digit, check for match. 
            txtHash = (txtHash + q - RM*txt.charAt(i-m) % q) % q; 
            txtHash = (txtHash*R + txt.charAt(i)) % q; 

            // match
            int offset = i - m + 1;
            if ((patHash == txtHash) && check(txt, offset))
                return offset;
        }

        // no match
        return -1;
    }
	//********************************************************
    // Las Vegas version: does pat[] match txt[i..i-m+1] ?
    private boolean check(String txt, int i) {
        for (int j = 0; j < m; j++) 
            if (pat.charAt(j) != txt.charAt(i + j)) 
                return false; 
        return true;
    }
    
    private static long longRandomPrime() {
        BigInteger prime = BigInteger.probablePrime(31, new Random());
        return prime.longValue();
    }
    private long hash(String key, int m) { 
        long h = 0; 
        for (int j = 0; j < m; j++) 
            h = (R * h + key.charAt(j)) % q;
        return h;
    }

}
