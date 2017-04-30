package extrapackage;

import edu.hendrix.csci250.csci250proj4.User;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class ChatroomSelectController {
	
	@FXML
	Label roomASize;
	
	@FXML
	Label roomBSize;
	
	@FXML
	Label roomCSize;
	
	@FXML
	Label roomDSize;
	
	@FXML
	Button selectRoomA;
	
	@FXML
	Button selectRoomB;
	
	@FXML
	Button selectRoomC;
	
	@FXML
	Button selectRoomD;
	
	@FXML
	Button quit;
	
	User currentUser;
	
	@FXML
	public void initialize() {
		//TODO Check number of people in chatrooms and display accordingly
	}
	
	
	
	@FXML
	public void quitClicked() {
		quit.getScene().getWindow().hide();
	}
	
	public void getUserFromSettings(User currentUser) {
		this.currentUser = currentUser;
	}
	
	@FXML
	public void openCanvasScene() {
		try { 
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(this.getClass().getResource("CanvasGUI.fxml"));
			BorderPane root = (BorderPane) loader.load();
			
			CanvasController canvasController = (CanvasController) loader.getController();
			canvasController.getUserFromChatroomSelect(currentUser);
			
			Stage secondStage = new Stage();
			Scene scene = new Scene(root);
			
			secondStage.setOnCloseRequest(e -> {
				Platform.exit();
				System.exit(0);
			});
			secondStage.setTitle("Chatroom");
			secondStage.setScene(scene);
			secondStage.show();
			
			quitClicked();
			
		} catch (Exception e) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Cannot Open Chatroom Window");
			alert.setHeaderText("Oops, something has gone wrong!");
			alert.showAndWait();
			e.printStackTrace();
		}
		
	} 
	
	
	
	
}
