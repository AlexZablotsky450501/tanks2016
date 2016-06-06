package tanksMenu;

import javafx.geometry.Rectangle2D;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;
/**
 * Класс, создающий модель игрока и управляющий передвижением по экрану
 * @author zork
 *
 */
public class Character extends Pane {

	ImageView imageView;
	int count = 2;
	int columns = 8;
	int offsetX = 0;
	int offsetY = 0;
	int width = 16;
	int height = 15;
	int score = 0;
	int DIRECTION = 0;
	int TANK_SIZE;
	private int speedX = 1;
	private int speedY = 0;
	Rectangle removeRect = null;
	SpriteAnimation animation;
/**
 * Метод отрисовки модели игрока
 * @param imageView
 * @param TANK_SIZE1
 */
	public Character(ImageView imageView, int TANK_SIZE1) {
		this.TANK_SIZE = TANK_SIZE1;
		this.imageView = imageView;
		this.imageView.setViewport(new Rectangle2D(offsetX, offsetY, width, height));
		this.imageView.setFitHeight(TANK_SIZE1);
		this.imageView.setFitWidth(TANK_SIZE1);
		animation = new SpriteAnimation(imageView, Duration.millis(200), count, columns, offsetX, offsetY, width,
				height);
		getChildren().addAll(imageView);
		this.setTranslateX(198);
		this.setTranslateY(560);
	}
/**
 * Метод передвижения игрока по оси Х
 * @param value
 */
	public void moveX(int value) {
		boolean movingRight = value > 0;
		for (int i = 0; i < Math.abs(value); i++) {
			for (Block platform : game.platforms) {
				if (this.getBoundsInParent().intersects(platform.getBoundsInParent())) {
					if (movingRight) {
						if (this.getTranslateX() + TANK_SIZE == platform.getTranslateX()) {
							this.setTranslateX(this.getTranslateX() - 1);
							return;
						}
					} else {
						if (this.getTranslateX() == platform.getTranslateX() + game.BLOCK_SIZE) {
							this.setTranslateX(this.getTranslateX() + 1);
							return;
						}
					}
				}
			}
			if (this.getTranslateX() < 570 && movingRight)
				this.setTranslateX(this.getTranslateX() + 1);
			if (this.getTranslateX() > 0 && !movingRight)
				this.setTranslateX(this.getTranslateX() - 1);
		}
	}
/**
 * Функция передвижения по оси У
 * @param value
 */
	public void moveY(int value) {
		boolean movingDown = value > 0;
		for (int i = 0; i < Math.abs(value); i++) {
			for (Block platform : game.platforms) {
				if (getBoundsInParent().intersects(platform.getBoundsInParent()) && this.getTranslateY() <= 570) {
					if (movingDown) {
						if (this.getTranslateY() + TANK_SIZE == platform.getTranslateY()) {
							this.setTranslateY(this.getTranslateY() - 1);
							return;
						}
					} else {
						if ((this.getTranslateY() == platform.getTranslateY() + game.BLOCK_SIZE)) {
							this.setTranslateY(this.getTranslateY() + 1);
							return;
						}
					}
				}
			}
			if (this.getTranslateY() < 570 && movingDown)
				this.setTranslateY(this.getTranslateY() + 1);
			if (this.getTranslateY() > 0 && !movingDown)
				this.setTranslateY(this.getTranslateY() - 1);
		}
	}
/**
 * Функция автопередвижения модели игрока при автоматическом редиме игры
 */
	public void AutoMooving() {
		if (speedX != 0)
			AutomoveX(speedX);
		if (speedY != 0)
			AutomoveY(speedY);
		return;
	}
/**
 * Функция передвижения игрока по оси Х при автоматическом режиме игры
 * @param value
 */
	public void AutomoveX(int value) {
		boolean movingRight = value > 0;
		if (movingRight)
			DIRECTION = 2;
		if (!movingRight)
			DIRECTION = 3;
		int counter = 0;
		for (Block platform : game.platforms) {
			if (this.getBoundsInParent().intersects(platform.getBoundsInParent())) {
				if (movingRight) {
					if (this.getTranslateX() + game.BOT_SIZE == platform.getTranslateX()) {
						this.setTranslateX(this.getTranslateX() - 2);
						counter = 1;

					}
				} else {
					if (this.getTranslateX() == platform.getTranslateX() + game.BLOCK_SIZE) {
						this.setTranslateX(this.getTranslateX() + 2);
						counter = 1;
					}
				}
			}
		}
		double rand = Math.random();
		if (rand <= 0.10 && rand >= 0)
			counter = 1;

		if (this.getTranslateX() < 561 && movingRight)
			this.setTranslateX(this.getTranslateX() + 1);
		if (this.getTranslateX() > 1 && !movingRight)
			this.setTranslateX(this.getTranslateX() - 1);
		if (this.getTranslateX() == 0 || this.getTranslateX() == 561)
			counter = 1;

		if ((this.getTranslateX()) % game.BLOCK_SIZE <= 1 && this.getTranslateX() % game.BLOCK_SIZE >= 0
				&& (this.getTranslateY()) % game.BLOCK_SIZE >= 0 && (this.getTranslateY()) % game.BLOCK_SIZE <= 1
				&& counter == 1) {
			int count = 0;
			rand = Math.random();

			if (rand >= 0 && rand <= 0.33) {

				for (Block platform : game.platforms) {
					if (this.getBoundsInParent().intersects(platform.getBoundsInParent())) {
						if ((this.getTranslateY() == platform.getTranslateY() + game.BLOCK_SIZE)) {
							count = 1;
						}
					}
				}
				if (count == 0 && this.getTranslateY() > 0) {
					speedY = -Math.abs(speedX);
					speedX = 0;
					return;
				}
			}
			if (rand >= 0.33 && rand <= 0.66) {
				count = 0;
				for (Block platform : game.platforms) {
					if (this.getBoundsInParent().intersects(platform.getBoundsInParent())) {
						if ((this.getTranslateY() + game.BOT_SIZE == platform.getTranslateY())) {
							count = 1;
						}
					}
				}
				if (count == 0 && this.getTranslateY() < 562) {

					speedY = Math.abs(speedX);
					speedX = 0;
					return;
				}
			}
			if (rand >= 0.66 && rand <= 0.69 && this.getTranslateX() > 0 && this.getTranslateX() < 562) {

				speedX = -speedX;
				speedY = 0;
				return;
			} else
				return;
		}
	}
/**
 * Функция перемещения по оси У при автоматическом режиме игры
 * @param value
 */
	public void AutomoveY(int value) {
		boolean movingDown = value > 0;
		if (movingDown)
			DIRECTION = 1;
		if (!movingDown)
			DIRECTION = 0;
		int counter = 0;
		for (Block platform : game.platforms) {
			if (this.getBoundsInParent().intersects(platform.getBoundsInParent())) {
				if (movingDown) {
					if (this.getTranslateY() + game.BOT_SIZE == platform.getTranslateY()) {
						this.setTranslateY(this.getTranslateY() - 2);
						counter = 1;
					}
				} else {
					if ((this.getTranslateY() == platform.getTranslateY() + game.BLOCK_SIZE)) {
						this.setTranslateY(this.getTranslateY() + 2);
						counter = 1;
					}
				}
			}
		}
		double rand = Math.random();
		if (rand <= 0.20 && rand >= 0)
			counter = 1;

		if (this.getTranslateY() < 561 && movingDown)
			this.setTranslateY(this.getTranslateY() + 1);
		if (this.getTranslateY() > 1 && !movingDown)
			this.setTranslateY(this.getTranslateY() - 1);

		if (this.getTranslateY() == 0 || this.getTranslateY() == 561)
			counter = 1;

		if ((this.getTranslateX()) % game.BLOCK_SIZE <= 1 && this.getTranslateX() % game.BLOCK_SIZE >= 0
				&& (this.getTranslateY()) % game.BLOCK_SIZE >= 0 && (this.getTranslateY()) % game.BLOCK_SIZE <= 1
				&& counter == 1) {
			int count = 0;
			rand = Math.random();

			if (rand >= 0 && rand <= 0.33) {

				for (Block platform : game.platforms) {
					if (this.getBoundsInParent().intersects(platform.getBoundsInParent())) {
						if ((this.getTranslateX() == platform.getTranslateX() + game.BLOCK_SIZE)) {
							count = 1;
						}
					}
				}
				if (count == 0 && this.getTranslateX() > 0) {

					speedX = -Math.abs(speedY);
					speedY = 0;
					return;
				}
			}
			if (rand >= 0.33 && rand <= 0.66) {
				count = 0;
				for (Block platform : game.platforms) {
					if (this.getBoundsInParent().intersects(platform.getBoundsInParent())) {
						if ((this.getTranslateX() + game.BOT_SIZE == platform.getTranslateX())) {
							count = 1;
						}
					}
				}
				if (count == 0 && this.getTranslateX() < 562) {

					speedX = Math.abs(speedY);
					speedY = 0;
					return;
				}
			}
			if (rand >= 0.66 && rand <= 0.69 && this.getTranslateY() > 0 && this.getTranslateY() < 562) {
				speedY = -speedY;
				speedX = 0;
				return;
			}
		}
	}
}
