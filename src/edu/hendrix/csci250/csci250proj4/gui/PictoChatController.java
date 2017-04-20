package edu.hendrix.csci250.csci250proj4.gui;

import java.util.Optional;

import javax.imageio.ImageIO;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import edu.hendrix.csci250.csci250proj4.User;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.StrokeLineCap;
import javafx.stage.Stage;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;

public class PictoChatController {
	@FXML
	private MenuItem exitChatroomMenuItem;
	@FXML
	private MenuItem quitMenuItem;
	@FXML
	private VBox chatroomContents;
	@FXML
	private Pane drawingCanvas;
	
	private ServerSocket serverSocket;
	private Socket server;

	@FXML
	private void initialize() {
		try {
			serverSocket = new ServerSocket(8888);
			serverSocket.setSoTimeout(180000);
		} catch (IOException e) {
			e.printStackTrace();
			outputMessage(AlertType.ERROR, e.getMessage());
			Platform.exit();
			System.exit(0);
		}
		drawingCanvas.setOnMousePressed(event -> draw(event));
		drawingCanvas.setOnMouseDragged(event -> draw(event));
		drawingCanvas.setBackground(new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)));
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
		} else {
			Platform.exit();
			System.exit(0);
		}
		afterInitialize();
	}
	
	private void afterInitialize() {
		((Node)drawingCanvas.getScene()).setOnKeyPressed(event -> {if(event.getCode().equals(KeyCode.ENTER)){sendMessage();}});
	}
	
	public void draw(MouseEvent event) {
		double sx = event.getX();
		double sy = event.getY();
		double fx = event.getX();
		double fy = event.getY();
		double canvasWidth = drawingCanvas.getWidth();
		double canvasHeight = drawingCanvas.getHeight();
		if (fx < 0) {
			fx = 0;
		} else if (fx > canvasWidth) {
			fx = canvasWidth;
		}
		if (fy < 0) {
			fy = 0;
		} else if (fy > canvasHeight) {
			fy = canvasHeight;
		}
		Line line = new Line(sx, sy, fx, fy);
		line.setStroke(Color.BLACK);
		line.setStrokeLineCap(StrokeLineCap.ROUND);
		line.setStrokeWidth(5);
		drawingCanvas.getChildren().add(line);
		sx = fx;
		sy = fy;
	}
	
	public void sendMessage() {
		Socket client;
		try {
			client = new Socket("localhost", 8888);
			WritableImage savedPane = drawingCanvas.snapshot(new SnapshotParameters(), null);
			ImageIO.write(SwingFXUtils.fromFXImage(savedPane, null), "jpg", client.getOutputStream());
		} catch (IOException e) {
			e.printStackTrace();
			outputMessage(AlertType.ERROR, e.getMessage());
		}
	}
	
	Task<Void> receiveMessage = new Task<Void>() {
        @Override
        protected Void call() throws Exception {
            server = serverSocket.accept();
			BufferedImage img = ImageIO.read(ImageIO.createImageInputStream(server.getInputStream()));
			chatroomContents.getChildren().add(new ImageView(SwingFXUtils.toFXImage(img, null)));
            return null;
        }
    };
	
	private void outputMessage(AlertType alertType, String message) {
		Alert alert = new Alert(alertType, message);
		alert.initOwner(drawingCanvas.getScene().getWindow());
		alert.showAndWait();
	}
	
}
