package dong.dms.dong;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by MI on 16/05/16.
 */
public class BluetoothHelper implements ComNode{
    private BroadcastReceiver deviceDiscoveryBroadcastReceiver;
    private List<BluetoothDevice> devices;
    private ClientActivity clientActivity;
    private boolean stopRequested;
    private BluetoothSocket socket;
    private List<String> messages;//list of messages still to be mailed


    BluetoothHelper() {
        stopRequested = false;
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

    }

    @Override
    public void forwardObject(Object o) {

    }

    @Override
    public Object receieveObject() {
        return null;
    }

    @Override
    public void run() {

    }
}
