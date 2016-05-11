package dong.dms.dong;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.view.View.OnClickListener;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends Activity implements OnClickListener {

    Button clientStart;
    Button serverStart;

    Toast t;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        clientStart = (Button) findViewById(R.id.client_start_button);
        serverStart = (Button) findViewById(R.id.server_start_button);

        serverStart.setOnClickListener(this);
        clientStart.setOnClickListener(this);


    }

    // Create an anonymous implementation of OnClickListener
    private View.OnClickListener mCorkyListener = new View.OnClickListener() {
        public void onClick(View v) {
            // do something when the button is clicked
            // Yes we will handle click here but which button clicked??? We don't know

            // So we will make

        }
    };

    @Override
    public void onClick(View view) {
        switch (view.getId() /*to get clicked view id**/) {
            case R.id.client_start_button:
                Log.d("lol", "ClientClicked");
                ComNode chatNode = new DongClient();
                Intent intent = new Intent(this, ClientActivity.class);
                intent.putExtra(ClientActivity.class.getName(), chatNode);
                startActivity(intent);

                break;
            case R.id.server_start_button:
                Log.d("lol", "ServerClicked");
                ComNode cn = new DongClient();
                Intent i = new Intent(this, ClientActivity.class);
                i.putExtra(ClientActivity.class.getName(), cn);
                startActivity(i);
            default:
                break;
        }
    }
}
