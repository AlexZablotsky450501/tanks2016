package tanksMenu;

import javafx.animation.Animation;
import javafx.animation.FillTransition;
import javafx.geometry.Pos;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.util.Duration;

class MenuMain extends StackPane {
		String PATH=null;
	public MenuMain(String name) {
		PATH=name;
		Rectangle bg = new Rectangle(200, 20);
		bg.setFill(javafx.scene.paint.Color.WHITE);
		bg.setOpacity(0.8);

		Text text = new Text(name);
		text.setFill(javafx.scene.paint.Color.GREEN);
		text.setFont(Font.font("Arial", FontWeight.BOLD, 14));

		this.setAlignment(Pos.CENTER);
		this.getChildren().addAll(bg, text);
		FillTransition tr = new FillTransition(Duration.seconds(0.5), bg);
		this.setOnMouseEntered(event -> {
			tr.setFromValue(javafx.scene.paint.Color.DARKGRAY);
			tr.setToValue(javafx.scene.paint.Color.DARKGOLDENROD);
			tr.setCycleCount(Animation.INDEFINITE);
			tr.setAutoReverse(true);
			tr.play();
		});
		this.setOnMouseExited(event -> {
			tr.stop();
			bg.setFill(javafx.scene.paint.Color.WHITE);
		});

	}
}