package Model;

import android.icu.util.Output;
import android.os.Handler;
import android.os.StrictMode;

import com.example.family_share_community.View.ChatActivity;

import java.io.DataOutput;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;

public class SendToServer {
    Socket socket;
    String msg;
    DataOutputStream dos;
    private final int SEND_MESSAGE = 0;
    private Handler handler;

    public SendToServer(Socket socket, String msg, Handler handler) {
        try {
            this.socket = socket;
            this.msg = msg;
            this.handler = handler;
            OutputStream os = socket.getOutputStream();
            dos = new DataOutputStream(os);
            dos.writeUTF(msg);
            handler.sendEmptyMessage(SEND_MESSAGE);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
