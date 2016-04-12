package server;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.Socket;

import utils.Utils;

public class ConnectionsListener implements Runnable {
	
	private Server server;
	private Utils utils;
	
	private synchronized void start() {
		new Thread(this).start();
	}
	
	private void tryToConnect() {
		try {
			Socket socket = server.getServerSocket().accept();
			server.getSockets().add(socket);
			
			utils.sendMessage(server, "SERVER", new BufferedReader(new InputStreamReader(socket.getInputStream())).readLine() + " a rejoint.");
		} catch(Exception e) {}
	}
	
	@Override
	public void run() {
		while(true) tryToConnect();
	}
	
	public ConnectionsListener(Server server, Utils utils) {
		this.server = server;
		this.utils = utils;
		start();
	}

}
