package mathTutor;
import javafx.scene.layout.StackPane;
import javafx.scene.image.*;
import javafx.scene.shape.Line;
import javafx.util.Duration;
import javafx.animation.*;
import javafx.scene.media.*;


// Used to show animations and play sound within Practice or Quiz StackPane
public class Animate {

   // shows balloons for good performance
   public void showBalloons(StackPane sp) {
      
      // image and audio assets
      ImageView balloons = new ImageView(new Image("https://s3.amazonaws.com/mathtutorimg/balloons.png"));
      AudioClip cheer = new AudioClip("https://s3.amazonaws.com/mathtutorimg/level.mp3");

      sp.getChildren().add(balloons);
      Line line  = new Line(125.0,150.0,125.0, -400.0);
      
      // path transition for balloons over line
      PathTransition pt = new PathTransition();
      pt.setDuration(Duration.millis(3000));
      pt.setPath(line);
      pt.setNode(balloons);
      pt.play();
      // play audio
      cheer.play();
   }

   // shows terrible picture for bad performance
   public void showTerrible(StackPane sp) {
      
      // image and audio assets
      ImageView terrible = new ImageView(new Image("https://s3.amazonaws.com/mathtutorimg/terrible.png"));
      AudioClip sad = new AudioClip("https://s3.amazonaws.com/mathtutorimg/sad.mp3");
      
      sp.getChildren().add(terrible);
      Line line  = new Line(125.0,200.0,125.0, -400.0);
      
      // path transition for terrible picture over line
      PathTransition pt = new PathTransition();
      pt.setDuration(Duration.millis(3000));
      pt.setPath(line);
      pt.setNode(terrible);
      pt.play();
      // play audio
      sad.play();
   }
}