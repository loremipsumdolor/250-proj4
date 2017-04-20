package edu.hendrix.csci250.csci250proj4.gui;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;

import javax.imageio.ImageIO;

public class PictoChatServer extends Thread {
	private ServerSocket serverSocket;
	Socket server;

	public PictoChatServer(int port) throws IOException {
		serverSocket = new ServerSocket(port);
		serverSocket.setSoTimeout(180000);
	}

	public void run() {
		while(true) { 
			try {
				server = serverSocket.accept();
				BufferedImage img = ImageIO.read(ImageIO.createImageInputStream(server.getInputStream()));
				//SHOW IMAGE
			} catch(SocketTimeoutException st) {
				System.out.println("Socket timed out!");
				break;
			} catch(IOException e) {
				e.printStackTrace();
				break;
			} catch(Exception ex) {
				System.out.println(ex);
			}
		}
	}

	public static void main(String[] args) throws IOException {
		Thread t = new PictoChatServer(8888);
		t.start();
	}
}