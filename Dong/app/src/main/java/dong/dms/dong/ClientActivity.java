package dong.dms.dong;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

public class ClientActivity extends Activity {


    private BroadcastReceiver bluetoothStatusBroadcastReceiver;
    private ComNode comNode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client);

        Intent intent = getIntent();
        comNode = (ComNode)intent.getExtras().get("type");
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "You sent LOL", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                comNode.forward("LOL");
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        comNode.registerActivity(this);
        Thread thread = new Thread(comNode);
        thread.start();

    }

    public void displayResult(String r) {
        //Toast.makeText(this, r, Toast.LENGTH_SHORT).show();
        Log.d("Device", r);
    }

    @Override
    public void onStop()
    {  super.onStop();

            comNode.stop();
    }
    

}
