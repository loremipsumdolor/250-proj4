package edu.hendrix.csci250.csci250proj4;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.HashSet;

public class Server {
    private static HashSet<String> names = new HashSet<String>();
    private static HashSet<ObjectOutputStream> streams = new HashSet<ObjectOutputStream>();

    public static class Handler extends Thread {
        private String name;
        private Socket socket;
        private ObjectInputStream in;
        private ObjectOutputStream out;

        public Handler(Socket socket) {
            this.socket = socket;
        }

        public void run() {
            try {
                in = new ObjectInputStream(socket.getInputStream());
                out = new ObjectOutputStream(socket.getOutputStream());
                while (true) {
                    Object input = in.readObject();
                    if (input == null) {
                        return;
                    }
                    for (ObjectOutputStream stream : streams) {
                    	stream.writeObject(input);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (name != null) {
                    names.remove(name);
                }
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