package tanksMenu;

import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;

import javafx.animation.AnimationTimer;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;
/**
 * Класс воспроизведения игры по выбранному файлу
 * @author zork
 *
 */
public class Replay {
	public static ArrayList<Block> platforms = new ArrayList<>();
	public static ArrayList<Shooting> shoots = new ArrayList<>();
	public static ArrayList<Shooting> botshoots = new ArrayList<>();
	public static ArrayList<BotGameplay> bots = new ArrayList<>();
	int TANK_SIZE = 34;
	AnimationTimer timer;
	Image image = new Image(getClass().getResourceAsStream("1.png"));
	Image image1 = new Image(getClass().getResourceAsStream("3.png"));
	ImageView imageView = new ImageView(image);
	Character player;

	ImageView Bot1image = new ImageView(image1);
	BotGameplay bot1 = new BotGameplay(Bot1image, 0, 0);
	ImageView Bot2image = new ImageView(image1);
	BotGameplay bot2 = new BotGameplay(Bot2image, 80, 0);
	ImageView Bot3image = new ImageView(image1);
	BotGameplay bot3 = new BotGameplay(Bot3image, 160, 0);
	Shooting sh;
	int botLifeStatus = 0;
	boolean AUTOMOD = false;
	int DIRECTION = 0;
	int botSHOOTTIMER = 110;
	Timeline tl;
	public static final int BLOCK_SIZE = 40;

	public static final int BOT_SIZE = 38;
	double levelNumber = 0;
	int shoottimer = 0;
	static Pane root = new Pane();
	Stage GameStage = new Stage();
	private DataInputStream dos;
	String Filepath;
	Stage Main=new Stage();

