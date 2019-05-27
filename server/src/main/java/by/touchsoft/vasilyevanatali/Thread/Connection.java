package by.touchsoft.vasilyevanatali.Thread;


import by.touchsoft.vasilyevanatali.ConversationHandler;
import by.touchsoft.vasilyevanatali.Server;
import by.touchsoft.vasilyevanatali.User.User;

import java.io.*;
import java.net.Socket;
import java.nio.charset.Charset;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Connection {

    private final Socket socket;
    private final Thread receiveThread;
    private final BufferedReader in;
    private final BufferedWriter out;
    private final Server server;

    public Connection(Socket socket, Server server) throws IOException {

        this.socket = socket;
        this.server = server;
        in = new BufferedReader(new InputStreamReader(socket.getInputStream(), Charset.forName("UTF-8")));
        out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(), Charset.forName("UTF-8")));
        receiveThread = new Thread(() -> {
            User user;
            try {

                String regString = in.readLine();
                while (!checkFirstMessage(regString)) {
                    out.write("Please, check you information \n");
                    regString = in.readLine();
                }
                String[] splittedFirstMessage = regString.split(" ");
                String role = splittedFirstMessage[1];
                String name = splittedFirstMessage[2];
                user = new User(socket, name, role, server);
                if (role.equals("client")) {
                    sendString("You are connected. Please write the message.");
                } else {
                    sendString("You are connected. Now one of client type to you the message");
                }
                server.addUser(user);

               // while (!receiveThread.isInterrupted()) {
                    new Thread(new ConversationHandler(user)).start();
              //  }

            } catch (IOException e) {
                disconnect();
            } finally {
             //   disconnect();
            }
        });
        receiveThread.start();
    }

    public synchronized void sendString(String value) {
        try {
            out.write(value + "\r\n");
            out.flush();

        } catch (IOException e) {

            disconnect();
        }
    }

    public synchronized void disconnect() {
        receiveThread.interrupt();
        try {
            socket.close();
        } catch (IOException e) {
        }
    }

    @Override
    public String toString() {
        return "Connection" + socket.getInetAddress() + ": " + socket.getPort();
    }

    private boolean checkFirstMessage(String message) {
        Matcher matcher = Pattern.compile("/reg (client|agent) [A-z]+").matcher(message);
        String userMessage = null;
        if (matcher.find()) {
            userMessage = matcher.group(0);
        }

        return message.equals(userMessage);
    }
}
