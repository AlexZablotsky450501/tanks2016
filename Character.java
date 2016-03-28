package tanksMenu;

import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

public class Character extends Pane{

    ImageView imageView;
    int count = 2;
    int columns = 8;
    int offsetX = 0;
    int offsetY = 0;
    int width = 16;
    int height = 15;
    int score = 0;
    Rectangle removeRect = null;
    SpriteAnimation animation;
    public Character(ImageView imageView)
    {
        this.imageView = imageView;
        this.imageView.setViewport(new Rectangle2D(offsetX,offsetY,width,height));
        this.imageView.setFitHeight(30);
        this.imageView.setFitWidth(30);
        animation = new SpriteAnimation(imageView,Duration.millis(200),count,columns,offsetX,offsetY,width,height);
        getChildren().addAll(imageView);
        this.setTranslateX(198);
        this.setTranslateY(560);
    }
    public void moveX(int value)
    {
    	boolean movingRight = value > 0;
        for(int i = 0; i<Math.abs(value); i++) 
        { 
            for (Node platform : game.platforms) 
            {
                if(this.getBoundsInParent().intersects(platform.getBoundsInParent())) 
                {  
                    if (movingRight) 
                    {
                        if (this.getTranslateX() + game.TANK_SIZE == platform.getTranslateX())
                        {
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
            if(this.getTranslateX()<560 && movingRight )
                this.setTranslateX(this.getTranslateX()+1);
            if(this.getTranslateX()>0 && !movingRight )
                this.setTranslateX(this.getTranslateX()-1);
        }
    }
    public void moveY(int value) {
    	  boolean movingDown = value >0;
          for(int i = 0; i < Math.abs(value); i++)
          {
              for(Block platform :game.platforms)
              {
                  if(getBoundsInParent().intersects(platform.getBoundsInParent()) && this.getTranslateY()<=560)
                  { 
                      if(movingDown)
                      {
                          if(this.getTranslateY()+ game.TANK_SIZE == platform.getTranslateY()){
                              this.setTranslateY(this.getTranslateY()-1);
                              return;
                          }
                      }
                      else{
                          if((this.getTranslateY() == platform.getTranslateY()+ game.BLOCK_SIZE) )
                          {
                              this.setTranslateY(this.getTranslateY()+1);
                              return;
                          }
                      }
                  }
              }
              if(this.getTranslateY()<560 && movingDown)
                  this.setTranslateY(this.getTranslateY()+1);
              if(this.getTranslateY()>0 && !movingDown)
                  this.setTranslateY(this.getTranslateY()-1);
    	}
    }  
}
