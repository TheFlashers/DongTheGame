package dong.dms.dong;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class GameActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        // Inflate layout
        setContentView(R.layout.game_layout);

        // TODO - Set up game, play game

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Game finished
        Intent intent = new Intent(this, GameFinishedActivity.class);
        // Package data into bundle?
        startActivity(intent);
    }
}
