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
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import java.util.Random;
import javafx.scene.image.*;
import java.sql.*;

// Practice Mode provides the user with problems based on criteria provided
public class PracticeMode {
   public int numCorrect = 0 ;            // number of problems answered correctly
   public String operationString = "";    // operation as a string
   public int num1 = 0;                   // holds first operand
   public int num2 = 0;                   // holds second operand
   public int answer = 0;                 // holds answer
   Random rand = new Random();            // for random number generation
   ArrayList<Problem> pracProblems = new ArrayList<Problem>();		//array of practice Problems
   
   public Image correctImg = new Image("https://s3.amazonaws.com/mathtutorimg/check.jpg");
   public Image incorrectImg = new Image("https://s3.amazonaws.com/mathtutorimg/x.jpg");
	
	public void start(Stage practiceStage, String username, int operation, int factor, 
                     int numProblems, int opMin, int opMax) {
      
      // stackpane used to allow animation overlay
      StackPane practiceStack = new StackPane();  
      practiceStack.getStyleClass().add("background-window");
      VBox practicePane = new VBox();
      practiceStack.getChildren().add(practicePane);
      // add userpane with user information
      UserPane  userPane = new UserPane();
      practicePane.getChildren().add(userPane.get(practiceStage, username));
      
      // add title box
      VBox titleBox = new VBox();
      Label title = new Label("Math Tutor - Practice Mode");
      title.getStyleClass().add("h3");
      titleBox.setAlignment(Pos.CENTER);
      titleBox.getChildren().add(title);
      practicePane.setMargin(titleBox, new Insets(20,0,0,0));
      practicePane.getChildren().add(titleBox); 
      
      // generate problems and populate pracProblems ArrayList
      switch(operation) {
         case 0: // addition
            // picks random operands between provided min and max
            for (int i = 0; i < numProblems; ++i) {
               num1 = rand.nextInt(opMax - opMin + 1) + opMin;
		         num2 = rand.nextInt(opMax - opMin + 1) + opMin;
		         answer = num1 + num2;
               pracProblems.add(new Problem(num1 + " + " + num2 + " = ", answer));
               operationString = "addition";
            }
            break;
         
         case 1: // multiplication
            // uses selected factor and randomly generated unique second factor
            ArrayList<Integer> secondFactorList = new ArrayList<Integer>();
            for (int i = 1; i < 11; ++i)
               secondFactorList.add(i);      // make second factor arraylist
               
            for (int i = 0; i < numProblems; ++i)
            {
               num1 = factor;
               // randomly select index of second factor from list
               int indexOfSecondFactor = rand.nextInt(secondFactorList.size());
               // use index to get the number, then delete this one from the list
      		   num2 = secondFactorList.get(indexOfSecondFactor);
               secondFactorList.remove(indexOfSecondFactor);
      		   answer = num1 * num2;
               pracProblems.add(new Problem(num1 + " X " + num2 + " = ", answer));
               operationString = "multiplication";
            }
            break;
         
         case 2: // subtraction
            // picks random operands between provided min and max
            for (int i = 0; i < numProblems; ++i) {
               num1 = rand.nextInt(opMax - opMin + 1) + opMin;
               // make sure second operand won't create a negative answer
               do {
                  num2 = rand.nextInt(opMax - opMin + 1) + opMin;
               } while ( num2 > num1 );
      		   answer = num1 - num2;
               pracProblems.add(new Problem(num1 + " - " + num2 + " = ", answer));
               operationString = "subtraction";
            }
            break;
            
         case 3: // division
            // uses selected factor and randomly generated unique answer
            ArrayList<Integer> answerList = new ArrayList<Integer>();
            for (int i = 1; i < 11; ++i)
               answerList.add(i);      // make answer arraylist
               
            for (int i = 0; i < numProblems; ++i) {
               num2 = factor;
               // randomly select index of answer from list
               int indexOfAnswer = rand.nextInt(answerList.size());
               // use index to get the answer, then delete this one from the list
               answer = answerList.get(indexOfAnswer);
               answerList.remove(indexOfAnswer);
               num1 = answer * num2;
               pracProblems.add(new Problem(num1 + " \u00F7 " + num2 + " = ", answer));
               operationString = "division";
         }
      }
      
      // testBox holds problem pane and scoring pane
      HBox testBox = new HBox();
      testBox.setPadding(new Insets(0,0,0,35));
      testBox.getStyleClass().add("op-background");
      testBox.getStyleClass().add("h4");
      
      // problem pane holds the math problems and text input fields
      GridPane problemPane = new GridPane();
		problemPane.setPadding(new Insets(10, 0, 10, 20));
	   problemPane.setHgap(10);
		problemPane.setVgap(5);
      problemPane.setAlignment(Pos.CENTER);
      testBox.getChildren().add(problemPane);
      practicePane.getChildren().add(testBox); 
		// add equation string and TextFields to problem pane
		for (int i = 0; i < numProblems; i++) {
			problemPane.addRow(i, new Label(pracProblems.get(i).getEquation()), pracProblems.get(i).createTextField());
			problemPane.getRowConstraints().add(new RowConstraints(40));
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
      returnBtn.getStyleClass().add("large-button");
      returnBtnBox.setAlignment(Pos.CENTER);
      returnBtnBox.getChildren().add(returnBtn);
      
      // box to report numcorrect (added after submit)
      VBox feedbackBox = new VBox();
      Label correctLabel = new Label();
      feedbackBox.setAlignment(Pos.CENTER);
      feedbackBox.getChildren().add(correctLabel);
      correctLabel.getStyleClass().add("h4");
      feedbackBox.setPadding(new Insets(10, 0, 0, 0));
      
      // scoring pane holds correct/incorrect image and correct answers
      GridPane scoringPane = new GridPane();
      scoringPane.setPadding(new Insets(10,0,10,20));
	   scoringPane.setHgap(30);
	   scoringPane.setVgap(5);
	   scoringPane.setAlignment(Pos.BASELINE_LEFT);
      
		// action for submit button
		submitBtn.setOnAction(e -> {
         // get user answers and populate scoring pane
			for (int i = 0; i < numProblems; i++) {
				if (!(pracProblems.get(i).ansText.getText().isEmpty()))
				   pracProblems.get(i).setUserAnswer();
            if (pracProblems.get(i).isCorrect()) {
               scoringPane.addRow(i, new ImageView(correctImg));
               ++numCorrect;
               scoringPane.getRowConstraints().add(new RowConstraints(40));
               }
            else {
               scoringPane.addRow(i, new ImageView(incorrectImg), new Text(pracProblems.get(i).getEquation() +  
                           Integer.toString(pracProblems.get(i).getAnswer())));
               scoringPane.getRowConstraints().add(new RowConstraints(40));
            	}
			}

         //disable submit button, show results, and add return button
         submitBtn.setDisable(true);
         scoringPane.setMargin(returnBtnBox, new Insets(10,0,0,0));
         scoringPane.add(returnBtnBox, 0, numProblems + 1, 2, 1);
         testBox.getChildren().add(scoringPane);
         correctLabel.setText("You answered " + numCorrect + " out of " + numProblems + " problems correctly.");
         practicePane.setMargin(feedbackBox, new Insets(0,0,0,20));
         practicePane.getChildren().add(feedbackBox);
         
         // if perfect score show balloons animation
         if (numProblems == numCorrect) {
            Animate animate = new Animate();
            animate.showBalloons(practiceStack);
         }
         
         // if zero score show terrible animation         
         if (numCorrect == 0) {
            Animate animate = new Animate();
            animate.showTerrible(practiceStack);
         }
         
         // for logged in user, send results to sql database 
         if (!username.equals(""))
         {
            PreparedStatement preparedStatement;
            int attempt_details_id = 0;
            
            // build up strings with the information for the query
            String colNamesString = "(";
            String valuesString = "(";
            for (int i = 0; i < numProblems; ++i) {
               if (i == numProblems - 1) {
                  colNamesString += ("prob" + (i+1) + ", ans" + (i+1) + ", userans" + (i+1) + ")");
                  valuesString += ("'" + pracProblems.get(i).getEquation() +" ', " + pracProblems.get(i).getAnswer() + ", " + pracProblems.get(i).getUserAnswer() + ")");
               }
               else {
                  colNamesString += ("prob" + (i+1) + ", ans" + (i+1) + ", userans" + (i+1) + ", ");
                  valuesString += ("'" + pracProblems.get(i).getEquation() +" ', " + pracProblems.get(i).getAnswer() + ", " + pracProblems.get(i).getUserAnswer() + ", ");
               }
            }
            // sends attempt_details to sql database
            try {
               Connection connection = ConnectionManager.getConnection();
               String query = "insert into attempt_details " + colNamesString + " values " + valuesString;
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
            String query2 = "insert into attempt(username, attempt_type, operation, num_problems, num_correct, attempt_details_id) values (?,?,?,?,?,?)";
            String query3 = "update users set points= points + ? where username=?";
            try {
               // sends attempt information to sql database
               Connection connection = ConnectionManager.getConnection();
               preparedStatement = connection.prepareStatement(query2);
               preparedStatement.setString(1, username);
               preparedStatement.setString(2, "practice");
               preparedStatement.setString(3, operationString);
               preparedStatement.setInt(4, numProblems);
               preparedStatement.setInt(5, numCorrect);
               preparedStatement.setInt(6, attempt_details_id);
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
		
      // action for return button (returns to choosemode)
      returnBtn.setOnAction(e -> {
         ChooseMode chooseMode = new ChooseMode();
         chooseMode.start(practiceStage, username);
      });

      // set scene and put it on the stage
      Scene scene = new Scene(practiceStack, 500, 675);
      StyleClasses.getCSS(scene);      
		practiceStage.setScene(scene);		
		practiceStage.setTitle("Math Tutor - Practice Mode");
		practiceStage.show();	
	}
}

