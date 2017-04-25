package extrapackage;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class StartGUI extends Application {

	@Override
	public void start(Stage primaryStage) throws Exception {
		Pane root = (Pane)FXMLLoader.load(getClass().getResource("SettingsGUI.fxml"));
		Scene scene = new Scene(root);
		primaryStage.setTitle("PictoChat");
		primaryStage.setScene(scene);
		primaryStage.show();
	}
	
	public static void main(String[] args) {
		launch(args);
	}
	
}
