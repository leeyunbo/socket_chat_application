package Model;

import android.os.Message;

import com.example.family_share_community.View.ChatMainActivity;

import java.io.DataInputStream;
import java.io.InputStream;
import java.net.Socket;

public class MessageThread extends Thread {
    Socket socket;
    DataInputStream dis;
    boolean isRunning = false;

    public MessageThread(Socket socket) {
        try {
            this.socket = socket;
            InputStream is = socket.getInputStream();
            dis = new DataInputStream(is);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        try {
            while (isRunning) {
                final String msg = dis.readUTF();
                ChatMainActivity.getInstance()
            }
        }
    }
}
