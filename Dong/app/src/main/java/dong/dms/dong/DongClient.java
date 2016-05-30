package dong.dms.dong;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by MI on 11/05/16.
 */
public class DongClient implements ComNode {

    private BroadcastReceiver deviceDiscoveryBroadcastReceiver;
    private List<BluetoothDevice> devices;
    private GameActivity activity;
    private GameLogic logic;
    private boolean stopRequested = false;
    private BluetoothSocket socket;
    private List<String> messages;//list of messages still to be mailed


    DongClient() {
        messages = new ArrayList<String>();
        deviceDiscoveryBroadcastReceiver = null;
        activity = null;
        devices = new ArrayList<BluetoothDevice>();
        socket = null;

    }


    @Override
    public void setGameLogic(GameLogic gl) {
        this.logic = gl;
    }

    @Override
    public void forward(GameObject go) {
        try {
            String gameObjectString = go.createJsonString();
            PrintWriter pw = new PrintWriter(new BufferedWriter
                    (new OutputStreamWriter(socket.getOutputStream())));
            pw.println(gameObjectString);
            pw.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // implementation of ChatNode method
    public void stop() {
        stopRequested = true;
        if (deviceDiscoveryBroadcastReceiver != null) {
            activity.unregisterReceiver
                    (deviceDiscoveryBroadcastReceiver);
            deviceDiscoveryBroadcastReceiver = null;
        }
        synchronized (devices) {
            devices.notifyAll();
        }
        synchronized (messages) {
            messages.notifyAll();
        }
        if (socket != null) {
            try {
                socket.close();
            } catch (IOException e) { // ignore
            }
        }
    }

    @Override
    public void registerActivity(GameActivity activity) {
        this.activity = activity;
    }

    @Override
    public void confirmConnect() {
        GameObject go = new GameObject();
        go.connectConfirm = true;
        forward(go);
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
        activity.registerReceiver(deviceDiscoveryBroadcastReceiver,
                discoveryIntentFilter);
        BluetoothAdapter bluetoothAdapter
                = BluetoothAdapter.getDefaultAdapter();
        bluetoothAdapter.startDiscovery();
        synchronized (devices) {
            try {
                devices.wait();
            } catch (InterruptedException e) {  // ignore
            }
        }
        if (devices.size() == 0 && !stopRequested) {
            activity.displayResult
                    ("CLIENT: no devices discovered, restart client");
            Log.w("Devices", "No devices discovered");
            stopRequested = true;
            return;
        }

        // now check each device for the Bluetooth application UUID
        // note only newer API support fetchUuidsWithSdp to perform SDP
        socket = null;
        for (BluetoothDevice device : devices) {  // try to open a connection to device using UUID
            try {
                activity.displayResult
                        ("CLIENT: checking for server on " + device.getName());
                Log.w("ChatClient", "Checking for server on "
                        + device.getName());
                socket = device.createRfcommSocketToServiceRecord
                        (ComNode.SERVICE_UUID);
                // open the connection
                socket.connect();
                bluetoothAdapter.cancelDiscovery();
                break;
            } catch (IOException e) {  // ignore and try next device
                socket = null;
            }
        }
        if (socket == null) {
            activity.displayResult
                    ("CLIENT: no server found, restart client");
            Log.e("ChatClient", "No server service found");
            stopRequested = true;
            return;
        }
        activity.displayResult("CLIENT: chat server found");
        Log.w("ChatClient", "Chat server service found");
        activity.setConnected(true);
        new Thread(new ObjectReceiver()).start();

        activity.confirmConnect(true);
        //sending


    }


    // inner class that receives device discovery changes
    public class DeviceDiscoveryBroadcastReceiver
            extends BroadcastReceiver {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {  // a device has been found
                BluetoothDevice device = intent.getParcelableExtra
                        (BluetoothDevice.EXTRA_DEVICE);
                synchronized (devices) {
                    devices.add(device);
                }
                // note newer API can use device.fetchUuidsWithSdp for SDP
                activity.displayResult
                        ("CLIENT: device discovered " + device.getName());
                Log.w("ChatClient", "Device discovered " + device.getName());
            } else if (BluetoothAdapter.ACTION_DISCOVERY_STARTED.equals
                    (action)) {
                activity.displayResult
                        ("CLIENT: device discovery started");
                Log.w("ChatClient", "Device discovery started");
            } else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals
                    (action)) {  // notify chat client that device discovery has finished
                synchronized (devices) {
                    devices.notifyAll();
                }
                activity.displayResult
                        ("CLIENT: device discovery finished");
                Log.w("ChatClient", "Device discovery finished");
            }
        }


    }

    private class ObjectReceiver implements Runnable {

        @Override
        public void run() {
            try {
                BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                while (!stopRequested) {
                    String m = br.readLine();
                    GameObject go = GameObject.parseJSON(m);
                    logic.receiveMessage(go);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


    }
}
