package tanksMenu;

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

public class MainWind {
	/**
	 * Ìåòîä ñîçäàíèÿ îêíà ÃËÀÂÍÎÅ ÌÅÍÞ
	 */
		public void start() {
			Stage GameStage=new Stage();
			InputStream is = null;
			try {
				is = Files.newInputStream(Paths.get("src/tanksMenu/main.jpg"));
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
			Pane menu = new Pane();
			vj.setFitHeight(500);
			vj.setFitWidth(500);
			menu.getChildren().add(vj);
			MenuMain newGame = new MenuMain("ÍÎÂÀß ÈÃÐÀ");
			MenuMain help = new MenuMain("ÀÂÒÎÌÀÒÈ×. ÐÅÆÈÌ");
			MenuMain sort = new MenuMain("CÎÐÒÈÐÎÂÊÀ");
			MenuMain stat=new MenuMain("ÑÒÀÒÈÑÒÈÊÀ");
			MenuMain repl = new MenuMain("Replay");
			MenuMain not=new MenuMain("Íîòàöèÿ");
			MenuMain exitGame = new MenuMain("ÂÛÕÎÄ");
			SubMenu mainMenu = new SubMenu(newGame, help,sort, stat,repl,not, exitGame);

			FileSort fs=new FileSort();
			MenuBox menuBox = new MenuBox(mainMenu);
			game gamewindow = new game();
			ChooseSave rpl = new ChooseSave();
			Statistic statist=new Statistic();
			FileContent fc=new FileContent();
			help.setOnMouseClicked(event -> gamewindow.AutoGameMod(GameStage));
			newGame.setOnMouseClicked(event -> gamewindow.CloseMenu(GameStage));
			sort.setOnMouseClicked(event->fs.CreateFileList(GameStage));
			stat.setOnMouseClicked(event->statist.ShowStat(GameStage));
			repl.setOnMouseClicked(event -> rpl.SaveList(GameStage));
			not.setOnMouseClicked(event->fc.ShowList(GameStage));
			exitGame.setOnMouseClicked(event -> System.exit(0));
			menu.getChildren().addAll(menuBox);
			Scene sc = new Scene(menu, 500, 500);
			sc.setOnKeyPressed(event -> {
				if (event.getCode() == KeyCode.ESCAPE) {
					FadeTransition ft = new FadeTransition(Duration.seconds(1), menuBox);
					if (!menuBox.isVisible()) {
						ft.setFromValue(0);
						ft.setToValue(1);
						ft.play();
						menuBox.setVisible(true);
					} else {
						ft.setFromValue(1);
						ft.setToValue(0);
						ft.setOnFinished(evt -> menuBox.setVisible(false));
						ft.play();

					}
				}
			});
			GameStage.setScene(sc);
			GameStage.setTitle("TANKS!");
			GameStage.show();

		}
}
