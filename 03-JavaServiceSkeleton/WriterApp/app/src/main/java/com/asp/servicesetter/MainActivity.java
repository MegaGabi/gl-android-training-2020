package com.asp.servicesetter;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.asp.myservice.IMyAInterface;

public class MainActivity extends AppCompatActivity {
    private static final String LOG_TAG = "SetServiceVal:";
    private static final String SERVICE_PKG = "com.asp.myservice";
    private static final String SERVICE = "com.asp.myservice.HolderService";
    private IMyAInterface sservice;

    private ServiceConnection mConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            if(service != null)
            {
                sservice = IMyAInterface.Stub.asInterface(service);
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button saveButton = (Button)findViewById(R.id.save_button);
        saveButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                TextView text = (TextView)findViewById(R.id.saveText);
                String s = text.getText().toString();
                try {
                    Log.i(LOG_TAG, "Set service val");
                    sservice.setValue(s);
                } catch (RemoteException |NullPointerException e) {
                    Log.e(LOG_TAG, "Some exception catched" + e.getMessage().toString());
                }
            }
        });

        Intent inten = new Intent();
        inten.setClassName(SERVICE_PKG,SERVICE);
        bindService(inten, mConnection, BIND_AUTO_CREATE);
    }

    protected void onDestroy() {
        super.onDestroy();
        unbindService(mConnection);
    }
}