package mathTutor;

import javafx.application.Application;
import javafx.geometry.*;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.scene.input.MouseEvent;

// Allows user to select Practice Mode, Quiz Mode, or User History (if signed in)
public class ChooseMode { 
	public void start(Stage chooseModeStage, String username) {
      
      VBox chooseModePane = new VBox();
      chooseModePane.getStyleClass().add("background-window");
      // add user pane with user information
      UserPane userPane = new UserPane();
      chooseModePane.getChildren().add(userPane.get(chooseModeStage, username));
      // add title box
      VBox titleBox = new VBox();
      Label welcome = new Label("Welcome to");
      welcome.getStyleClass().add("h2");
      Label title = new Label("Math Tutor");
      title.getStyleClass().add("h1");
      titleBox.setAlignment(Pos.CENTER);
      titleBox.setMargin(welcome, new Insets(80,0,0,0));
      titleBox.getChildren().addAll(welcome, title);
      chooseModePane.getChildren().add(titleBox);
      
      // add grid pane with buttons for each choice
      GridPane modePane = new GridPane();
      modePane.setPadding(new Insets(30, 30, 30, 30));
      modePane.setAlignment(Pos.CENTER);
      modePane.setHgap(10.0);
      modePane.setVgap(10.0);
      modePane.setPrefWidth(95.0);
      VBox modeLabelBox = new VBox();
      Label modeLabel = new Label("Select option to begin:");
      modeLabel.getStyleClass().add("h4");
      modeLabelBox.setAlignment(Pos.CENTER);
      modeLabelBox.setMargin(modeLabel, new Insets(0,0,15,0));
      modeLabelBox.getChildren().add(modeLabel);
      Button pracBtn = new Button("Practice Mode");
      pracBtn.setMinWidth(modePane.getPrefWidth());
      pracBtn.getStyleClass().add("small-mult-button");
      Button quizBtn = new Button("Quiz Mode");
      quizBtn.setMinWidth(modePane.getPrefWidth());
      quizBtn.getStyleClass().add("small-mult-button");
      Button histBtn = new Button("User History");
      histBtn.setMinWidth(modePane.getPrefWidth());
      histBtn.getStyleClass().add("small-mult-button");
      Button leaderBtn = new Button("Leaderboard");
      leaderBtn.setMinWidth(modePane.getPrefWidth());
      leaderBtn.getStyleClass().add("small-mult-button");
      modePane.add(modeLabelBox, 1, 0, 3, 1);
      modePane.add(pracBtn, 1, 1);
      modePane.add(quizBtn, 2, 1);
      if (!username.equals("")) {
         modePane.add(histBtn, 1, 2);
         modePane.add(leaderBtn, 2, 2);
         }
      else {
         modePane.setHalignment(leaderBtn, HPos.CENTER);
         modePane.add(leaderBtn,1,2,2,1);
         }
      chooseModePane.getChildren().add(modePane);
      
      // action for practice mode button
      pracBtn.setOnAction(e-> {
         PracticeOptions options = new PracticeOptions();
         options.chooseOptions(chooseModeStage, username);
      });
      
      // action for quiz mode button 
      quizBtn.setOnAction(e-> {
         QuizMode quiz = new QuizMode();
         quiz.start(chooseModeStage, username);
      });
      
      // action for user history button
      histBtn.setOnAction(e-> {
         UserHistory history = new UserHistory();
         history.show(chooseModeStage, username);
      });
      
      // action for leaderboard button
      leaderBtn.setOnAction(e-> {
         LeaderBoard leaderboard = new LeaderBoard();
         leaderboard.show(chooseModeStage, username);
      });
      
      // set scene and put on stage
		Scene scene = new Scene(chooseModePane, 500, 600);
		StyleClasses.getCSS(scene);
      chooseModeStage.setTitle("Math Tutor - Choose Mode");
		chooseModeStage.setScene(scene);
		chooseModeStage.show();
	}
}