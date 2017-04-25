package extrapackage;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
	
	private ServerSocket accepter;
	
	public Server(int port) throws IOException {
		accepter = new ServerSocket(port);
	}
	
	public void listen() throws IOException {
		for (;;) {
			Socket s = accepter.accept();
		}
	}
}
