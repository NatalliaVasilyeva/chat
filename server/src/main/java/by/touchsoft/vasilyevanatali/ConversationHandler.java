package by.touchsoft.vasilyevanatali;

import by.touchsoft.vasilyevanatali.User.User;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;

public class ConversationHandler implements Runnable {
    User user;
    boolean status = true;


    public ConversationHandler(User user) {
        this.user = user;
    }

    @Override
    public void run() {
        //the second message from client, after client find agent
        try (BufferedReader reader = user.getReader()) {
            String message = reader.readLine();
            if (user.getRole().equals("client")) {
                if (!(message.equals("/exit") || message.equals("/leave") || message != null)) {
                    user.findOpponent(user); // doesn't work!!!!
                    sendMessageToOpponent(user, message);
                    chatRoom(user);
                }

            } else {
                //agent wait while client make connect to him
                while (status) {
                    checkClientStatus(user);
                }
                chatRoom(user);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //chat between client and agent
    private void chatRoom(User user) {
        while (true) {
            try (BufferedReader reader = user.getReader()) {
                String message = reader.readLine();

                if (!message.equals("/exit") || !message.equals("/leave") || message != null) {
                    sendMessageToOpponent(user, message);
                }


            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }


    private void sendMessageToOpponent(User user, String message) {
        try (BufferedWriter writer = user.getOpponent().getWriter()) {
            writer.write(message);
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private boolean checkClientStatus(User user) {
        return user.getOpponent() == null;
    }
}
