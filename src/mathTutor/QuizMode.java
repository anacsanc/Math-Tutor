package mathTutor;
import java.util.ArrayList;
import javafx.geometry.*;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.scene.layout.RowConstraints;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.scene.shape.Rectangle;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.util.Duration;
import javafx.animation.*;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import java.util.Random;
import java.sql.*;
import java.lang.Math;
import javafx.scene.image.*;

// Quiz Mode provides the user with problems taken from problem bank in sql database
public class QuizMode {	
	
   int numProblems = 5;             // number of problems in quiz
   int quizBankCount;               // number of problems in quiz bank
   int numCorrect = 0;              // number of problems answered correctly
   Random rand = new Random();      // for random number generation
   double timeElapsed;              // time elapsed while taking quiz
   
   // index of problems selected for quiz
   ArrayList<Integer> indexOfProblem = new ArrayList<Integer>(numProblems);       
   // array of quiz problems
   ArrayList<Problem> quizProblems = new ArrayList<Problem>(numProblems);		    
   Connection connection;
   String query = "";
   Statement statement = null;
   PreparedStatement preparedStatement = null;
   
   Image correctImg = new Image("https://s3.amazonaws.com/mathtutorimg/check.jpg");
   Image incorrectImg = new Image("https://s3.amazonaws.com/mathtutorimg/x.jpg");
      
	public void start(Stage quizStage, String username) {
      
      // stackpane used to allow animation overlay
      StackPane quizStack = new StackPane();
      VBox quizPane = new VBox();
      quizPane.getStyleClass().add("background-window");
      quizStack.getChildren().add(quizPane);
      
      // add userpane with user information
      UserPane  userPane = new UserPane();
      quizPane.getChildren().add(userPane.get(quizStage, username));
            
      // add title box
      VBox titleBox = new VBox();
      Label title = new Label("Math Tutor - Quiz Mode");
      title.getStyleClass().add("h3");
      titleBox.setAlignment(Pos.CENTER);
      titleBox.getChildren().add(title);
      titleBox.setPadding(new Insets(0, 0, 10, 0));
      quizPane.setMargin(titleBox, new Insets(20,0,10,0));
      quizPane.getChildren().add(titleBox); 

      
      // get the current number of problems in the quiz bank
      connection = ConnectionManager.getConnection();
      query = "select count(*) from quiz_bank";
      try {
         statement = connection.createStatement();
         ResultSet resultSet = statement.executeQuery(query);
         if (resultSet.next()) {
            quizBankCount = resultSet.getInt(1);
            connection.close();
         }
      } catch (SQLException e ) {
         System.out.println("SQL query for quiz bank count failed");
      } 
   
      // randomly select problems by index from quiz bank, no duplicates
      boolean uniqueFlag;
      do {
         int num = rand.nextInt(quizBankCount) + 1;
         uniqueFlag = true;
         for (int i = 0; i < indexOfProblem.size(); ++i) {
            if (num == indexOfProblem.get(i))
               uniqueFlag = false;
         }
         if (uniqueFlag == true)
            indexOfProblem.add(num);
      } while (indexOfProblem.size() < numProblems);
      
      // add problems selected from quiz bank to quiz problems
      connection = ConnectionManager.getConnection();
      query = "select problem_string, answer from quiz_bank where question_num=?";
      try {
         preparedStatement = connection.prepareStatement(query);
         for (int i = 0; i < numProblems; ++i) {
            preparedStatement.setInt(1, indexOfProblem.get(i));
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next())
               quizProblems.add(new Problem(resultSet.getString("problem_string"), 
                                            resultSet.getInt("answer")));
         }
         connection.close();
      } catch (SQLException ex) {
         System.out.println("SQL query failed in quiz problem selection");
         System.exit(1);
      }
      
      // use shapes to build a timer
      GridPane timerPane = new GridPane();
      Rectangle rectangle = new Rectangle(10, 10, 350, 25);
      rectangle.setFill(Color.WHITE);
      Rectangle timeFill = new Rectangle(10,10,10,25);
      timeFill.setFill(Color.web("#00E676"));
      timerPane.add(rectangle, 0, 1, 3, 1);
      timerPane.add(timeFill, 0, 1, 3, 1);
      timerPane.setAlignment(Pos.CENTER);
      quizPane.setMargin(timerPane, new Insets(5,25,15,25));
      quizPane.getChildren().add(timerPane);
   
