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
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
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
        try {
            PrintWriter pw = new PrintWriter(new BufferedWriter
                     (new OutputStreamWriter(socket.getOutputStream())));
            pw.println(message);
            pw.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // implementation of ChatNode method
    public void stop()
    {  stopRequested = true;
        if (deviceDiscoveryBroadcastReceiver != null)
        {  clientActivity.unregisterReceiver
                (deviceDiscoveryBroadcastReceiver);
            deviceDiscoveryBroadcastReceiver = null;
        }
        synchronized (devices)
        {  devices.notifyAll();
        }
        synchronized (messages)
        {  messages.notifyAll();
        }
        if (socket != null)
        {  try
        {  socket.close();
        }
        catch (IOException e)
        { // ignore
        }
        }
    }

    @Override
    public void registerActivity(ClientActivity clientActivity) {
        this.clientActivity = clientActivity;
    }

    @Override
    public void sendObject(GameObject o) throws IOException {
        OutputStream os = socket.getOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(os);
        oos.writeObject(o);
        oos.flush();
        os.close();

    }

    @Override
    public GameObject receiveObject() throws IOException {
        InputStream i = socket.getInputStream();
        ObjectInputStream ois = new ObjectInputStream(i);
        GameObject o = null;
        try {
            o = (GameObject) ois.readObject();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return o;
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

        // now check each device for the Bluetooth application UUID
        // note only newer API support fetchUuidsWithSdp to perform SDP
        socket = null;
        for (BluetoothDevice device : devices)
        {  // try to open a connection to device using UUID
            try
            {  clientActivity.displayResult
                    ("CLIENT: checking for server on " + device.getName());
                Log.w("ChatClient", "Checking for server on "
                        + device.getName());
                socket = device.createRfcommSocketToServiceRecord
                        (ComNode.SERVICE_UUID);
                // open the connection
                socket.connect();
                bluetoothAdapter.cancelDiscovery();
                break;
            }
            catch (IOException e)
            {  // ignore and try next device
                socket = null;
            }
        }
        if (socket == null)
        {  clientActivity.displayResult
                ("CLIENT: no server found, restart client");
            Log.e("ChatClient", "No server service found");
            stopRequested = true;
            return;
        }
        clientActivity.displayResult("CLIENT: chat server found");
        Log.w("ChatClient", "Chat server service found");

        //sending


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



}
