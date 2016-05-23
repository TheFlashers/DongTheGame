package dong.dms.dong;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends Activity implements OnClickListener {

    Button playButton,
           registerButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        // Inflate layout
        setContentView(R.layout.menu_layout);

        // Get view elements
        playButton = (Button) findViewById(R.id.playButton);
        registerButton = (Button) findViewById(R.id.registerButton);

        // Bind click events
        playButton.setOnClickListener(this);
        registerButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {

        Intent intent = null;

        switch (view.getId()) {
            case R.id.playButton :
                intent = new Intent(this, ClientServerActivity.class); break;
            case R.id.registerButton :
                intent = new Intent(this, RegisterTagActivity.class); break;
        }

        startActivity(intent);
    }
}
