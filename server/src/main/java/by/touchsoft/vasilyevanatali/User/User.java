package by.touchsoft.vasilyevanatali.User;


import by.touchsoft.vasilyevanatali.Server;

import java.io.*;
import java.net.Socket;
import java.util.LinkedList;
import java.util.List;

public class User {
    BufferedReader reader;
    BufferedWriter writer;
    Socket socket;
    private String name;
    private String role;
    private boolean isOnline;
    private boolean isInConversation;
    Server server;
    User opponent;
    List<String> messages = new LinkedList<>();

    public User(Socket socket, String name, String role, Server server) {
        this.socket = socket;
        this.name = name;
        this.role = role;
        this.server = server;
        try {
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream(), "UTF-8"));
            writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(), "UTF-8"));
        } catch (IOException e) {
        }
    }

    public BufferedReader getReader() {
        return reader;
    }

    public void setReader(BufferedReader reader) {
        this.reader = reader;
    }

    public BufferedWriter getWriter() {
        return writer;
    }

    public void setWriter(BufferedWriter writer) {
        this.writer = writer;
    }

    public Socket getSocket() {
        return socket;
    }

    public String getName() {
        return name;
    }

    public String getRole() {
        return role;
    }

    public boolean isOnline() {
        return isOnline;
    }

    public void setOnline(boolean online) {
        isOnline = online;
    }

    public boolean isInConversation() {
        return isInConversation;
    }

    public void setInConversation(boolean inConversation) {
        isInConversation = inConversation;
    }

    public User getOpponent() {
        return opponent;
    }

    public void setOpponent(User opponent) {
        this.opponent = opponent;
    }

    public List<String> getMessages() {
        return messages;
    }

    public void addMessages(String message) {
        messages.add(message);
    }

    public void disconnectUser() {
        try {
            if (!socket.isClosed()) {
                socket.close();
            }
            if (reader != null) {
                reader.close();
            }
            if (writer != null) {
                writer.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void findOpponent(User client) {
        User agent = server.getAgent();
        client.setOpponent(agent);
        agent.setOpponent(client);
        agent.setInConversation(true);
        client.setInConversation(true);
        agent.setOnline(true);
        client.setOnline(true);

    }
}
