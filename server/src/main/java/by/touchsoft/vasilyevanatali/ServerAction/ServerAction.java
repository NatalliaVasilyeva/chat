package by.touchsoft.vasilyevanatali.ServerAction;

import java.io.IOException;
import java.net.ServerSocket;

public class ServerAction {

    public ServerAction() {
    }

    public synchronized void disconnectServer(ServerSocket serverSocket) {
        try {
            if (serverSocket != null) {
                serverSocket.close();
            }
            System.exit(0);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
