package application;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

public class Message implements Serializable {
	
	protected static final long serialVersionUID = 1112122200L;
	
	static final int WHOISIN = 0, MESSAGE = 1, LOGOUT = 2, NEWSERVER = 3, DRAFT = 4, SEAT = 5, SERVERLIST = 6, DRAFTINFOLIST = 7;
	private ArrayList<String> bans;
	private ArrayList<String> team1Picks;
	private ArrayList<String> team2Picks;
	private String team1Seat;
	private String team2Seat;
	private String hero;
	private int turn;
	private int type;
	private int team;
	private String message;
	private ArrayList<String> servers;
	private RoomInfo room;
	private HashMap<String, RoomInfo> draftDir;
	
	Message(int type, ArrayList<String> servers) {
		this.type = type;
		this.servers = servers;
	}
	
	Message(int type, HashMap<String, RoomInfo> draftDir) {
		this.type = type;
		this.draftDir = new HashMap<String, RoomInfo>();
		for(String s : draftDir.keySet()) {
			this.draftDir.put(s, draftDir.get(s));
		}
	}

	Message(int type, String message) {
		this.type = type;
		this.message = message;
	}
	
	Message(int type, RoomInfo room) {
		this.type = type;
		this.room = new RoomInfo(room.getRoomName(), room.getBanList(), room.getTeam1Picks(), room.getTeam2Picks(), room.getTeam1(), room.getTeam2(), room.getTurn());
	}
	
	Message(int type, String hero, int team, ArrayList<String> bans, ArrayList<String> team1Picks, 
			ArrayList<String> team2Picks, String team1Seat, String team2Seat, int turn) {
		
		this.type = type;
		this.bans = new ArrayList<String>();
		this.bans.addAll(bans);
		this.team1Picks = new ArrayList<String>();
		this.team1Picks.addAll(team1Picks);
		this.team2Picks = new ArrayList<String>();
		this.team2Picks.addAll(team2Picks);
		this.team1Seat = team1Seat;
		this.team2Seat = team2Seat;
		this.turn = turn;
		this.hero = hero;
		this.team = team;
	}

	public HashMap<String, RoomInfo> getDraftDir() {
		return draftDir;
	}

	public ArrayList<String> getBans() {
		return bans;
	}

	public ArrayList<String> getTeam1Picks() {
		return team1Picks;
	}

	public ArrayList<String> getTeam2Picks() {
		return team2Picks;
	}

	public String getTeam1Seat() {
		return team1Seat;
	}

	public String getTeam2Seat() {
		return team2Seat;
	}

	public String getHero() {
		return hero;
	}

	public int getTurn() {
		return turn;
	}

	public int getTeam() {
		return team;
	}
	
	public ArrayList<String> getServerList() {
		return servers;
	}

	public int getType() {
		return type;
	}
	
	public String getMessage() {
		return message;
	}
	
	public RoomInfo getRoom() {
		return room;
	}
}
