package com.example.family_share_community.View;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.Bundle;

import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;

import com.example.family_share_community.R;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

import Model.ConnectionServer;
import Model.ReceiveMessage;
import Model.SendToServer;
import Model.SocketHandler;

public class ChatActivity extends AppCompatActivity {
    private LinearLayout container;
    private ScrollView scroll;
    private String nickName;
    private EditText chat_edit;
    private Handler handler;
    private ImageView send_button;
    private MessageThread messageThread;
    private Socket socket;
    ProgressDialog pro;
    private boolean isRunning = true;
    private final int SEND_MESSAGE = 0;
    private final int RECEIVE_MESSAGE = 1;
    private final int DESTROY_ACTIVITY = 2;
    private final int CONNECT_SERVER = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

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
                else if (msg.what == CONNECT_SERVER) {
                    pro.dismiss();
                    messageThread = new MessageThread(handler);
                    messageThread.start();
                }
            }
        };

        ConnectionThread connectionThread = new ConnectionThread(handler);
        connectionThread.start();

        pro = ProgressDialog.show(this, null, "접속중입니다.");


        send_button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                String msg = chat_edit.getText().toString();
                SendToServerThread sendToServerThread = new SendToServerThread(msg,handler);
                sendToServerThread.start();
            }

        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            messageThread.getHandler().sendEmptyMessage(DESTROY_ACTIVITY);
            socket.close();
            SocketHandler.deleteNickName();
            SocketHandler.deleteSocket();
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public void changeMessageView(String msg) {
        TextView tv = new TextView(ChatActivity.this);
        tv.setText(msg);
        tv.setTextColor(Color.BLACK);
        tv.setTextSize(TypedValue.COMPLEX_UNIT_SP,12);
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
        Handler mainHandler;
        ReceiveMessage receiveMessage;

        public MessageThread(Handler mainHandler) {
            this.mainHandler = mainHandler;
            this.handler = new Handler() {
                @Override
                public void handleMessage(Message msg) {
                    if(msg.what == DESTROY_ACTIVITY) {
                        System.out.println("finish");
                        isRunning = false;
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
                    InputStream is = socket.getInputStream();
                    DataInputStream dis = new DataInputStream(is);
                    while (isRunning) {
                        final String chat_message = dis.readUTF();
                        System.out.println(chat_message);
                        // 화면에 출력
                        Message msg = new Message();
                        Bundle data = new Bundle();
                        data.putString("chat_message",chat_message);
                        msg.setData(data);
                        msg.what = RECEIVE_MESSAGE;
                        mainHandler.sendMessage(msg);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
        }

    }

    class SendToServerThread extends Thread {
        String msg;
        Handler mainHandler;

        public SendToServerThread(String msg, Handler mainHandler)
        {
            this.mainHandler = mainHandler;
            this.msg = msg;
        }

        @Override
        public void run() {
            try {
                OutputStream os = socket.getOutputStream();
                DataOutputStream dos = new DataOutputStream(os);
                dos.writeUTF(msg);
                mainHandler.sendEmptyMessage(SEND_MESSAGE);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    class ConnectionThread extends Thread {
        Handler mainHandler;
        public ConnectionThread(Handler mainHandler) {
            this.mainHandler = mainHandler;
        }
        @Override
        public void run() {
            try {
                socket = new Socket("192.168.219.100", 32500);
                OutputStream os = socket.getOutputStream();
                DataOutputStream dos = new DataOutputStream(os);
                dos.writeUTF(SocketHandler.getNickName());
                mainHandler.sendEmptyMessage(CONNECT_SERVER);
                Log.i("ConnectionServer","Connect Success");
            } catch (IOException ioe) {
                ioe.printStackTrace();
            } catch (SecurityException se) {
                se.printStackTrace();
            } catch (IllegalArgumentException Ie) {
                Ie.printStackTrace();
            }
        }
    }

}
