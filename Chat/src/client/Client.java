package client;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;

import utils.Utils;

public class Client implements Runnable {
	
	private Socket socket;
	private OutputStream out;
	private InputStream in;
	
	public String username;
	public String ip;
	public int port;
	
	private Utils utils;
	
	public void initialize() {
		if(socket == null) {
			try {
				socket = new Socket(ip, port);
				out = socket.getOutputStream();
				in = socket.getInputStream();
			} catch (Exception e) {
				System.err.println("Serveur introuvable.");
				System.exit(0);
			}
			
			PrintWriter printOut = new PrintWriter(out);
			
			printOut.println(username);
			printOut.flush();
			
			initialize();
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
	
	public Client() {
		utils = new Utils();
		utils.createLoginFrame(this);
	}
	
	public static void main(String[] args) {
		new Client();
	}
	
	public Socket getSocket() {
		return socket;
	}
	
	public OutputStream getOutputStream() {
		return out;
	}
	
	public InputStream getInputStream() {
		return in;
	}

}
