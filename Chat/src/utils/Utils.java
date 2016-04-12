package utils;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import client.Client;
import server.Server;

public class Utils {
	
	private JFrame frame;
	private JTextArea chat;
	
	public void createLoginFrame(Client client) {
		JFrame frame = new JFrame("Zamichat");
		JPanel start = new JPanel(), south = new JPanel();
		JTextField username = new JTextField("Votre Pseudo"), ip = new JTextField("IP du Serveur"), port = new JTextField("Port du Serveur");
		JButton login = new JButton("Connecter");
		
		frame.setSize(320, 100);
		frame.setResizable(false);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLocationRelativeTo(null);
		
		login.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				if(!username.getText().equalsIgnoreCase("") && !ip.getText().equalsIgnoreCase("") && !port.getText().equalsIgnoreCase("")) {
					client.username = username.getText();
					client.ip = ip.getText();
					client.port = Integer.valueOf(port.getText());
					frame.setVisible(false);
					client.initialize();
				}
			}
			
		});
		
		start.add(username);
		start.add(ip);
		start.add(port);
		south.add(login);
		
		frame.add(start, BorderLayout.CENTER);
		frame.add(south, BorderLayout.SOUTH);
		
		frame.setVisible(true);
	}
	
	public void createLoginFrame(Server server) {
		JFrame frame = new JFrame("Zamichat");
		JPanel start = new JPanel(), south = new JPanel();
		JTextField username = new JTextField("Votre Pseudo"), port = new JTextField("Port du Serveur");
		JButton login = new JButton("Connecter");
		
		frame.setSize(320, 100);
		frame.setResizable(false);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLocationRelativeTo(null);
		
		login.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				if(!username.getText().equalsIgnoreCase("") && !port.getText().equalsIgnoreCase("")) {
					server.username = username.getText();
					server.port = Integer.valueOf(port.getText());
					frame.setVisible(false);
					server.initialize();
				}
			}
			
		});
		
		start.add(username);
		start.add(port);
		south.add(login);
		
		frame.add(start, BorderLayout.CENTER);
		frame.add(south, BorderLayout.SOUTH);
		
		frame.setVisible(true);
	}
	
	public void createFrame(Client client) {
		frame = new JFrame("Zamichat - " + client.username);
		chat = new JTextArea();
		JTextField message = new JTextField();
		
		frame.setSize(480, 320);
		frame.setResizable(false);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLocationRelativeTo(null);
		
		chat.setEditable(false);
		
		message.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				if(!e.getActionCommand().equalsIgnoreCase("")) {
					sendMessage(client, client.username, e.getActionCommand());
					message.setText("");
				}
			}
			
		});
		
		frame.add(new JScrollPane(chat), BorderLayout.CENTER);
		frame.add(message, BorderLayout.SOUTH);
		
		frame.setVisible(true);
	}
	
	public void createFrame(Server server) {
		frame = new JFrame("Zamichat - " + server.username);
		chat = new JTextArea();
		JTextField message = new JTextField();
		
		frame.setSize(480, 320);
		frame.setResizable(false);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLocationRelativeTo(null);
		
		chat.setEditable(false);
		
		message.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				if(!e.getActionCommand().equalsIgnoreCase("")) {
					sendMessage(server, server.username, e.getActionCommand());
					message.setText("");
				}
			}
			
		});
		
		frame.add(new JScrollPane(chat), BorderLayout.CENTER);
		frame.add(message, BorderLayout.SOUTH);
		
		frame.setVisible(true);
	}
	
	public void sendMessage(Client client, String username, String message) {
		PrintWriter out = new PrintWriter(client.getOutputStream());
		
		out.println(username);
		out.flush();
		
		out.println(message);
		out.flush();
	}
	
	public void sendMessage(Server server, String username, String message) {
		try {
			for(Socket socket : server.getSockets()) {
				PrintWriter out = new PrintWriter(socket.getOutputStream());
				
				out.println(username);
				out.flush();
				
				out.println(message);
				out.flush();
			}
			
			showMessage("<" + username + "> " + message);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void getMessages(Client client) {
		try {
			BufferedReader in = new BufferedReader(new InputStreamReader(client.getSocket().getInputStream()));
			
			String username = in.readLine();
			String message = in.readLine();
			
			showMessage("<" + username + "> " + message);
		} catch (Exception e) {}
	}
	
	public void getMessages(Server server) {
		try {
			List<String> usernames = new ArrayList<>(), messages = new ArrayList<>();
			
			for(Socket socket : server.getSockets()) {
				BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
				
				if(in.ready()) {
					String username = in.readLine();
					String message = in.readLine();
					
					usernames.add(username);
					messages.add(message);
					
					showMessage("<" + username + "> " + message);
				}
			}
			
			for(Socket socket : server.getSockets()) {
				PrintWriter out = new PrintWriter(socket.getOutputStream());
				
				for(int i = 0; i < usernames.size(); i++) {
					String username = usernames.get(i), message = messages.get(i);
					
					out.println(username);
					out.flush();
					
					out.println(message);
					out.flush();
				}
			}
		} catch (Exception e) {}
	}
	
	public void showMessage(String message) {
		chat.append(message + "\n");
	}
	
}
