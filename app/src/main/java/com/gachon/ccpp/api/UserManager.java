package com.gachon.ccpp.api;

import android.util.Log;

import androidx.annotation.NonNull;

import com.gachon.ccpp.util.DataHandler;
import com.gachon.ccpp.util.DataHandler.*;
import com.gachon.ccpp.util.RSA.Encrypt;
import com.gachon.ccpp.util.RSA.Decrypt;

import org.json.JSONObject;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import okio.ByteString;

public class UserManager extends WebSocketListener {
    private static final Encrypt enc = new Encrypt("User", 1711, 989);
    private static final Decrypt dec = new Decrypt("User", 1457, 1369);

    // Socket will be the identifier for client
    private WebSocket clientSocket = null;
    // Validity (login succeed) for socket
    private String uId = null;

    public UserManager(String id, String sId) {
        try {
            String unifiedId = sId + id;
            String plain = DataHandler.packData(MSG_ID.LOGIN, "id", unifiedId);
            String cipher = enc.doEncrypt(plain, plain.length());

            Request request = new Request.Builder().url("wss://ccppwebserver.herokuapp.com/ws").build();

            clientSocket = new OkHttpClient().newWebSocket(request, this);
            clientSocket.send(cipher);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void closeSocket(){
        if (clientSocket != null)
            clientSocket.close(0, null);
    }

    public void handleMessage(@NonNull String message) {
        try {
            String plain = dec.doDecrypt(message, message.length());
            JSONObject data = new JSONObject(plain);

            switch (MSG_ID.valueOf((String)data.get("request"))) {
                case LOGIN:
                    if (data.has("uId")) {
                        uId = (String)data.get("uId");
                        Log.d("CCPP", "Sucessfully logined with: " + uId);
                    }
                    else
                        Log.d("CCPP", "Failed to login.");
                    break;
                case CHAT:
                    if (data.has("result")) {
                        Log.d("CCPP", "Test: " + data);
                    }
                    break;
                case ALARM:
                    if (data.has("result")) {
                        Log.d("CCPP", "Test: " + data);
                    }
                    break;
                default:
                    break;
            }
        } catch (Exception e) {
            Log.d("CCPP", "Failed parsing json file: " + message + ", " + e);
        }
    }

    @Override
    public void onOpen(@NonNull WebSocket socket, @NonNull Response response) {
    }

    @Override
    public void onMessage(@NonNull WebSocket socket, @NonNull String message) {
        handleMessage(message);
    }

    @Override
    public void onMessage(@NonNull WebSocket socket, @NonNull ByteString message) {
        handleMessage(message.toString());
    }
}
