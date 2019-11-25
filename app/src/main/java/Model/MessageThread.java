package Model;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.example.family_share_community.View.ChatActivity;
import com.example.family_share_community.View.ChatMainActivity;

import java.io.DataInputStream;
import java.io.InputStream;
import java.net.Socket;

public class MessageThread extends Thread {
    Socket socket;
    DataInputStream dis;
    boolean isRunning = false;
    private Handler handler;
    private final int RECEIVE_MESSAGE = 1;

    public void FinishCallbackMethod() {
        this.isRunning = false;
    }

    public MessageThread(Socket socket, Handler handler) {
        try {
            this.handler = handler;
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
                final String chat_message = dis.readUTF();
                // 화면에 출력
                Message msg = new Message();
                Bundle data = new Bundle();
                data.putString("chat_message",chat_message);
                msg.setData(data);
                handler.sendEmptyMessage(RECEIVE_MESSAGE);
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
}
