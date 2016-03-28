package tanksMenu;
import javafx.animation.*;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

 
public class main extends Application 
{
    public static void main(String[] args) 
    {
        launch(args);
    }
    
    public  void start(Stage primaryStage) 
    {
    	Image image=new Image(getClass().getResourceAsStream("main.jpg"));
    	ImageView vj=new ImageView(image);
    	Pane menu=new Pane();
    	vj.setFitHeight(500);
    	vj.setFitWidth(500);
    	menu.getChildren().add(vj);
    	MenuMain newGame = new MenuMain("ÍÎÂÀß ÈÃÐÀ");
    	MenuMain help = new MenuMain("ÏÎÌÎÙÜ");
    	MenuMain exitGame = new MenuMain("ÂÛÕÎÄ");
        SubMenu mainMenu = new SubMenu(
                newGame,help,exitGame
        );
       
       /* MenuMain NG1 = new MenuMain("ÎÄÈÍ ÈÃÐÎÊ");
        MenuMain NG2 = new MenuMain("2 ÈÃÐÎÊÀ");
        MenuMain NG4 = new MenuMain("ÍÀÇÀÄ");
        SubMenu newGameMenu = new SubMenu(
                NG1,NG2,NG4
        );*/
        MenuBox menuBox = new MenuBox(mainMenu);
        
        game gamewindow=new game();
        
        newGame.setOnMouseClicked(event->gamewindow.CloseMenu(primaryStage));
        exitGame.setOnMouseClicked(event-> System.exit(0));
        //NG4.setOnMouseClicked(event-> menuBox.setSubMenu(mainMenu));
        menu.getChildren().addAll(menuBox);
    	Scene sc=new Scene(menu,500,500);
    	sc.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ESCAPE) {
                FadeTransition ft = new FadeTransition(Duration.seconds(1),menuBox);
                if (!menuBox.isVisible()) {
                    ft.setFromValue(0);
                    ft.setToValue(1);
                    ft.play();
                    menuBox.setVisible(true);
                }
                else{
                    ft.setFromValue(1);
                    ft.setToValue(0);
                    ft.setOnFinished(evt ->   menuBox.setVisible(false));
                    ft.play();

                }
            }
        });
    	primaryStage.setScene(sc);
    	primaryStage.setTitle("TANKS!");
    	primaryStage.show();
        
    }
}
class MenuMain extends StackPane
{
	public MenuMain(String name){
		Rectangle bg=new Rectangle(200,20);
		bg.setFill(javafx.scene.paint.Color.WHITE);
		bg.setOpacity(0.5);
		
		Text text=new Text(name);
		text.setFill(javafx.scene.paint.Color.WHITE);
		text.setFont(Font.font("Arial",FontWeight.BOLD,14));
		
		this.setAlignment(Pos.CENTER);
		this.getChildren().addAll(bg,text);
		FillTransition tr=new FillTransition(Duration.seconds(0.5),bg);
		this.setOnMouseEntered(event->{
			tr.setFromValue(javafx.scene.paint.Color.DARKGRAY);
			tr.setToValue(javafx.scene.paint.Color.DARKGOLDENROD);
			tr.setCycleCount(Animation.INDEFINITE);
			tr.setAutoReverse(true);
			tr.play();
		});
		this.setOnMouseExited(event->{
			tr.stop();
			bg.setFill(javafx.scene.paint.Color.WHITE);
		});
		
	}	
}
class MenuBox extends Pane
{
	static SubMenu sub;
	public MenuBox(SubMenu sub)
	{
		MenuBox.sub=sub;
		setVisible(false);
		Rectangle bg = new Rectangle(500,500,javafx.scene.paint.Color.LIGHTBLUE);
        bg.setOpacity(0.4);
        getChildren().addAll(bg, sub);
    }
    public void setSubMenu(SubMenu subMenu)
    {
        getChildren().remove(MenuBox.sub);
        MenuBox.sub = subMenu;
        getChildren().add(MenuBox.sub);
    }
	}
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