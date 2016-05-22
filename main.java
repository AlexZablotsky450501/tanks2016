package tanksMenu;

import javafx.animation.FadeTransition;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.util.Duration;

public class main extends Application {
	public static void main(String[] args) {
		launch(args);
	}

	public void start(Stage primaryStage) {
		Image image = new Image(getClass().getResourceAsStream("main.jpg"));
		ImageView vj = new ImageView(image);
		Pane menu = new Pane();
		vj.setFitHeight(500);
		vj.setFitWidth(500);
		menu.getChildren().add(vj);
		MenuMain newGame = new MenuMain("ÍÎÂÀß ÈÃÐÀ");
		MenuMain help = new MenuMain("ÀÂÒÎÌÀÒÈ×. ÐÅÆÈÌ");

		MenuMain repl = new MenuMain("Replay");
		MenuMain exitGame = new MenuMain("ÂÛÕÎÄ");
		SubMenu mainMenu = new SubMenu(newGame, help, repl, exitGame);

		/*
		 * MenuMain NG1 = new MenuMain("ÎÄÈÍ ÈÃÐÎÊ"); MenuMain NG2 = new
		 * MenuMain("2 ÈÃÐÎÊÀ"); MenuMain NG4 = new MenuMain("ÍÀÇÀÄ"); SubMenu
		 * newGameMenu = new SubMenu( NG1,NG2,NG4 );
		 */
		MenuBox menuBox = new MenuBox(mainMenu);

		game gamewindow = new game();
		// Replay rpl=new Replay();
		ChooseSave rpl = new ChooseSave();
		help.setOnMouseClicked(event -> gamewindow.AutoGameMod(primaryStage));
		newGame.setOnMouseClicked(event -> gamewindow.CloseMenu(primaryStage));
		// repl.setOnMouseClicked(event->rpl.CloseMenu(primaryStage));
		repl.setOnMouseClicked(event -> rpl.SaveList(primaryStage));
		exitGame.setOnMouseClicked(event -> System.exit(0));
		// NG4.setOnMouseClicked(event-> menuBox.setSubMenu(mainMenu));
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
		primaryStage.setScene(sc);
		primaryStage.setTitle("TANKS!");
		primaryStage.show();

	}
}
