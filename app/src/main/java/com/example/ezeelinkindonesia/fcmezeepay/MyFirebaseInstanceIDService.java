package com.example.ezeelinkindonesia.fcmezeepay;

import android.content.Intent;
import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.DataOutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import static android.content.ContentValues.TAG;


public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService {

    public static final String TOKEN_BROADCAST = "fcmToken";

    /**
     *
     */
    @Override
    public void onTokenRefresh()
    {
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();


        getApplicationContext().sendBroadcast(new Intent(TOKEN_BROADCAST));

        // If you want to send messages to this application instance or
        // manage this apps subscriptions on the server side, send the
        // Instance ID token to your app server.
        storageToken(refreshedToken);
        //sendToken(refreshedToken);

    }

    /**
     * Storage token refreshed in shared prefs
     *
     * @param token
     */
    private void storageToken(String token) {
        SharedPrefManager.getInstance(getApplicationContext()).storeToken(token);
    }

    /**
     * send entity Person with token to save in API.
     * @param token
     */
    private void sendToken(String token)
    {
        JSONObject person = new JSONObject();
        JSONObject tokenFCM = new JSONObject();
        try
        {
            tokenFCM.put("token", token);
            person.put("name", "Ezeelink");
            person.put("tokenFCM", tokenFCM);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        executePost("http://192.168.0.110:8080/person", person);

    }

    /**
     *
     * @param targetURL
     * @param jsonParam
     * @return
     */
    private static Boolean executePost(String targetURL, JSONObject jsonParam)
    {
        URL url;
        HttpURLConnection connection = null;
        try {
            url = new URL(targetURL);
            connection = (HttpURLConnection)url.openConnection();
            connection.setDoOutput(true);
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            connection.connect();

            DataOutputStream wr = new DataOutputStream(
                    connection.getOutputStream ());
            wr.writeBytes(jsonParam.toString());
            wr.flush();
            wr.close ();

            int response = connection.getResponseCode();
            if (response >= 200 && response <=399){
                return true;
            } else {
                return false;
            }

        } catch (Exception e) {
            Log.e("API REQUEST", e.getMessage());
            return false;

        } finally {

            if(connection != null) {
                connection.disconnect();
            }
        }
    }
}