	public void CloseMenu(Stage primary, String path) {
		root.getChildren().clear();
		shoots.clear();
		botshoots.clear();
		bots.clear();
		platforms.clear();
		primary.close();
		Filepath = path;
		this.TANK_SIZE = 30;
		player = new Character(imageView, TANK_SIZE);
		Pane newp=new Pane();
		root=newp;
		AUTOMOD = false;
		GameWindow(primary);
	}
/**
 * Метод создания поля для воспроизведения сохранения
 * @param primary
 */
	public void GameWindow(Stage primary) {
		bots.add(bot1);
		bots.add(bot2);
		bots.add(bot3);
		root.setPrefSize(600, 600);
		root.getChildren().addAll(player, bot1, bot2, bot3);
		
		createShoots();		
	
		Thread changelvl = new Thread(new Runnable() {

			@Override
			public void run() {
				changeLevel((int) levelNumber);
			}

		});
		changelvl.start();

		bot1.setSpeed(1, 0);
		bot2.setSpeed(1, 0);
		bot3.setSpeed(1, 0);
		
		try {
			changelvl.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		Scene Gamescene = new Scene(root, Color.BLACK);
		//Gamescene.setFill(Color.BLACK);

		GameStage.setTitle("Game");
		GameStage.setScene(Gamescene);
		GameStage.show();
		dos = null;
		tl = null;
		try {
			dos = new DataInputStream(new FileInputStream("saves/" + Filepath));
			tl = new Timeline(new KeyFrame(Duration.seconds(0.018), new EventHandler<ActionEvent>() {

				@Override
				public void handle(ActionEvent arg0) {
					ReadFromFile();

				}

			}));
			tl.setCycleCount(Timeline.INDEFINITE);
			tl.play();
		} catch (IOException ex) {
			System.out.println(ex.getMessage());
		}

	}
/**
 * Метод создания пуль
 */
	public void createShoots() {
		Shooting.GameStatus = false;

		for (int i = 0; i < 5; i++) {
			sh = new Shooting(800, 800);
			shoots.add(sh);
		}
		for (int i = 0; i < 15; i++) {
			sh = new Shooting(800, 800);
			botshoots.add(sh);
		}
	}
/**
 * Метод смены уровня
 * @param levelnum
 */
	@SuppressWarnings("unused")
	public void changeLevel(int levelnum) // функция смены уровня, изменяющая
	// текстуры
	{
		Block.GameMod = false;
		for (int i = 0; i < LevelData.levels[levelnum].length; i++) {
			String line = LevelData.levels[levelnum][i];
			for (int j = 0; j < line.length(); j++) {
				switch (line.charAt(j)) {
				case '0':
					break;
				case '1':
					Block brick = new Block(Block.BlockType.BRICK, j * BLOCK_SIZE, i * BLOCK_SIZE);
					break;
				case '2':
					Block stone = new Block(Block.BlockType.STONE, j * BLOCK_SIZE, i * BLOCK_SIZE);
					break;
				case '3':
					Block water = new Block(Block.BlockType.WATER, j * BLOCK_SIZE, i * BLOCK_SIZE);
					break;
				case '4':
					Block King = new Block(Block.BlockType.KING, j * BLOCK_SIZE, i * BLOCK_SIZE);
					break;
				}
			}

		}
		player.setTranslateX(198);
		player.setTranslateY(560);
		bot1.LIVE = true;
		bot1.setTranslateX(0);
		bot1.setTranslateY(0);
		bot1.move();
		bot1.animation.play();
		bot2.LIVE = true;
		bot2.setTranslateX(80);
		bot2.setTranslateY(0);
		bot2.move();
		bot2.animation.play();
		bot3.LIVE = true;
		bot3.setTranslateX(160);
		bot3.setTranslateY(0);
		bot3.move();
		bot3.animation.play();

	}
/**
 * Метод чтения из файла
 */
	public void ReadFromFile() {
		double[] num = new double[500];
		int i = 0;
		int fl = 0;
		if (fl == 0) {
			i = 0;
			while (i < 500) {
				try {
					num[i] = dos.readDouble();
				} catch (IOException e) {
					fl = 1;
				}

				i++;
			}
			if (fl == 0) {

				REPLAY(num);
			} else
			{
				tl.stop();
				GameStage.close();
			}
		}

	}
/**
 * Метод передвижения моделей в соответствии со считанным массивом из файла
 * @param data
 */
	public void REPLAY(double[] data) {
		int i = 0;
		if (data[i] > levelNumber) {
			levelNumber = data[i];
			botLifeStatus = 0;
			root.getChildren().clear();
			platforms.clear();
			if (levelNumber < 2) {
				changeLevel((int) levelNumber);
				root.getChildren().addAll(player, bot1, bot2, bot3);
				for (Shooting sht : shoots) {
					root.getChildren().add(sht);
				}
				for (Shooting sht : botshoots) {
					root.getChildren().add(sht);
				}
			}
		}
		i++;
		for (Block bc : platforms) {
			bc.setTranslateX(data[i]);
			i++;
			bc.setTranslateY(data[i]);
			i++;
		}
		i = 451;
		for (BotGameplay bg : bots) {
			bg.animation.play();
			bg.animation.setOffsetX(96);
			bg.setTranslateX(data[i]);
			i++;
			bg.setTranslateY(data[i]);
			i++;
		}
		if (data[i] == 0.0) {
			player.animation.play();
			player.animation.setOffsetX(0);
		}
		if (data[i] == 0.1) {
			player.animation.play();
			player.animation.setOffsetX(64);
		}
		if (data[i] == 0.2) {
			player.animation.play();
			player.animation.setOffsetX(96);
		}
		if (data[i] == 0.3) {
			player.animation.play();
			player.animation.setOffsetX(32);
		}
		i++;
		player.setTranslateX(data[i]);
		i++;
		player.setTranslateY(data[i]);
		i++;
		for (Shooting sh : shoots) {
			sh.setTranslateX(data[i]);
			i++;
			sh.setTranslateY(data[i]);
			i++;
		}
		for (Shooting sh : botshoots) {
			sh.setTranslateX(data[i]);
			i++;
			sh.setTranslateY(data[i]);
			i++;
		}
	}
}
