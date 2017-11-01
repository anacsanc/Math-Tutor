package mathTutor;
import javafx.application.Application;
import javafx.geometry.*;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.MouseEvent;

import java.io.File;
import java.net.URL;
import java.sql.*;


// Opening menu for Application with User Registration and Sign in
public class MainWindow extends Application {
   private static Connection connection;                 
   private static String query;                         
   private static PreparedStatement preparedStatement;  
   private static String username = "";                  
   private static String password = "";
   private static String passwordConfirm = ""; 
   
	public void start(Stage primaryStage) {
      username = "";                   // resets username
      VBox homePane = new VBox();
      homePane.getStyleClass().add("background-window");
      // add title box
      VBox titleBox = new VBox();
      Label welcome = new Label("Welcome to");
      welcome.getStyleClass().add("h2");
      Label title = new Label("Math Tutor");
      title.getStyleClass().add("h1");
      titleBox.setAlignment(Pos.CENTER);
      titleBox.setMargin(welcome, new Insets(75,0,0,0));
      titleBox.getChildren().addAll(welcome, title);
      homePane.getChildren().add(titleBox);

      // Gridpane with options to register, sign in, or use without registering
      GridPane optionPane = new GridPane();
      optionPane.setPadding(new Insets(0, 30, 15, 30));
      optionPane.setAlignment(Pos.CENTER);
      optionPane.setHgap(20.0);
      optionPane.setVgap(25.0);
      optionPane.setPrefWidth(65.0);
      Button regOptionBtn = new Button("Register");
      regOptionBtn.getStyleClass().add("large-button");
      regOptionBtn.setMinWidth(optionPane.getPrefWidth());
      Button signOptionBtn = new Button("Sign In");
      signOptionBtn.setMinWidth(optionPane.getPrefWidth());
      signOptionBtn.getStyleClass().add("large-button");
      Label noSignInLbl = new Label("Use without Registering");
      noSignInLbl.getStyleClass().add("a-small");
      optionPane.setHalignment(noSignInLbl, HPos.CENTER);
      optionPane.add(regOptionBtn, 1, 2);
      optionPane.add(signOptionBtn, 2, 2);
      optionPane.add(noSignInLbl, 1,3,2,1);
      homePane.getChildren().add(optionPane);

      // gridpane with fields needed for new registration
      GridPane regPane = new GridPane();
      regPane.setPadding(new Insets(5,30,0,30));
      regPane.setAlignment(Pos.CENTER);
      regPane.setHgap(20.0);
      regPane.setVgap(5.0);
      Label regUserLabel = new Label("User name: ");
      Label regPwChooseLabel = new Label("Password: ");
      Label regPwConfirmLabel = new Label("Confirm password: ");
      regUserLabel.getStyleClass().add("h4");
      regPwChooseLabel.getStyleClass().add("h4");
      regPwConfirmLabel.getStyleClass().add("h4");
      TextField regUserField = new TextField();
      TextField regPwField = new TextField();
      TextField regPwConfirmField = new TextField();
      regUserField.getStyleClass().add("h5");
      regPwField.getStyleClass().add("h5");
      regPwConfirmField.getStyleClass().add("h5");
      Button regBtn = new Button("Register");
      regBtn.getStyleClass().add("small-button");
      Button regCancelBtn = new Button("Cancel");
      regCancelBtn.getStyleClass().add("small-button");
      HBox regBtnBox = new HBox();
      regBtnBox.setAlignment(Pos.CENTER);
      regBtnBox.setSpacing(20.0);
      regBtnBox.getChildren().addAll(regCancelBtn, regBtn);
      regPane.add(regUserLabel, 1,1);
      regPane.add(regUserField, 2,1);
      regPane.add(regPwChooseLabel, 1,2);
      regPane.add(regPwField, 2,2);
      regPane.add(regPwConfirmLabel, 1,3);
      regPane.add(regPwConfirmField, 2, 3);
      regPane.setMargin(regBtnBox, new Insets(15,0,0,0));
      regPane.add(regBtnBox, 1, 4, 2, 1);
      
      // gridpane with fields needed to sign in
      GridPane signPane = new GridPane();
      signPane.setPadding(new Insets(5,30,3,30));
      signPane.setAlignment(Pos.CENTER);
      signPane.setHgap(30.0);
      signPane.setVgap(8.0);
      Label signUserLabel = new Label("User name:");
      Label signPwLabel = new Label("Password:");
      signUserLabel.getStyleClass().add("h4");
      signPwLabel.getStyleClass().add("h4");
      TextField signUserField = new TextField();
      TextField signPwField = new TextField();
      signUserField.getStyleClass().add("h5");
      signPwField.getStyleClass().add("h5");
      Button signCancelBtn = new Button("Cancel");
      Button signBtn = new Button("Sign In");
      signCancelBtn.getStyleClass().add("small-button");
      signBtn.getStyleClass().add("small-button");
      HBox signBtnBox = new HBox();
      signBtnBox.setAlignment(Pos.CENTER);
      signBtnBox.setSpacing(15.0);
      signBtnBox.getChildren().addAll(signCancelBtn, signBtn);
      signPane.add(signUserLabel, 1,1);
      signPane.add(signUserField, 2, 1);
      signPane.add(signPwLabel, 1, 2);
      signPane.add(signPwField, 2,2);
      signPane.setMargin(signBtnBox, new Insets(10,0,0,0));
      signPane.add(signBtnBox, 1,4,2,1);
      
      //App image
      HBox imgBox = new HBox();
      imgBox.setAlignment(Pos.CENTER);
      ImageView appImg = new ImageView();
      appImg.getStyleClass().add("app-icon");
      imgBox.getChildren().add(appImg);
      homePane.getChildren().add(imgBox);
      
      // action for button to request registration
      regOptionBtn.setOnAction(e-> {
         homePane.getChildren().clear();
         homePane.getChildren().addAll(titleBox, regPane, imgBox);
      });
      
      // action for button to request sign in
      signOptionBtn.setOnAction(e-> {
         homePane.getChildren().clear();
         homePane.getChildren().addAll(titleBox, signPane, imgBox);
      });
      
      // action for registration cancel button
      regCancelBtn.setOnAction(e-> {
         homePane.getChildren().clear();
         homePane.getChildren().addAll(titleBox, optionPane, imgBox);
      });
      
      // action for sign in cancel button
      signCancelBtn.setOnAction(e-> {
         homePane.getChildren().clear();
         homePane.getChildren().addAll(titleBox, optionPane, imgBox);
      });
      
      // action for label to use without registering
      noSignInLbl.setOnMouseClicked(e-> {
         ChooseMode chooseMode = new ChooseMode();
         chooseMode.start(primaryStage, username);
      });
      
      // action for registration button including input validation
      regBtn.setOnAction(e-> {
         username = regUserField.getText();
         password = regPwField.getText();
         passwordConfirm = regPwConfirmField.getText();
         
         // make sure user name uses only valid characters
         if (username.isEmpty() || !username.matches("[A-Za-z0-9_]+")) {
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("Invalid user name");
            alert.setHeaderText("Invalid user name");
            alert.setContentText("Please enter a valid user name using letters, numbers, and underscore.");
            alert.show();
            return;
         } 
         
         // make sure password and confirm password have been entered
         if (password.isEmpty() || passwordConfirm.isEmpty()) {
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("Enter password");
            alert.setHeaderText("Enter password");
            alert.setContentText("Please make entry in password and confirm password.");
            alert.show();
            return;
         }
         
         // make sure password and confirm password match
         if (!password.equals(passwordConfirm)) {
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("Password mismatch");
            alert.setHeaderText("Password mismatch");
            alert.setContentText("Password and confirm password mismatch. Please try again.");
            alert.show();
            return;
         }
         
         // make sure user name has not already been created
         connection = ConnectionManager.getConnection();
         query = "select count(*) from users where username=?";
         
         try {
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, username);
            ResultSet result = preparedStatement.executeQuery();
            if (result.next()) {
               if (result.getInt(1) != 0) {
                  Alert alert = new Alert(AlertType.INFORMATION);
                  alert.setTitle("User name already exists");
                  alert.setHeaderText("User name already exists");
                  alert.setContentText("User name already exists. Please try again.");
                  alert.show();
                  connection.close();
                  return;
               }
            }
         } catch (SQLException ex) {
            System.out.println("SQL query failed in registration check");
            System.exit(1);
         }
         
         // if valid registration information...
         // add username and password to database and move ahead
         connection = ConnectionManager.getConnection();
         query = "insert into users(username, password) values (?, ?)";
         try {
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, password);
            preparedStatement.execute();
            connection.close();
            ChooseMode chooseMode = new ChooseMode();
            chooseMode.start(primaryStage, username);
         } catch (SQLException ex) {
            System.out.println("SQL query failed in user registration");
            System.exit(1);
         }
      });
      
      // action for sign in button including input validation
      signBtn.setOnAction(e-> {
         username = signUserField.getText();
         password = signPwField.getText();
         
         // make sure user name has been entered
         if (username.isEmpty()) {
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("Enter user name");
            alert.setHeaderText("Enter user name");
            alert.setContentText("Please enter your user name.");
            alert.show();
            return;
         } 
         
         // make sure password has been entered
         if (password.isEmpty()) {
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("Enter password");
            alert.setHeaderText("Enter password");
            alert.setContentText("Please enter your password.");
            alert.show();
            return;
         }
         
         // make sure user name exists in database
         connection = ConnectionManager.getConnection();
         query = "select count(*) from users where username=?";
         
         try {
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, username);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
               if (resultSet.getInt(1) == 0) {
                  Alert alert = new Alert(AlertType.INFORMATION);
                  alert.setTitle("User name does not exist");
                  alert.setHeaderText("User name does not exist");
                  alert.setContentText("User name does not exist. Please try again.");
                  alert.show();
                  connection.close();
                  return;
               }
            }
         } catch (SQLException ex) {
            System.out.println("SQL query failed in sign in username check");
            System.exit(1);
         }
         
         // check username and password against database
         connection = ConnectionManager.getConnection();
         query = "select password from users where username=?";
         try {
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, username);
            ResultSet result = preparedStatement.executeQuery();
            if (result.next()) {
               if (result.getString(1).equals(password)) {
                  connection.close();
                  ChooseMode chooseMode = new ChooseMode();
                  chooseMode.start(primaryStage, username);
               } else {
                  Alert alert = new Alert(AlertType.INFORMATION);
                  alert.setTitle("Sign in failed");
                  alert.setHeaderText("Sign in failed");
                  alert.setContentText("Sign in failed. Please check credentials and try again.");
                  alert.show();
                  connection.close();
               }
            }
         } catch (SQLException ex) {
            System.out.println("SQL query failed in sign in");
            System.exit(1);
         }
      });
      
      // set scene and put on stage
		Scene scene = new Scene(homePane, 500, 600);
		StyleClasses.getCSS(scene);
		primaryStage.setTitle("Welcome to Math Tutor!");
		primaryStage.setScene(scene);
		primaryStage.show();
	}
   
	public static void main(String[] args) {
		launch(args);
	}

}
