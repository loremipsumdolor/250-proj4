package extrapackage;

import edu.hendrix.csci250.csci250proj4.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.ComboBox;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;

public class CanvasController {
	
	@FXML
	ColorPicker colorPicker;
	
	@FXML
	Button send;
	
	@FXML
	Canvas canvas;
	
	@FXML
	ComboBox fontSize;
	
	private User currentUser;
	
	private ObservableList<Double> options = FXCollections.observableArrayList();
	
	
	@FXML
	public void initialize() {
		for (int i = 1; i <= 10; i++) {
			options.add((double) i);
		}
		
		fontSize.setItems(options);
		
		final GraphicsContext graphicsContext = canvas.getGraphicsContext2D();
		initDraw(graphicsContext);
		
		canvas.addEventHandler(MouseEvent.MOUSE_PRESSED, 
                new EventHandler<MouseEvent>(){
 
            @Override
            public void handle(MouseEvent event) {
                graphicsContext.beginPath();
                graphicsContext.moveTo(event.getX(), event.getY());
                graphicsContext.stroke();
            }
        });
		
		canvas.addEventHandler(MouseEvent.MOUSE_DRAGGED, 
                new EventHandler<MouseEvent>(){
 
            @Override
            public void handle(MouseEvent event) {
                graphicsContext.lineTo(event.getX(), event.getY());
                graphicsContext.stroke();
            }
        });
		
		canvas.addEventHandler(MouseEvent.MOUSE_RELEASED, 
                new EventHandler<MouseEvent>(){
 
            @Override
            public void handle(MouseEvent event) {
 
            }
        });
		
		colorPicker.setOnAction(new EventHandler() {
			public void handle(Event t) {
				graphicsContext.setStroke(colorPicker.getValue());
			}
		});
		
		fontSize.setOnAction(new EventHandler() {
			public void handle(Event t) {
				graphicsContext.setLineWidth((double) fontSize.getValue());
			}
		});
		
	}
	
	public void getUserFromChatroomSelect(User currentUser){
		this.currentUser = currentUser;
		colorPicker.setValue(currentUser.getFavoriteColor());
	}
	
    private void initDraw(GraphicsContext gc){
        double canvasWidth = gc.getCanvas().getWidth();
        double canvasHeight = gc.getCanvas().getHeight();
         
        gc.setFill(Color.LIGHTGRAY);
        gc.setStroke(Color.BLACK);
        gc.setLineWidth(5);
 
        gc.fill();
        gc.strokeRect(
                0,              //x of the upper left corner
                0,              //y of the upper left corner
                canvasWidth,    //width of the rectangle
                canvasHeight);  //height of the rectangle
         
        gc.setFill(Color.RED);
        gc.setStroke(colorPicker.getValue());
        gc.setLineWidth(1);
    }
}
