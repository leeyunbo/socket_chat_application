package com.example.family_share_community.View;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.family_share_community.R;

import java.net.Socket;

import Model.ConnectionServer;
import Model.SocketHandler;

public class ChatMainActivity extends AppCompatActivity {

    private Button connect_button;
    private EditText nickname_editText;
    private final int CONNECT_SERVER = 0;
    private Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_main);

        connect_button = findViewById(R.id.connect_button);
        nickname_editText = findViewById(R.id.nickname_editText);

        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                if(msg.what == CONNECT_SERVER) {
                    Log.i("CONNECT_SERVER_MESSAGE","Connect Success Next Activity");
                    MessageStart();
                }
            }
        };

        connect_button.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                   SocketHandler.setNickName(nickname_editText.getText().toString());
                   if (!SocketHandler.getNickName().isEmpty()) {
                       SocketHandler.setNickName(SocketHandler.getNickName());
                       ConnectionThread thread = new ConnectionThread();
                       thread.start();
                   }

                   else {
                       AlertDialog.Builder builder = new AlertDialog.Builder(ChatMainActivity.this);
                       builder.setMessage("닉네임을 입력해주세요.");
                       builder.setPositiveButton("확인", null);
                       builder.show();
                   }
               }

        });

    }

    public void MessageStart() {
        Intent intent = new Intent(getApplicationContext(),ChatActivity.class);
        startActivity(intent);
    }
    class ConnectionThread extends Thread {
        ConnectionServer connectionServer;
        @Override
        public void run() {
            connectionServer = new ConnectionServer(SocketHandler.getNickName(),handler);
        }
    }
}
