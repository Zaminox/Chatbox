package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import utils.Utils;

public class Server implements Runnable {
	
	private ServerSocket serverSocket;
	
	public String username;
	public int port;
	
	private List<Socket> sockets = new ArrayList<>();
	
	private Utils utils;

	public void initialize() {
		if(serverSocket == null) {
			try {
				serverSocket = new ServerSocket(port);
			} catch (IOException e) {
				System.err.println("Port déjà utilisé.");
				System.exit(0);
			}
			
			new ConnectionsListener(this, utils);
			utils.createFrame(this);
			start();
		}
	}
	
	private synchronized void start() {
		new Thread(this).start();
	}
	
	@Override
	public void run() {
		while(true) utils.getMessages(this);
	}
	
	public Server() {
		utils = new Utils();
		utils.createLoginFrame(this);
	}
	
	public static void main(String[] args) {
		new Server();
	}
	
	public ServerSocket getServerSocket() {
		return serverSocket;
	}
	
	public List<Socket> getSockets() {
		return sockets;
	}
	
}
