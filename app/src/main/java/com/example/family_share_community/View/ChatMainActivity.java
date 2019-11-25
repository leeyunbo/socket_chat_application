package com.example.family_share_community.View;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import com.example.family_share_community.R;

import java.net.Socket;

import Model.ConnectionThread;
import Model.MessageThread;
import Model.SocketHandler;

public class ChatMainActivity extends AppCompatActivity {

    private Button connect_button;
    private EditText nickname_editText;
    private final int CONNECT_SERVER = 0;
    private Handler handler;

    /*
    private static final ChatMainActivity instance = new ChatMainActivity();
    private ChatMainActivity() {};

    public static ChatMainActivity getInstance() {
        return instance;
    } //singleton pattern
*/
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
                    MessageStart(SocketHandler.getSocket());
                }
            }
        };

        connect_button.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                   String nickName = nickname_editText.getText().toString();
                   if (!nickName.isEmpty()) {
                       SocketHandler.setNickName(nickName);
                       ConnectionThread thread = new ConnectionThread(nickName,handler);
                       thread.run();
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

    public void MessageStart(Socket member_socket) {
        Intent intent = new Intent(this,ChatActivity.class);
        startActivity(intent);
        this.onDestroy();
    }
}
