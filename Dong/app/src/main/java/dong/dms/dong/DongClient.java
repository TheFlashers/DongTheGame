package dong.dms.dong;

import android.content.BroadcastReceiver;

/**
 * Created by MI on 11/05/16.
 */
public class DongClient implements ComNode{

    BroadcastReceiver deviceDiscoveryBroadcastReceiver;

    DongClient() {
        deviceDiscoveryBroadcastReceiver = null;
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
    public void run() {


    }
}
