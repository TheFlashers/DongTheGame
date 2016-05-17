package dong.dms.dong;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import org.w3c.dom.Text;

public class TagDetailsActivity extends Activity {

    Player player;

    TextView nameText,
             winsText,
             lossesText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        // Inflate layout
        setContentView(R.layout.tag_details_layout);
        // Get player data from intent
        player = (Player) this.getIntent().getSerializableExtra("player");
        // Get view elements
        nameText = (TextView) findViewById(R.id.detailsNameText);
        winsText = (TextView) findViewById(R.id.detailsWinsCountText);
        lossesText = (TextView) findViewById(R.id.detailsLossesCountText);
        // Set values on text elements
        nameText.setText(player.getName());
        winsText.setText(Integer.toString(player.getWins()));
        lossesText.setText(Integer.toString(player.getLosses()));
    }
}
