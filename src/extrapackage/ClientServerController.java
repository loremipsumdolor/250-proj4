package extrapackage;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Optional;

import edu.hendrix.csci250.csci250proj4.Server;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class ClientServerController {
	
	@FXML
	Button server;
	
	@FXML
	Button client;
	
	@FXML
	Button cancel;
	
	
	private Socket socket;
	private ObjectInputStream in;
    private ObjectOutputStream out;
    
    
	@FXML
	public void initialize() {
	}
	
	@FXML
	public void clickedServer() {
	}
	
	@FXML
	public void clickedClient() {
		TextInputDialog ipDialog = new TextInputDialog();
		ipDialog.setTitle("Enter IP Address");
		ipDialog.setHeaderText("Enter IP Address");
		ipDialog.setContentText("Please enter the IP of the server:");
		Optional<String> ipResult = ipDialog.showAndWait();
		if (ipResult.isPresent()) {
			try {
				socket = new Socket(ipResult.get(), 8888);
				out = new ObjectOutputStream(socket.getOutputStream());
				out.flush();
				in = new ObjectInputStream(socket.getInputStream());
			} catch (Exception e) {
				e.printStackTrace();
				outputMessage(AlertType.ERROR, e.getMessage());
				Platform.exit();
			    System.exit(0);
			}
		} else {
			Platform.exit();
		    System.exit(0);
		}
	}
	
	
	
	public void openCanvasScene() {
		try { 
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(this.getClass().getResource("CanvasGUI.fxml"));
			BorderPane root = (BorderPane) loader.load();
			
			//CanvasController canvasController = (CanvasController) loader.getController();
			//canvasController.getUserFromChatroomSelect(currentUser);
			
			Stage secondStage = new Stage();
			Scene scene = new Scene(root);
			
			secondStage.setOnCloseRequest(e -> {
				Platform.exit();
				System.exit(0);
			});
			secondStage.setTitle("Chatroom");
			secondStage.setScene(scene);
			secondStage.show();
			
			cancelClicked();
			
		} catch (Exception e) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Cannot Open Chatroom Window");
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
