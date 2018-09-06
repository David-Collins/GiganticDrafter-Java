package application;

import java.util.ArrayList;
import java.util.Arrays;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class DraftRoom extends Application {

	private String userName, roomName, team1Seat, team2Seat;
	private int currTeam, turn = 0;
	private int[] draftSequence = {-1, -2, 1, 2, 2, 1, 1, 2, 2, 1, 1, 2, 0};
	Parent root;
	Client client;
	private ArrayList<String> banList, team1Picks, team2Picks;
	private enum Heroes { 
		Aisling("Aisling"),
		Beckett("Beckett"),
		Charnok("Charnok"),
		Griselma("Griselma"),
		HK206("HK"),
		Imani("Imani"),
		Knossos("Knossos"),
		Margrave("Margrave"),
		Mozu("Mozu"),
		Pakko("Pakko"),
		Sven("Sven"),
		Tripp("Tripp"),
		Tyto("Tyto"),
		Vadasi("Vadasi"),
		Voden("Voden"),
		Wu("Wu"),
		Xenobia("Xenobia");
        private String name; 
        private Heroes(String name) { 
            this.name = name; 
        } 
        
        @Override 
        public String toString(){ 
            return name; 
        } 
    } 
	
	public DraftRoom(String userName, String roomName, Client client) {
		this.userName = userName;
		this.roomName = roomName;
		banList = new ArrayList<String>();
		team1Picks = new ArrayList<String>();
		team2Picks = new ArrayList<String>();
		banList.addAll(client.getRoomDir().get(roomName).getBanList());
		team1Picks.addAll(client.getRoomDir().get(roomName).getTeam1Picks());
		team2Picks.addAll(client.getRoomDir().get(roomName).getTeam2Picks());
		this.client = client;
		this.turn = client.getRoomDir().get(roomName).getTurn();
		team1Seat = client.getRoomDir().get(roomName).getTeam1();
		team2Seat = client.getRoomDir().get(roomName).getTeam2();
	}
	@Override
	public void start(Stage primaryStage) throws Exception {
		try {
			root = FXMLLoader.load(getClass().getResource("DraftRoom.fxml"));
			client.setDraftRoom(this);
			//Adding heroes to main drafting area
			VBox heroGrid = (VBox) root.lookup("#heroGrid");
			int count = 0;
			for(int i = 0; i < Heroes.values().length / 5 + 1; i++) {
				HBox row = new HBox();
				row.setSpacing(25);
				row.setAlignment(Pos.CENTER);
				for(int j = 0; j < 5; j++) {
					if (count < Heroes.values().length) {
						
						Image image = new Image("/imgs/" + Heroes.values()[count].toString() + ".png", 75, 75, false, false);
						ImageView imageView = new ImageView(image);

						Button hero = new Button(Heroes.values()[count].toString(), imageView);
						hero.setContentDisplay(ContentDisplay.TOP);

				        hero.setOnAction(new EventHandler<ActionEvent>() {
				            @Override
				            public void handle(ActionEvent event) {
				                handleTurn(hero.getText());
				            }
				        });
				        row.getChildren().add(hero);
				        count++;
					}
				}
				heroGrid.getChildren().add(i + 1, row);
			}
			
			Button team1 = (Button) root.lookup("#team1");
			Button team2 = (Button) root.lookup("#team2");
			team1.setOnAction(new EventHandler<ActionEvent>() {
				 
	            @Override
	            public void handle(ActionEvent event) {
	            	setTeam1(client.getRoomDir().get(roomName).getTeam1());
	            	setTeam2(client.getRoomDir().get(roomName).getTeam2());
	            	setTeam1Picks(client.getRoomDir().get(roomName).getTeam1Picks());
	            	setTeam2Picks(client.getRoomDir().get(roomName).getTeam2Picks());
	            	setBanList(client.getRoomDir().get(roomName).getBanList());
	            	setTurn(client.getRoomDir().get(roomName).getTurn());
	            	if(currTeam != 1 && team1Seat.equals("")) {
	            		if(currTeam == 2) {
	            			team2Seat = "";
	            			team2.setText("Join");
		            	}
	            		team1.setText(userName);
		            	currTeam = 1;
		            	team1Seat = userName;
	            	}
	            	else if(currTeam == 1) {
	            		team1.setText("Join");
	            		currTeam = 0;
	            		team1Seat = "";
	            	}
	            	client.sendMessage(new Message(Message.DRAFT, new RoomInfo(roomName, banList, team1Picks, team2Picks, team1Seat, team2Seat, turn)));
	            }
	        });
			
			team2.setOnAction(new EventHandler<ActionEvent>() {
				 
	            @Override
	            public void handle(ActionEvent event) {
	            	setTeam1(client.getRoomDir().get(roomName).getTeam1());
	            	setTeam2(client.getRoomDir().get(roomName).getTeam2());
	            	setTeam1Picks(client.getRoomDir().get(roomName).getTeam1Picks());
	            	setTeam2Picks(client.getRoomDir().get(roomName).getTeam2Picks());
	            	setBanList(client.getRoomDir().get(roomName).getBanList());
	            	setTurn(client.getRoomDir().get(roomName).getTurn());
	            	if(currTeam != 2 && team2Seat.equals("")) {
	            		if(currTeam == 1) {
	            			team1Seat = "";
	            			team1.setText("Join");
		            	}
	            		team2.setText(userName);
		            	currTeam = 2;
		            	team2Seat = userName;
	            	}
	            	else if(currTeam == 2){
	            		team2.setText("Join");
	            		currTeam = 0;
	            		team2Seat = "";
	            	}
	            	client.sendMessage(new Message(Message.DRAFT, new RoomInfo(roomName, banList, team1Picks, team2Picks, team1Seat, team2Seat, turn)));
	            }
	        });
			
			Scene scene = new Scene(root);
			
			//CSS Ill add later at some point
			//scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			
			primaryStage.setScene(scene);
			primaryStage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}

	}
	
	public void handleTurn(String hero) {

		setTeam1(client.getRoomDir().get(roomName).getTeam1());
    	setTeam2(client.getRoomDir().get(roomName).getTeam2());
    	setBanList(client.getRoomDir().get(roomName).getBanList());
    	setTeam1Picks(client.getRoomDir().get(roomName).getTeam1Picks());
    	setTeam2Picks(client.getRoomDir().get(roomName).getTeam2Picks());
    	setTurn(client.getRoomDir().get(roomName).getTurn());
		System.out.println(turn + " " + currTeam + " " + Arrays.toString(banList.toArray()));
		
		if(turn > 11) {
			turn++;
			//draft ends
		}
		else if(currTeam == 1 && draftSequence[turn] == -1) {
        	banList.add(hero);
        	turn++;
        }
		else if(currTeam == 2 && draftSequence[turn] == -2) {
        	banList.add(hero);
        	turn++;
        }
        else if(currTeam == 1 && draftSequence[turn] == 1 && !team1Picks.contains(hero) && !banList.contains(hero)) {	//team 1 is drafting
        	team1Picks.add(hero);
        	turn++;
        }
        else if (currTeam == 2 && draftSequence[turn] == 2 && !team2Picks.contains(hero) && !banList.contains(hero)) {	//team 2 is drafting
        	team2Picks.add(hero);
        	turn++;
        }
        else {
        	//draft ends
        }
		client.sendMessage(new Message(Message.DRAFT, new RoomInfo(roomName, banList, team1Picks, team2Picks, team1Seat, team2Seat, turn)));
		
	}
	
	public void drawRoom() {
		Platform.runLater(new Runnable() {
			
			public void run() {
				//Updates all info on room
				setTeam1(client.getRoomDir().get(roomName).getTeam1());
		    	setTeam2(client.getRoomDir().get(roomName).getTeam2());
		    	setBanList(client.getRoomDir().get(roomName).getBanList());
		    	setTeam1Picks(client.getRoomDir().get(roomName).getTeam1Picks());
		    	setTeam2Picks(client.getRoomDir().get(roomName).getTeam2Picks());
		    	setTurn(client.getRoomDir().get(roomName).getTurn());
		    	
				HBox banBox = (HBox) root.lookup("#Bans");
				VBox team1 = (VBox) root.lookup("#team1Picks");
	        	VBox team2 = (VBox) root.lookup("#team2Picks");
				banBox.getChildren().clear();
				team1.getChildren().clear();
				team2.getChildren().clear();
				Button joinTeam1 = (Button) root.lookup("#team1");
				Button joinTeam2 = (Button) root.lookup("#team2");
				Text currentPhase = (Text) root.lookup("#currentPhase");
				
				switch(draftSequence[turn]) {
				
				case 0:
					currentPhase.setText("Draft Complete");
					break;
				
				case -1:
					currentPhase.setText("Team 1 Ban");
					break;
				case -2:
					currentPhase.setText("Team 2 Ban");
					break;
				case 1:
					currentPhase.setText("Team 1 Pick");
					break;
				case 2:
					currentPhase.setText("Team 2 Pick");
				}
				
				if(!client.getRoomDir().get(roomName).getTeam1().equals("")) {
					joinTeam1.setText(client.getRoomDir().get(roomName).getTeam1());
				}
				else {
					joinTeam1.setText("Join");
				}
				
				if(!client.getRoomDir().get(roomName).getTeam2().equals("")) {
					joinTeam2.setText(client.getRoomDir().get(roomName).getTeam2());
				}
				else {
					joinTeam2.setText("Join");
				}
				
				System.out.println(Arrays.toString(team1Picks.toArray()));
				System.out.println(Arrays.toString(team2Picks.toArray()));
				System.out.println(turn + " " + userName);
				
				for(String hero : banList) {
					Image imageSmaller = new Image("/imgs/" + hero + ".png", 50, 50, false, false);
			    	Label currBan = new Label(hero, new ImageView(imageSmaller));
			    	currBan.setContentDisplay(ContentDisplay.TOP);
			    	banBox.getChildren().add(currBan);
				}
				for(String hero : team1Picks) {
					Image imageSmaller = new Image("/imgs/" + hero + ".png", 50, 50, false, false);
		        	Label currBan = new Label(hero, new ImageView(imageSmaller));
		        	currBan.setContentDisplay(ContentDisplay.TOP);
		        	team1.getChildren().add(currBan);
				}
				for(String hero: team2Picks) {
					Image imageSmaller = new Image("/imgs/" + hero + ".png", 50, 50, false, false);
		        	Label currBan = new Label(hero, new ImageView(imageSmaller));
		        	currBan.setContentDisplay(ContentDisplay.TOP);
		        	team2.getChildren().add(currBan);
				}
			}
		});
	}
	
	public void setBanList(ArrayList<String> banList) {
		this.banList = new ArrayList<String>();
		this.banList.addAll(banList);
	}

	public void setTeam1Picks(ArrayList<String> team1Picks) {
		this.team1Picks = new ArrayList<String>();
		this.team1Picks.addAll(team1Picks);
	}

	public void setTeam2Picks(ArrayList<String> team2Picks) {
		this.team2Picks = new ArrayList<String>();
		this.team2Picks.addAll(team2Picks);
	}
	
	public void setTeam1(String team1) {
		this.team1Seat = team1;
	}

	public void setTeam2(String team2) {
		this.team2Seat = team2;
	}
	
	public void setTurn(int turn) {
		this.turn = turn;
	}
}
