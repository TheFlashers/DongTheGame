package dong.dms.dong;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by MI on 11/05/16.
 */
public class DongClient implements ComNode{

    private BroadcastReceiver deviceDiscoveryBroadcastReceiver;
    private List<BluetoothDevice> devices;
    private ClientActivity clientActivity;
    private boolean stopRequested = false;
    private BluetoothSocket socket;
    private List<String> messages;//list of messages still to be mailed


    DongClient() {
        messages = new ArrayList<String>();
        deviceDiscoveryBroadcastReceiver = null;
        clientActivity = null;
        devices = new ArrayList<BluetoothDevice>();
        socket = null;

    }



    @Override
    public void forward(String message) {

    }

    @Override
    public void stop() {

    }

    @Override
    public void registerActivity(ClientActivity clientActivity) {
        this.clientActivity = clientActivity;
    }

    @Override
    public void run() {
        stopRequested = false;
        devices.clear();

        // start device discovery(could instead first try paired devices)
        deviceDiscoveryBroadcastReceiver
                = new DeviceDiscoveryBroadcastReceiver();
        IntentFilter discoveryIntentFilter = new IntentFilter();
        discoveryIntentFilter.addAction(BluetoothDevice.ACTION_FOUND);
        discoveryIntentFilter.addAction
                (BluetoothAdapter.ACTION_DISCOVERY_STARTED);
        discoveryIntentFilter.addAction
                (BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        clientActivity.registerReceiver(deviceDiscoveryBroadcastReceiver,
                discoveryIntentFilter);
        BluetoothAdapter bluetoothAdapter
                = BluetoothAdapter.getDefaultAdapter();
        bluetoothAdapter.startDiscovery();
        synchronized (devices)
        {  try
        {  devices.wait();
        }
        catch (InterruptedException e)
        {  // ignore
        }
        }
        if (devices.size() == 0 && !stopRequested)
        {  clientActivity.displayResult
                ("CLIENT: no devices discovered, restart client");
            Log.w("Devices", "No devices discovered");
            stopRequested = true;
            return;
        }

    }

    // inner class that receives device discovery changes
    public class DeviceDiscoveryBroadcastReceiver
            extends BroadcastReceiver
    {
        public void onReceive(Context context, Intent intent)
        {  String action = intent.getAction();
            if (BluetoothDevice.ACTION_FOUND.equals(action))
            {  // a device has been found
                BluetoothDevice device = intent.getParcelableExtra
                        (BluetoothDevice.EXTRA_DEVICE);
                synchronized (devices)
                {  devices.add(device);
                }
                // note newer API can use device.fetchUuidsWithSdp for SDP
                clientActivity.displayResult
                        ("CLIENT: device discovered " + device.getName());
                Log.w("ChatClient", "Device discovered " + device.getName());
            }
            else if (BluetoothAdapter.ACTION_DISCOVERY_STARTED.equals
                    (action))
            {  clientActivity.displayResult
                    ("CLIENT: device discovery started");
                Log.w("ChatClient", "Device discovery started");
            }
            else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals
                    (action))
            {  // notify chat client that device discovery has finished
                synchronized (devices)
                {  devices.notifyAll();
                }
                clientActivity.displayResult
                        ("CLIENT: device discovery finished");
                Log.w("ChatClient", "Device discovery finished");
            }
        }
    }


    // inner class that handles sending messages to server chat nodes
    private class DongSender implements Runnable
    {
        public void run()
        {  PrintWriter pw = null;
            try
            {  pw = new PrintWriter(new BufferedWriter
                    (new OutputStreamWriter(socket.getOutputStream())));
            }
            catch (IOException e)
            {  Log.e("ChatClient", "Mailer IOException: " + e);
                stop();
            }
            while (!stopRequested)
            {  // get a message
                String message;
                synchronized (messages)
                {  while (messages.size() == 0)
                {  try
                {  messages.wait();
                }
                catch (InterruptedException e)
                { // ignore
                }
                    if (stopRequested)
                        return;
                }
                    message = messages.remove(0);
                }
                // forward message to server
                pw.println(message);
                pw.flush();
                clientActivity.displayResult
                        ("CLIENT: sending message " + message);
            }
        }
    }
}
