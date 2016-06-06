package tanksMenu;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;

import javafx.stage.Stage;
import scala_pack.Sort;

public class FileSort {
	public void CreateFileList(Stage prim) {
		prim.close();
		File[] fileList;
		int j = 0;
		RandomAccessFile file = null;
		File directory = new File("saves/");
		fileList = directory.listFiles();
		double[] timers = new double[fileList.length];
		for (File fl : fileList) {
			try {
				file = new RandomAccessFile(fl.getPath(), "r");
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
			try {
				file.seek(file.length() - 2 * 8);
			} catch (IOException e) {
				e.printStackTrace();
			}
			try {
				timers[j] = file.readDouble();
			} catch (IOException e) {
				e.printStackTrace();
			}
			j++;
		}
		long timer = System.currentTimeMillis();
		Sort.sortButtons(timers);
		//javaSorting(timers);
		timer = System.currentTimeMillis() - timer;
		System.out.println("Sorting time - " + timer);
		CreateFileNameList(timers);

	}

	private void CreateFileNameList(double[] timers) {
		File[] fileList;
		File directory = new File("saves/");
		fileList = directory.listFiles();
		String[] filenames = new String[fileList.length];
		RandomAccessFile file = null;
		int i = 0;
		double timerfl = 0;
		for (File fl : fileList) {
			try {
				file = new RandomAccessFile(fl.getPath(), "r");
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
			try {
				file.seek(file.length() - 2 * 8);
			} catch (IOException e) {
				e.printStackTrace();
			}
			try {
				timerfl = file.readDouble();
			} catch (IOException e) {
				e.printStackTrace();
			}
			for (i = 0; i < timers.length; i++) {
				if (timerfl == timers[i]) {
					filenames[i] = fl.getName();
					timers[i] = -1;
					break;
				}
			}
		}
		ChooseSave chf = new ChooseSave();
		chf.SavesWnd(filenames);
	}

	private double[] javaSorting(double[] array) {
		for (int i = 0; i < array.length; i++) {
			for (int j = 0; j < array.length - 1 - i; j++) {
				if (array[j] > array[j + 1]) {
					double temp = array[j];
					array[j] = array[j + 1];
					array[j + 1] = temp;
				}
			}
		}
		return array;
	}

}
