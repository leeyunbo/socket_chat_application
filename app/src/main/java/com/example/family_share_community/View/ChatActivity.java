package com.example.family_share_community.View;

import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.MenuItem;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;

import com.example.family_share_community.R;

import Model.ChatDatabaseManager;

public class ChatActivity extends AppCompatActivity {
    ChatDatabaseManager helper;
    SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        helper = new ChatDatabaseManager(this);
        try {
            db = helper.getWritableDatabase();
        } catch (SQLException ex) { //만약 데이터베이스에 접근을 하지 못한다면, 읽기 전용 모드로 접근한다.
            db = helper.getReadableDatabase();
        }
    }


    public void dbUpdate(String message, String name) {

        db.execSQL("INSERT INTO chat_message VALUES (1,'"+ message + "','"+name+"');"); //CREATE TABLE chat_message (chat_id INT, message TEXT, name TEXT)
    }
}
