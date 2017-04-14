package edu.hendrix.csci250.csci250proj4.gui;

import java.util.Optional;

import edu.hendrix.csci250.csci250proj4.User;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.layout.GridPane;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;

public class PictoChatController {
	@FXML
	private ScrollPane chatroomContents;
	@FXML
	private Canvas drawingCanvas;
	
	private GraphicsContext gc;

	public void initialize() {
		gc = drawingCanvas.getGraphicsContext2D();
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("Welcome to PictoChat");
		alert.setHeaderText("Welcome to PictoChat");
		alert.setContentText("Select a chatroom:\n\nA: 0/15\nB: 0/15\nC: 0/15\nD: 0/15\n");
		ButtonType buttonTypeA = new ButtonType("A");
		ButtonType buttonTypeB = new ButtonType("B");
		ButtonType buttonTypeC = new ButtonType("C");
		ButtonType buttonTypeD = new ButtonType("D");
		ButtonType buttonTypeSettings = new ButtonType("Settings");
		ButtonType buttonTypeCancel = new ButtonType("Quit", ButtonData.CANCEL_CLOSE);
		alert.getButtonTypes().setAll(buttonTypeA, buttonTypeB, buttonTypeC, buttonTypeD, buttonTypeSettings, buttonTypeCancel);
		Optional<ButtonType> result = alert.showAndWait();
		if (result.get() == buttonTypeA){
		} else if (result.get() == buttonTypeB) {
		} else if (result.get() == buttonTypeC) {
		} else if (result.get() == buttonTypeD) {
		} else if (result.get() == buttonTypeSettings) {
			Dialog<User> dialog = new Dialog<>();
			dialog.initOwner(drawingCanvas.getScene().getWindow());
			dialog.setTitle("Settings");
			dialog.setHeaderText("PictoChat Settings");
			ButtonType buttonTypeSave = new ButtonType("Save", ButtonData.OK_DONE);
			dialog.getDialogPane().getButtonTypes().addAll(buttonTypeSave, ButtonType.CANCEL);
			GridPane grid = new GridPane();
			grid.setHgap(10);
			grid.setVgap(10);
			TextField name = new TextField();
			DatePicker birthdayPicker = new DatePicker();
			ColorPicker favoriteColor = new ColorPicker();
			grid.add(new Label("Name:"), 0, 0);
			grid.add(name, 1, 0);
			grid.add(new Label("Birthday:"), 0, 1);
			grid.add(birthdayPicker, 1, 1);
			grid.add(new Label("Color:"), 0, 2);
			grid.add(favoriteColor, 1, 2);
			dialog.getDialogPane().setContent(grid);
			dialog.setResultConverter(dialogButton -> {
			    if (dialogButton == buttonTypeSave) {
			        return new User(name.getText(), birthdayPicker.getValue(), favoriteColor.getValue());
			    }
			    return null;
			});
			Optional<User> userResult = dialog.showAndWait();
			if (userResult.isPresent()){
			} else {
			}
		}
	}
}
