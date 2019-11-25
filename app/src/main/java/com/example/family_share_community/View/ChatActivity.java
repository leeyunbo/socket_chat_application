package com.example.family_share_community.View;

import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;

import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.example.family_share_community.R;

import java.net.Socket;

import Model.ConnectionThread;
import Model.MessageThread;
import Model.SendToServerThread;
import Model.SocketHandler;

public class ChatActivity extends AppCompatActivity {
    private LinearLayout container;
    private ScrollView scroll;
    private Socket socket;
    private String nickName;
    private EditText chat_edit;
    private MessageThread thread;
    private Handler handler;
    private ImageView send_button;
    private final int SEND_MESSAGE = 0;
    private final int RECEIVE_MESSAGE = 1;

    private static final ChatActivity instance = new ChatActivity();
    private ChatActivity() {};

    public static ChatActivity getInstance() {
        return instance;
    } //singleton pattern


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        container = findViewById(R.id.chat_container);
        scroll = findViewById(R.id.chat_scroll);
        chat_edit = findViewById(R.id.chat_edit_text);
        send_button = findViewById(R.id.chat_send_button);
        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                if(msg.what == RECEIVE_MESSAGE) {
                    String chat_message = msg.getData().getString("chat_message");
                    changeMessageView(chat_message);
                }
                else if (msg.what == SEND_MESSAGE) {
                    setDefaultEditText();
                }
            }
        };

        thread = new MessageThread(SocketHandler.getSocket(),handler);
        thread.start();

        send_button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                String msg = chat_edit.getText().toString();
                SendToServerThread thread = new SendToServerThread(socket, msg,handler);
                thread.start();
            }

        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            socket.close();
            thread.FinishCallbackMethod();
            SocketHandler.deleteNickName();
            SocketHandler.deleteSocket();
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public void changeMessageView(String msg) {
        TextView tv = new TextView(ChatActivity.this);
        tv.setTextColor(Color.BLACK);
        tv.setTextSize(TypedValue.COMPLEX_UNIT_SP,22);
        if(msg.startsWith(SocketHandler.getNickName())) {
            tv.setGravity(Gravity.RIGHT);
        } else {
            tv.setGravity(Gravity.LEFT);
        }
        container.addView(tv);
        scroll.fullScroll(View.FOCUS_DOWN);
    }

    public void setDefaultEditText() {
        chat_edit.setText("");
    }

}
