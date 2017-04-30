package extrapackage;

import edu.hendrix.csci250.csci250proj4.User;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.scene.control.Alert.AlertType;

public class SettingsController {
	
	@FXML
	TextField name;
	
	@FXML
	DatePicker birthday;
	
	@FXML
	ColorPicker color; 
	
	@FXML
	Button save;
	
	@FXML
	Button cancel;
	
	private User currentUser; 
	
	@FXML
	public void initialize() {}
	
	@FXML
	public void saveClicked() {
		if (name.getText() != "" && birthday.getValue() != null) {
			currentUser = new User(name.getText(), birthday.getValue(), color.getValue());
			openChatroomSelectScene();
			cancelClicked();
		} else {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Missing Values");
			alert.setHeaderText("Please key in the information required!");
			alert.setContentText("The name field and the birthday field are required!");
			alert.showAndWait();
		}
	}
	
	@FXML
	public void openChatroomSelectScene() {
		try { 
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(this.getClass().getResource("ChatroomSelectGUI.fxml"));
			Pane root = (Pane) loader.load();
			
			ChatroomSelectController CSController = (ChatroomSelectController) loader.getController();
			CSController.getUserFromSettings(currentUser);
			System.out.println(currentUser.getFavoriteColor());
			
			Stage secondStage = new Stage();
			Scene scene = new Scene(root);
			
			secondStage.setOnCloseRequest(e -> {
				Platform.exit();
				System.exit(0);
			});
			secondStage.setTitle("Select Chatroom");
			secondStage.setScene(scene);
			secondStage.show();
			
		} catch (Exception e) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Cannot Open Chatroom Select Window");
			alert.setHeaderText("Oops, something has gone wrong!");
			alert.showAndWait();
			e.printStackTrace();
		}
		
	}
	
	
	@FXML
	public void cancelClicked() {
		cancel.getScene().getWindow().hide();
	}
	
	
	

	
}
