package tanksMenu;

import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;

class MenuBox extends Pane {
	static SubMenu sub;

	public MenuBox(SubMenu sub) {
		MenuBox.sub = sub;
		setVisible(false);
		Rectangle bg = new Rectangle(500, 500, javafx.scene.paint.Color.LIGHTBLUE);
		bg.setOpacity(0.9);
		getChildren().addAll(bg, sub);
	}

	public void setSubMenu(SubMenu subMenu) {
		getChildren().remove(MenuBox.sub);
		MenuBox.sub = subMenu;
		getChildren().add(MenuBox.sub);
	}
}