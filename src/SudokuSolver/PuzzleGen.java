package SudokuSolver;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class PuzzleGen {
	private Solver newbie = new Solver();
	private Random rand = new Random();
	private int difficulty;

	public PuzzleGen(int x) {
		this.difficulty = x;
	}

	public int[] generate() {
		int[] returnPuzz = new int[82];
		while (true) {
			returnPuzz = makeIt();
			returnPuzz = newbie.solve(returnPuzz);
			// System.out.println(newbie.mathCheck(returnPuzz));
			if (newbie.mathCheck(returnPuzz))
				break;
		}
		
		returnPuzz = removeIt(returnPuzz);
		return returnPuzz;
		
	}

	private int[] removeIt(int[] returnPuzz) {
		List<Integer> randomPositions = new ArrayList<>();
		int x = 0;
		switch (difficulty) {
		// easy - 40
		case 1:
			x = 45;
			break;
		// medium - 50
		case 2:
			x = 55;
			break;
		// hard - 60
		case 3:
			x = 65;
			break;
		}
		for (int i = 0; i < x;) {
			int position = rand.nextInt(81) + 1;
			if (!randomPositions.contains(position)) {
				randomPositions.add(position);
				i++;
			}
		}
		for (int i = 0; i < randomPositions.size(); i++) {
			returnPuzz[randomPositions.get(i)] = 0;
		}
		return returnPuzz;
	}

	private int[] makeIt() {
		int[] generate = new int[82];
		while (true) {
			for (int i = 0; i < 15; i++) {
				int position = rand.nextInt(81) + 1;
				int value = rand.nextInt(9) + 1;
				generate[position] = value;
			}
			if (!newbie.errorCheck2(generate)) {
				int[] blank = new int[82];
				generate = Arrays.copyOf(blank, 82);

			} else {
				break;
			}

		}
		return generate;

	}

	// private void printGrid(int[] masteranswer) {
	// // prints the current state of the grid. Will show _ if the position is
	// // unresolved (i.e. a value has not been committed to the masteranswer
	// // array for that position)
	// System.out.printf("%n%n\t1\t2\t3\t4\t5\t6\t7\t8\t9%n");
	// System.out.println("----------------------------------------------------------------------------");
	// int position = 1;
	// for (int i = 1; i < 10; i++) {
	// System.out.printf("%n%d |", i);
	// for (int j = 1; j < 10; j++) {
	// if (masteranswer[position] == 0) {
	// System.out.printf("\t_");
	// position++;
	// } else {
	// System.out.printf("\t%d", masteranswer[position]);
	// position++;
	// }
	// }
	// }
	// }

}
