package application;

import java.io.*;
import java.net.*;
import java.text.SimpleDateFormat;
import java.util.*;

/*
 * The server, currently just run in a console window
 */
public class Server {
	// a unique ID for each connection
	private static int uniqueId;
	// list of all clients connected
	private ArrayList<ClientThread> clientList;
	// to display time
	private SimpleDateFormat sdf;
	// the port number to listen for connection
	private int port;
	// the boolean that will be turned of to stop the server
	private boolean keepGoing;
	//list of all current DraftRooms running
	private ArrayList<String> serverList;
	//HashMap containing information for all the DraftRooms active
	private HashMap<String, RoomInfo> lobbyList;
	
	
	public Server(int port) {
		// the port
		this.port = port;
		// to display hh:mm:ss
		sdf = new SimpleDateFormat("HH:mm:ss");
		// ArrayList for the Client list
		clientList = new ArrayList<ClientThread>();
		serverList = new ArrayList<String>();
		lobbyList = new HashMap<String, RoomInfo>();
	}
	
	public void start() {
		keepGoing = true;
		/* create socket server and wait for connection requests */
		try 
		{
			// the socket used by the server
			ServerSocket serverSocket = new ServerSocket(port);

			// infinite loop to wait for connections
			while(keepGoing) 
			{
				display("Server waiting for Clients on port " + port + ".");
				
				Socket socket = serverSocket.accept();  	// accept connection
				// if I was asked to stop
				if(!keepGoing)
					break;
				ClientThread t = new ClientThread(socket);  // make a thread of it
				clientList.add(t);									// save it in the ArrayList
				t.start();
				broadcast(new Message(Message.SERVERLIST, serverList));
				broadcast(new Message(Message.DRAFTINFOLIST, lobbyList));
			}
			// I was asked to stop
			try {
				serverSocket.close();
				for(int i = 0; i < clientList.size(); ++i) {
					ClientThread tc = clientList.get(i);
					try {
						tc.sInput.close();
						tc.sOutput.close();
						tc.socket.close();
					}
					catch(IOException ioE) {
						// not much I can do
					}
				}
			}
			catch(Exception e) {
				display("Exception closing the server and clients: " + e);
			}
		}
		// something went bad
		catch (IOException e) {
            String msg = sdf.format(new Date()) + " Exception on new ServerSocket: " + e + "\n";
			display(msg);
		}
	}	
	/*
	 * Display an event (not a message) to the console or the GUI
	 */
	private void display(String msg) {
		String time = sdf.format(new Date()) + " " + msg;
		//if(gui == null)
			System.out.println(time);
		//else
		//	gui.appendEvent(time + "\n");
	}
	/*
	 *  to broadcast a message to all Clients
	 */
	private synchronized void broadcast(Message message) {
		for(int i = clientList.size(); --i >= 0;) {
			ClientThread ct = clientList.get(i);
			// try to write to the Client if it fails remove it from the list
			if(!ct.writeMsg(message)) {
				clientList.remove(i);
				display("Disconnected Client " + ct.username + " removed from list.");
			}
		}
	}

	// for a client who logoff using the LOGOUT message
	synchronized void remove(int id) {
		for(int i = 0; i < clientList.size(); ++i) {
			ClientThread ct = clientList.get(i);
			if(ct.id == id) {
				clientList.remove(i);
				return;
			}
		}
	}
	
	public static void main(String[] args) {
		// start server on port 4700 unless a port number is specified 
		int portNumber = 4700;
		switch(args.length) {
			case 1:
				try {
					portNumber = Integer.parseInt(args[0]);
				}
				catch(Exception e) {
					System.out.println("Invalid port number.");
					System.out.println("Usage is: > java Server [portNumber]");
					return;
				}
			case 0:
				break;
			default:
				System.out.println("Usage is: > java Server [portNumber]");
				return;
				
		}
		//start server on specified port
		Server server = new Server(portNumber);
		server.start();
	}

	/** One instance of this thread will run for each client */
	class ClientThread extends Thread {
		// the socket where to listen/talk
		Socket socket;
		ObjectInputStream sInput;
		ObjectOutputStream sOutput;
		// my unique id (easier for deconnection)
		int id;
		// the Username of the Client
		String username;
		// the only type of message a will receive
		Message incoming;
		// the date I connect
		String date;

		ClientThread(Socket socket) {
			// assign a unique id
			id = ++uniqueId;
			this.socket = socket;
			// Creating both Data Streams
			System.out.println("Thread trying to create Object Input/Output Streams");
			try
			{
				sOutput = new ObjectOutputStream(socket.getOutputStream());
				sInput  = new ObjectInputStream(socket.getInputStream());
				// read the username
				username = (String) sInput.readObject();
				display(username + " just connected.");
				
			}
			catch (IOException e) {
				display("Exception creating new Input/output Streams: " + e);
				return;
			}
			// have to catch ClassNotFoundException but I read a String, I am sure it will work
			catch (ClassNotFoundException e) {
			}
            date = new Date().toString() + "\n";
		}

		// this will run forever
		public void run() {
			// to loop until LOGOUT
			boolean keepGoing = true;
			while(keepGoing) {
				// read incoming message
				try {
					incoming = (Message) sInput.readObject();
				}
				catch (IOException e) {
					display(username + " Exception reading Streams: " + e);
					break;				
				}
				catch(ClassNotFoundException e2) {
					break;
				}
				
				// checks type of message and responds accordingly
				if(incoming.getType() == Message.NEWSERVER) {
					if(!serverList.contains(incoming.getMessage())) {
						broadcast(incoming);
						serverList.add(incoming.getMessage());
						lobbyList.put(incoming.getMessage(), new RoomInfo(incoming.getMessage()));
					}
				}
				else if(incoming.getType() == Message.DRAFT) {
					lobbyList.put(incoming.getRoom().getRoomName(), incoming.getRoom());
					broadcast(new Message(Message.DRAFTINFOLIST, lobbyList));
					broadcast(incoming);
				}
				else if(incoming.getType() == Message.LOGOUT) {
					display(username + " disconnected");
					keepGoing = false;
				}
				else {
					broadcast(incoming);
				}
			}
			// remove client from the arrayList containing the list of the connected Clients
			remove(id);
			close();
		}
		
		// try to close everything
		private void close() {
			// try to close the connection
			try {
				if(sOutput != null) sOutput.close();
			}
			catch(Exception e) {}
			try {
				if(sInput != null) sInput.close();
			}
			catch(Exception e) {};
			try {
				if(socket != null) socket.close();
			}
			catch (Exception e) {}
		}

		/*
		 * Write a Message to the Client output stream
		 */
		private boolean writeMsg(Message msg) {
			// if Client is still connected send the message to it
			if(!socket.isConnected()) {
				close();
				return false;
			}
			// write the message to the stream
			try {
				sOutput.writeObject(msg);
			}
			// if an error occurs, do not abort just inform the user
			catch(IOException e) {
				display("Error sending message to " + username);
				display(e.toString());
			}
			return true;
		}
	}
}

