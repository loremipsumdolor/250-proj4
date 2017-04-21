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
import javafx.scene.SnapshotParameters;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
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
import javafx.scene.control.ButtonType;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.input.MouseEvent;

public class PictoChatController {
	@FXML
	private MenuItem exitChatroomMenuItem;
	@FXML
	private MenuItem quitMenuItem;
	@FXML
	private ScrollPane chatroomScrollPane;
	@FXML
	private VBox chatroomContents;
	@FXML
	private Pane drawingCanvas;
	@FXML
	private Button sendButton;
	@FXML
	private ColorPicker chatColor;
	
	private ServerSocket serverSocket;
	private Socket server;
	private User user;
	private double sx;
	private double sy;

	@FXML
	private void initialize() {
		try {
			serverSocket = new ServerSocket(8888);
			serverSocket.setSoTimeout(180000);
		} catch (IOException e) {
			e.printStackTrace();
			outputMessage(AlertType.ERROR, e.getMessage());
			
		}
		chatColor.setValue(Color.BLACK);
		drawingCanvas.setOnMousePressed(event -> startDrag(event));
		drawingCanvas.setOnMouseDragged(event -> draw(event));
		drawingCanvas.setBackground(new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)));
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
			user = userResult.get();
		} else {
			Platform.exit();
			System.exit(0);
		}
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("Welcome to PictoChat");
		alert.setHeaderText("Welcome to PictoChat");
		alert.setContentText("Select a chatroom:\n\nA: 0/15\nB: 0/15\nC: 0/15\nD: 0/15\n");
		ButtonType buttonTypeA = new ButtonType("A");
		ButtonType buttonTypeB = new ButtonType("B");
		ButtonType buttonTypeC = new ButtonType("C");
		ButtonType buttonTypeD = new ButtonType("D");
		ButtonType buttonTypeCancel = new ButtonType("Quit", ButtonData.CANCEL_CLOSE);
		alert.getButtonTypes().setAll(buttonTypeA, buttonTypeB, buttonTypeC, buttonTypeD, buttonTypeCancel);
		Optional<ButtonType> result = alert.showAndWait();
		if (result.get() == buttonTypeA){
		} else if (result.get() == buttonTypeB) {
		} else if (result.get() == buttonTypeC) {
		} else if (result.get() == buttonTypeD) {
		} else {
			Platform.exit();
			System.exit(0);
		}
		new Thread(receiveMessage).start();
	}
	
	public void startDrag(MouseEvent event) {
		sx = event.getX();
		sy = event.getY();
	}
	
	public void draw(MouseEvent event) {
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
		line.setStroke(chatColor.getValue());
		line.setStrokeLineCap(StrokeLineCap.ROUND);
		line.setStrokeWidth(5);
		drawingCanvas.getChildren().add(line);
		sx = fx;
		sy = fy;
	}
	
	public void sendMessage() {
		WritableImage savedPane = drawingCanvas.snapshot(new SnapshotParameters(), null);
		chatroomContents.getChildren().add(new ImageView(savedPane));
		drawingCanvas.getChildren().clear();
		chatroomScrollPane.setVvalue(chatroomScrollPane.getVmax());
		Socket client;
		try {
			client = new Socket("localhost", 8888);
			ImageIO.write(SwingFXUtils.fromFXImage(savedPane, null), "jpg", client.getOutputStream());
		} catch (IOException e) {
			e.printStackTrace();
			outputMessage(AlertType.ERROR, e.getMessage());
		}
	}
	
	Task<Void> receiveMessage = new Task<Void>() {
        @Override
        protected Void call() throws Exception {
        	while(true) {
	            server = serverSocket.accept();
				BufferedImage img = ImageIO.read(ImageIO.createImageInputStream(server.getInputStream()));
				chatroomContents.getChildren().add(new ImageView(SwingFXUtils.toFXImage(img, null)));
        	}
        }
    };
	
	private void outputMessage(AlertType alertType, String message) {
		Alert alert = new Alert(alertType, message);
		alert.initOwner(drawingCanvas.getScene().getWindow());
		alert.showAndWait();
	}
	
}
