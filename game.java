package tanksMenu;

import java.io.DataOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import javafx.animation.AnimationTimer;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class game {
	private HashMap<KeyCode, Boolean> keys = new HashMap<>();
	public static ArrayList<Block> platforms = new ArrayList<>();
	public static ArrayList<Shooting> shoots = new ArrayList<>();
	public static ArrayList<Shooting> botshoots = new ArrayList<>();
	public static ArrayList<BotGameplay> bots = new ArrayList<>();
	int TANK_SIZE = 30;
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
	DataOutputStream dos;
	int botLifeStatus = 0;
	boolean AUTOMOD = false;
	int DIRECTION = 0;
	int botSHOOTTIMER = 110;
	public static final int BLOCK_SIZE = 40;

	public static final int BOT_SIZE = 38;
	int levelNumber = 0;
	int shoottimer = 0;
	Scene Gamescene;
	static Pane root = new Pane();
	Stage GameStage = new Stage();
	boolean SCENESTATUS = false;
	String filename = null;

	public void CloseMenu(Stage primary) // закрываем главное меню и выводим
											// окно игры
	{
		primary.close();
		this.TANK_SIZE = 30;
		player = new Character(imageView, TANK_SIZE);
		AUTOMOD = false;
		GameWindow(primary);
	}

	public void AutoGameMod(Stage primary) {
		primary.close();
		this.TANK_SIZE = 38;
		player = new Character(imageView, TANK_SIZE);
		AUTOMOD = true;
		GameWindow(primary);
	}

	public void GameWindow(Stage primary) // рисуем окно игры и добавляем на
											// него все элементы
	{

		Thread filecreating = new Thread(new Runnable() {

			@Override
			public void run() {
				CreateNewFile newFile = new CreateNewFile();
				filename = newFile.findLastFileName();
			}

		});
		filecreating.start();
		bots.add(bot1);
		bots.add(bot2);
		bots.add(bot3);
		root.setPrefSize(600, 600);
		root.getChildren().addAll(player, bot1, bot2, bot3);
		Thread tr = new Thread(new Runnable() {

			@Override
			public void run() {
				createShoots();
			}

		});
		Thread tr1 = new Thread(new Runnable() {

			@Override
			public void run() {
				changeLevel(levelNumber);
			}

		});

		bot1.setSpeed(1, 0);
		bot2.setSpeed(1, 0);
		bot3.setSpeed(1, 0);

		tr.start();
		tr1.start();
		try {
			tr1.join();
			tr.join();
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}

		Gamescene = new Scene(root);
		Gamescene.setFill(Color.BLACK);
		Gamescene.setOnKeyPressed(event -> keys.put(event.getCode(), true));
		Gamescene.setOnKeyReleased(event -> {
			keys.put(event.getCode(), false);
		});
		try {
			filecreating.join();
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
		try {
			dos = new DataOutputStream(new FileOutputStream(filename));
		} catch (FileNotFoundException e) {

			e.printStackTrace();
		}

		timer = new AnimationTimer() {
			@Override
			public void handle(long now) {
				update(primary);
				// bonus();
			}
		};
		timer.start();
		GameStage.setTitle("Game");
		GameStage.setScene(Gamescene);
		SCENESTATUS = true;

		GameStage.show();
	}

	public void update(Stage primary) {

		Thread tr = new Thread(new Runnable() {

			@Override
			public void run() {
				WriteInFile();
			}

		});
		tr.start();
		for (BotGameplay bg : bots) { // проверка Состояния ботов(живы или нет)
										// для мены уровня
			if (bg.LIVE == false)
				botLifeStatus++;
			if (bg.getBoundsInParent().intersects(player.getBoundsInParent())) {
				timer.stop();
				gameOverWind();
			}
		}
		Thread botmoving = new Thread(new Runnable() {

			@Override
			public void run() {
				BotMoving();
			}

		});
		botmoving.start();

		if (isPressed(KeyCode.N) || botLifeStatus == 3) {
			try {
				tr.join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			try {
				botmoving.join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			CreateNewLevel();
		}
		Thread playermoving = new Thread(new Runnable() {

			@Override
			public void run() {
				PlayerMoving();
			}

		});
		playermoving.start();

		Thread autoplayermod = new Thread(new Runnable() {

			@Override
			public void run() {
				AutoPlayerMod();
			}

		});
		autoplayermod.start();

		if (shoottimer <= 60 && shoottimer > 0) { // Установка Шуттаймеров
			shoottimer--;

		}
		if (botSHOOTTIMER <= 110 && botSHOOTTIMER > 0) {
			botSHOOTTIMER--;

		}
		IntersectCheck(); // проверка на пересечения пуль с игроками, с королём
							// и т.д.

		try {
			tr.join();
		} catch (InterruptedException e) {

			e.printStackTrace();
		}
		try {
			botmoving.join();
		} catch (InterruptedException e) {

			e.printStackTrace();
		}
		try {
			playermoving.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		try {
			autoplayermod.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		botLifeStatus = 0;
	}

	private void CreateNewLevel() {
		botLifeStatus = 0;
		levelNumber++;
		game.root.getChildren().clear();
		platforms.clear();
		if (levelNumber < 2) {
			changeLevel(levelNumber);
			game.root.getChildren().addAll(player, bot1, bot2, bot3);
			for (Shooting sht : shoots) {
				game.root.getChildren().add(sht);
			}
			for (Shooting sht : botshoots) {
				game.root.getChildren().add(sht);
			}
		}
		if (levelNumber == 3) {
			WinnerWind();
		}
	}

	public void IntersectCheck() {
		for (Shooting shts : this.botshoots) {

			if (shts.STATUS == false || shts.getBoundsInParent().intersects(player.getBoundsInParent())) {
				timer.stop();
				gameOverWind();

			}
			if (shts.getTranslateX() != 800 && shts.getTranslateY() != 800)
				shts.move();
		}
		for (Shooting shts : this.shoots) {
			for (BotGameplay bg : bots) {
				if (shts.getBoundsInParent().intersects(bg.getBoundsInParent())) {
					bg.LIVE = false;
					bg.animation.stop();
					bg.setTranslateX(800);
					bg.setTranslateY(800);
					shts.setTranslateX(800);
					shts.setTranslateY(800);
				}
			}
			if (shts.STATUS == false) {
				timer.stop();
				gameOverWind();

			}
			if (shts.getTranslateX() != 800 && shts.getTranslateY() != 800)
				shts.move();
		}
	}

	public void AutoPlayerMod() {
		if (AUTOMOD == true) {
			if (player.DIRECTION == 0) {
				player.animation.play();
				player.animation.setOffsetX(0);
			}
			if (player.DIRECTION == 1) {
				player.animation.play();
				player.animation.setOffsetX(64);
			}
			if (player.DIRECTION == 2) {
				player.animation.play();
				player.animation.setOffsetX(96);
			}
			if (player.DIRECTION == 3) {
				player.animation.play();
				player.animation.setOffsetX(32);
			}
			if (shoottimer == 0) {
				for (Shooting sht : shoots) {
					if (sht.getTranslateX() == 800 && sht.getTranslateY() == 800) {
						sht.setShoot(player.getTranslateX() + 4, player.getTranslateY() + 4, player.DIRECTION);
						break;
					}
				}
				shoottimer = 60;
			}
			player.AutoMooving();

		}
	}

	public void PlayerMoving() {
		if (AUTOMOD == false) {
			if (isPressed(KeyCode.SPACE)) {
				if (shoottimer == 0) {
					for (Shooting sht : shoots) {
						if (sht.getTranslateX() == 800 && sht.getTranslateY() == 800) {
							sht.setShoot(player.getTranslateX(), player.getTranslateY(), DIRECTION);
							break;
						}
					}
					shoottimer = 60;
				}

			}
			if (isPressed(KeyCode.UP)) {
				this.DIRECTION = 0;
				player.DIRECTION = 0;
				player.animation.play();
				player.animation.setOffsetX(0);
				player.moveY(-2);
			} else if (isPressed(KeyCode.DOWN)) {
				this.DIRECTION = 1;
				player.DIRECTION = 1;
				player.animation.play();
				player.animation.setOffsetX(64);
				player.moveY(2);
			} else if (isPressed(KeyCode.RIGHT)) {
				this.DIRECTION = 2;
				player.DIRECTION = 2;
				player.animation.play();
				player.animation.setOffsetX(96);
				player.moveX(2);
			} else if (isPressed(KeyCode.LEFT)) {
				this.DIRECTION = 3;
				player.DIRECTION = 3;
				player.animation.play();
				player.animation.setOffsetX(32);
				player.moveX(-2);
			} else {
				player.animation.stop();
			}
		}
	}

	public void BotMoving() {
		if (botSHOOTTIMER == 0) {
			for (BotGameplay bg : bots) {
				for (Shooting sht : botshoots) {
					if (sht.getTranslateX() == 800 && sht.getTranslateY() == 800) {
						sht.setShoot(bg.getTranslateX(), bg.getTranslateY(), bg.DIRECTION);
						break;
					}
				}

			}
			botSHOOTTIMER = 110;
		}
		if (bot1.LIVE == true) {
			bot1.move();
			bot1.animation.play();
			bot1.animation.setOffsetX(96);
		}
		if (bot2.LIVE == true) {
			bot2.move();
			bot2.animation.play();
			bot2.animation.setOffsetX(96);
		}
		if (bot3.LIVE == true) {
			bot3.move();
			bot3.animation.play();
			bot3.animation.setOffsetX(96);
		}
	}

	public boolean isPressed(KeyCode key) {
		return keys.getOrDefault(key, false);
	}

	@SuppressWarnings("unused")
	public void changeLevel(int levelnum) // функция смены уровня, изменяющая
											// текстуры
	{
		Block.GameMod = true;
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

	public void WinnerWind() // вывод сообщения после прохождения всех уровней
	{
		GameStage.close();

		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("WINNER");
		alert.setHeaderText(null);
		alert.setContentText("Поздравляю, Вы прошли игру!)");
		alert.show();

	}

	public void gameOverWind() // вывод сообщения после прохождения всех уровней
	{
		GameStage.close();
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("GAME OVER");
		alert.setHeaderText(null);
		alert.setContentText("YOU LOSE)");
		alert.show();

	}

	public void createShoots() {

		Shooting.GameStatus = true;
		for (int i = 0; i < 5; i++) {
			sh = new Shooting(800, 800);
			shoots.add(sh);
		}
		for (int i = 0; i < 15; i++) {
			sh = new Shooting(800, 800);
			botshoots.add(sh);

		}

	}

	public void WriteInFile() {
		double[] data = new double[500];
		WriteInArray(data);
		try {
			for (double db : data) {
				dos.writeDouble(db);
			}

		} catch (IOException ex) {

			System.out.println(ex.getMessage());
		}
	}

	public void WriteInArray(double[] data) {
		int i = 0;
		data[i] = levelNumber;
		i++;
		for (Block bc : platforms) {
			data[i] = bc.getTranslateX();
			i++;
			data[i] = bc.getTranslateY();
			i++;
		}
		for (; i < 451; i++)
			data[i] = 0;
		for (BotGameplay bg : bots) {
			data[i] = bg.getTranslateX();
			i++;
			data[i] = bg.getTranslateY();
			i++;
		}
		if (player.DIRECTION == 0)
			data[i] = 0.0;
		if (player.DIRECTION == 1)
			data[i] = 0.1;
		if (player.DIRECTION == 2)
			data[i] = 0.2;
		if (player.DIRECTION == 3)
			data[i] = 0.3;
		i++;
		data[i] = player.getTranslateX();
		i++;
		data[i] = player.getTranslateY();
		i++;
		for (Shooting sh : shoots) {
			data[i] = sh.getTranslateX();
			i++;
			data[i] = sh.getTranslateY();
			i++;
		}
		for (Shooting sh : botshoots) {
			data[i] = sh.getTranslateX();
			i++;
			data[i] = sh.getTranslateY();
			i++;
		}
	}
}
