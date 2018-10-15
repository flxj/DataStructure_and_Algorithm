package hello;

import java.util.ArrayList;

class Solution {
    public static void reverse(int[] a,int start,int n){
        if(start==n-1) return ;
        for(int i=start;i<(n+start-1)/2;i++){
            int tmp=a[i];
            a[i]=a[n+start-i-1];
            a[n+start-i-1]=tmp;
        }
        
    }
    public void nextPermutation(int[] nums) {
        if(nums==null || nums.length<2) return ;
        int i=nums.length-1;
        while(true){
            int ii=i;
            --i;
            if(nums[i]<=nums[ii]){
                int j=nums.length-1;
                while(j>=0){
                	if(nums[i]>nums[j]){
                		j-=1;
                	}else{
                		break;
                	}
                }
                int tmp=nums[i];
                nums[i]=nums[j];
                nums[j]=tmp;
                reverse(nums,ii,nums.length);
                return;
                
            }
            if(i==0){
                reverse(nums,0,nums.length);
               return; 
            }
        }
        
    }
}

public class hello {
	public static void main(String[] args){
		/*
		Solution s=new Solution();
		int[] a={3,2,1};
		System.out.println(a[0]);
		s.nextPermutation(a);
		System.out.println(a[1]);
		*/
		/*
		long a=3221225473L;
		int b=(int)a;
		System.out.println(a);
		System.out.println(Integer.MAX_VALUE>Integer.MAX_VALUE-1);
		System.out.println(b);
		*/
		int R=256;
		String pat="asdfghjuhgfrebxzdarejudsbxf";
		int[] a=new int[R];
		for (int c = 0; c < R; c++)
            a[c] = -1;
        for (int j = 0; j < pat.length(); j++){
            a[pat.charAt(j)] = j;
            //System.out.println(pat.charAt(j));
        }
        System.out.println(a[97]);
		
		
		
		
		
		
	}
}
