package edu.hendrix.csci250.csci250proj4.gui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class PictoChat extends Application {
	@Override
	public void start(Stage primaryStage) {
		try {
			BorderPane root = (BorderPane)FXMLLoader.load(getClass().getResource("PictoChatGUI.fxml"));
			Scene scene = new Scene(root);
			primaryStage.setTitle("PictoChat");
			primaryStage.setScene(scene);
			primaryStage.show();
		} catch(Exception e) {
			e.printStackTrace();
			System.exit(1);
		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
