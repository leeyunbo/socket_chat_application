package com.example.family_share_community.View;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import android.support.v7.app.AppCompatActivity;

import com.example.family_share_community.R;

public class ChatActivity extends AppCompatActivity {
    ChatDatabaseManager helper;
    SQLiteDatabase db;

    private static final ChatActivity instance = new ChatActivity();
    private ChatActivity() {};

    public static ChatActivity getInstance() {
        return instance;
    } //singleton pattern


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

    }

}
