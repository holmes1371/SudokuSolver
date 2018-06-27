package arrayTest;

import java.util.ArrayList;
import java.util.List;

public class MathCheckerTest {

	public static List<Integer> rowMaster = new ArrayList<>();
	public static List<Integer> colMaster = new ArrayList<>();
	public static List<Integer> squareMaster = new ArrayList<>();
	public static List<Integer> eliminatedPositions = new ArrayList<>();
	public static List<Integer> eliminatedSolutions = new ArrayList<>();
	public static List<Integer> possiblePositions = new ArrayList<>();
	public static int[] boxPosition = new int[6];
	public static int[] squareDesignations = { 11, 14, 17, 38, 41, 44, 65, 68, 80 };

	public static void main(String[] args) {

		initMaster();
		// SudokuStream.mathCheck();

		SudokuStream.printGrid();
		System.out.println();
		SudokuStream.numberSolved(SudokuStream.masteranswer);
		// int position = 1;
		// int row = SudokuStream.getCoordinates(position)[0];
		// int col = SudokuStream.getCoordinates(position)[1];
		// setRCSMasters(row, col, position);
		int count = 0;
		while (true) {
			checkSquareL2();
			checkRowL2();
			checkColL2();
			SudokuStream.printGrid();
			count++;
			System.out.printf("%nIterations: %d%n", count);
			SudokuStream.numberSolved(SudokuStream.masteranswer);
			if (count == 10) {
				break;
			}

		}
		System.out.println("Solved: " + SudokuStream.numberSolved(SudokuStream.masteranswer));

		//
		// System.out.println();
		// System.out.println(rowMaster);
		// System.out.println(colMaster);
		// System.out.println("SquareMaster: " + squareMaster);

	}

	public static void initMaster() {
		// initializes the masteranswer array with the imported template.
		int[] importTemplate = FileImporter.readFile("excelTest.csv");

		for (int i = 0; i < 81; i++) {
			if (importTemplate[i] != 0) {
				SudokuStream.masteranswer[i + 1] = importTemplate[i];
			}

		}

	}

	public static void checkColL2()
	// L2 for Col Key: 2
	{

		for (int thisCol = 0; thisCol < 9; thisCol++) {
			setColMaster(thisCol);
			setEliminatedPositions(2);
			// System.out.println(eliminatedPositions);

			for (int i = 1; i < 10; i++) {
				// eliminated positions for current test value (i)
				setEliminatedPositions(2);
				List<Integer> currentEliminated = eliminatedPositions;
				List<Integer> currentPossible = getCurrentPossible(2);
				List<Integer> usedValues = getUsedValues(2);

				// System.out.println(usedValues);
				if (usedValues.contains(i) == false) {
					for (int j = 0; j < currentPossible.size(); j++) {

						List<Integer> currentSquareValues = getCurrentSquareValues(currentPossible.get(j));
						if (currentSquareValues.contains(i)) {
							if (currentEliminated.contains(currentPossible.get(j)) == false) {
								currentEliminated.add(currentPossible.get(j));
							}
						}

						List<Integer> currentRowValues = getCurrentRowValues(currentPossible.get(j));
						if (currentRowValues.contains(i)) {
							if (currentEliminated.contains(currentPossible.get(j)) == false) {
								currentEliminated.add(currentPossible.get(j));
							}
						}

					}
					if (currentEliminated.size() == 8) {
						for (int z = 0; z < 9; z++) {
							if (currentEliminated.contains(colMaster.get(z)) == false) {
								SudokuStream.masteranswer[colMaster.get(z)] = i;
							}
						}
					}
				}

			}

		}

	}

	public static void checkRowL2() {
		// L2 for Row key: 1
		for (int thisrow = 0; thisrow < 9; thisrow++) {
			setRowMaster(thisrow);
			setEliminatedPositions(1);
			// System.out.println(eliminatedPositions);

			for (int i = 1; i < 10; i++) {
				// eliminated positions for current test value (i)
				setEliminatedPositions(1);
				List<Integer> currentEliminated = eliminatedPositions;
				List<Integer> currentPossible = getCurrentPossible(1);
				List<Integer> usedValues = getUsedValues(1);

				// System.out.println(usedValues);
				if (usedValues.contains(i) == false) {
					for (int j = 0; j < currentPossible.size(); j++) {

						List<Integer> currentSquareValues = getCurrentSquareValues(currentPossible.get(j));
						if (currentSquareValues.contains(i)) {
							if (currentEliminated.contains(currentPossible.get(j)) == false) {
								currentEliminated.add(currentPossible.get(j));
							}
						}

						List<Integer> currentColValues = getCurrentColValues(currentPossible.get(j));
						if (currentColValues.contains(i)) {
							if (currentEliminated.contains(currentPossible.get(j)) == false) {
								currentEliminated.add(currentPossible.get(j));
							}
						}

					}
					if (currentEliminated.size() == 8) {
						for (int z = 0; z < 9; z++) {
							if (currentEliminated.contains(rowMaster.get(z)) == false) {
								SudokuStream.masteranswer[rowMaster.get(z)] = i;
							}
						}
					}
				}

			}

		}

	}

