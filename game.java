package tanksMenu;

import java.io.DataOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;

import javafx.animation.AnimationTimer;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;


/**
 * Основной класс игры, создающий игровое поле со всеми моделями
 * 
 * @author zork
 *
 */
public class game {
	private HashMap<KeyCode, Boolean> keys = new HashMap<>();
	public static ArrayList<Block> platforms = new ArrayList<>();
	public static ArrayList<Shooting> shoots = new ArrayList<>();
	public static ArrayList<Shooting> botshoots = new ArrayList<>();
	public static ArrayList<BotGameplay> bots = new ArrayList<>();
	int TANK_SIZE = 30;
	AnimationTimer timer;
	ImageView imageView;
	ImageView imageView1;
	ImageView imageView2;
	ImageView imageView3;
	Character player;
	BotGameplay bot1;
	BotGameplay bot2;
	BotGameplay bot3;
	Shooting sh;
	DataOutputStream dos;
	Thread tr;
	Thread botmoving;
	Thread playermoving;
	Thread autoplayermod;
	int botLifeStatus = 0;
	boolean AUTOMOD = false;
	int DIRECTION = 0;
	int BotShootTimer = 110;
	public static final int BLOCK_SIZE = 40;
	public static final int BOT_SIZE = 38;
	int levelNumber = 0;
	int shoottimer = 0;
	double GameTimer=0;

	static Pane root = new Pane();
	Stage GameStage;
	boolean SCENESTATUS = false;
	String filename = null;
	Stage Primary;
	Scene Gamescene;

	/**
	 * Метод закрытия главного меню и создания игрового поля для режима
	 * "игрок+компьютер"
	 * 
	 * @param primary
	 */
	public void CloseMenu(Stage primary) // закрываем главное меню и выводим
											// окно игры
	{
		Primary = primary;
		primary.close();
		this.TANK_SIZE = 30;
		AUTOMOD = false;
		Pane newp = new Pane();
		root = newp;
		GameWindow();
	}

	/**
	 * Метод закрытия главного меню и создания игрового поля для режима
	 * "компьютер+компьютер"
	 * 
	 * @param primary
	 */
	public void AutoGameMod(Stage primary) {

		Primary = primary;
		primary.close();
		this.TANK_SIZE = 38;
		AUTOMOD = true;
		Pane newp = new Pane();
		root = newp;
		GameWindow();
	}

