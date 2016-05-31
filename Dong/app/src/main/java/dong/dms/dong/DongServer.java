package dong.dms.dong;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.nio.Buffer;

/**
 * Created by Naki on 11/05/2016.
 */
public class DongServer implements ComNode{

    private boolean stopRequested;
    private ClientRunnable client;
    private GameLogic logic;
    private GameActivity activity;

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

        boolean found = false;

        while (!found) {
            try {
                BluetoothSocket socket = serverSocket.accept(20000);
                ClientRunnable client = new ClientRunnable(socket);
                this.client = client;
                new Thread(client).start();
                activity.setConnected(true);
                found = true;

            } catch (IOException e) {
                e.printStackTrace();
            }
        }



    }

    @Override
    public void setGameLogic(GameLogic gl) {
        this.logic = gl;
    }

    @Override
    public void forward(GameObject go) {
        client.send(go);
    }

    @Override
    public void stop() {
       stopRequested = true;
    }

    @Override
    public void registerActivity(GameActivity activity) {
        this.activity = activity;
    }

    @Override
    public void sendConfirm(boolean confirm) {
        GameObject go = new GameObject();
        go.connectConfirm = confirm;
        forward(go);
    }

    @Override
    public void setConfirm(boolean set) {
        activity.confirmConnect(set);
    }

    private class ClientRunnable implements Runnable {

        private BluetoothSocket socket;
        private BufferedReader br;
        PrintWriter pw;

        public ClientRunnable(BluetoothSocket sc) {
            socket = sc;

            try {
                br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                pw = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void run() {
            try {
                while(!stopRequested){
                    String m = br.readLine();
                    GameObject go = GameObject.parseJSON(m);
                    logic.receiveMessage(go);
                }
                br.close();
                pw.close();
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public synchronized void send(GameObject go) {

                String gameObjectString = go.createJsonString();
                pw.println(gameObjectString);
                pw.flush();
        }

    }

}






