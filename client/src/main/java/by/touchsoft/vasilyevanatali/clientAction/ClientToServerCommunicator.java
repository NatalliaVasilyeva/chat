package by.touchsoft.vasilyevanatali.clientAction;

import java.io.*;
import java.net.Socket;


public class ClientToServerCommunicator {

    public static final int PORT = 8189;
    public static final String HOST = "localhost";

    private Socket socket;
    private BufferedWriter socketWriter;
    private BufferedReader socketReader;


    public void connectToServer() {
        try {
            socket = new Socket(HOST, PORT);
            socketReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            socketWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void disconnectFromServer() {
        try {
            if (!socket.isClosed()) {
                socket.close();
            }
            if (socketReader != null) {
                socketReader.close();
            }
            if (socketWriter != null) {
                socketWriter.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    public String receiveMessage() {

        try {
            return socketReader.readLine();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }


    public void sendMessage(String message) {

        try {
            socketWriter.write(message + "\n");
            socketWriter.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
