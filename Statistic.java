package tanksMenu;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;
import scala_pack.Stat;

public class Statistic {
	public void ShowStat(Stage MainStage) {
		MainStage.close();
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
				file.seek(file.length() - 8);
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
		float[] statArray = Stat.find(timers);
		StatWnd(statArray);

	}

	private void StatWnd(float[] StatArray) {
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("СТАТИСТИКА");
		alert.setHeaderText(null);
		alert.setContentText("Победы-" + StatArray[1] * 100 + "%\n" + "Поражения-" + StatArray[0] * 100 + "%");
		alert.show();
	}
}
