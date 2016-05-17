package dong.dms.dong;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;

/**
 * Created by Naki on 11/05/2016.
 */
public class DongServer implements ComNode{

    private boolean stopRequested;
    private ClientRunnable client;
    private ClientActivity clientActivity;
    BluetoothSocket socket;

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
                socket = serverSocket.accept(10000);
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

    private class ClientRunnable implements Runnable {

        private BluetoothSocket socket;

        public ClientRunnable(BluetoothSocket sc) {
            socket = sc;
        }

        @Override
        public void run() {
            try {
                while(!stopRequested){
                    GameObject m = receiveObject();
                    Log.d("received", m.lol);
                    clientActivity.displayResult(m.lol);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}


