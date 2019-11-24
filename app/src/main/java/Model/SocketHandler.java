package Model;

import java.net.Socket;

public class SocketHandler {

    private static Socket socket;
    private static String nickName;

    public static synchronized Socket getSocket() {
        return socket;
    }

    public static synchronized void setSocket(Socket socket) {
        SocketHandler.socket = socket;
    }

    public static synchronized void deleteSocket() { SocketHandler.socket = null; }

    public static String getNickName() {
        return nickName;
    }

    public static void setNickName(String nickName) {
        SocketHandler.nickName = nickName;
    }

    public static void deleteNickName() { SocketHandler.nickName = null; }
}