	public static void checkSquareL2()

	// L2 for Square key: 3
	{
		for (int square = 0; square < 9; square++) {
			setSquareMaster(squareDesignations[square]);

			setEliminatedPositions(3);

			for (int i = 1; i < 10; i++) {
				// eliminated positions for current test value (i)
				setEliminatedPositions(3);
				List<Integer> currentEliminated = eliminatedPositions;
				List<Integer> currentPossible = getCurrentPossible(3);
				List<Integer> usedValues = getUsedValues(3);
				// System.out.println("For Value: " + i);
				if (usedValues.contains(i) == false) {
					for (int j = 0; j < currentPossible.size(); j++) {
						// System.out.println();
						// System.out.println("Current Eliminated Positions: " +
						// currentEliminated);
						// System.out.println("Position : " +
						// currentPossible.get(j));

						// System.out.println(currentPossible);
						// System.out.println(usedValues);

						List<Integer> currentRowValues = getCurrentRowValues(currentPossible.get(j));

						if (currentRowValues.contains(i)) {
							if (currentEliminated.contains(currentPossible.get(j)) == false) {
								currentEliminated.add(currentPossible.get(j));
							}
						}

						List<Integer> currentColValues = getCurrentColValues(currentPossible.get(j));

						if (currentColValues.contains(i)) {
							if (currentEliminated.contains(currentPossible.get(j)) == false) {
								currentEliminated.add(currentPossible.get(j));
							}
						}

					}
					if (currentEliminated.size() == 8) {
						for (int z = 0; z < 9; z++) {
							if (currentEliminated.contains(squareMaster.get(z)) == false) {
								SudokuStream.masteranswer[squareMaster.get(z)] = i;
							}
						}
					}
				}

			}

		}
	}

	public static List<Integer> getCurrentSquareValues(int position) {
		List<Integer> returnSquareValues = new ArrayList<>();
		setSquareMaster(position);
		int currentposition = 0;
		for (int i = 0; i < 9; i++) {
			currentposition = squareMaster.get(i);
			returnSquareValues.add(SudokuStream.masteranswer[currentposition]);
		}

		return returnSquareValues;

	}

	public static List<Integer> getCurrentColValues(int position) {
		List<Integer> returnColValues = new ArrayList<>();
		setColMaster(SudokuStream.getCoordinates(position)[1]);
		int currentposition = 0;
		for (int i = 0; i < 9; i++) {
			currentposition = colMaster.get(i);
			returnColValues.add(SudokuStream.masteranswer[currentposition]);
		}

		return returnColValues;
	}

	public static List<Integer> getCurrentRowValues(int position) {
		List<Integer> returnRowValues = new ArrayList<>();
		setRowMaster(SudokuStream.getCoordinates(position)[0]);
		int currentposition = 0;
		for (int i = 0; i < 9; i++) {
			currentposition = rowMaster.get(i);
			returnRowValues.add(SudokuStream.masteranswer[currentposition]);
		}

		return returnRowValues;
	}

	public static List<Integer> getUsedValues(int rowcolSquare) {
		List<Integer> usedValues = new ArrayList<>();

		switch (rowcolSquare) {
		case 1:
			for (int i = 0; i < 9; i++) {
				if (SudokuStream.masteranswer[rowMaster.get(i)] != 0) {
					usedValues.add(SudokuStream.masteranswer[rowMaster.get(i)]);
				}
			}
			break;
		case 2:
			// set for Col
			for (int i = 0; i < 9; i++) {
				if (SudokuStream.masteranswer[colMaster.get(i)] != 0) {
					usedValues.add(SudokuStream.masteranswer[colMaster.get(i)]);
				}
			}
			break;

		case 3:
			// set for Square
			for (int i = 0; i < 9; i++) {
				if (SudokuStream.masteranswer[squareMaster.get(i)] != 0) {
					usedValues.add(SudokuStream.masteranswer[squareMaster.get(i)]);
				}
			}
			break;
		}

		return usedValues;

	}

	public static void setRowMaster(int row) {

		rowMaster.clear();

		for (int i = 0; i < 9; i++) {
			rowMaster.add(SudokuStream.getPosition(row, i));

		}

	}

	public static List<Integer> getCurrentPossible(int rowcolSquare) {
		List<Integer> x = new ArrayList<>();

		switch (rowcolSquare) {
		case 1:
			// row
			for (int i = 0; i < 9; i++) {
				if (eliminatedPositions.contains(rowMaster.get(i)) == false) {
					x.add(rowMaster.get(i));
				}

			}
			break;
		case 2:
			// col
			for (int i = 0; i < 9; i++) {
				if (eliminatedPositions.contains(colMaster.get(i)) == false) {
					x.add(colMaster.get(i));
				}

			}
			break;
		case 3:
			// square
			for (int i = 0; i < 9; i++) {
				if (eliminatedPositions.contains(squareMaster.get(i)) == false) {
					x.add(squareMaster.get(i));
				}

			}
			break;
		}
		return x;

	}

