package com.example.ezeelinkindonesia.fcmezeepay;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;

import static android.content.ContentValues.TAG;

public class MainActivity extends AppCompatActivity {

    private TextView tvToken;
    private BroadcastReceiver broadcastReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvToken = (TextView) findViewById(R.id.tv_token);

        FirebaseMessaging.getInstance().subscribeToTopic("weather").addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task)
                    {
                        String msg = "subscribe Success";
                        if (!task.isSuccessful()) {
                            msg = "subscribe Fail";
                        }
                        Log.d(TAG, msg);
                        Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();
                    }
                });


        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                tvToken.setText(SharedPrefManager.getInstance(MainActivity.this).getToken());
            }
        };

        if(SharedPrefManager.getInstance(this).getToken() != null)
        {
            tvToken.setText(SharedPrefManager.getInstance(MainActivity.this).getToken());
        }

        registerReceiver(broadcastReceiver, new IntentFilter(MyFirebaseInstanceIDService.TOKEN_BROADCAST));

    }

    @Override
    protected void onResume()
    {
        super.onResume();

        //unregisterReceiver(broadcastReceiver);
    }

    @Override
    protected void onPause()
    {
        super.onPause();

        try {
            unregisterReceiver(broadcastReceiver);
        }
        catch(IllegalArgumentException e)
        {
            e.printStackTrace();
        }

    }

    @Override
    protected void onStop()
    {
        //unregisterReceiver(broadcastReceiver);
        super.onStop();
    }
}
