package by.touchsoft.vasilyevanatali.clientAction;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class ConsoleReader {

    private BufferedReader consoleBufferedReader;

    public String readFromConsole() {
        try {
            if (consoleBufferedReader == null) {
                consoleBufferedReader = new BufferedReader(new InputStreamReader(System.in, "UTF-8"));
            }

            return consoleBufferedReader.readLine().trim();
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }

    }

    public void destroy() {
        try {
            if (consoleBufferedReader != null) {
                consoleBufferedReader.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}

