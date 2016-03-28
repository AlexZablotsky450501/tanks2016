package tanksMenu;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Optional;

public class game 
{
	private HashMap<KeyCode, Boolean> keys = new HashMap<>();
	public static ArrayList<Block> platforms = new ArrayList<>();
	
    Image image = new Image(getClass().getResourceAsStream("1.png"));
    ImageView imageView = new ImageView(image);
    Character player = new Character(imageView);
    static Pane root = new Pane();
    
    public static final int BLOCK_SIZE = 40;
    public static final int TANK_SIZE = 30;
    int levelNumber = 0;
    Stage GameStage=new Stage();
    
    
public void CloseMenu(Stage primary)                     //закрываем главное меню и выводим окно игры
	{
		primary.close();
		GameWindow(primary);
	}
public void GameWindow(Stage primary)                     //рисуем окно игры и добавляем на него все элементы
{   
	root.setPrefSize(600,600);
    root.getChildren().addAll(player);

    changeLevel(levelNumber);
    
    Scene Gamescene = new Scene(root);
    Gamescene.setFill(Color.BLACK);
    Gamescene.setOnKeyPressed(event->keys.put(event.getCode(),true));
    Gamescene.setOnKeyReleased(event-> { keys.put(event.getCode(), false);});
    AnimationTimer timer = new AnimationTimer() 
    {
        @Override
        public void handle(long now) 
        {
            update(primary);
 //           bonus();
        }
    };
    timer.start();
    GameStage.setTitle("Game");
    GameStage.setScene(Gamescene);
    GameStage.show();
}
/*public void bonus()
{
    int random = (int)Math.floor(Math.random()*100);
    int x = (int)Math.floor(Math.random()*600);
    int y = (int)Math.floor(Math.random()*600);
    if(random == 5){
        Rectangle rect = new Rectangle(20,20,Color.RED);
        rect.setX(x);
        rect.setY(y);
        bonuses.add(rect);
        root.getChildren().addAll(rect);
    }
}*/
public void update(Stage primary)                          //каждый момент времени проверяет нажатые клавиши
{
	if(isPressed(KeyCode.N))
	{
		levelNumber++;
		game.root.getChildren().clear();
		platforms.clear();
		if(levelNumber<2)
		{
		changeLevel(levelNumber);
		game.root.getChildren().add(player);
		player.setTranslateX(198);
        player.setTranslateY(560);
		}
		if(levelNumber==3)
		{
			WinnerWind(primary);
		}
	}
    if (isPressed(KeyCode.UP)) {
        player.animation.play();
        player.animation.setOffsetX(0);
        player.moveY(-2);
    } else if (isPressed(KeyCode.DOWN)) {
        player.animation.play();
        player.animation.setOffsetX(64);
        player.moveY(2);
    } else if (isPressed(KeyCode.RIGHT)) {
        player.animation.play();
        player.animation.setOffsetX(96);
        player.moveX(2);
    } else if (isPressed(KeyCode.LEFT)) {
        player.animation.play();
        player.animation.setOffsetX(32);
        player.moveX(-2);
    }
    else{
        player.animation.stop();
    }
}
public boolean isPressed(KeyCode key) 
{
    return keys.getOrDefault(key, false);
}
public void changeLevel(int levelnum)                               //функция смены уровня, изменяющая текстуры
{
	for(int i = 0; i < LevelData.levels[levelnum].length; i++)
    {
        String line = LevelData.levels[levelnum][i];
        for(int j = 0; j < line.length();j++){
            switch (line.charAt(j)){
                case '0':
                    break;
                case '1':
                    Block brick = new Block(Block.BlockType.BRICK,j*BLOCK_SIZE,i*BLOCK_SIZE);
                    break;
                case '2':
                    Block stone = new Block(Block.BlockType.STONE,j * BLOCK_SIZE, i * BLOCK_SIZE);
                    break;
                case '3':
                    Block water = new Block(Block.BlockType.WATER,j * BLOCK_SIZE, i * BLOCK_SIZE);
                    break;
                case '4':
                    Block King = new Block(Block.BlockType.KING,j * BLOCK_SIZE, i * BLOCK_SIZE);
                    break;
            }
        }

    }
    
}
public void WinnerWind(Stage primary)             // вывод сообщения после прохождения всех уровней
{    
	GameStage.close();
	
	Alert alert = new Alert(AlertType.INFORMATION);
	alert.setTitle("WINNER");
	alert.setHeaderText(null);
	alert.setContentText("Поздравляю, Вы прошли игру!)");
	alert.show();
	
}
}
