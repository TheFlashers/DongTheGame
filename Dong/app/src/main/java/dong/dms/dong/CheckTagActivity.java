package dong.dms.dong;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

public class CheckTagActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Inflate layout
        setContentView(R.layout.check_tag_layout);

        // Bind NFC logic
        nfcLogic();
    }

    private void nfcLogic() {

        // TODO - SCAN NFC TAG FOR PLAYER DATA

        // Get player data (will be from reading NFC tag)
        Player player = new Player("Joe Bloggs");
        // Create intent, store player
        Intent intent = new Intent(this, TagDetailsActivity.class);
        intent.putExtra("player", player);
        // Call tag details
        startActivity(intent);
    }
}
