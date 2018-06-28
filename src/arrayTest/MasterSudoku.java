package arrayTest;
//import java.util.ArrayList;
//import java.util.List;

public class MasterSudoku {

	public static void main(String[] args) {
		
		long startTime = System.nanoTime();	
		int[] localMaster = initLocalMaster();
		
		
		localMaster = SudokuStream.solveIt(localMaster, 0, 0);
		long endTime = System.nanoTime();
		printLocalGrid(localMaster);
		
		if (SudokuStream.mathCheck(localMaster))
		{
			System.out.printf("%n%nPuzzle solved in %d iterations.", SudokuStream.iterations);
		}else{
			System.out.printf("%n%nAfter %d iterations no solution was found.%n", SudokuStream.iterations);
			SudokuStream.getFailedRow(localMaster);
		}
		
		System.out.println();
		System.out.println("Time elapsed: "+((endTime - startTime)/1000000) + " ms"); 
		System.exit(0);
	}
	
	
	public static int[] initLocalMaster() {
		// initializes the masteranswer array with the imported template.
		int[] importTemplate = FileImporter.readFile("excelTest.csv");
		int[] returnTemplate = new int[82];
		
		for (int i = 0; i < 81; i++) {
			if (importTemplate[i] != 0) {
				returnTemplate[i+1] = importTemplate[i];
			}

		}
		return returnTemplate;

	}
	public static void printLocalGrid(int [] x) {
		// prints the current state of the grid. Will show 0 if the position is
		// unresolved (i.e. a value has not been committed to the masteranswer
		// array for that position)
		System.out.printf("%n%n\t0\t1\t2\t3\t4\t5\t6\t7\t8%n");
		System.out.println("------------------------------------------------------------------------------");
		int position = 1;
		for (int i = 0; i < 9; i++) {
			System.out.printf("%n%d   |", i);
			for (int j = 0; j < 9; j++) {
				System.out.printf("\t%d", x[position]);
				position++;
			}
		}
	}
}
