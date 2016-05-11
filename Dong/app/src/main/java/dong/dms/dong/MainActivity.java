package dong.dms.dong;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.view.View.OnClickListener;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends Activity implements OnClickListener {

    Button clientStart;
    Button serverStart;

    BroadcastReceiver bluetoothStatusBroadcastReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        clientStart = (Button) findViewById(R.id.client_start_button);
        serverStart = (Button) findViewById(R.id.server_start_button);

        serverStart.setOnClickListener(this);
        clientStart.setOnClickListener(this);


    }

    @Override
    protected void onStart() {
        super.onStart();
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
                        = new BroadcastReceiver() {
                    @Override
                    public void onReceive(Context context, Intent intent) {
                        //add receive code
                    }
                };
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

    }

    @Override
    public void onClick(View view) {
        switch (view.getId() /*to get clicked view id**/) {
            case R.id.client_start_button:
                Log.d("lol", "ClientClicked");
                ComNode chatNode = new DongClient();
                Intent intent = new Intent(this, ClientActivity.class);
                intent.putExtra(ClientActivity.class.getName(), chatNode);
                startActivity(intent);

                break;
            case R.id.server_start_button:
                Log.d("lol", "ServerClicked");
                ComNode cn = new DongClient();
                Intent i = new Intent(this, ClientActivity.class);
                i.putExtra(ClientActivity.class.getName(), cn);
                startActivity(i);
            default:
                break;
        }
    }

    @Override
    public void onStop()
    {  super.onStop();
        if (bluetoothStatusBroadcastReceiver != null)
            unregisterReceiver(bluetoothStatusBroadcastReceiver);
    }
}
