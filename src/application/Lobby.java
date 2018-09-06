package application;

import java.io.IOException;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class Lobby extends Application {
	
	Parent root;
	private String userName;
	
	public Lobby(String userName) {
		this.userName = userName;
	}
	
	
	@Override
	public void start(Stage primaryStage) {
		try {
			root = FXMLLoader.load(getClass().getResource("serverList.fxml"));
			
			TextField serverName = (TextField) root.lookup("#serverName");
			Button createServer = (Button) root.lookup("#createServer");
			createServer.setOnAction(new EventHandler<ActionEvent>() {
				 
	            @Override
	            public void handle(ActionEvent event) {
	            	
	            	/*VBox serverList = (VBox) root.lookup("#serverList");
	            	Button newServer = new Button(serverName.getText());
	            	newServer.setOnAction(new EventHandler<ActionEvent>() {

						@Override
						public void handle(ActionEvent event) {
							//join room corresponding to this server
						}
	            		
	            	});
	            	serverList.getChildren().add(new Button(serverName.getText()));*/
	            	
	            	DraftRoom draft = new DraftRoom(userName, serverName.getText(), new Client("blah", 12, null));
					try {
						Stage draftRoom = new Stage();
						draftRoom.setX(primaryStage.getX() - 300);
		                draftRoom.setY(primaryStage.getY() - 150);
						draft.start(draftRoom);
					} catch (Exception e) {
						e.printStackTrace();
					}
	            }
	        });
			
			//ArrayList<DraftRoom> rooms = 
			Scene scene = new Scene(root);
			//scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			primaryStage.setScene(scene);
			primaryStage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}
}
