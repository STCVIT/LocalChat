package com.vasu.localchat.helper;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.nsd.NsdManager;
import android.net.nsd.NsdServiceInfo;
import android.os.CountDownTimer;
import android.util.Log;
import android.widget.Toast;

import com.vasu.localchat.activity.ClientActivity;
import com.vasu.localchat.fragment.ChatFragment;

import static com.vasu.localchat.util.Constants.CLIENT_TAG;
import static com.vasu.localchat.util.Constants.NSD_HELPER_TAG;
import static com.vasu.localchat.util.Constants.SERVICE_TYPE;

//  A helper class for Network Service Discovery
public class NsdHelper {

    //Data Members
    private final Context context;
    private final NsdManager nsdManager;
    private final String serviceName;
    private NsdManager.DiscoveryListener nsdDiscoverListener;
    private NsdManager.RegistrationListener nsdRegistrationListener;
    private NsdManager.ResolveListener nsdResolveListener;
    private NsdServiceInfo serviceInfo;
    private int found = -1;

    //Constructor
    public NsdHelper(Context context,String serviceName) {
        this.context = context;
        this.nsdManager = (NsdManager) context.getSystemService(Context.NSD_SERVICE);
        this.serviceName = serviceName;
    }

    //Member Functions

    public NsdServiceInfo getServiceInfo() {
        return serviceInfo;
    }

    public void setServiceInfo(NsdServiceInfo serviceInfo) {
        this.serviceInfo = serviceInfo;
    }

    public void discoverServices(){

        final ProgressDialog[] dialog = new ProgressDialog[1];
        nsdResolveListener = new NsdManager.ResolveListener() {

            @Override
            public void onResolveFailed(NsdServiceInfo serviceInfo, int errorCode) {

            }

            @Override
            public void onServiceResolved(NsdServiceInfo serviceInfo) {
                Log.e(CLIENT_TAG, "Resolve Succeeded. " + serviceInfo);
                setServiceInfo(serviceInfo);
                dialog[0].dismiss();
            }
        };
        nsdDiscoverListener = new NsdManager.DiscoveryListener(){

            @Override
            public void onStartDiscoveryFailed(String serviceType, int errorCode) {
                Log.e(NSD_HELPER_TAG,"Service Discovery Start Failed: "+ errorCode);
            }

            @Override
            public void onStopDiscoveryFailed(String serviceType, int errorCode) {
                Log.e(NSD_HELPER_TAG,"Service Discovery Stop Failed: "+ errorCode);

            }

            @Override
            public void onDiscoveryStarted(String serviceType) {
                Log.d(NSD_HELPER_TAG,"Service Discovery Started");
                dialog[0] = ProgressDialog.show(context, "", "Discovering. Please wait...");
                dialog[0].setProgressStyle(ProgressDialog.STYLE_SPINNER);
                dialog[0].create();
                new CountDownTimer(15000,1000) {
                    @Override
                    public void onTick(long l) {

                    }

                    @Override
                    public void onFinish() {
                     if(found==-1){
                         stopDiscovery();
                         dialog[0].cancel();
                     }
                    }
                }.start();
            }

            @Override
            public void onDiscoveryStopped(String serviceType) {
                Log.e(NSD_HELPER_TAG,"Service Discovery Stopped");
                Toast.makeText(context, "Server not found. Go back to try again.", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onServiceFound(NsdServiceInfo serviceInfo) {
                if(serviceInfo.getServiceName().equals(serviceName)){
                    found=1;
                    setServiceInfo(serviceInfo);
                    nsdManager.resolveService(serviceInfo,nsdResolveListener);
                    Log.d(NSD_HELPER_TAG, "Service Found"+serviceInfo.getPort()+":"+serviceInfo.getHost());
                    ChatFragment.connected=1;
                }
            }

            @Override
            public void onServiceLost(NsdServiceInfo serviceInfo) {

            }
        };
        nsdManager.discoverServices(SERVICE_TYPE,NsdManager.PROTOCOL_DNS_SD,nsdDiscoverListener);

    }

    public void registerServices(int port){
        NsdServiceInfo serviceInfo  = new NsdServiceInfo();
        serviceInfo.setPort(port);
        serviceInfo.setServiceName(serviceName);
        serviceInfo.setServiceType(SERVICE_TYPE);

        nsdRegistrationListener = new NsdManager.RegistrationListener() {
            @Override
            public void onRegistrationFailed(NsdServiceInfo serviceInfo, int errorCode) {

            }

            @Override
            public void onUnregistrationFailed(NsdServiceInfo serviceInfo, int errorCode) {

            }

            @Override
            public void onServiceRegistered(NsdServiceInfo serviceInfo) {

            }

            @Override
            public void onServiceUnregistered(NsdServiceInfo serviceInfo) {

            }
        };
        nsdManager.registerService(serviceInfo, NsdManager.PROTOCOL_DNS_SD,nsdRegistrationListener);
    }

    public void stopDiscovery() {
        nsdManager.stopServiceDiscovery(nsdDiscoverListener);
    }

    public void unregisterService(){
        Log.i(NSD_HELPER_TAG,"service unregistered");
        nsdManager.unregisterService(nsdRegistrationListener);
    }

}

