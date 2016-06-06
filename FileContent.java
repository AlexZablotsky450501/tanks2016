package tanksMenu;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;

import javafx.animation.FadeTransition;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.util.Duration;
import scala_pack.notation;

public class FileContent {
	public void ShowList(Stage Gamestage) {
		Gamestage.close();
		File[] fileList;
		File directory = new File("saves/");
		fileList = directory.listFiles();
		String[] filenames = new String[fileList.length];
		int i = 0;
		for (File fl : fileList) {
			filenames[i] = fl.getName();
			i++;
		}
		FilesWnd(filenames);
	}

	private void FilesWnd(String[] fileList) {
		Stage st = new Stage();
		MenuMain[] saveMenu = new MenuMain[fileList.length + 1];
		int i = 0;
		for (String fl : fileList) {
			MenuMain Mm = new MenuMain(fl);
			Mm.setOnMouseClicked(event -> StartNotation(Mm.PATH,st));
			saveMenu[i] = Mm;
			i++;
		}
		String back = "Выход";
	
		MenuMain ba = new MenuMain(back);
		ba.setOnMouseClicked(event -> st.close());
		saveMenu[i] = ba;
		InputStream is = null;
		try {
			is = Files.newInputStream(Paths.get("src/tanksMenu/ESC.jpg"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		Image image = new Image(is);
		try {
			is.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		ImageView vj = new ImageView(image);
		vj.setFitHeight(500);
		vj.setFitWidth(500);
		SubMenu sb = new SubMenu(saveMenu);
		MenuBox mb = new MenuBox(sb);
		Pane saves = new Pane();
		saves.getChildren().add(vj);
		saves.getChildren().add(mb);
		Scene sc = new Scene(saves, 500, 500);
		sc.setOnScroll(event -> {
			if (saveMenu[saveMenu.length - 1].getBoundsInParent().getMaxY() <= 400 && event.getDeltaY() < 0) {
				return;
			} else if (saveMenu[0].getBoundsInParent().getMaxY() >= 0 && event.getDeltaY() > 0) {
				return;
			}
			for (MenuMain mm : saveMenu) {
				mm.setTranslateY(mm.getTranslateY() + event.getDeltaY() / Math.abs(event.getDeltaY()) * 10);
			}
		});
		sc.setOnKeyPressed(event -> {
			if (event.getCode() == KeyCode.ESCAPE) {
				FadeTransition ft = new FadeTransition(Duration.seconds(1), mb);
				if (!mb.isVisible()) {
					ft.setFromValue(0);
					ft.setToValue(1);
					ft.play();
					mb.setVisible(true);
				} else {
					ft.setFromValue(1);
					ft.setToValue(0);
					ft.setOnFinished(evt -> mb.setVisible(false));
					ft.play();

				}
			}
		});

		st.setScene(sc);
		st.show();
	}

	private void StartNotation(String path,Stage stage) {
		DataInputStream dos = null;
		try {
			dos = new DataInputStream(new FileInputStream("saves/" + path));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		double[] num = new double[500];
		int i = 0;
		int fl = 0;
		while (fl == 0) {
			i = 0;
			while (i < 500 && fl==0) {
				try {
					num[i] = dos.readDouble();
				} catch (IOException e) {
					fl = 1;
				}
				i++;
			}
			if (fl == 0 ) {
				notation.not(num);
			} 
		}
	}
}
