package tanksMenu;

import java.io.File;
import java.util.ArrayList;

import javafx.animation.FadeTransition;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.util.Duration;

public class ChooseSave {
	Stage st = new Stage();
	Stage Primary=new Stage();
	public void SaveList(Stage primary) {
		Primary=primary;
		primary.close();
		File[] fileList;
		File directory = new File("saves/");
		fileList = directory.listFiles();
		String[] filenames = new String[fileList.length];
		int i = 0;
		for (File fl : fileList) {
			filenames[i] = fl.getName();

			i++;
		}

		SavesWnd(filenames);
	}

	private void SavesWnd(String[] fileList) {
		MenuMain[] saveMenu = new MenuMain[fileList.length + 1];
		int i = 0;
		for (String fl : fileList) {
			MenuMain Mm = new MenuMain(fl);
			Mm.setOnMouseClicked(event->StartReplay(Mm.PATH,st));
			saveMenu[i] = Mm;
			i++;
		}
		String back = "ÍÀÇÀÄ";
		MenuMain ba = new MenuMain(back);
		ba.setOnMouseClicked(event->BackToMain(st,Primary));
		saveMenu[i] = ba;
		// System.out.println(Mm.PATH);
		Image image = new Image(getClass().getResourceAsStream("ESC.jpg"));
		ImageView vj = new ImageView(image);
		vj.setFitHeight(500);
		vj.setFitWidth(500);
		SubMenu sb = new SubMenu(saveMenu);
		MenuBox mb = new MenuBox(sb);
		Pane saves = new Pane();
		saves.getChildren().add(vj);
		saves.getChildren().add(mb);
		Scene sc = new Scene(saves, 500, 500);
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
	private void BackToMain(Stage ST,Stage PRIMARY)
	{
		st.close();
		PRIMARY.show();
	}
	private void StartReplay(String Path, Stage stage)
	{
		Replay newRepl=new Replay();
		newRepl.CloseMenu(Primary,stage, Path);
	}
}
