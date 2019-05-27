package by.touchsoft.vasilyevanatali;


import by.touchsoft.vasilyevanatali.Thread.Connection;
import by.touchsoft.vasilyevanatali.User.User;


import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class Server implements ConnectionListener {
    public static final int PORT = 8189;

    private List<User> clients = Collections.synchronizedList(new ArrayList<>());
    private BlockingQueue<User> agents = new ArrayBlockingQueue<>(100);

    private final List<Connection> connections = new ArrayList<>(); // TODO Remake for users


    public Server() {
        System.out.println("Server is running ...");
        try (ServerSocket serverSocket = new ServerSocket(8189)) {
            while (true) {
                try {
                    new Connection(serverSocket.accept(), this);
                } catch (IOException e) {
                    System.out.println("Connection exception: " + e);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    public synchronized void addUser(User user) {
        if (user.getRole().equals("client")) {
            clients.add(user);
        } else {
            try {
                agents.put(user);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public synchronized void addAgent(User user) {
        try {
            agents.put(user);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public synchronized void addClient(User user) {
        clients.add(user);
    }

    public synchronized void removeAgent(User user) {
        agents.remove(user);
    }

    public synchronized void removeClient(User user) {
        clients.remove(user);
    }

    public synchronized List<User> getClients() {
        return clients;
    }
    public synchronized BlockingQueue<User> getAgents() {
        return agents;
    }

    public synchronized User getAgent() {
        try {
            return agents.take();
        } catch (InterruptedException e) {
            e.printStackTrace();
            return null;
        }
    }


    @Override
    public synchronized void onReceiveString(Connection connection, String value) {
        System.out.println("Debug: onReceiveString");


    }

    @Override
    public synchronized void onDisconnect(Connection connection) {
        System.out.println("Debug: onDisconnect");
        connections.remove(connection);
    }

    @Override
    public synchronized void onException(Connection connection, Exception e) {
        System.out.println("Connection Exception: " + e);
    }



}

