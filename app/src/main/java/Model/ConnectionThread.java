package Model;

import java.io.DataOutputStream;
import java.io.OutputStream;
import java.net.Socket;

public class ConnectionThread extends Thread {
    Socket member_socket;
    String user_nickname;

    public ConnectionThread(String user_nickname) {
        this.user_nickname = user_nickname;
    }


    @Override
    public void run() {
        try {
            final Socket socket = new Socket("192.168.219.101", 40000);
            member_socket = socket;
            OutputStream os = socket.getOutputStream();
            DataOutputStream dos = new DataOutputStream(os);
            dos.writeUTF(user_nickname);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}