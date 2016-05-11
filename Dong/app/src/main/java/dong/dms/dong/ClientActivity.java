package dong.dms.dong;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

public class ClientActivity extends Activity {


    private BroadcastReceiver bluetoothStatusBroadcastReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
/*
        BluetoothAdapter bluetoothAdapter
                = BluetoothAdapter.getDefaultAdapter();
        if (bluetoothAdapter == null)
            Toast.makeText(this, "no access to blue tooth",
                    Toast.LENGTH_LONG).show();
        else {
            Toast.makeText(this, "bluetooth available",
                    Toast.LENGTH_LONG).show();
            // create a broadcast receiver notified when Bluetooth status
            // changes
            if (bluetoothStatusBroadcastReceiver == null)
                bluetoothStatusBroadcastReceiver
                        = new BluetoothStatusBroadcastReceiver();
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);
            intentFilter.addAction
                    (BluetoothAdapter.ACTION_SCAN_MODE_CHANGED);
            registerReceiver(bluetoothStatusBroadcastReceiver,
                    intentFilter);
            if (!bluetoothAdapter.isEnabled()) {
                Toast.makeText(this, "bluetooth not enabled",
                        Toast.LENGTH_LONG).show();
                // try to enable Bluetooth on device
                Intent enableBluetoothIntent
                        = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivity(enableBluetoothIntent);
            } else
                Toast.makeText(this, "bluetooth enabled",
                        Toast.LENGTH_LONG).show();
        }
    */
    }
    

}
