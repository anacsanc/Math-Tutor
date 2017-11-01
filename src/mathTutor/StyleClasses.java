package mathTutor;

import javafx.scene.Scene;

public class StyleClasses {
	
	public static String cssUrl = "mainWindow.css";
	
	public static Scene getCSS(Scene s){
		s.getStylesheets().add(MainWindow.class.getResource(cssUrl).toExternalForm());
		return s;
	}
}
