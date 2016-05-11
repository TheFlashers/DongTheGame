package dong.dms.dong;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Created by Naki on 11/05/2016.
 */
public class DongServer implements ComNode{

    private boolean stopRequested;
    private ClientRunnable client;
    private ClientActivity clientActivity;

    @Override
    public void run() {
        stopRequested = false;
        BluetoothServerSocket serverSocket = null;

        try {
            serverSocket = BluetoothAdapter.getDefaultAdapter()
                    .listenUsingRfcommWithServiceRecord(ComNode.SERVICE_NAME, ComNode.SERVICE_UUID);
        } catch (IOException e) {
            e.printStackTrace();
        }

        while (!stopRequested) {
            try {
                BluetoothSocket socket = serverSocket.accept(10000);
                ClientRunnable client = new ClientRunnable(socket);
                this.client = client;
                new Thread(client).start();


            } catch (IOException e) {
                e.printStackTrace();
            }
        }

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

    private class ClientRunnable implements Runnable {

        private BluetoothSocket socket;

        public ClientRunnable(BluetoothSocket sc) {
            socket = sc;
        }

        @Override
        public void run() {
            try {
                BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                while(!stopRequested){
                    String m = br.readLine();
                    Log.d("received", m);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}


