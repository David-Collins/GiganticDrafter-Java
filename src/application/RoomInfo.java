package application;

import java.io.Serializable;
import java.util.ArrayList;

public class RoomInfo implements Serializable {
	
	protected static final long serialVersionUID = 7526472295622776147L;
	
	private ArrayList<String> banList;
	private ArrayList<String> team1Picks;
	private ArrayList<String> team2Picks;
	private String team1;
	private String team2;
	private String roomName;
	private int turn;
	
	RoomInfo (String roomName, ArrayList<String> banList, ArrayList<String> team1Picks, ArrayList<String> team2Picks, String team1, String team2, int turn) {
		this.roomName = roomName;
		this.banList = new ArrayList<String>();
		this.banList.addAll(banList);
		this.team1Picks = new ArrayList<String>();
		this.team1Picks.addAll(team1Picks);
		this.team2Picks = new ArrayList<String>();
		this.team2Picks.addAll(team2Picks);
		this.team1 = team1;
		this.team2 = team2;
		this.turn = turn;
	}
	
	RoomInfo(String roomName) {
		this.roomName = roomName;
		this.banList = new ArrayList<String>();
		this.team1Picks = new ArrayList<String>();
		this.team2Picks = new ArrayList<String>();
		this.team1 = "";
		this.team2 = "";
		this.turn = 0;
	}

	public ArrayList<String> getBanList() {
		return banList;
	}

	public ArrayList<String> getTeam1Picks() {
		return team1Picks;
	}

	public ArrayList<String> getTeam2Picks() {
		return team2Picks;
	}

	public String getTeam1() {
		return team1;
	}

	public String getTeam2() {
		return team2;
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
		this.team1 = team1;
	}

	public void setTeam2(String team2) {
		this.team2 = team2;
	}

	public void setRoomName(String roomName) {
		this.roomName = roomName;
	}

	public String getRoomName() {
		return roomName;
	}
	
	public int getTurn() {
		return turn;
	}
	
	public void setTurn(int turn) {
		this.turn = turn;
	}
}
