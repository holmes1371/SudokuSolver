package arrayTest;

public class MasterSudoku {

	public static long startTime = System.nanoTime();

	public static void main(String[] args) {
		
		SudokuStream game = new SudokuStream();
		
		game.solve();
		
		System.exit(0);
	}	
}
