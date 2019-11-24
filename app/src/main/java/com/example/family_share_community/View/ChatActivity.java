package com.example.family_share_community.View;

import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;

import android.support.v7.app.AppCompatActivity;
import android.util.TypedValue;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.example.family_share_community.R;

import java.net.Socket;

import Model.MessageThread;
import Model.SocketHandler;

public class ChatActivity extends AppCompatActivity {
    private LinearLayout container;
    private ScrollView scroll;
    private Socket socket;
    private String nickName;

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
        socket = SocketHandler.getSocket();
        nickName = SocketHandler.getNickName();

        MessageThread thread = new MessageThread(socket);
        thread.start();
    }

    public void changeMessageView(String msg) {
        TextView tv = new TextView(ChatActivity.this);
        tv.setTextColor(Color.BLACK);
        tv.setTextSize(TypedValue.COMPLEX_UNIT_SP,22);
        if(msg.startsWith(nickName)) {

        }

    }

}
