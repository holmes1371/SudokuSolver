package SudokuSolver;

//import java.util.Random;

//import java.util.ArrayList;
//import java.util.List;

public class Sandbox2 {

	public static void main(String[] args) {

		PuzzleGen newPuzz = new PuzzleGen(3);
		
		int[] testPuzz = newPuzz.generate();
		
		printGrid(testPuzz);
		
		System.exit(0);
		
		
	}
	public static void printGrid(int[] masteranswer) {
		// prints the current state of the grid. Will show _ if the position is
		// unresolved (i.e. a value has not been committed to the masteranswer
		// array for that position)
		System.out.printf("%n%n\t1\t2\t3\t4\t5\t6\t7\t8\t9%n");
		System.out.println("----------------------------------------------------------------------------");
		int position = 1;
		for (int i = 1; i < 10; i++) {
			System.out.printf("%n%d   |", i);
			for (int j = 1; j < 10; j++) {
				if (masteranswer[position] == 0) {
					System.out.printf("\t_");
					position++;
				} else {
					System.out.printf("\t%d", masteranswer[position]);
					position++;
				}
			}
		}
	}
}
