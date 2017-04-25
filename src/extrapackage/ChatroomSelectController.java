package extrapackage;

import edu.hendrix.csci250.csci250proj4.User;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

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
	
	
	
	
}
