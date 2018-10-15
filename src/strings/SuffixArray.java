package strings;
//2018-5-13 后缀数字---没有优化版的
import java.util.Arrays;

public class SuffixArray {
	private Suffix[] suffixes;
	
	private static class Suffix implements Comparable<Suffix>{
		private final String text;
		private final int index;
		
		private Suffix(String text,int index){
			this.text=text;
			this.index=index;
		}
		private int length(){
			return text.length()-index;
		}
		private char charAt(int i){
			return text.charAt(index+i);
		}
		public int compareTo(Suffix that){
			int n=Math.min(this.length(), that.length());
			for(int i=0;i<n;i++){
				if(this.charAt(i)<that.charAt(i)) return -1;
				if(this.charAt(i)>that.charAt(i)) return +1;
			}
			return this.length()-that.length();
		}
		public String toString(){
			return text.substring(index);
		}
	}
	
	public SuffixArray(String text){
		int n=text.length();
		this.suffixes=new Suffix[n];
		for(int i=0;i<n;i++){
			suffixes[i]=new Suffix(text,i);
		Arrays.sort(suffixes);;
		}
	}
	
	public int length(){
		return suffixes.length;
	}
	
	public int index(int i){
		if(i<0 || i>=suffixes.length){
			throw new IllegalArgumentException();
		}
		return suffixes[i].index;
	}
	
    public int lcp(int i) {
        if (i < 1 || i >= suffixes.length) throw new IllegalArgumentException();
        return lcpSuffix(suffixes[i], suffixes[i-1]);
    }

    // longest common prefix of s and t
    private static int lcpSuffix(Suffix s, Suffix t) {
        int n = Math.min(s.length(), t.length());
        for (int i = 0; i < n; i++) {
            if (s.charAt(i) != t.charAt(i)) return i;
        }
        return n;
    }
    public String select(int i) {
        if (i < 0 || i >= suffixes.length) throw new IllegalArgumentException();
        return suffixes[i].toString();
    }
    public int rank(String query) {
        int lo = 0, hi = suffixes.length - 1;
        while (lo <= hi) {
            int mid = lo + (hi - lo) / 2;
            int cmp = compare(query, suffixes[mid]);
            if (cmp < 0) hi = mid - 1;
            else if (cmp > 0) lo = mid + 1;
            else return mid;
        }
        return lo;
    }

    // compare query string to suffix
    private static int compare(String query, Suffix suffix) {
        int n = Math.min(query.length(), suffix.length());
        for (int i = 0; i < n; i++) {
            if (query.charAt(i) < suffix.charAt(i)) return -1;
            if (query.charAt(i) > suffix.charAt(i)) return +1;
        }
        return query.length() - suffix.length();
    }

}
