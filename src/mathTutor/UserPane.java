package mathTutor;
import javafx.application.Application;
import javafx.geometry.*;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.scene.input.MouseEvent;
import java.sql.*;

// Creates userpane with user information, points, sign in/out options
public class UserPane {
   public Label signOutLabel = new Label("Sign out");
   public int points = 0;
   
	public BorderPane get(Stage currentStage, String username) {

      BorderPane userPane = new BorderPane();
      
      // if signed in, get user points
      if (!username.equals("")) {
         Connection connection = ConnectionManager.getConnection();
         String query = "select points from users where username=?";
         PreparedStatement preparedStatement = null;
         try {
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, username);
            ResultSet result = preparedStatement.executeQuery();
            if (result.next())
               points = result.getInt(1);
            connection.close();
         } catch (SQLException ex) {
            System.out.println("SQL get user points failed");
            System.exit(1);
         }
      }
      
      // update label text with proper information
      Label userLabel = new Label("Welcome " + username + "!");
      userLabel.setPrefWidth(150);
      Label pointsLabel = new Label("Points: " + points);
      pointsLabel.setAlignment(Pos.CENTER);
      
      // update label text for non-signed in user
      if (username.equals("")) {
         userLabel.setText("Not signed in");
         pointsLabel.setText("");
         signOutLabel.setText("Sign in / Register");
      }      
      signOutLabel.setPrefWidth(150);
      signOutLabel.setAlignment(Pos.BASELINE_RIGHT);
      signOutLabel.setUnderline(true);
      signOutLabel.getStyleClass().add("link");
      
      // add to labels to border pane
      userPane.setLeft(userLabel);
      userPane.setCenter(pointsLabel);
      userPane.setRight(signOutLabel);
      userPane.setStyle("-fx-background-color: #CFD8DC; -fx-font-size: 12;");
      userPane.setMargin(userLabel, new Insets(5,0,5,10));
      userPane.setMargin(pointsLabel, new Insets(5,0,5,0));
      userPane.setMargin(signOutLabel, new Insets(5,10,5,0));
      
      // action for sign out label
      signOutLabel.setOnMouseClicked(e-> {
         MainWindow mainWindow = new MainWindow();
         mainWindow.start(currentStage);
      });
      
      // return the formatted userpane
      return userPane;
   }
}
      