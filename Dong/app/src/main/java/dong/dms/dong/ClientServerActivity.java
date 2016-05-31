package dong.dms.dong;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class ClientServerActivity extends Activity implements View.OnClickListener {

    Button hostButton,
           joinButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        // Inflate layout
        setContentView(R.layout.client_server_layout);

        // Get view elements
        hostButton = (Button) findViewById(R.id.hostButton);
        joinButton = (Button) findViewById(R.id.joinButton);
        // Bind click events
        hostButton.setOnClickListener(this);
        joinButton.setOnClickListener(this);

        Intent discoverableIntent
                = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
        discoverableIntent.putExtra
                (BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION,300); //5 min
        startActivity(discoverableIntent);
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.hostButton : hostLogic(); break;
            case R.id.joinButton : joinLogic(); break;
        }

        //Intent intent = new Intent(this, GameActivity.class);
        // Maybe bundle some data before starting?
        //startActivity(intent);
    }

    private void hostLogic() {
        ComNode cn = new DongServer();
        Intent i = new Intent(this, GameActivity.class);

        i.putExtra("client", cn);
        startActivity(i);
    }

    private void joinLogic() {
        ComNode chatNode = new DongClient();
        Intent intent = new Intent(this, GameActivity.class);
        intent.putExtra("client", chatNode);
        startActivity(intent);
    }

    private void gameWinLossSimulation() {

        // Temp method for testing NFC functionality

        Intent intent = new Intent(this, GameActivity.class);
        startActivity(intent);
    }
}
