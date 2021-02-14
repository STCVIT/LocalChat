package com.vasu.localchat.network.server;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;

import com.vasu.localchat.fragment.ChatFragment;
import com.vasu.localchat.helper.NsdHelper;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Iterator;

import static com.vasu.localchat.util.Constants.SERVER_TAG;

public class ServerService extends Service {

    private final IBinder binder = new ServerBinder();
    ChatServer mChatServer;

//     Inner classes

    public class ChatServer {

        //Data Members
        private final ServerSocket serverSocket;
        private ArrayList<ClientHandler> clientHandlers;
        private int serverPort = -1;
        private final Context context;
        private final NsdHelper nsdHelper;
        private Thread serverThread;

        //Constructor
        public ChatServer(Context context,String str) throws IOException {

            this.context = context;
            nsdHelper = new NsdHelper(context, str);
            serverSocket = new ServerSocket(0);
            setServerPort(serverSocket.getLocalPort());
            serverThread = new Thread(new ServerThread());
            serverThread.start();
            nsdHelper.registerServices(getServerPort());
            clientHandlers = new ArrayList<>();

        }

        //Member Functions
        public int getServerPort() {
            return serverPort;
        }

        public void setServerPort(int serverPort) {
            this.serverPort = serverPort;
        }


        public void tearDown() throws IOException {
            serverSocket.close();
        }


        //Send To Client
        public void SendToClients(String message) throws IOException {
            if(clientHandlers.isEmpty()){
                Log.d(SERVER_TAG, "No Client Found to Send Message");
            }
            else{
                for(int i=0;i<clientHandlers.size();i++){
                    clientHandlers.get(i).SendToClient(message);
//                    ServerActivity.setServerReceiveText(message);
                    Log.d(SERVER_TAG,"Message send to client "+i);
                }
                ChatFragment.addToList(message);
            }
        }

        //Inner Classes
        private class ServerThread implements Runnable {

            @Override
            public void run() {

                while (!Thread.currentThread().isInterrupted()) {
                    try {
                        //Connecting to Client Socket
                        Log.d(SERVER_TAG, "Server Socket created, awaiting Connection");
                        Socket client = serverSocket.accept();
                        Log.d(SERVER_TAG, "Connected");
                        ClientHandler clientHandler = new ClientHandler(client);
                        clientHandler.start();
                        clientHandlers.add(clientHandler);
                    } catch (IOException e) {
                        Log.d(SERVER_TAG, "Error Creating Server: " + e.getMessage());
                        e.printStackTrace();
                    }
                }
            }
        }

        private class ClientHandler extends Thread{

            private final Socket client;

            public ClientHandler(Socket client) {
                this.client = client;
            }

            public void remove(){
                Iterator<ClientHandler> itr = clientHandlers.iterator();
                while (itr.hasNext()){
                    Socket var = itr.next().client;
                    if(var==this.client){
                        itr.remove();
                        Log.i("client","removed");
                    }
                }
            }

            public void run() {
                try{
                    BufferedReader input = new BufferedReader(new InputStreamReader(client.getInputStream()));
                    while (true) {

                        String message = null;
                        message = input.readLine();
                        if (message != null) {
                            Log.d(SERVER_TAG, "Message Received");
                            Log.d(SERVER_TAG,message);
                            SendToClients(message);
                        } else {
                            Log.d(SERVER_TAG, "Null Message");
                            remove();
                            break;
                        }

                    }
                }
                catch(Exception e){
                    e.printStackTrace();
                }
            }

            public void SendToClient(String message) throws IOException {
                PrintWriter out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(client.getOutputStream())), true);
                out.println(message);
                out.flush();
            }
        }
    }

    public class ServerBinder extends Binder {
        public ServerService getService(){
            return ServerService.this;
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    //    Methods for server
    public void constructor(Context context,String str) throws IOException {
        mChatServer = new ChatServer(context,str);
    }

    public void sendToClients(String message) throws IOException {
        mChatServer.SendToClients(message);
    }
}