	public static void setEliminatedPositions(int rowcolSquare) {
		eliminatedPositions.clear();

		switch (rowcolSquare) {
		case 1:
			// set for Row
			for (int i = 0; i < 9; i++) {
				if (SudokuStream.masteranswer[rowMaster.get(i)] != 0) {
					eliminatedPositions.add(rowMaster.get(i));
				}

			}
			break;
		case 2:
			// set for Col
			for (int i = 0; i < 9; i++) {
				if (SudokuStream.masteranswer[colMaster.get(i)] != 0) {
					eliminatedPositions.add(colMaster.get(i));
				}

			}

			break;

		case 3:
			// set for Square
			for (int i = 0; i < 9; i++) {
				if (SudokuStream.masteranswer[squareMaster.get(i)] != 0) {
					eliminatedPositions.add(squareMaster.get(i));
				}
			}
			break;

		}

	}

	public static void setRCSMasters(int row, int col, int position) {
		setRowMaster(row);
		setColMaster(col);
		setSquareMaster(position);
	}

	public static void setColMaster(int col) {

		colMaster.clear();
		for (int i = 0; i < 9; i++) {
			colMaster.add(SudokuStream.getPosition(i, col));

		}

	}

	public static void setSquareMaster(int position) {
		int row = SudokuStream.getCoordinates(position)[0];
		int col = SudokuStream.getCoordinates(position)[1];
		getBoxRow(row);
		getBoxCol(col);
		squareMaster.clear();
		for (int i = 0; i < 3; i++) {
			for (int j = 3; j < 6; j++) {

				squareMaster.add(SudokuStream.getPosition(boxPosition[i], boxPosition[j]));

			}

		}
	}

	public static void getBoxRow(int row) {
		// given the row, will set boxPosition [0-2] with the rows that make up
		// the corresponding square.
		switch (row) {
		case 0:
			boxPosition[0] = 0;
			boxPosition[1] = 1;
			boxPosition[2] = 2;
			break;

		case 1:
			boxPosition[0] = 0;
			boxPosition[1] = 1;
			boxPosition[2] = 2;
			break;
		case 2:
			boxPosition[0] = 0;
			boxPosition[1] = 1;
			boxPosition[2] = 2;
			break;
		case 3:
			boxPosition[0] = 3;
			boxPosition[1] = 4;
			boxPosition[2] = 5;
			break;
		case 4:
			boxPosition[0] = 3;
			boxPosition[1] = 4;
			boxPosition[2] = 5;
			break;
		case 5:
			boxPosition[0] = 3;
			boxPosition[1] = 4;
			boxPosition[2] = 5;
			break;
		case 6:
			boxPosition[0] = 6;
			boxPosition[1] = 7;
			boxPosition[2] = 8;
			break;
		case 7:
			boxPosition[0] = 6;
			boxPosition[1] = 7;
			boxPosition[2] = 8;
			break;
		case 8:
			boxPosition[0] = 6;
			boxPosition[1] = 7;
			boxPosition[2] = 8;
			break;
		}

	}

	public static void getBoxCol(int col) {
		// given the col, will set boxPosition [3-5] with the columns that make
		// up the corresponding square
		switch (col) {
		case 0:
			boxPosition[3] = 0;
			boxPosition[4] = 1;
			boxPosition[5] = 2;
			break;

		case 1:
			boxPosition[3] = 0;
			boxPosition[4] = 1;
			boxPosition[5] = 2;
			break;
		case 2:
			boxPosition[3] = 0;
			boxPosition[4] = 1;
			boxPosition[5] = 2;
			break;
		case 3:
			boxPosition[3] = 3;
			boxPosition[4] = 4;
			boxPosition[5] = 5;
			break;
		case 4:
			boxPosition[3] = 3;
			boxPosition[4] = 4;
			boxPosition[5] = 5;
			break;
		case 5:
			boxPosition[3] = 3;
			boxPosition[4] = 4;
			boxPosition[5] = 5;
			break;
		case 6:
			boxPosition[3] = 6;
			boxPosition[4] = 7;
			boxPosition[5] = 8;
			break;
		case 7:
			boxPosition[3] = 6;
			boxPosition[4] = 7;
			boxPosition[5] = 8;
			break;
		case 8:
			boxPosition[3] = 6;
			boxPosition[4] = 7;
			boxPosition[5] = 8;
			break;
		}

	}

}

// SudokuStream.printGrid();
// count++;
// System.out.printf("%nIterations: %d%n", count);
// SudokuStream.numberSolved();
// System.out.println();
// System.out.println(rowMaster);
// System.out.println(colMaster);
// System.out.println("SquareMaster: " + squareMaster);