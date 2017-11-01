package mathTutor;
import javafx.application.Application;
import javafx.geometry.*;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.Toggle;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.control.Labeled;
import javafx.scene.paint.Paint;
import javafx.scene.paint.Color;


// Allows user to select criteria for Practice Mode
public class PracticeOptions {
	String cssUrl = "mainWindow.css";   
   int operation = 0;      // 0 = add, 1= mult, 2 = sub, 3 = div
   int opMin = 0;          // minimum value for operand
   int opMax = 20;         // maximum value for operand
   int numProblems = 10;   // number of problems
   int factor = 1;         // factor (used for multiplication and division)
   boolean valid = true;   // checks for valid options
   
	public void chooseOptions(Stage optionsStage, String username) {
      
      VBox optionsPane = new VBox();
      optionsPane.getStyleClass().add("background-window");
      // add userpane with user information
      UserPane userPane = new UserPane();
      BorderPane userBorderPane = userPane.get(optionsStage, username);
      optionsPane.getChildren().add(userBorderPane);
      
      // add title box
      VBox titleBox = new VBox();
      Label title = new Label("Math Tutor - Practice Mode");
      title.getStyleClass().add("h3");
      titleBox.setAlignment(Pos.CENTER);
      titleBox.getChildren().add(title);
      optionsPane.setMargin(titleBox, new Insets(20,0,0,0));
      optionsPane.getChildren().add(titleBox); 
      
         
      // gridpane with buttons to select operation
      GridPane opPane = new GridPane();
      opPane.setAlignment(Pos.CENTER);
      opPane.setPadding(new Insets(10, 10, 20, 10));
      opPane.setHgap(5.0);
      opPane.setVgap(5.0);
      opPane.setPrefWidth(95.0);
      Label opLabel = new Label("Select operation: ");
      opLabel.getStyleClass().add("h4");
      ToggleButton addBtn = new ToggleButton("Addition");
      addBtn.setMinWidth(opPane.getPrefWidth());
      addBtn.getStyleClass().add("toggle-button");
      ToggleButton subBtn = new ToggleButton("Subtraction");
      subBtn.setMinWidth(opPane.getPrefWidth());
      subBtn.getStyleClass().add("toggle-button");
      ToggleButton multBtn = new ToggleButton("Multiplication");
      multBtn.setMinWidth(opPane.getPrefWidth());
      multBtn.getStyleClass().add("toggle-button");
      ToggleButton divBtn = new ToggleButton("Division");
      divBtn.setMinWidth(opPane.getPrefWidth());
      divBtn.getStyleClass().add("toggle-button");
      ToggleGroup opGroup = new ToggleGroup();
      multBtn.setToggleGroup(opGroup);
      addBtn.setToggleGroup(opGroup);
      subBtn.setToggleGroup(opGroup);
      divBtn.setToggleGroup(opGroup);
      addBtn.setSelected(true);
      opPane.add(opLabel, 1, 0, 2, 1);
      opPane.setHalignment(opLabel, HPos.CENTER);
      opPane.add(addBtn, 1, 1);
      opPane.add(subBtn, 2, 1);
      opPane.add(multBtn,1,2);
      opPane.add(divBtn,2,2);
      optionsPane.getChildren().add(opPane);
      
      // gridpane with radiobuttons to select factor (used for multiplication and divison)
      GridPane factorPane = new GridPane();
      factorPane.getStyleClass().add("op-background");
      factorPane.setAlignment(Pos.CENTER);
      factorPane.setPadding(new Insets(20, 10, 20, 10));
      factorPane.setHgap(5.0);
      factorPane.setVgap(5.0);
      Label factorLabel = new Label("Select factor:");
      factorLabel.getStyleClass().add("h5");
      RadioButton rb1 = new RadioButton("1");
      RadioButton rb2 = new RadioButton("2");
      RadioButton rb3 = new RadioButton("3");
      RadioButton rb4 = new RadioButton("4");
      RadioButton rb5 = new RadioButton("5");
      RadioButton rb6 = new RadioButton("6");
      RadioButton rb7 = new RadioButton("7");
      RadioButton rb8 = new RadioButton("8");
      RadioButton rb9 = new RadioButton("9");
      RadioButton rb10 = new RadioButton("10");
      ToggleGroup factorGroup = new ToggleGroup();
      rb1.setToggleGroup(factorGroup);
      rb2.setToggleGroup(factorGroup);
      rb3.setToggleGroup(factorGroup);
      rb4.setToggleGroup(factorGroup);
      rb5.setToggleGroup(factorGroup);
      rb6.setToggleGroup(factorGroup);
      rb7.setToggleGroup(factorGroup);
      rb8.setToggleGroup(factorGroup);
      rb9.setToggleGroup(factorGroup);
      rb10.setToggleGroup(factorGroup);
      factorPane.setHalignment(factorLabel, HPos.CENTER);
      factorPane.add(factorLabel, 0, 0, 10, 1);
      factorPane.add(rb1, 0, 1);
      factorPane.add(rb2, 1, 1);
      factorPane.add(rb3, 2, 1);
      factorPane.add(rb4, 3, 1);
      factorPane.add(rb5, 4, 1);
      factorPane.add(rb6, 5, 1);
      factorPane.add(rb7, 6, 1);
      factorPane.add(rb8, 7, 1);
      factorPane.add(rb9, 8, 1);
      factorPane.add(rb10, 9, 1);
      rb1.setSelected(true);
     
      // vbox with sliders to select min and max operands (used for addition and subtraction)
      VBox rangePane = new VBox();
      rangePane.getStyleClass().add("op-background");
      rangePane.setPadding(new Insets(10, 35, 10, 35));
      rangePane.setSpacing(5.0);
      rangePane.setAlignment(Pos.CENTER);
      Label opMinLabel = new Label("Select minimum operand:");
      opMinLabel.getStyleClass().add("h5");
      Label opMaxLabel = new Label("Select maximum operand:");
      opMaxLabel.getStyleClass().add("h5");
      Slider opMinSlider = new Slider(0, 20, 0);
      opMinSlider.getStyleClass().add("h5");
      opMinSlider.setMajorTickUnit(5);
      opMinSlider.setBlockIncrement(1);
      opMinSlider.setShowTickLabels(true);
      opMinSlider.setShowTickMarks(true);
      opMinSlider.setSnapToTicks(true);
      Slider opMaxSlider = new Slider(0, 20, 20);
      opMaxSlider.getStyleClass().add("h5");
      opMaxSlider.setMajorTickUnit(5);
      opMaxSlider.setBlockIncrement(1);
      opMaxSlider.setShowTickLabels(true);
      opMaxSlider.setShowTickMarks(true);
      opMaxSlider.setSnapToTicks(true);
      rangePane.getChildren().addAll(opMinLabel, opMinSlider, opMaxLabel, opMaxSlider);
      optionsPane.getChildren().add(rangePane);
      
      // vbox with slider to select number of questions
      VBox numPane = new VBox();
      numPane.getStyleClass().add("op-background");
      numPane.setPadding(new Insets(10, 35, 10, 35));
      numPane.setSpacing(5.0);
      numPane.setAlignment(Pos.CENTER);
      Label numLabel = new Label("Select number of problems:");
      numLabel.getStyleClass().add("h5");
      Slider numSlider = new Slider(0, 10, 10);
      numSlider.getStyleClass().add("h5");
      numSlider.setMajorTickUnit(5);
      numSlider.setBlockIncrement(1);
      numSlider.setShowTickLabels(true);
      numSlider.setShowTickMarks(true);
      numSlider.setSnapToTicks(true);
      numPane.getChildren().addAll(numLabel, numSlider);
      optionsPane.getChildren().add(numPane);

      // start button
      VBox startBox = new VBox();
      Button startBtn = new Button("Start");
      startBtn.getStyleClass().add("large-button");
      startBtn.setPrefSize(100,50);
      startBox.setAlignment(Pos.CENTER);
      startBox.getChildren().add(startBtn);
      optionsPane.setMargin(startBox, new Insets(15,0,20,0));
      optionsPane.getChildren().add(startBox);
      
      // field for error message
      HBox errorBox = new HBox();
      Labeled errorLabel = new Label("");
      errorLabel.setTextFill(Color.RED);
      errorBox.setAlignment(Pos.CENTER);
      errorBox.getChildren().add(errorLabel);
      optionsPane.setMargin(errorBox, new Insets(5,0,0,0));
      optionsPane.getChildren().add(errorBox);
      
      // set action for addition button
      addBtn.setOnAction(e -> {
         operation = 0;
         optionsPane.getChildren().clear();
         optionsPane.getChildren().addAll(userBorderPane, titleBox, opPane, rangePane, numPane, startBox);
      });
      
      // set action for subtraction button
      subBtn.setOnAction(e -> {
         operation = 2;
         optionsPane.getChildren().clear();
         optionsPane.getChildren().addAll(userBorderPane, titleBox, opPane, rangePane, numPane, startBox);
      });
      
      // set action for multiplication button
      multBtn.setOnAction(e -> {
         operation = 1;
         optionsPane.getChildren().clear();
         optionsPane.getChildren().addAll(userBorderPane, titleBox, opPane, factorPane, numPane, startBox);
      });
      
      // set action for division button
      divBtn.setOnAction(e -> {
         operation = 3;
         optionsPane.getChildren().clear();
         optionsPane.getChildren().addAll(userBorderPane, titleBox, opPane, factorPane, numPane, startBox);
      });

		// set action for start button
		startBtn.setOnAction(e -> {
         
         // get values from buttons and sliders
         RadioButton selected = (RadioButton)factorGroup.getSelectedToggle();
         factor = Integer.parseInt(selected.getText());
         opMin = (int)opMinSlider.getValue();
         opMax = (int)opMaxSlider.getValue();
         numProblems = (int)numSlider.getValue();
         
         // make sure inputs are valid, and to to practice mode
         if (opMin > opMax) {
            errorLabel.setText("Minimum operand cannot be greater than maximum operand!");
            opMaxSlider.setValue(opMaxSlider.getMax());
         } else if (numProblems < 1) {
            errorLabel.setText("Number of problems cannot be zero!");
            numSlider.setValue(1.0);
         } else {
            PracticeMode practice = new PracticeMode();
   			practice.start(optionsStage, username, operation, factor, numProblems, opMin, opMax);
         }
		});
      
      // set scene and put on stage
		Scene scene = new Scene(optionsPane, 500, 600);
		StyleClasses.getCSS(scene);
		optionsStage.setTitle("Math Tutor - Practice Mode Options");
		optionsStage.setScene(scene);
		optionsStage.show();
	}
}