package by.touchsoft.vasilyevanatali;


import by.touchsoft.vasilyevanatali.Thread.Connection;

public interface ConnectionListener {
    void onReceiveString(Connection connection, String value);
    void onDisconnect(Connection connection);
    void onException(Connection connection, Exception e);
}
