package mathTutor;
import javafx.application.Application;
import javafx.geometry.*;
import java.util.ArrayList;
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

// Shows the uesr a summary of activity in the application
public class UserHistory {
   ArrayList<Result> results = new ArrayList<Result>();   // holds the user results from database
   
   public void show(Stage historyStage, String username) {
   
   VBox historyBox = new VBox();
   historyBox.getStyleClass().add("background-window");
   
   // add userpane with user information
   UserPane userPane = new UserPane();
   historyBox.getChildren().add(userPane.get(historyStage, username));
   
   // add title box
   VBox titleBox = new VBox();
   Label title = new Label("Math Tutor - User History");
   title.getStyleClass().add("h3");
   titleBox.setAlignment(Pos.CENTER);
   titleBox.getChildren().add(title);
   historyBox.setMargin(titleBox, new Insets(20,0,10,0));
   historyBox.getChildren().add(titleBox); 
   
   // vertical scroll bar appears if needed
   ScrollPane scrollPane = new ScrollPane();
   scrollPane.setHbarPolicy(ScrollBarPolicy.NEVER);
   
   // historyPane to hold user activity data
   GridPane historyPane = new GridPane();
   historyPane.setPadding(new Insets(10, 10, 10, 10));
   historyPane.setHgap(5.0);
   historyPane.setVgap(5.0);
   historyPane.setAlignment(Pos.BASELINE_LEFT);

   Label dateHead = new Label("Date");
   dateHead.getStyleClass().add("bold");
   Label modeHead = new Label("Mode");
   modeHead.getStyleClass().add("bold");
   Label scoreHead = new Label("Score");
   scoreHead.getStyleClass().add("bold");
   Label timeHead = new Label("Time");
   timeHead.getStyleClass().add("bold");
   Label viewHead = new Label("View Quiz");
   viewHead.getStyleClass().add("bold");
   historyPane.add(dateHead, 0,0);
   historyPane.add(modeHead, 1,0);
   historyPane.add(scoreHead, 2,0);
   historyPane.add(timeHead, 3,0);
   historyPane.add(viewHead, 4, 0);
   historyPane.getColumnConstraints().add(new ColumnConstraints(100));
   historyPane.getColumnConstraints().add(new ColumnConstraints(100));
   historyPane.getColumnConstraints().add(new ColumnConstraints(80));
   historyPane.getColumnConstraints().add(new ColumnConstraints(80));
   historyPane.getColumnConstraints().add(new ColumnConstraints(100));
   scrollPane.setContent(historyPane);
   scrollPane.setMaxHeight(340.0);
   historyBox.getChildren().add(scrollPane);
   scrollPane.getStyleClass().add("op-background");
   
   // get user history data from sql database and put into arraylist
   PreparedStatement preparedStatement;
   String query = "select * from attempt where username=?";
   try {
      Connection connection = ConnectionManager.getConnection();
      preparedStatement = connection.prepareStatement(query);
      preparedStatement.setString(1, username);
      ResultSet resultSet = preparedStatement.executeQuery();
      while (resultSet.next()) {
         Date date = resultSet.getDate(3);
         String type = resultSet.getString(4);
         int numProblems = resultSet.getInt(6);
         int numCorrect = resultSet.getInt(7);
         double time = resultSet.getDouble(8);
         int attemptID = resultSet.getInt(9);
         if (type.equals("practice"))
            results.add(new Result(date, type, numProblems, numCorrect, attemptID));
         if (type.equals("quiz"))
            results.add(new Result(date, type, numProblems, numCorrect, time, attemptID));
      }
      connection.close();
   } catch (SQLException ex) {
      System.out.println("SQL user history query failed");
      System.exit(1);
   }
   
   // label used if there is no user history for this user
   Label emptyLabel = new Label("No results found for user " + username);
   
   // out put user history
   if (results.isEmpty())
      // if no uesr history, add emptyLabel
      historyPane.add(emptyLabel, 0, 1,5,1);
   else {
      // otherwise add the results to the history pane
      for (int i=0; i < results.size(); ++i) {
         historyPane.add(new Text(results.get(i).getDate().toString()), 0, i+1);
         historyPane.add(new Text(results.get(i).getMode()),1, i+1);
         historyPane.add(new Text(results.get(i).getScore()),2,i+1);
         if (results.get(i).getMode().equals("quiz"))
            historyPane.add(new Text(Double.toString(results.get(i).getTime()) + " s"),3,i+1);
         
         // this view text will allow user to see detailed results for an attempt
         Text text = new Text("View");
         text.setUnderline(true);
         text.getStyleClass().add("link");
         int attemptID = results.get(i).getAttemptDetailsID();
         historyPane.setRowIndex(text, i);
         historyPane.add(text, 4, i+1);
         
         // set action for view button
         text.setOnMousePressed(e -> {
            // when pressed show all problems and answers for given attempt
            ShowResultDetails showResultDetails = new ShowResultDetails();
            showResultDetails.get(attemptID);
         });
      }
   }
   
   // return button
   HBox returnBtnBox = new HBox();
   Button returnBtn = new Button("Return");
   returnBtn.getStyleClass().add("small-button");
   returnBtnBox.setAlignment(Pos.CENTER);
   returnBtnBox.getChildren().add(returnBtn);
   historyBox.getChildren().add(returnBtnBox);
   historyBox.setMargin(returnBtnBox, new Insets(20,0,0,0));
   historyPane.getStyleClass().add("op-background");
   historyPane.getStyleClass().add("h4");
   // action for return button (goes to choose mode)
   returnBtn.setOnAction(e -> {
      ChooseMode chooseMode = new ChooseMode();
      chooseMode.start(historyStage, username);
   });
   
   // set the scene and put on stage
   Scene scene = new Scene(historyBox, 500, 600);
   StyleClasses.getCSS(scene);   
   historyStage.setTitle("Math Tutor - User History");
	historyStage.setScene(scene);
	historyStage.show();
   }
}