package com.dexter.watsonstt;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    TextView textView;
    recievr reciever;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView = (TextView) findViewById(R.id.textView);

        startService(new Intent(MainActivity.this,WatsonSTT.class));


    }

    @Override
    protected void onResume() {
        reciever = new recievr();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(WatsonSTT.MY_ACTION);
        registerReceiver(reciever, intentFilter);
        super.onResume();
    }

    @Override
    protected void onStop() {
        super.onStop();

        unregisterReceiver(reciever);
    }

    public class recievr extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            textView.setText(intent.getStringExtra("Status"));
        }
    }

}
