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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_main);

        connect_button = findViewById(R.id.connect_button);
        nickname_editText = findViewById(R.id.nickname_editText);


        connect_button.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                   SocketHandler.setNickName(nickname_editText.getText().toString());
                   if (!SocketHandler.getNickName().isEmpty()) {
                       Intent intent = new Intent(getApplicationContext(),ChatActivity.class);
                       startActivity(intent);
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



}
