package Model;

import android.icu.util.Output;
import android.os.StrictMode;

import com.example.family_share_community.View.ChatActivity;

import java.io.DataOutput;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;

public class SendToServerThread extends Thread {
    Socket socket;
    String msg;
    DataOutputStream dos;

    public SendToServerThread(Socket socket, String msg) {
        try {
            this.socket = socket;
            this.msg = msg;
            OutputStream os = socket.getOutputStream();
            dos = new DataOutputStream(os);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        try {
            dos.writeUTF(msg);
            ChatActivity.getInstance().setDefaultEditText();

        } catch(Exception e) {
            e.printStackTrace();
        }
    }
}
