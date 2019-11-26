package Model;

import android.os.Handler;
import android.util.Log;

import com.example.family_share_community.View.ChatActivity;
import com.example.family_share_community.View.ChatMainActivity;

import java.io.DataOutputStream;
import java.io.OutputStream;
import java.net.Socket;

public class ConnectionServer {
    private String user_nickname;
    private Handler handler;
    private final int CONNECT_SERVER = 0;

    public ConnectionServer(String user_nickname, Handler handler) {
        this.user_nickname = user_nickname;
        this.handler = handler;

        try {
            final Socket socket = new Socket("192.168.219.100", 35000);
            SocketHandler.setSocket(socket);
            OutputStream os = socket.getOutputStream();
            DataOutputStream dos = new DataOutputStream(os);
            dos.writeUTF(user_nickname);
            handler.sendEmptyMessage(CONNECT_SERVER);
            Log.i("ConnectionServer","Connect Success");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}