package mathTutor;
import javafx.application.Application;
import javafx.geometry.*;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.scene.control.TextField;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ScrollPane.*;
import java.sql.Date;
import java.sql.*;

// Shows top users by points for the application
public class LeaderBoard {
   
   public void show(Stage leaderStage, String username) {
   
   VBox leaderBox = new VBox();
   leaderBox.getStyleClass().add("background-window");
   // add userpane with user information
   UserPane userPane = new UserPane();
   leaderBox.getChildren().add(userPane.get(leaderStage, username));
   
   // add title box
   VBox titleBox = new VBox();
   Label title = new Label("Math Tutor - Top Users");
   title.getStyleClass().add("h3");
   titleBox.setAlignment(Pos.CENTER);
   titleBox.getChildren().add(title);
   leaderBox.setMargin(titleBox, new Insets(20,0,10,0));
   leaderBox.getChildren().add(titleBox); 
   
   // vertical scroll bar appears if needed
   ScrollPane scrollPane = new ScrollPane();
   scrollPane.setHbarPolicy(ScrollBarPolicy.NEVER);
   
   // historyPane to hold user activity data
   GridPane leaderPane = new GridPane();
   leaderPane.setPadding(new Insets(20, 0, 20, 100));
   leaderPane.setHgap(5.0);
   leaderPane.setVgap(5.0);
   leaderPane.setAlignment(Pos.BASELINE_LEFT);
   Label userHead = new Label("Username");
   userHead.getStyleClass().add("bold");
   Label pointsHead = new Label("Points");
   pointsHead.getStyleClass().add("bold");
   pointsHead.setFont(new Font(18));
   leaderPane.add(userHead, 0,0);
   leaderPane.add(pointsHead, 1,0);
   leaderPane.getColumnConstraints().add(new ColumnConstraints(150));
   leaderPane.getColumnConstraints().add(new ColumnConstraints(100));
   scrollPane.setContent(leaderPane);
   scrollPane.setMaxHeight(340.0);
   leaderPane.getStyleClass().add("op-background");
   leaderPane.getStyleClass().add("h4");
   leaderBox.getChildren().add(leaderPane);
     
   // get user points data from sql database 
   PreparedStatement preparedStatement;
   String query = "select * from users order by points desc";
   try {
      Connection connection = ConnectionManager.getConnection();
      preparedStatement = connection.prepareStatement(query);
      ResultSet resultSet = preparedStatement.executeQuery();
      int rows = 1;
      while (resultSet.next() && rows < 21) {
         String name = resultSet.getString(1);
         int userPoints = resultSet.getInt(3);
         leaderPane.addRow(rows, new Text(name), new Text(Integer.toString(userPoints)));
         rows += 1;
      }
      connection.close();
   } catch (SQLException ex) {
      System.out.println("SQL leaderboard query failed");
      System.exit(1);
   }
   
   // return button
   HBox returnBtnBox = new HBox();
   Button returnBtn = new Button("Return");
   returnBtn.getStyleClass().add("small-button");
   returnBtnBox.setAlignment(Pos.CENTER);
   returnBtnBox.getChildren().add(returnBtn);
   leaderBox.getChildren().add(returnBtnBox);
   leaderBox.setMargin(returnBtnBox, new Insets(20,0,0,0));
   
   // action for return button (goes to choose mode)
   returnBtn.setOnAction(e -> {
      ChooseMode chooseMode = new ChooseMode();
      chooseMode.start(leaderStage, username);
   });
   
   // set the scene and put on stage
   Scene scene = new Scene(leaderBox, 500, 600);
   StyleClasses.getCSS(scene);   
   leaderStage.setTitle("Math Tutor - Leaderboard");
	leaderStage.setScene(scene);
	leaderStage.show();
   }
}