      // set up alert to trigger when time is up
      Alert alert = new Alert(AlertType.INFORMATION);
      alert.setTitle("Time up!");
      alert.setHeaderText("Time up!");
      alert.setContentText("Click OK to score your quiz.");
   
      // Timeline animates the timer and triggers time up alert when finished
      Timeline timeline = new Timeline();
      timeline.jumpTo(Duration.ZERO);
      timeline.setCycleCount(1);
      timeline.setAutoReverse(false);
      timeline.getKeyFrames().add(new KeyFrame(Duration.millis(10000), 
                                  new KeyValue(timeFill.translateXProperty(), 340)));
      timeline.play();
      timeline.setOnFinished(e -> alert.show());
      
      // textbox holds problem pane and scoring pane
      HBox testBox = new HBox();
      testBox.setPadding(new Insets(0,0,0,35));
      testBox.getStyleClass().add("op-background");
      
      // problem pane holds the math problems and text input fields
      GridPane problemPane = new GridPane();
      problemPane.setPadding(new Insets(20, 0, 20, 20));
      problemPane.setHgap(10);
      problemPane.setVgap(5);
      problemPane.setAlignment(Pos.CENTER);
      problemPane.getStyleClass().add("h4");
      testBox.getChildren().add(problemPane);
      quizPane.getChildren().add(testBox); 
      
      // add equation string and TextFields to problem pane
   	for (int i = 0; i < numProblems; i++) {
   		problemPane.addRow(i, new Text(quizProblems.get(i).getEquation()), 
                            quizProblems.get(i).createTextField());
   		problemPane.getRowConstraints().add(new RowConstraints(45));
   	}
      
      // add submit button
      HBox submitBtnBox = new HBox();
      Button submitBtn = new Button("Submit");
      submitBtn.getStyleClass().add("small-button");
      submitBtnBox.setAlignment(Pos.CENTER);
      submitBtnBox.getChildren().add(submitBtn);
      problemPane.setMargin(submitBtnBox, new Insets(10,0,0,0));
      problemPane.add(submitBtnBox, 0, numProblems + 1, 2, 1);
      
      // return button (added after submit)
      HBox returnBtnBox = new HBox();
      Button returnBtn = new Button("Return");
      returnBtn.getStyleClass().add("small-button");
      returnBtnBox.setAlignment(Pos.CENTER);
      returnBtnBox.getChildren().add(returnBtn);
          
      // box to report numcorrect and timeElapsed (added after submit)
      VBox feedbackBox = new VBox();
      Label timeLabel = new Label();
      timeLabel.getStyleClass().add("h4");
      Label correctLabel = new Label();
      correctLabel.getStyleClass().add("h4");
      feedbackBox.setAlignment(Pos.CENTER);
      feedbackBox.setPadding(new Insets(10, 0, 0, 0));
      feedbackBox.getChildren().addAll(timeLabel, correctLabel);
      
      // scoring pane holds correct/incorrect image and correct answers
      GridPane scoringPane = new GridPane();
      scoringPane.setPadding(new Insets(20, 20, 20, 20));
      scoringPane.setHgap(30);
   	  scoringPane.setVgap(5);
      scoringPane.setAlignment(Pos.BASELINE_LEFT);
      scoringPane.getStyleClass().add("h4");
      
