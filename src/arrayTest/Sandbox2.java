package arrayTest;


import java.util.ArrayList;
import java.util.List;

public class Sandbox2 {
	
	public static List<int[]> globalKey = new ArrayList<>();
	
	public static void main(String[] args){
		
		int[] test1 = {1,2,3,4};
		int[] test2 = {4,3,2,1};
		int[] test3 = {1,4,3,2};
		int[] test4 = {2,4,1,3};
		
		
		
		
		
		
	}
		
	
		
	
	
	public int[] toArray(int position)
	{
		int [] passback = globalKey.get(position);
		int [] readback = new int[4];
		int count = 0;
		for (int i : passback)
		{
			readback[count]=i;
			count++;
		}
		return readback;
		
	}


}
