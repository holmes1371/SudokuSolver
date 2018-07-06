package arrayTest;

public class MasterSudoku {

	public static long startTime = System.nanoTime();

	public static void main(String[] args) {
		
		SudokuStream game = new SudokuStream();
		
		int[] localMaster = game.initLocalMaster();
		game.errorCheck(localMaster);
		
		int[] finished = game.solver(localMaster);
		
		game.printGrid();
		game.endIt(finished);
		
		System.exit(0);
	}

	
}