   	// action for submit button
   	submitBtn.setOnAction(e -> {
         // pause timeline and get timeElapsed
         timeline.pause();
         timeElapsed = Math.round((timeline.getCurrentTime().toSeconds() * 100)) / 100.0;
         // get user answers and populate scoring pane
   		for (int i = 0; i < numProblems; i++) {
   			if (!(quizProblems.get(i).ansText.getText().isEmpty()))
   			   quizProblems.get(i).setUserAnswer();
            if (quizProblems.get(i).isCorrect()) {
               scoringPane.addRow(i, new ImageView(correctImg));
               scoringPane.getRowConstraints().add(new RowConstraints(45));
               ++numCorrect;
               }
            else
            {
               scoringPane.addRow(i, new ImageView(incorrectImg), 
               new Text(quizProblems.get(i).getEquation() +  Integer.toString(quizProblems.get(i).getAnswer())));
       		   scoringPane.getRowConstraints().add(new RowConstraints(45));
            }
   		}
         
         //disable submit button, show results, and add return button
         submitBtn.setDisable(true);
         scoringPane.setMargin(returnBtnBox, new Insets(10,0,0,0));
         scoringPane.add(returnBtnBox, 0, numProblems + 1, 2, 1);
         testBox.getChildren().add(scoringPane);
         timeLabel.setText("Quiz completed in: " + timeElapsed + " seconds.");
         correctLabel.setText("You answered " + numCorrect + " out of " + 
                              numProblems + " problems correctly.");
         quizPane.setMargin(feedbackBox, new Insets(0,0,0,20));
         quizPane.getChildren().addAll(feedbackBox);
         
         // if perfect score show balloons animation
         if (numCorrect == numProblems) {
            Animate animate = new Animate();
            animate.showBalloons(quizStack);
         }
         
         // if zero score show terrible animation  
         if (numCorrect == 0) {
            Animate animate = new Animate();
            animate.showTerrible(quizStack);
         }
         
         // for logged in user, send results to sql database 
         if (!username.equals(""))
         {
            // build up strings with the information for the query
            int attempt_details_id = 0;
            String colNamesString = "(";
            String valuesString = "(";
            for (int i = 0; i < numProblems; ++i) {
               if (i == numProblems - 1) {
                  colNamesString += ("prob" + (i+1) + ", ans" + (i+1) + ", userans" + (i+1) + ")");
                  valuesString += ("'" + quizProblems.get(i).getEquation() +" ', " + quizProblems.get(i).getAnswer() + ", " + quizProblems.get(i).getUserAnswer() + ")");
               }
               else {
                  colNamesString += ("prob" + (i+1) + ", ans" + (i+1) + ", userans" + (i+1) + ", ");
                  valuesString += ("'" + quizProblems.get(i).getEquation() +" ', " + quizProblems.get(i).getAnswer() + ", " + quizProblems.get(i).getUserAnswer() + ", ");
               }
            }
            // sends attempt_details to sql database
            try {
               connection = ConnectionManager.getConnection();
               query = "insert into attempt_details " + colNamesString + " values " + valuesString;
               preparedStatement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
               preparedStatement.executeUpdate();
               ResultSet resultSet = preparedStatement.getGeneratedKeys();
               if (resultSet.next())
                  attempt_details_id = resultSet.getInt(1);
               connection.close();
            } catch (SQLException ex) {
               System.out.println("SQL update attempt details failed");
               System.exit(1);
            }
            
            // more queries for other updates to sql database
            String query2 = "insert into attempt(username, attempt_type, operation, num_problems, num_correct, attempt_details_id, time_elapsed) values (?,?,?,?,?,?, ?)";
            String query3 = "update users set points= points + ? where username=?";
            try {
               // sends attempt information to sql database
               connection = ConnectionManager.getConnection();
               preparedStatement = connection.prepareStatement(query2);
               preparedStatement.setString(1, username);
               preparedStatement.setString(2, "quiz");
               preparedStatement.setString(3, "mixed");
               preparedStatement.setInt(4, numProblems);
               preparedStatement.setInt(5, numCorrect);
               preparedStatement.setInt(6, attempt_details_id);
               preparedStatement.setDouble(7, timeElapsed);
               preparedStatement.execute();
               // sends updated user points information to sql database
               preparedStatement = connection.prepareStatement(query3);
               preparedStatement.setInt(1, numCorrect);
               preparedStatement.setString(2, username);
               preparedStatement.execute();
               connection.close();
            } catch (SQLException ex) {
               System.out.println("SQL update practice results failed");
               System.exit(1);
            }
         }
   	});
      
      // action for time up- fires submit button
      alert.setOnCloseRequest( e-> {
         submitBtn.fire();
      });
   
      // stop the animation if sign out button clicked
      userPane.signOutLabel.setOnMouseClicked(e-> {
         timeline.pause();
         MainWindow mainWindow = new MainWindow();
         mainWindow.start(quizStage);
      });
      
      // action for return button (return to choose mode)
      returnBtn.setOnAction(e -> {
         ChooseMode chooseMode = new ChooseMode();
         chooseMode.start(quizStage, username);
      });
      
      // set the scene and put on stage
   	Scene scene = new Scene(quizStack, 500, 600);
   	StyleClasses.getCSS(scene);
   	quizStage.setScene(scene);
   	quizStage.setTitle("Math Tutor - Quiz Mode");
   	quizStage.show();	
   }
}