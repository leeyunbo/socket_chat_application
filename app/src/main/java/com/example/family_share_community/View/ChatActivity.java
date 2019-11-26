package com.example.family_share_community.View;

import android.graphics.Color;
import android.os.Bundle;

import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.example.family_share_community.R;

import java.net.Socket;

import Model.ReceiveMessage;
import Model.SendToServer;
import Model.SocketHandler;

public class ChatActivity extends AppCompatActivity {
    private LinearLayout container;
    private ScrollView scroll;
    private Socket socket;
    private String nickName;
    private EditText chat_edit;
    private Handler handler;
    private ImageView send_button;
    private MessageThread messageThread;
    private final int SEND_MESSAGE = 0;
    private final int RECEIVE_MESSAGE = 1;
    private final int DESTROY_ACTIVITY = 2;

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

        messageThread = new MessageThread();
        messageThread.start();

        send_button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                String msg = chat_edit.getText().toString();
                SendToServerThread sendToServerThread = new SendToServerThread(msg);
                sendToServerThread.run();
            }

        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            socket.close();
            //thread.FinishCallbackMethod();
            SocketHandler.deleteNickName();
            SocketHandler.deleteSocket();
            messageThread.getHandler().sendEmptyMessage(DESTROY_ACTIVITY);
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

    class MessageThread extends Thread {
        Handler handler;
        ReceiveMessage receiveMessage;

        public MessageThread() {
            this.handler = new Handler() {
                @Override
                public void handleMessage(Message msg) {
                    if(msg.what == DESTROY_ACTIVITY) {
                        receiveMessage.FinishCallbackMethod();
                    }
                }
            };
        }

        public Handler getHandler() {
            return handler;
        }

        @Override
        public void run() {
            try {
                receiveMessage = new ReceiveMessage(socket,handler);
            } catch(Exception e) {
                e.printStackTrace();
            }
        }

    }

    class SendToServerThread extends Thread {
        String msg;
        SendToServer sendToServer;

        public Handler getHandler() {
            return handler;
        }

        public SendToServerThread(String msg) {
            this.msg = msg;
        }

        @Override
        public void run() {
            try{
                sendToServer = new SendToServer(socket,msg,handler);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
