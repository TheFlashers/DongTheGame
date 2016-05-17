package dong.dms.dong;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.Random;

public class GameFinishedActivity extends Activity {

    TextView resultText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        // Inflate layout
        setContentView(R.layout.game_finished_layout);

        // Get view elements
        resultText = (TextView) findViewById(R.id.gameFinishedText);

        // Demonstration text
        String result = (new Random().nextInt(2) == 0) ? "You Lost!" : "You Won!" ;
        resultText.setText(result);
    }
}
