package mathTutor;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import java.util.ArrayList;
import javafx.geometry.*;
import javafx.scene.control.Label;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.scene.layout.RowConstraints;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import java.sql.*;
import javafx.scene.image.*;

// shows result detail including all problems and answers
public class ShowResultDetails {
	public static void get(int resultID) {
      int numCorrect = 0;
      Image correctImg = new Image("https://s3.amazonaws.com/mathtutorimg/check_sm.jpg");
      Image incorrectImg = new Image("https://s3.amazonaws.com/mathtutorimg/x_sm.jpg");
   
      // get problems from database and add to arraylist
      PreparedStatement preparedStatement;
      
      String query = "select * from attempt_details where attempt_details_id=?";
      
      ArrayList<Problem> problems = new ArrayList<Problem>();   // holds the problems and answers
      
      try {
         Connection connection = ConnectionManager.getConnection();
         preparedStatement = connection.prepareStatement(query);
         preparedStatement.setInt(1, resultID);
         ResultSet resultSet = preparedStatement.executeQuery();

         while (resultSet.next()) {
            // make a for loop with int i = 0, get with i+2, i+12, i+13
            // run while i < 10 or getString != null 
            for (int i = 0; i < 10 && resultSet.getString(i + 2) != null; ++i) {
               String problem = resultSet.getString(i+ 2);
               int answer = resultSet.getInt(i+12);
               int userAnswer = resultSet.getInt(i+22);
               problems.add(new Problem(problem, answer, userAnswer));
            }
         }
         connection.close();
      } catch (SQLException ex) {
         System.out.println("SQL show result details query failed");
         System.exit(1);
      }

      ///////////
      // use a new stage to show results
      Stage resultsStage = new Stage();
      
      VBox showResultsPane = new VBox();
      showResultsPane.getStyleClass().add("background-window");
      
      // Title for Application
      VBox titleBox = new VBox();
      Label title = new Label("Math Tutor - Show Results");
      title.getStyleClass().add("h3");
      titleBox.setAlignment(Pos.CENTER);
      titleBox.getChildren().add(title);
      showResultsPane.setMargin(titleBox, new Insets(25,0,0,0));
      showResultsPane.getChildren().add(titleBox); 
      
      
      // Hbox test holds problem pane and scoring pane
      HBox testBox = new HBox();
      testBox.setPadding(new Insets(0,0,0,20));
      testBox.getStyleClass().add("op-background");
      testBox.getStyleClass().add("h4");
      
      // problem pane holds the math problems and text input fields
      GridPane problemPane = new GridPane();
		problemPane.setPadding(new Insets(20, 0, 20, 20));
	   problemPane.setHgap(10);
		problemPane.setVgap(5);
      problemPane.setAlignment(Pos.CENTER);
      problemPane.getStyleClass().add("op-background");

      showResultsPane.getChildren().add(testBox); 
      
      GridPane scoringPane = new GridPane();
      scoringPane.setPadding(new Insets(20, 20, 20, 20));
	   scoringPane.setHgap(30);
		scoringPane.setVgap(5);
      scoringPane.setAlignment(Pos.BASELINE_LEFT);
   
		//Add equation text and TextFields for problems in array
      Text yourRes = new Text("Your responses");
      yourRes.getStyleClass().add("bold");
      problemPane.add(yourRes,0,0,2,1);
		for (int i = 0; i < problems.size(); i++) {
			problemPane.addRow(i+1, new Text(problems.get(i).getEquation() + Integer.toString(problems.get(i).getUserAnswer())));
			problemPane.getRowConstraints().add(new RowConstraints(20));
		}
           
      // box to report time elapsed and numcorrect
      VBox feedbackBox = new VBox();
      Label correctLabel = new Label();
      correctLabel.getStyleClass().add("h4");
      correctLabel.setPadding(new Insets(10,0,0,0));
      feedbackBox.getChildren().add(correctLabel);
      feedbackBox.setAlignment(Pos.CENTER);

      for (int i = 0; i < problems.size(); i++) {
				//if (!(problems.get(i).getUserAnswer().isEmpty()))
				  // problems.get(i).setUserAnswer();
            if (problems.get(i).isCorrect()) {
               scoringPane.addRow(i+1, new ImageView(correctImg));
               ++numCorrect;
               scoringPane.getRowConstraints().add(new RowConstraints(20));
               }
            else {
               scoringPane.addRow(i+1, new ImageView(incorrectImg), 
                  new Text(problems.get(i).getEquation() +  
                           Integer.toString(problems.get(i).getAnswer())));
               scoringPane.getRowConstraints().add(new RowConstraints(20));
            	}
			}
      Text scorePaneText = new Text("");
      scorePaneText.getStyleClass().add("bold");
      if (numCorrect != problems.size()) {
         scorePaneText.setText("Correct responses");
      }
      scoringPane.add(scorePaneText,1,0);
	   //Show results 

      testBox.getChildren().addAll(problemPane, scoringPane);
      correctLabel.setText("You answered " + numCorrect + " out of " + problems.size() + " problems correctly.");
      showResultsPane.setMargin(feedbackBox, new Insets(0,0,0,20));
      showResultsPane.getChildren().add(feedbackBox);

      // close window button
      HBox closeBtnBox = new HBox();
      closeBtnBox.setPadding(new Insets(20,0,0,0));
      Button closeBtn = new Button("Close");
      closeBtn.getStyleClass().add("small-button");
      closeBtnBox.setAlignment(Pos.CENTER);
      closeBtnBox.getChildren().add(closeBtn);
      showResultsPane.getChildren().add(closeBtnBox);
      
      closeBtn.setOnAction(e -> {
         resultsStage.close();
      });
      
      /////////
      		
		Scene scene = new Scene(showResultsPane, 400, 525);
		StyleClasses.getCSS(scene);
		resultsStage.setTitle("Show Results");
		resultsStage.setScene(scene);
		resultsStage.show();
	}
}