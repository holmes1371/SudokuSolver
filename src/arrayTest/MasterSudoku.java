package arrayTest;

public class MasterSudoku {

	public static long startTime = System.nanoTime();

	public static void main(String[] args) {
		int[] localMaster = initLocalMaster();

		errorCheck(localMaster);

		localMaster = SudokuStream.solveIt(localMaster, 0, 0);
		long endTime = System.nanoTime();
		printLocalGrid(localMaster);

		if (SudokuStream.mathCheck(localMaster)) {
			System.out.printf("%n%nPuzzle solved in %d iterations.", SudokuStream.iterations - 1);
		} else {
			System.out.printf("%n%nAfter %d iterations no solution was found.%n", SudokuStream.iterations - 1);
			SudokuStream.getFailedRow(localMaster);
		}

		System.out.println();
		printElapsedTime(endTime);
		System.out.println();

		System.exit(0);
	}

	private static void errorCheck(int[] localMaster) {
		int temp = 0;
		for (int i = 1; i < 82; i++) {
			if (localMaster[i] != 0) {

				if (!SudokuStream.checkConstraints(localMaster, i, localMaster[i])) {
					if (temp == 0) {
						System.out.printf("%n---INPUT ERROR---%n");
					}
					System.out.printf("%nCheck position %d for a discrepancy.", i);
					temp++;

				}

			}

		}
		if (temp != 0)System.exit(0);
	}

	private static int[] initLocalMaster() {
		// initializes the masteranswer array with the imported template.
		int[] importTemplate = FileImporter.readFile("excelTest.csv");
		int[] returnTemplate = new int[82];

		for (int i = 0; i < 81; i++) {
			if (importTemplate[i] != 0) {
				returnTemplate[i + 1] = importTemplate[i];
			}

		}
		return returnTemplate;

	}

	private static void printLocalGrid(int[] x) {
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

	private static void printElapsedTime(long endTime) {
		long elapsedMS = (endTime - startTime) / 1000000;

		if (elapsedMS < 1000)
			System.out.println("Time elapsed: " + elapsedMS + " ms");

		if (elapsedMS >= 1000 && elapsedMS < 60000) {
			// long l = (System.nanoTime() - MasterSudoku.startTime) / 1000000;

			double number = (double) elapsedMS / (double) 1000;

			System.out.printf("Time elapsed: %.3f seconds %n", number);
		}

		if (elapsedMS >= 60000) {

			double number = (double) elapsedMS / (double) 60000;
			double seconds = (number - Math.floor(number)) * 60;
			if (seconds <= 9.5) {
				System.out.printf("Time elapsed: %.0f:0%.0f minutes", number, seconds);
			} else {
				System.out.printf("Time elapsed: %.0f:%.0f minutes", number, seconds);
			}
		}
	}

	private static double getElapsedTime(long endTime) {
		long elapsedMS = (endTime - startTime) / 1000000;

		return (double) elapsedMS / (double) 1000;

	}
}
