package com.vasu.localchat.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.vasu.localchat.R;
import com.vasu.localchat.fragment.ClientInputFragment;
import com.vasu.localchat.fragment.ServerInputFragment;

public class ServerActivity extends AppCompatActivity {

    ServerInputFragment serverInputFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_server);

        initialize();
        getSupportFragmentManager().beginTransaction().replace(R.id.asFragment,serverInputFragment).commit();
    }

    void initialize(){
        serverInputFragment = new ServerInputFragment();
    }
}