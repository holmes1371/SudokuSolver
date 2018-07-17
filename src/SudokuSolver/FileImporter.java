package SudokuSolver;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.StringBuilder;
import java.util.ArrayList;
import java.util.List;

public class FileImporter {

	private static int[] template = new int[81];
	private static String inputTemp;
	private static String[] convert = new String[81];

	@SuppressWarnings("deprecation")

	public static int[] readFile(String filename) {
		File file = new File(filename);
		FileInputStream fis = null;
		BufferedInputStream bis = null;
		DataInputStream dis = null;

		try {
			fis = new FileInputStream(file);
			bis = new BufferedInputStream(fis);
			dis = new DataInputStream(bis);
			StringBuffer inputLine = new StringBuffer();

			String tmp;
			int count = 0;
			while ((tmp = dis.readLine()) != null) {

				inputLine.append(tmp);
				count++;
				if (count < 9) {
					inputLine.append(",");
				}

			}

			inputTemp = inputLine.toString();

			fis.close();
			bis.close();
			dis.close();

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		convert = inputTemp.split(",");

		for (int i = 0; i < 81; i++) {
			template[i] = Integer.parseInt(convert[i]);
		}

		return template;
	}

//	private static StringBuilder getBuffer(String filename) throws IOException {
//		
//		File file = new File(filename);
//		FileInputStream fis = null;
//		BufferedInputStream bis = null;
//		
//		try {
//			fis = new FileInputStream(file);
//		} catch (FileNotFoundException e) {
//			e.printStackTrace();
//		}
//		bis = new BufferedInputStream(fis);
//			
//		BufferedReader d = new BufferedReader(new InputStreamReader(bis));
//		
//		StringBuilder inputTemp = new StringBuilder();
//
//		String temp;
//		
//		while ((temp = d.readLine()) != null){
//			inputTemp.append(temp);
//		}
//		
//		return inputTemp;
//		
//	}

//	public static void writeIt(int[] answer) throws IOException {
//		// List<Integer> x = new ArrayList<>();
//
//		StringBuilder x = createOutputString(answer);
//
//		BufferedWriter writer = new BufferedWriter(new FileWriter("answer.csv"));
//
//		writer.write(x.toString());
//
//		writer.close();
//	}
//
//	private static StringBuilder createOutputString(StringBuilder inputStream, int[] answer) throws IOException {
//		StringBuilder x = new StringBuilder();
//		
//		int tally = getTally("answer.csv");
//		int temp = 0;
//		
//		
//		for (int i : answer) {
//			x.append(i);
//			if (temp == 9) {
//				x.append("\n");
//				temp = 0;
//			} else {
//				x.append(",");
//			}
//			temp++;
//		}
//		x.deleteCharAt(0);
//		x.deleteCharAt(0);
//		x.deleteCharAt(161);
//		return x;
//	}
}
