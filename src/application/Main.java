package application;
	
import java.io.IOException;
import java.util.ArrayList;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;


public class Main extends Application {
	
	Parent root;
	String userName;
	Client client;
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		try {
			root = FXMLLoader.load(getClass().getResource("login.fxml"));
		} catch(Exception e) {
			System.out.println("failed to load login.fxml");
		}
		TextField userInput = (TextField) root.lookup("#userName");
		
		Button joinButton = (Button) root.lookup("#joinButton");
		Main m = this;
		
		joinButton.setOnAction(new EventHandler<ActionEvent>() {
			 
            @Override
            public void handle(ActionEvent event) {
    			try {
					root = FXMLLoader.load(getClass().getResource("serverList.fxml"));
				} catch (IOException e1) {
					System.out.println("failed to load serverList.fxml");
				}
    			
    			userName = userInput.getText();
    			client = new Client("192.168.1.159", 4700, userName, m);
    			
    			if(!client.start()) {
    				return;
    			}
    			
    			client.sendMessage(new Message(Message.MESSAGE, "registered user"));
    			TextField serverName = (TextField) root.lookup("#serverName");
    			Button createServer = (Button) root.lookup("#createServer");
    			createServer.setOnAction(new EventHandler<ActionEvent>() {
    				 
    	            @Override
    	            public void handle(ActionEvent event) {
    	            	client.sendMessage(new Message(Message.NEWSERVER, serverName.getText()));
    	            }
    	        });
    			
    			//ArrayList<DraftRoom> rooms = 
    			Scene scene = new Scene(root);
    			//scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
    			primaryStage.setScene(scene);
    			primaryStage.show();
            }
        });
		
		Button info = (Button) root.lookup("#readMe");
        info.setOnAction(new EventHandler<ActionEvent>() {
 
            @Override
            public void handle(ActionEvent event) {
            	try {
					Parent informationPanel = FXMLLoader.load(getClass().getResource("info.fxml"));
	                
	                // New window (Stage)
	                Stage infoWindow = new Stage();
	                infoWindow.setTitle("Information");
	                Scene secondScene = new Scene(informationPanel);
	                infoWindow.setScene(secondScene);
	                
	                // Set position of second window, related to primary window.
	                infoWindow.setX(primaryStage.getX() + 200);
	                infoWindow.setY(primaryStage.getY() + 100);
	 
	                infoWindow.show();
				} catch (IOException e) {
					e.printStackTrace();
				}
            }
            
        });
		
		Scene scene = new Scene(root);
		//scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
		primaryStage.setScene(scene);
		primaryStage.show();
	}
	
	public void createServerButton(String name) {
		Platform.runLater(new Runnable() {
			
			@Override
			public void run() {
				VBox serverList = (VBox) root.lookup("#serverList");
				
		    	Button newServer = new Button(name);
		    	newServer.setOnAction(new EventHandler<ActionEvent>() {
		
					@Override
					public void handle(ActionEvent event) {
						DraftRoom draft;
						draft = new DraftRoom(userName, name, client);
    					try {
    						Stage draftRoom = new Stage();
    						draftRoom.setX(300);
    		                draftRoom.setY(75);
    						draft.start(draftRoom);
    					} catch (Exception e) {
    						e.printStackTrace();
    					}
					}
		    		
		    	});
		    	serverList.getChildren().add(newServer);
			}
		});
	}
	
	public void createServerButtons(ArrayList<String> servers) {
		Platform.runLater(new Runnable() {
			
			@Override
			public void run() {
				VBox serverList = (VBox) root.lookup("#serverList");
				
				for(String name: servers) {
			    	Button newServer = new Button(name);
			    	newServer.setOnAction(new EventHandler<ActionEvent>() {
			    		
						@Override
						public void handle(ActionEvent event) {
	    	            	DraftRoom draft = new DraftRoom(userName, name, client);
	    	        		draft.drawRoom();
	    					try {
	    						Stage draftRoom = new Stage();
	    						draftRoom.setX(300);
	    		                draftRoom.setY(75);
	    						draft.start(draftRoom);
	    					} catch (Exception e) {
	    						e.printStackTrace();
	    					}
						}
			    		
			    	});
			    	serverList.getChildren().add(newServer);
				}
			}
		});
	}

	public static void main(String[] args) {
		launch(args);
	}
}
