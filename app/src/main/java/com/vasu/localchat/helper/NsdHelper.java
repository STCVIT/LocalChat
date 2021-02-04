package com.vasu.localchat.helper;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.nsd.NsdManager;
import android.net.nsd.NsdServiceInfo;
import android.util.Log;

import static com.vasu.localchat.util.Constants.CLIENT_TAG;
import static com.vasu.localchat.util.Constants.NSD_HELPER_TAG;
import static com.vasu.localchat.util.Constants.SERVICE_TYPE;

public class NsdHelper {

    //Data Members
    private final Context context;
    private final NsdManager nsdManager;
    private final String serviceName;
    private NsdManager.DiscoveryListener nsdDiscoverListener;
    private NsdManager.RegistrationListener nsdRegistrationListener;
    private NsdManager.ResolveListener nsdResolveListener;
    private NsdServiceInfo serviceInfo;

    //Constructor
    public NsdHelper(Context context,String serviceName) {
        this.context = context;
        this.nsdManager = (NsdManager) context.getSystemService(Context.NSD_SERVICE);
        this.serviceName = serviceName;
    }

    //Member Function

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
            }

            @Override
            public void onDiscoveryStopped(String serviceType) {
                Log.e(NSD_HELPER_TAG,"Service Discovery Stopped");
            }

            @Override
            public void onServiceFound(NsdServiceInfo serviceInfo) {
                if(serviceInfo.getServiceName().equals(serviceName)){
                    setServiceInfo(serviceInfo);
                    Log.d(NSD_HELPER_TAG, "Service Found"+serviceInfo.getPort()+":"+serviceInfo.getHost());
                    nsdManager.resolveService(serviceInfo,nsdResolveListener);
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
        nsdManager.unregisterService(nsdRegistrationListener);
    }

}

