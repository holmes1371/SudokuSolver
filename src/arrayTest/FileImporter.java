package arrayTest;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class FileImporter {

	public static int[] template = new int[81];
	public static String inputTemp;
	public static String[] convert = new String[81];
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
}
