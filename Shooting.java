package tanksMenu;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;

import javafx.geometry.Rectangle2D;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
/**
 * �����, ��������� ������ ���� � ����������� �� �������������
 * @author zork
 *
 */
public class Shooting extends Pane {
	public static boolean GameStatus = true;
	boolean STATUS = true;
	int napr;
/**
 * ����� �������� �������� ����
 * @param x
 * @param y
 */
	public Shooting(int x, int y) {
		InputStream is = null;
		try {
			is = Files.newInputStream(Paths.get("src/tanksMenu/shoot.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}

		Image sh = new Image(is);
		sh.cancel();
		try {
			is.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		ImageView shoot=new ImageView(sh);
		shoot.setViewport(new Rectangle2D(210, 1110, 90, 90));
		shoot.setFitHeight(20);
		shoot.setFitWidth(20);

		this.setTranslateX(x);
		this.setTranslateY(y);
		if (GameStatus == true) {
			getChildren().addAll(shoot);
			game.root.getChildren().addAll(this);
		} else {
			getChildren().addAll(shoot);
			Replay.root.getChildren().addAll(this);
		}
	}
/**
 * ����� ��������� ��������� ������� ��� ����������� �����
 * @param x
 * @param y
 * @param napr
 */
	public void setShoot(double x, double y, int napr) {
		if (napr == 0) {
			this.napr = napr;
			this.setTranslateX(x + 4);
			this.setTranslateY(y - 10);
		}
		if (napr == 1) {
			this.napr = napr;
			this.setTranslateX(x + 4);
			this.setTranslateY(y + 24);
		}
		if (napr == 2) {
			this.napr = napr;
			this.setTranslateX(x + 22);
			this.setTranslateY(y + 5);
		}

		if (napr == 3) {
			this.napr = napr;
			this.setTranslateX(x - 10);
			this.setTranslateY(y + 5);
		}
	}
/**
 * ����� ������������ ����
 */
	public void move() {
		if (napr == 0) {
			moveY(-3);
		}
		if (napr == 1) {
			moveY(3);
		}
		if (napr == 2) {
			moveX(3);
		}
		if (napr == 3) {
			moveX(-3);
		}
	}
/**
 * ����� ������������ ���� �� ����
 * @param value
 */
	public void moveX(int value) {
		boolean movingRight = value > 0;
		for (int i = 0; i < Math.abs(value); i++) {

			for (Block platform : game.platforms) {
				if (this.getBoundsInParent().intersects(platform.getBoundsInParent())
						&& platform.gettype() != Block.BlockType.WATER) {

					this.setTranslateX(800);
					this.setTranslateY(800);

					if (platform.gettype() == Block.BlockType.BRICK) {
						platform.setTranslateX(800);
						platform.setTranslateY(800);

					}
					if (platform.gettype() == Block.BlockType.KING) {
						this.STATUS = false;
					}

					return;
				}
			}
			if (this.getTranslateX() == -10 || this.getTranslateX() == 590) {
				this.setTranslateX(800);
				this.setTranslateY(800);
				return;
			}
			if (this.getTranslateX() < 590 && movingRight)
				this.setTranslateX(this.getTranslateX() + 1);
			if (this.getTranslateX() > -10 && !movingRight)
				this.setTranslateX(this.getTranslateX() - 1);

		}
	}
/**
 * ����� ������������ �� ��� �
 * @param value
 */
	public void moveY(int value) {
		boolean movingDown = value > 0;
		for (int i = 0; i < Math.abs(value); i++) {

			for (Block platform : game.platforms) {
				if (getBoundsInParent().intersects(platform.getBoundsInParent())
						&& platform.gettype() != Block.BlockType.WATER) {
					this.setTranslateX(800);
					this.setTranslateY(800);

					if (platform.gettype() == Block.BlockType.BRICK) {
						platform.setTranslateX(800);
						platform.setTranslateY(800);

					}
					if (platform.gettype() == Block.BlockType.KING) {
						this.STATUS = false;
					}
					return;
				}
			}
			if (this.getTranslateY() == -10 || this.getTranslateY() == 590) {
				this.setTranslateX(800);
				this.setTranslateY(800);
				return;
			}
			if (this.getTranslateY() < 590 && movingDown)
				this.setTranslateY(this.getTranslateY() + 1);
			if (this.getTranslateY() > -10 && !movingDown)
				this.setTranslateY(this.getTranslateY() - 1);

		}
	}

}
