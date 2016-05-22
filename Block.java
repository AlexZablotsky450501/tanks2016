package tanksMenu;

import javafx.geometry.Rectangle2D;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

public class Block extends Pane {
	Image blocksImg = new Image(getClass().getResourceAsStream("1.png"));
	ImageView block;
	public static boolean GameMod = true;

	public enum BlockType {
		BRICK, WATER, STONE, KING
	}

	BlockType blockt;

	public Block(BlockType blockType, int x, int y) {
		blockt = blockType;
		block = new ImageView(blocksImg);
		block.setFitWidth(game.BLOCK_SIZE);
		block.setFitHeight(game.BLOCK_SIZE);
		setTranslateX(x);
		setTranslateY(y);

		switch (blockType) {
		case BRICK:
			block.setViewport(new Rectangle2D(257, 0, 14, 14));
			break;
		case WATER:
			block.setViewport(new Rectangle2D(257, 48, 16, 16));
			break;
		case STONE:
			block.setViewport(new Rectangle2D(256, 16, 16, 16));
			break;
		case KING:
			block.setViewport(new Rectangle2D(305, 33, 15, 16));
			break;
		}

		if (GameMod == true) {
			getChildren().add(block);
			game.platforms.add(this);
			game.root.getChildren().add(this);
		} else {
			getChildren().add(block);
			Replay.platforms.add(this);
			Replay.root.getChildren().add(this);
		}
	}

	public Block.BlockType gettype() {
		return this.blockt;
	}
}
