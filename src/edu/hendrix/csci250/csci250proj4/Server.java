package edu.hendrix.csci250.csci250proj4;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashSet;

//THANK YOU TO http://cs.lmu.edu/~ray/notes/javanetexamples/#chat

public class Server {
    private static HashSet<ObjectOutputStream> streams = new HashSet<ObjectOutputStream>();

    public static void main(String[] args) throws Exception {
        ServerSocket listener = new ServerSocket(8888);
        try {
            while (true) {
                new Handler(listener.accept()).start();
            }
        } finally {
            listener.close();
        }
    }

    private static class Handler extends Thread {
        private Socket socket;
        private ObjectInputStream in;
        private ObjectOutputStream out;

        public Handler(Socket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {
            try {
            	System.out.println("CONNECT " + socket.getInetAddress().getHostAddress());
            	out = new ObjectOutputStream(socket.getOutputStream());
                in = new ObjectInputStream(socket.getInputStream());
                streams.add(out);
                while (true) {
                    Object input = in.readObject();
                    if (input == null) {
                        return;
                    }
                    for (ObjectOutputStream stream : streams) {
                    	stream.writeObject(input);
                    	stream.flush();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (out != null) {
                    streams.remove(out);
                }
                try {
                    socket.close();
                } catch (Exception e) {
                }
            }
        }
    }
}