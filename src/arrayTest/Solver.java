package SudokuSolver;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Solver {
	private List<int[]> globalKey = new ArrayList<int[]>();
	private int[][][] grid = new int[9][9][9];
	private int[] masteranswer = new int[82];
	private int[] boxPosition = new int[6];
	private int iterations = 0;
	private List<Integer> rowMaster = new ArrayList<>();
	private List<Integer> colMaster = new ArrayList<>();
	private List<Integer> squareMaster = new ArrayList<>();
	private List<Integer> eliminatedPositions = new ArrayList<>();
	private int[] squareDesignations = { 11, 14, 17, 38, 41, 44, 65, 68, 80 };
	private int keyCount = 0;

	public Solver() {
		this.masteranswer = importTemplate();
		errorCheck(masteranswer);
	}

	public void solve() {
		int[] solvedGame = solveIt(masteranswer, 0, 0);
		printGrid();
		endIt(solvedGame);
	}

	private int[] solveIt(int[] master, int forcePosition, int forceValue) {

		if (forcePosition != 0) {
			master[forcePosition] = forceValue;
		}

		initMaster(master);
		initGrid(master);
		refreshMaster();

		while (true) {

			int beforeSolved = numberSolved(masteranswer);
			solver();
			refreshMaster();
			printGrid();
			iterations++;
			System.out.println();
			System.out.println();
			System.out.println("Iterations: " + iterations);
			// System.out.printf("Solved: %d%n", numberSolved(masteranswer));
			printElapsedTime();
			System.out.println();

			if (beforeSolved == numberSolved(masteranswer)) {
				List<Integer> possibleValues = getPossibleValues(masteranswer);
				int[] y = Arrays.copyOf(masteranswer, 82);
				globalKey.add(y);
				keyCount = globalKey.size() - 1;

				if (mathCheck(y)) {
					return y;
				}
				if (numberSolved(y) == 81 && !mathCheck(y)) {
					return y;
				}

				if (possibleValues.isEmpty()) {
					return globalKey.get(keyCount);
				} else {

					int nextEmpty = possibleValues.get(0);

					int[] sandbox = globalKey.get(keyCount);

					int[] subGame = new int[82];

					for (int i = 1; i < possibleValues.size(); i++) {
						if (!checkConstraints(sandbox, nextEmpty, possibleValues.get(i)))
							return sandbox;
						subGame = solveIt(sandbox, nextEmpty, possibleValues.get(i));
						if (mathCheck(subGame)) {
							return subGame;
						}

					}

				}

			}

		}

	}

	private List<Integer> getPossibleValues(int[] masteranswer) {
		List<Integer> returnValues = new ArrayList<>();
		refreshGrid(masteranswer);
		int nextZero = 0;

		int smallest = 9;

		for (int i = 1; i < 82; i++) {
			if (masteranswer[i] == 0) {
				int size = howMany(i);
				if (size < smallest) {
					nextZero = i;
					smallest = size;
					break;
				}
			}

		}
		if (nextZero == 0) {
			returnValues.clear();
			return returnValues;
		}
		int[] fromGrid = getValues(nextZero);

		if (isBlank(fromGrid)) {
			returnValues.clear();
			return returnValues;
		} else {
			returnValues.add(nextZero);
			for (int j = 0; j < 9; j++) {
				if (fromGrid[j] != 0) {
					returnValues.add(fromGrid[j]);
				}
			}
			return returnValues;
		}
	}

	private int howMany(int position) {
		int[] values = getValues(position);
		int size = 0;
		for (int i : values) {
			if (i != 0)
				size++;
		}
		return size;
	}

	private int[] getCoordinates(int x) {
		// given the grid position, returns the coordinates (row, col)
		int pos = x;
		int[] response = new int[2];
		int[][] values = new int[9][9];
		int count = 1;
		for (int row = 0; row < values.length; row++) {
			for (int col = 0; col < values[row].length; col++) {
				values[row][col] = count;
				count++;
			}
		}
		for (int row = 0; row < values.length; row++) {
			for (int col = 0; col < values[row].length; col++) {
				if (values[row][col] == pos) {
					response[0] = row;
					response[1] = col;
				}
			}
		}
		return response;
	}

	private void refreshGrid(int[] master) {
		for (int i = 1; i < 82; i++) {
			if (master[i] != 0) {
				int[] values = new int[9];
				values[master[i] - 1] = master[i];
				setValues(values, i);
			}
		}
	}

	private void initGrid(int[] master) {
		// initializes the grid with the imported template.
		for (int row = 0; row < grid.length; row++) {
			for (int col = 0; col < grid[row].length; col++) {
				int count = 1;
				for (int z = 0; z < grid[col].length; z++) {
					grid[row][col][z] = count;
					count++;
				}
			}
		}
		refreshGrid(master);
	}

	private void solver() {
		checkColL2();
		checkRowL2();
		checkSquareL2();
		checkColumn();
		checkRow();
		checkSquare();

	}

	private int numberSolved(int[] keycheck) {
		int count = 0;
		for (int i = 1; i < 82; i++) {
			if (keycheck[i] != 0)
				count++;
		}
		return count;
	}

	private boolean checkConstraints(int[] master, int position, int value) {
		// check row
		int[] coordinates = getCoordinates(position);
		for (int i = 0; i < 9; i++) {
			if (i != coordinates[1]) {

				if (value == master[getPosition(coordinates[0], i)]) {
					return false;
				}
			}
		}

		// check column
		for (int i = 0; i < 9; i++) {
			if (i != coordinates[0]) {
				if (value == master[getPosition(i, coordinates[1])]) {
					return false;
				}
			}
		}

		// check square
		getBoxRow(coordinates[0]);
		getBoxCol(coordinates[1]);

		for (int i = 0; i < 3; i++) {
			for (int j = 3; j < 6; j++) {
				if (!isSame(boxPosition[i], boxPosition[j], position)) {
					if (value == master[getPosition(boxPosition[i], boxPosition[j])]) {
						return false;
					}
				}
			}
		}
		return true;
	}

	private void checkSquare() {
		// checks the surrounding square for the position.
		for (int position = 1; position < 82; position++) {

			int[] positionValues = getValues(position);
			int[] coordinates = getCoordinates(position);

			getBoxRow(coordinates[0]);
			getBoxCol(coordinates[1]);

			for (int i = 0; i < 3; i++) {
				for (int j = 3; j < 6; j++) {
					if (!isSame(boxPosition[i], boxPosition[j], position)) {
						int[] gridValues = getGridValues(boxPosition[i], boxPosition[j]);
						if (!isBlank(gridValues)) {
							compareValues(positionValues, boxPosition[i], boxPosition[j]);
						}
					}
				}
			}

			if (!isEmpty(positionValues)) {
				if (finalVerify(positionValues, position)) {
					setValues(positionValues, position);
					if (isFinal(positionValues)) {
						setMaster(positionValues, position);
					}
				}

			}

		}

	}

	private void checkColumn() {
		// checks the row of the values
		for (int position = 1; position < 82; position++) {
			int[] positionValues = getValues(position);
			int[] coordinates = getCoordinates(position);

			for (int i = 0; i < 9; i++) {
				if (i != coordinates[0]) {
					int[] gridValues = getGridValues(i, coordinates[1]);

					if (!isBlank(gridValues)) {
						compareValues(positionValues, i, coordinates[1]);
					}

				}

			}

			if (!isEmpty(positionValues)) {
				if (finalVerify(positionValues, position)) {
					setValues(positionValues, position);
					if (isFinal(positionValues)) {
						setMaster(positionValues, position);
					}
				}

			}

		}
	}

	private void checkRow() {
		// checks the row of the values
		for (int position = 1; position < 82; position++) {
			int[] positionValues = getValues(position);
			int[] coordinates = getCoordinates(position);

			for (int i = 0; i < 9; i++) {
				if (i != coordinates[1]) {
					int[] gridValues = getGridValues(coordinates[0], i);

					if (isBlank(gridValues) == false) {
						compareValues(positionValues, coordinates[0], i);
					}

				}

			}

			if (!isEmpty(positionValues)) {
				if (finalVerify(positionValues, position)) {
					setValues(positionValues, position);
					if (isFinal(positionValues)) {
						setMaster(positionValues, position);
					}
				}

			}
		}
	}

	// LEVEL 2 LOGIC

	private void checkColL2()
	// L2 for Col Key: 2
	{
		for (int thisCol = 0; thisCol < 9; thisCol++) {
			setColMaster(thisCol);
			setEliminatedPositions(2);

			for (int i = 1; i < 10; i++) {
				// eliminated positions for current test value (i)
				setEliminatedPositions(2);
				List<Integer> currentEliminated = eliminatedPositions;
				List<Integer> currentPossible = getCurrentPossible(2);
				List<Integer> usedValues = getUsedValues(2);

				if (!usedValues.contains(i)) {
					for (int j = 0; j < currentPossible.size(); j++) {

						List<Integer> currentSquareValues = getCurrentSquareValues(currentPossible.get(j));
						if (currentSquareValues.contains(i)) {
							if (!currentEliminated.contains(currentPossible.get(j))) {
								currentEliminated.add(currentPossible.get(j));
							}
						}
						List<Integer> currentRowValues = getCurrentRowValues(currentPossible.get(j));
						if (currentRowValues.contains(i)) {
							if (!currentEliminated.contains(currentPossible.get(j))) {
								currentEliminated.add(currentPossible.get(j));
							}
						}
					}
					if (currentEliminated.size() == 8) {
						for (int z = 0; z < 9; z++) {
							if (!currentEliminated.contains(colMaster.get(z))) {
								masteranswer[colMaster.get(z)] = i;
								int[] x = new int[9];
								x[i - 1] = i;
								setValues(x, colMaster.get(z));
							}
						}
					}
				}
			}
		}
		refreshGrid(masteranswer);
	}

	private void checkRowL2() {
		// L2 for Row key: 1
		for (int thisrow = 0; thisrow < 9; thisrow++) {
			setRowMaster(thisrow);
			setEliminatedPositions(1);

			for (int i = 1; i < 10; i++) {
				// eliminated positions for current test value (i)
				setEliminatedPositions(1);
				List<Integer> currentEliminated = eliminatedPositions;
				List<Integer> currentPossible = getCurrentPossible(1);
				List<Integer> usedValues = getUsedValues(1);

				if (usedValues.contains(i) == false) {
					for (int j = 0; j < currentPossible.size(); j++) {

						List<Integer> currentSquareValues = getCurrentSquareValues(currentPossible.get(j));
						if (currentSquareValues.contains(i)) {
							if (!currentEliminated.contains(currentPossible.get(j))) {
								currentEliminated.add(currentPossible.get(j));
							}
						}
						List<Integer> currentColValues = getCurrentColValues(currentPossible.get(j));
						if (currentColValues.contains(i)) {
							if (!currentEliminated.contains(currentPossible.get(j))) {
								currentEliminated.add(currentPossible.get(j));
							}
						}
					}
					if (currentEliminated.size() == 8) {
						for (int z = 0; z < 9; z++) {
							if (!currentEliminated.contains(rowMaster.get(z))) {
								masteranswer[rowMaster.get(z)] = i;
								int[] x = new int[9];
								x[i - 1] = i;
								setValues(x, rowMaster.get(z));
							}
						}
					}
				}
			}
		}
		refreshGrid(masteranswer);
	}

	private void checkSquareL2()

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

				if (usedValues.contains(i) == false) {
					for (int j = 0; j < currentPossible.size(); j++) {

						List<Integer> currentRowValues = getCurrentRowValues(currentPossible.get(j));

						if (currentRowValues.contains(i)) {
							if (!currentEliminated.contains(currentPossible.get(j))) {
								currentEliminated.add(currentPossible.get(j));
							}
						}
						List<Integer> currentColValues = getCurrentColValues(currentPossible.get(j));
						if (currentColValues.contains(i)) {
							if (!currentEliminated.contains(currentPossible.get(j))) {
								currentEliminated.add(currentPossible.get(j));
							}
						}
					}
					if (currentEliminated.size() == 8) {
						for (int z = 0; z < 9; z++) {
							if (!currentEliminated.contains(squareMaster.get(z))) {
								masteranswer[squareMaster.get(z)] = i;
								int[] x = new int[9];
								x[i - 1] = i;
								setValues(x, squareMaster.get(z));
							}
						}
					}
				}
			}
		}
		refreshGrid(masteranswer);
	}

	private void setValues(int[] x, int position) {
		// values are written back to the grid after compareValues has been
		// completed.
		int[] coordinates = getCoordinates(position);

		for (int i = 0; i < 9; i++) {
			grid[coordinates[0]][coordinates[1]][i] = x[i];
		}
	}

	private boolean mathCheck(int[] master) {

		int total;

		// verify row
		for (int i = 0; i < 9; i++) {
			total = 0;
			for (int j = 0; j < 9; j++) {
				total += master[getPosition(i, j)];
			}
			if (total != 45) {
				return false;
			}
		}

		// verify column
		for (int j = 0; j < 9; j++) {
			total = 0;
			for (int i = 0; i < 9; i++) {
				total += master[getPosition(i, j)];
			}
			if (total != 45) {
				return false;
			}
		}

		// verify square
		for (int position = 1; position < 82; position++) {

			total = 0;
			int[] coord = getCoordinates(position);
			getBoxRow(coord[0]);
			getBoxCol(coord[1]);

			for (int i = 0; i < 3; i++) {
				for (int j = 3; j < 6; j++) {
					total += master[getPosition(boxPosition[i], boxPosition[j])];
				}
			}
			if (total != 45) {
				return false;
			}
		}
		return true;
	}

	private boolean finalVerify(int[] x, int position) {
		// triple verification before any answer is committed to the
		// masteranswer array.
		int attemptedValue = 0;
		if (isFinal(x)) {
			for (int i = 0; i < 9; i++) {
				if (x[i] != 0) {
					attemptedValue = x[i];
				}
			}

			int[] coordinates = getCoordinates(position);

			// verify ROW:
			for (int i = 0; i < 9; i++) {
				if (i != coordinates[1]) {
					if (masteranswer[getPosition(coordinates[0], i)] == attemptedValue) {
						return false;
					}
				}
			}
			// verify COLUMN:
			for (int i = 0; i < 9; i++) {
				if (i != coordinates[0]) {
					if (masteranswer[getPosition(i, coordinates[1])] == attemptedValue) {
						return false;
					}
				}
			}
			// verify SQUARE:
			getBoxRow(coordinates[0]);
			getBoxCol(coordinates[1]);
			for (int i = 0; i < 3; i++) {
				for (int j = 3; j < 6; j++) {
					if (!isSame(boxPosition[i], boxPosition[j], position)) {
						if (masteranswer[getPosition(boxPosition[i], boxPosition[j])] == attemptedValue) {
							return false;
						}
					}
				}
			}
		}
		// if row, col and square tests pass:
		return true;
	}

	private int verifyValue(int x) {
		// verifies that there is only one possible solution left in the grid
		// before committing that solution to the masteranswer array
		int[] rowcol = getCoordinates(x);

		int answer = 0;
		int count = 0;

		for (int i = 0; i < 9; i++) {
			if (grid[rowcol[0]][rowcol[1]][i] == 0) {
				count++;
			} else {
				answer = grid[rowcol[0]][rowcol[1]][i];
			}
		}

		if (count != 8) {
			answer = 0;
		}
		return answer;
	}

	private void compareValues(int[] positionValues, int row, int col) {
		// compares the position values to the masteranswer array
		int position = getPosition(row, col);
		if (masteranswer[position] != 0) {
			positionValues[masteranswer[position] - 1] = 0;
		}
	}

	private void refreshMaster() {
		// masteranswer array refresh by verifying positions within the grid
		for (int i = 0; i < 82; i++) {
			masteranswer[i] = verifyValue(i);
		}
	}

	private boolean isFinal(int[] positionValues) {
		// checks to see if there is only 1 possible solution left in the given
		// position.
		int count = 0;
		for (int i = 0; i < 9; i++) {
			if (positionValues[i] == 0) {
				count++;
			}
		}
		return count == 8;
	}

	private boolean isBlank(int[] x) {
		// checks a position to see if it is blank on the main grid. i.e. no (or
		// all)
		// possibilities have been eliminated
		int count = 0;
		for (int i = 0; i < 9; i++) {
			if (x[i] == 0) {
				count++;
			}
		}
		return count == 0;
	}

	private boolean isSame(int i, int j, int position) {
		// used for Square Check. Compares coordinates (i,j) to that of position
		// (x,y)
		int[] positionCoords = getCoordinates(position);
		int[] testCoords = { i, j };
		return positionCoords[0] == testCoords[0] && positionCoords[1] == testCoords[1];
	}

	private boolean isEmpty(int[] x) {
		// checks to see if all possible values have been eliminated from a
		// position.
		int count = 0;
		for (int i = 0; i < 9; i++) {
			if (x[i] == 0) {
				count++;
			}
		}
		return count == 9;
	}

	private void initMaster(int[] master) {
		// initializes the masteranswer array with the imported template.
		// int[] importTemplate = FileImporter.readFile("excelTest.csv");
		masteranswer = Arrays.copyOf(master, 82);
	}

	private int getPosition(int row, int col) {
		// given coordinates (row, col) returns the grid position.
		int pos = (9 * row) + (col + 1);
		return pos;
	}

	private int[] getGridValues(int row, int column) {
		// given the coordinates (row, col) returns possible values stored in
		// that position.
		int[] gridValues = new int[9];

		for (int i = 0; i < 9; i++) {
			gridValues[i] = grid[row][column][i];
		}
		return gridValues;
	}

	private int[] getValues(int position) {
		// given the grid position, returns the possible values stored in the
		// grid.
		int[] coordinates = getCoordinates(position);
		int[] values = new int[9];

		for (int i = 0; i < 9; i++) {
			values[i] = grid[coordinates[0]][coordinates[1]][i];
		}
		return values;
	}

	private void getBoxRow(int row) {
		// given the row, will set boxPosition [0-2] with the rows that make up
		// the corresponding square.
		switch (row) {
		case 0:
		case 1:
		case 2:
			boxPosition[0] = 0;
			boxPosition[1] = 1;
			boxPosition[2] = 2;
			break;
		case 3:
		case 4:
		case 5:
			boxPosition[0] = 3;
			boxPosition[1] = 4;
			boxPosition[2] = 5;
			break;
		case 6:
		case 7:
		case 8:
			boxPosition[0] = 6;
			boxPosition[1] = 7;
			boxPosition[2] = 8;
			break;
		}
	}

	private void getBoxCol(int col) {
		// given the col, will set boxPosition [3-5] with the columns that make
		// up the corresponding square
		switch (col) {
		case 0:
		case 1:
		case 2:
			boxPosition[3] = 0;
			boxPosition[4] = 1;
			boxPosition[5] = 2;
			break;
		case 3:
		case 4:
		case 5:
			boxPosition[3] = 3;
			boxPosition[4] = 4;
			boxPosition[5] = 5;
			break;
		case 6:
		case 7:
		case 8:
			boxPosition[3] = 6;
			boxPosition[4] = 7;
			boxPosition[5] = 8;
			break;
		}
	}

	private void setMaster(int[] positionValues, int position) {
		// setter for masteranswer array
		for (int i = 0; i < 9; i++) {
			setValues(positionValues, position);
			if (positionValues[i] != 0) {
				masteranswer[position] = positionValues[i];
			}
		}
	}

	// LEVEL 2 LOGIC

	private List<Integer> getCurrentSquareValues(int position) {
		List<Integer> returnSquareValues = new ArrayList<>();
		setSquareMaster(position);
		for (int i = 0; i < 9; i++) {
			int currentposition = squareMaster.get(i);
			returnSquareValues.add(masteranswer[currentposition]);
		}
		return returnSquareValues;
	}

	private List<Integer> getCurrentColValues(int position) {
		List<Integer> returnColValues = new ArrayList<>();
		setColMaster(getCoordinates(position)[1]);
		for (int i = 0; i < 9; i++) {
			int currentposition = colMaster.get(i);
			returnColValues.add(masteranswer[currentposition]);
		}
		return returnColValues;
	}

	private List<Integer> getCurrentRowValues(int position) {
		List<Integer> returnRowValues = new ArrayList<>();
		setRowMaster(getCoordinates(position)[0]);
		for (int i = 0; i < 9; i++) {
			int currentposition = rowMaster.get(i);
			returnRowValues.add(masteranswer[currentposition]);
		}
		return returnRowValues;
	}

	private List<Integer> getUsedValues(int rowcolSquare) {
		List<Integer> usedValues = new ArrayList<>();

		switch (rowcolSquare) {
		case 1:
			for (int i = 0; i < 9; i++) {
				if (masteranswer[rowMaster.get(i)] != 0) {
					usedValues.add(masteranswer[rowMaster.get(i)]);
				}
			}
			break;
		case 2:
			// set for Col
			for (int i = 0; i < 9; i++) {
				if (masteranswer[colMaster.get(i)] != 0) {
					usedValues.add(masteranswer[colMaster.get(i)]);
				}
			}
			break;

		case 3:
			// set for Square
			for (int i = 0; i < 9; i++) {
				if (masteranswer[squareMaster.get(i)] != 0) {
					usedValues.add(masteranswer[squareMaster.get(i)]);
				}
			}
			break;
		}
		return usedValues;
	}

	private void setRowMaster(int row) {
		rowMaster.clear();
		for (int i = 0; i < 9; i++) {
			rowMaster.add(getPosition(row, i));
		}
	}

	private List<Integer> getCurrentPossible(int rowcolSquare) {
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

	private void setEliminatedPositions(int rowcolSquare) {
		eliminatedPositions.clear();

		switch (rowcolSquare) {
		case 1:
			// set for Row
			for (int i = 0; i < 9; i++) {
				if (masteranswer[rowMaster.get(i)] != 0) {
					eliminatedPositions.add(rowMaster.get(i));
				}
			}
			break;

		case 2:
			// set for Col
			for (int i = 0; i < 9; i++) {
				if (masteranswer[colMaster.get(i)] != 0) {
					eliminatedPositions.add(colMaster.get(i));
				}
			}
			break;

		case 3:
			// set for Square
			for (int i = 0; i < 9; i++) {
				if (masteranswer[squareMaster.get(i)] != 0) {
					eliminatedPositions.add(squareMaster.get(i));
				}
			}
			break;
		}
	}

	private void setColMaster(int col) {
		colMaster.clear();
		for (int i = 0; i < 9; i++) {
			colMaster.add(getPosition(i, col));
		}
	}

	private void setSquareMaster(int position) {
		getBoxRow(getCoordinates(position)[0]);
		getBoxCol(getCoordinates(position)[1]);
		squareMaster.clear();
		for (int i = 0; i < 3; i++) {
			for (int j = 3; j < 6; j++) {
				squareMaster.add(getPosition(boxPosition[i], boxPosition[j]));
			}
		}
	}

	private void printElapsedTime() {
		if ((System.nanoTime() - MasterSudoku.startTime) / 1000000 < 1000)
			System.out.println("Time elapsed: " + (System.nanoTime() - MasterSudoku.startTime) / 1000000 + " ms");

		if ((System.nanoTime() - MasterSudoku.startTime) / 1000000 >= 1000
				&& (System.nanoTime() - MasterSudoku.startTime) / 1000000 < 60000) {
			long l = (System.nanoTime() - MasterSudoku.startTime) / 1000000;

			double number = (double) l / (double) 1000;

			System.out.printf("Time elapsed: %.3f seconds %n", number);
		}
		if ((System.nanoTime() - MasterSudoku.startTime) / 1000000 >= 60000) {
			long l = (System.nanoTime() - MasterSudoku.startTime) / 1000000;

			double number = (double) l / (double) 60000;
			double seconds = (number - Math.floor(number)) * 60;
			if (seconds <= 9.5) {
				System.out.printf("Time elapsed: %.0f:0%.0f minutes%n", number, seconds);
			} else {
				System.out.printf("Time elapsed: %.0f:%.0f minutes%n", number, seconds);
			}
		}

	}

	private void printElapsedTime(long endTime) {
		long elapsedMS = (endTime - MasterSudoku.startTime) / 1000000;

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

	private void printGrid() {
		// prints the current state of the grid. Will show _ if the position is
		// unresolved (i.e. a value has not been committed to the masteranswer
		// array for that position)
		System.out.printf("%n%n\t0\t1\t2\t3\t4\t5\t6\t7\t8%n");
		System.out.println("----------------------------------------------------------------------------");
		int position = 1;
		for (int i = 0; i < 9; i++) {
			System.out.printf("%n%d   |", i);
			for (int j = 0; j < 9; j++) {
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

	private int[] importTemplate() {
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

	private void errorCheck(int[] localMaster) {
		int temp = 0;
		for (int i = 1; i < 82; i++) {
			if (localMaster[i] != 0) {
				if (!checkConstraints(localMaster, i, localMaster[i])) {
					if (temp == 0) {
						System.out.printf("%n---INPUT ERROR---%n");
					}
					System.out.printf("%nCheck position %d for a discrepancy.", i);
					temp++;
				}
			}
		}
		if (temp != 0)
			System.exit(0);
	}

	private void endIt(int[] finished) {
		long endTime = System.nanoTime();
		if (mathCheck(finished)) {
			System.out.printf("%n%nPuzzle solved in %d iterations.", iterations - 1);
		} else {
			System.out.printf("%n%nAfter %d iterations no solution was found.%n", iterations - 1);
		}
		System.out.println();
		printElapsedTime(endTime);
		System.out.println();
	}
}
