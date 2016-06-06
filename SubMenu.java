package tanksMenu;

import javafx.scene.layout.VBox;
/**
 * ����� ����������� "������" � ����� ����
 * @author zork
 *
 */
class SubMenu extends VBox
{
	public SubMenu(MenuMain...items)
	{
        setSpacing(15);
        setTranslateY(100);
        setTranslateX(50);
        for(MenuMain item : items)
        {
            getChildren().addAll(item);
        }
    }
}