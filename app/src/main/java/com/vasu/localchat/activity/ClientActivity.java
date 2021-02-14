package com.vasu.localchat.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.net.nsd.NsdServiceInfo;
import android.os.Bundle;
import android.os.IBinder;
import android.os.StrictMode;

import com.vasu.localchat.R;
import com.vasu.localchat.fragment.ChatFragment;
import com.vasu.localchat.fragment.ClientInputFragment;
import com.vasu.localchat.helper.NsdHelper;
import com.vasu.localchat.network.client.ClientService;

import org.apache.commons.lang3.RandomStringUtils;

public class ClientActivity extends AppCompatActivity {

    public ClientService mService;
    boolean mBound = false;

    ClientInputFragment clientInputFragment;
    public static String userId,name ;

    private NsdHelper nsdHelper;

    @Override
    protected void onStart() {
        super.onStart();
//        Binds the service
        Intent intent = new Intent(ClientActivity.this, ClientService.class);
        bindService(intent, connection, Context.BIND_AUTO_CREATE);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        initialize();
        getSupportFragmentManager().beginTransaction().replace(R.id.acFragment,clientInputFragment).commit();

    }

//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//        nsdHelper.stopDiscovery();
//        nsdHelper.stopDiscovery();
//    }

    public void startNSD(String str){
        nsdHelper = new NsdHelper(ClientActivity.this,str);
        nsdHelper.discoverServices();
    }

    void initialize(){
        userId = RandomStringUtils.random(5,"abcdefghijklmnopqrstuvwxyz");
        clientInputFragment = new ClientInputFragment();
    }

    public void nsdConstructor(){
        NsdServiceInfo serviceInfo = nsdHelper.getServiceInfo();
        mService.constructor(serviceInfo.getHost(),serviceInfo.getPort());
    }

    private ServiceConnection connection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName className,
                                       IBinder service) {
            // We've bound to LocalService, cast the IBinder and get LocalService instance
            ClientService.ClientBinder binder = (ClientService.ClientBinder) service;
            mService = binder.getService();
            mBound = true;
        }


        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            mBound = false;
        }
    };

    public void sendMessage(String message){
        mService.sendMessage(message);
    }
}