	/**
	 * метод отрисовки игрового поля и добавления на него всех моделей
	 * 
	 * @param primary
	 */
	public void GameWindow() // рисуем окно игры и добавляем на
								// него все элементы
	{
		CreateModels();
		GameStage = new Stage();
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
		CreateThreads();
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
		Scene Gamescene = new Scene(root, Color.BLACK);
		Gamescene.setOnKeyPressed(event -> keys.put(event.getCode(), true));
		Gamescene.setOnKeyReleased(event -> {
			keys.put(event.getCode(), false);
		});
		GameStage.setTitle("Game");
		GameStage.setScene(Gamescene);
		SCENESTATUS = true;

		GameStage.show();
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
				update();
			}
		};
		timer.start();

	}

	private void CreateThreads() {
		
		tr = new Thread(new Runnable() {
			@Override
			public void run() {
				WriteInFile();
			}
		});
		botmoving = new Thread(new Runnable() {
			@Override
			public void run() {
				BotMoving();
			}
		});
		playermoving = new Thread(new Runnable() {
			@Override
			public void run() {
				PlayerMoving();
			}
		});
		autoplayermod = new Thread(new Runnable() {
			@Override
			public void run() {
				AutoPlayerMod();
			}
		});
	}

	private void CreateModels() {
		InputStream is = null;
		try {
			is = Files.newInputStream(Paths.get("src/tanksMenu/1.png"));
		} catch (IOException e2) {
			e2.printStackTrace();
		}
		Image playerIm = new Image(is);
		try {
			is.close();
		} catch (IOException e2) {
			e2.printStackTrace();
		}
		imageView = new ImageView(playerIm);
		player = new Character(imageView, TANK_SIZE);
		InputStream is1 = null;
		try {
			is1 = Files.newInputStream(Paths.get("src/tanksMenu/3.png"));
		} catch (IOException e2) {
			e2.printStackTrace();
		}
		Image botIm = new Image(is1);
		try {
			is1.close();
		} catch (IOException e2) {
			e2.printStackTrace();
		}
		imageView1 = new ImageView(botIm);
		imageView2 = new ImageView(botIm);
		imageView3 = new ImageView(botIm);
		bot1 = new BotGameplay(imageView1, 0, 0);
		bot2 = new BotGameplay(imageView2, 80, 0);
		bot3 = new BotGameplay(imageView3, 160, 0);
	}

	/**
	 * Метод обновления игрового поля, вызывающийся каждый момент срабатывания
	 * системного таймера В нём производится запись в файл, проверки на смену
	 * уровней, состояния жизни ботов, а также вызов методов перевижения моделей
	 * по игровому полю.
	 * 
	 * @param primary
	 */
	public void update() {
		GameTimer+=1;
		tr.run();
		for (BotGameplay bg : bots) { // проверка Состояния ботов(живы или нет)
										// для мены уровня
			if (bg.LIVE == false)
				botLifeStatus++;
			if (bg.getBoundsInParent().intersects(player.getBoundsInParent())) {
				timer.stop();
				gameOverWind();
				return;
			}
		}
		
		botmoving.run();

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

		
		playermoving.run();
		
		
		autoplayermod.run();

		if (shoottimer <= 60 && shoottimer > 0) { // Установка Шуттаймеров
			shoottimer--;

		}
		if (BotShootTimer <= 110 && BotShootTimer > 0) {
			BotShootTimer--;

		}
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
		IntersectCheck();
		botLifeStatus = 0;
	}

	/**
	 * Метод, вызывающий смену уровня и заносящий на панель новые модели
	 */
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
			timer.stop();
			WinnerWind();
			return;
		}
	}

	/**
	 * Метод, выполняющий проверки на пересечения пуль с игроком и ботами
	 */
	public void IntersectCheck() {
		for (Shooting shts : this.botshoots) {

			if (shts.STATUS == false || shts.getBoundsInParent().intersects(player.getBoundsInParent())) {
				timer.stop();

				gameOverWind();
				return;

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
				return;

			}
			if (shts.getTranslateX() != 800 && shts.getTranslateY() != 800)
				shts.move();
		}
	}

	/**
	 * Метод передвижения игрока при автоматическом режиме игры
	 */
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

	/**
	 * Метод передвижения игрока в режиме "Игрок+компьютер"
	 */
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

	/**
	 * Метод передвижения ботов по игровому полю
	 */
	public void BotMoving() {
		if (BotShootTimer == 0) {
			for (BotGameplay bg : bots) {
				for (Shooting sht : botshoots) {
					if (sht.getTranslateX() == 800 && sht.getTranslateY() == 800) {
						sht.setShoot(bg.getTranslateX(), bg.getTranslateY(), bg.DIRECTION);
						break;
					}
				}

			}
			BotShootTimer = 110;
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

	/**
	 * метод считывания кода нажатой клавиши
	 * 
	 * @param key
	 * @return
	 */
	public boolean isPressed(KeyCode key) {
		return keys.getOrDefault(key, false);
	}

	/**
	 * Метод, создающий новые модели текстур
	 * 
	 * @param levelnum
	 */
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

	/**
	 * Вывод сообщения при прохождении игры
	 */
	public void WinnerWind() // вывод сообщения после прохождения всех уровней
	{
		shoots.clear();
		botshoots.clear();
		bots.clear();
		platforms.clear();
		GameStage.close();
		this.WriteTimerAndStatus(GameTimer, 1);
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("WINNER");
		alert.setHeaderText(null);
		alert.setContentText("Поздравляю, Вы прошли игру!)");
		alert.show();

	}

	/**
	 * Вывод сообщения при проигрыше
	 */
	public void gameOverWind() // вывод сообщения после прохождения всех уровней
	{
		shoots.clear();
		botshoots.clear();
		bots.clear();
		platforms.clear();
		GameStage.close();
		this.WriteTimerAndStatus(GameTimer, 0);
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("GAME OVER");
		alert.setHeaderText(null);
		alert.setContentText("YOU LOSE)");
		alert.show();
	}

	/**
	 * Создание объектов пуль
	 */
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
	/**
	 * Метод записи в файл
	 */
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

	/**
	 * Метод создания массива для записи в файл
	 * 
	 * @param data
	 */
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
	private void WriteTimerAndStatus(double time, double Status)
	{
		try {
			dos.writeDouble(time);
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			dos.writeDouble(Status);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
