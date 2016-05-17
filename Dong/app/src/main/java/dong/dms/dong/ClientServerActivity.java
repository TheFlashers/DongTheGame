package dong.dms.dong;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.hostButton : hostLogic(); break;
            case R.id.joinButton : joinLogic(); break;
        }

        Intent intent = new Intent(this, GameActivity.class);
        // Maybe bundle some data before starting?
        startActivity(intent);
    }

    private void hostLogic() {
        // TODO - Create session and await connection
    }

    private void joinLogic() {
        // TODO - Join created session
    }
}
