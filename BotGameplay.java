package tanksMenu;

import java.util.Random;

import javafx.geometry.Rectangle2D;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

public class BotGameplay extends Pane {
	boolean STATUS = true;
	ImageView bot_image;
	int count = 3;
	int columns = 3;
	int offsetX = 96;
	int offsetY = 128;
	int width = 32;
	int height = 32;
	int score = 0;
	int DIRECTION = 0;
	boolean LIVE = true;
	private boolean ready = true;
	private int speedX;
	private int speedY;
	Random rand = new Random();
	Rectangle removeRect = null;
	SpriteAnimation animation;

	public BotGameplay(ImageView imageView, int x, int y) {
		this.bot_image = imageView;
		this.bot_image.setViewport(new Rectangle2D(offsetX, offsetY, width, height));
		this.bot_image.setFitHeight(38);
		this.bot_image.setFitWidth(38);
		animation = new SpriteAnimation(bot_image, Duration.millis(200), count, columns, offsetX, offsetY, width,
				height);
		getChildren().addAll(bot_image);
		this.setTranslateX(x);
		this.setTranslateY(y);
	}

	public void moveX(int value) {
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

	public void moveY(int value) {
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
		if (rand <= 0.10 && rand >= 0)
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

	public void move() {
		if (STATUS == true) {
			if (speedX != 0)
				moveX(speedX);
			if (speedY != 0)
				moveY(speedY);
			return;
		} else
			return;

	}

	public void setSpeed(int x, int y) {
		speedX = x;
		speedY = y;
	}
}