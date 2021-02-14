package com.vasu.localchat.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.StrictMode;
import android.util.Log;

import com.vasu.localchat.R;
import com.vasu.localchat.fragment.ClientInputFragment;
import com.vasu.localchat.fragment.ServerInputFragment;
import com.vasu.localchat.network.server.ServerService;

import org.apache.commons.lang3.RandomStringUtils;

public class ServerActivity extends AppCompatActivity {

    ServerInputFragment serverInputFragment;

   public ServerService mService;
    boolean mBound = false;

   public static String charId,userId,name ;


    @Override
    protected void onStart() {

        super.onStart();
        Intent intent = new Intent(ServerActivity.this, ServerService.class);
        bindService(intent, connection, Context.BIND_AUTO_CREATE);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.MainTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_server);

        initialize();
        getSupportFragmentManager().beginTransaction().replace(R.id.asFragment,serverInputFragment).commit();

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);


    }

    void initialize(){

        charId = RandomStringUtils.random(6,"0123456789abcdef");
        userId = RandomStringUtils.random(5,"abcdefghijklmnopqrstuvwxyz");
        serverInputFragment = new ServerInputFragment();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i("ServerActivity","destroy");
    }

    @Override
    protected void onStop() {
        super.onStop();
        unbindService(connection);
        Log.i("service","unbind");
        mBound = false;
    }

    private ServiceConnection connection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName className,
                                       IBinder service) {
            // We've bound to LocalService, cast the IBinder and get LocalService instance
            ServerService.ServerBinder binder = (ServerService.ServerBinder) service;
            mService = binder.getService();
            mBound = true;
        }


        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            mBound = false;
        }
    };

}