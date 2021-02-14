package com.vasu.localchat.network.client;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;

import com.vasu.localchat.fragment.ChatFragment;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

import static com.vasu.localchat.util.Constants.CLIENT_TAG;

public class ClientService extends Service {

    ChatClient mChatClient;
    private final IBinder binder = new ClientBinder();

    //    inner classes
    public class ChatClient{
        //Data Members
        private final InetAddress serverAddress;
        private final int serverPort;
        private Socket socket;
        private final Thread receiveThread;

        //Constructor
        public ChatClient(InetAddress serverAddress, int serverPort) {
            Log.d(CLIENT_TAG, "Creating chatClient");
            this.serverAddress = serverAddress;
            this.serverPort = serverPort;
            this.receiveThread = new Thread( new ChatClient.ReceiveThread());
            receiveThread.start();
        }

        //Member Functions
        public Socket getSocket() {
            return socket;
        }

        public void setSocket(Socket socket) {
            this.socket = socket;
        }

        public void SendMessage(String message){
            {
                try {
                    Socket socket = getSocket();
                    if (socket == null) {
                        Log.d(CLIENT_TAG, "Socket is null");
                    } else if (socket.getOutputStream() == null) {
                        Log.d(CLIENT_TAG, "Socket output stream is null");
                    }
                    PrintWriter out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(getSocket().getOutputStream())), true);
                    out.println(message);
                    out.flush();
                    Log.d(CLIENT_TAG, "Message Sent");
                } catch (UnknownHostException e) {
                    Log.d(CLIENT_TAG, "Unknown Host:", e);
                } catch (IOException e) {
                    Log.d(CLIENT_TAG, "I/O Exception:", e);
                } catch (Exception e) {
                    Log.d(CLIENT_TAG, "Error:", e);
                }
                Log.d(CLIENT_TAG, "Client sent message.");
            }
        }

        public void tearDown() {
            receiveThread.interrupt();
        }

        //Inner Classes
        private class ReceiveThread implements Runnable{

            BufferedReader input;

            @Override
            public void run() {
                try {

                    //Set Client Side Socket
                    if (getSocket() == null) {
                        setSocket(new Socket(serverAddress, serverPort));
                        Log.d(CLIENT_TAG, "Client-side socket initialized.");
                    }
                    else {
                        Log.d(CLIENT_TAG, "Socket already initialized. skipping!");
                    }

                    //Receive Message
                    input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    while (!Thread.currentThread().isInterrupted()) {

                        String messageStr = null;
                        messageStr = input.readLine();
                        if (messageStr != null) {
                            Log.d(CLIENT_TAG, "Read from the stream: " + messageStr);
                            //TODO Update UI
//                            ClientActivity.setClientReceiveText(messageStr);
                              ChatFragment.addToList(messageStr);
                        } else {
                            Log.d(CLIENT_TAG, "The nulls! The nulls!");
                            break;
                        }
                    }
                    input.close();
                } catch (UnknownHostException e) {
                    Log.e(CLIENT_TAG, "Initializing socket failed, UHE", e);
                } catch (IOException e) {
                    Log.e(CLIENT_TAG, "Initializing socket failed, IOE.", e);
                }
                catch (Exception e){
                    Log.e(CLIENT_TAG, "Initializing socket failed", e);
                }
            }
        }
    }

    public class ClientBinder extends Binder {
        public ClientService getService(){
            return ClientService.this;
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }


    //    Methods for clients
    public void constructor(InetAddress serverAddress, int serverPort){
        mChatClient=new ChatClient(serverAddress,serverPort);
    }

    public void sendMessage(String message){
        mChatClient.SendMessage(message);
    }


}

