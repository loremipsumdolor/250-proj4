package edu.hendrix.csci250.csci250proj4.gui;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Optional;

import javax.swing.ImageIcon;

import edu.hendrix.csci250.csci250proj4.Server;
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
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.StrokeLineCap;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.input.MouseEvent;

public class PictoChatController {
	@FXML
	private MenuItem exitMenuItem;
	@FXML
	private MenuItem aboutMenuItem;
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

	private User user;
	private double sx;
	private double sy;
	
	private Socket socket;
	private ObjectInputStream in;
    private ObjectOutputStream out;

	@FXML
	private void initialize() {
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
		Alert clientServerDialog = new Alert(AlertType.CONFIRMATION);
		clientServerDialog.setTitle("Client/Server Selection");
		clientServerDialog.setHeaderText("Client/Server Selection");
		clientServerDialog.setContentText("Choose your option.");
		ButtonType buttonTypeServer = new ButtonType("Server");
		ButtonType buttonTypeClient = new ButtonType("Client");
		ButtonType buttonTypeCancel = new ButtonType("Cancel", ButtonData.CANCEL_CLOSE);
		clientServerDialog.getButtonTypes().setAll(buttonTypeServer, buttonTypeClient, buttonTypeCancel);
		Optional<ButtonType> buttonClicked = clientServerDialog.showAndWait();
		if (buttonClicked.get() == buttonTypeServer) {
			Task<Void> server = new Task<Void>() {
		        @Override
		        protected Void call() throws Exception {
		        	while(true) {
			        	Server.main(null);
		        	}
		        }
			};
			new Thread(server).start();
			try {
				socket = new Socket("0.0.0.0", 8888);
				out = new ObjectOutputStream(socket.getOutputStream());
				out.flush();
				in = new ObjectInputStream(socket.getInputStream());
			} catch (Exception e) {
				e.printStackTrace();
				outputMessage(AlertType.ERROR, e.getMessage());
				Platform.exit();
			    System.exit(0);
			}
		} else if (buttonClicked.get() == buttonTypeClient) {
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
		} else {
		    Platform.exit();
		    System.exit(0);
		}
		exitMenuItem.setOnAction(event -> {
			Platform.exit();
			System.exit(0);
		});
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
		Rectangle rec = new Rectangle(0, 0, drawingCanvas.getWidth(), drawingCanvas.getHeight());
		rec.setStroke(user.getFavoriteColor());
		rec.setFill(Color.TRANSPARENT);
		rec.setStrokeWidth(5);
		drawingCanvas.getChildren().add(rec);
		WritableImage savedPane = drawingCanvas.snapshot(new SnapshotParameters(), null);
		drawingCanvas.getChildren().clear();
		chatroomScrollPane.setVvalue(chatroomScrollPane.getVmax());
		try {
			out.writeObject(new ImageIcon(SwingFXUtils.fromFXImage(savedPane, null)));
			out.flush();
		} catch (IOException e) {
			e.printStackTrace();
			outputMessage(AlertType.ERROR, e.getMessage());
		}
	}
	
	Task<Void> receiveMessage = new Task<Void>() {
        @Override
        protected Void call() throws Exception {
        	while (true) {
                Object input = in.readObject();
                if (input == null) {} else {
                	try {
	                	ImageIcon imgIcon = (ImageIcon)input;
	                	java.awt.Image img = imgIcon.getImage();
	                	BufferedImage bimage = new BufferedImage(img.getWidth(null), img.getHeight(null), BufferedImage.TYPE_INT_ARGB);
	                    Graphics2D bGr = bimage.createGraphics();
	                    bGr.drawImage(img, 0, 0, null);
	                    bGr.dispose();
	                    WritableImage img2 = SwingFXUtils.toFXImage(bimage, null);
	                	addImage(img2);
                	} catch (Exception e) {
                		e.printStackTrace();
                	}
                }
        	}
        }
    };

    public void addImage(WritableImage img) {
    	Platform.runLater(new Runnable() {
			@Override
			public void run() {
				chatroomContents.getChildren().add(new ImageView(img));
			}
    	});
    }
    
	@FXML
	private void aboutDialog() {
		Alert aboutBox = new Alert(AlertType.INFORMATION);
		aboutBox.initOwner(chatroomContents.getScene().getWindow());
		aboutBox.setTitle("About PictoChat");
		aboutBox.setHeaderText("About PictoChat");
		aboutBox.setContentText("PictoChat v1.0\nCreated for Dr. Ferrer's CSCI 250 Spring 2017 class\n\nProject members:\n* Jacob Turner\n* Jonathan Kwee\n* Uzair Tariq");
		aboutBox.showAndWait();
	}
	
	private void outputMessage(AlertType alertType, String message) {
		Alert alert = new Alert(alertType, message);
		alert.initOwner(drawingCanvas.getScene().getWindow());
		alert.showAndWait();
	}
	
}
