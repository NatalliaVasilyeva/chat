package by.touchsoft.vasilyevanatali;

import by.touchsoft.vasilyevanatali.clientAction.ClientToServerCommunicator;
import by.touchsoft.vasilyevanatali.clientAction.ConsoleReader;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Client {

    private final Thread readerThread;


    private ClientToServerCommunicator connector = new ClientToServerCommunicator();
    private ConsoleReader consoleReader = new ConsoleReader();
    private boolean isStopped = false;


    public Client() {

        System.out.println("Hello. Please write #/reg client NAME# if you are the client or #/reg agent NAME# if you are the agent ");
        String message = consoleReader.readFromConsole();
        while (!checkFirstMessage(message)) {
            System.out.println("Please, check you information");
            message = consoleReader.readFromConsole();
        }
        connectClientToServer();
        connector.sendMessage(message);

        readerThread = new Thread(() -> {
            while (!Client.this.isStopped()) {
                showMessageFromServer();
            }
        });
        readerThread.start();


        while (!isStopped) {
            sendMessageFromClient();
        }

    }

    private boolean checkFirstMessage(String message) {
        Matcher matcher = Pattern.compile("/reg (client|agent) [A-z]+").matcher(message);
        String userMessage = null;
        if (matcher.find()) {
            userMessage = matcher.group(0);
        }

        return message.equals(userMessage);
    }

    private void connectClientToServer() {
        connector.connectToServer();
    }

    private void disconnectClientFromServer() {
        connector.disconnectFromServer();
        consoleReader.destroy();
        setStopped();
        System.exit(0);

    }

    private void sendMessageFromClient() {
        String message = consoleReader.readFromConsole();
        if (message != null) {
            connector.sendMessage(message);
            if (message.equals("quit")) {
                System.out.println("Have a nice day");
                disconnectClientFromServer();
            }
            if (message.equals("leave")) {
                System.out.println("We wait when you will begin to type again");
            }
        }
    }

    public void showMessageFromServer() {
        String message = connector.receiveMessage();
        if (message != null) {
            System.out.println(message);
        }
    }

    private void setStopped() {
        this.isStopped = true;

    }

    public boolean isStopped() {
        return isStopped;
    }
}
