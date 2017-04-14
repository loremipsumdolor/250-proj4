package edu.hendrix.csci250.csci250proj4.gui;

import java.util.Optional;

import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ScrollPane;

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
		} else {
		}
	}

}
