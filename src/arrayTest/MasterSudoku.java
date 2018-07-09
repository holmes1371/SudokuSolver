package SudokuSolver;

public class MasterSudoku {

	public static long startTime = System.nanoTime();

	public static void main(String[] args) {
		
		Solver game = new Solver();
		
		game.solve();
		
		System.exit(0);
	}	
}
