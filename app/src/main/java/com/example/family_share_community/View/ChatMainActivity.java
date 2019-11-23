package com.example.family_share_community.View;

import android.app.AlertDialog;
import android.app.ProgressDialog;
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

public class ChatMainActivity extends AppCompatActivity {

    private Button connect_button;
    private EditText nickname_editText;
    private LinearLayout container;
    private ScrollView scroll;
    boolean isConnect = true;
    ProgressDialog pro;
    boolean isRunning = false;
    private static final ChatMainActivity instance = new ChatMainActivity();
    private ChatMainActivity() {};

    public static ChatMainActivity getInstance() {
        return instance;
    } //singleton pattern

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_main);

        connect_button = findViewById(R.id.connect_button);
        nickname_editText = findViewById(R.id.nickname_editText);
        container = findViewById(R.id.chat_container);
        scroll = findViewById(R.id.chat_scroll);

        connect_button.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                   String nickName = nickname_editText.getText().toString();
                   if (!nickName.isEmpty()) {
                       pro = ProgressDialog.show(ChatMainActivity.this, null, "접속중..");
                       ConnectionThread thread = new ConnectionThread(nickName);
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
        pro.dismiss();
        isConnect = true;
        isRunning = true;
        MessageThread thread = new MessageThread(member_socket);
        thread.start();
    }
}
