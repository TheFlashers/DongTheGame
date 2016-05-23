package dong.dms.dong;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Map;
import java.util.Random;

public class GameFinishedActivity extends Activity implements View.OnClickListener  {

    private SharedPreferences playerDetails;
    private boolean writeModeEnabled;
    private NfcAdapter nfcAdapter;
    private NfcWriter nfcReadWrite;
    TextView resultText;
    Button writeButton;
    // Temporary flag for winning/losing testing
    int result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        // Inflate layout
        setContentView(R.layout.game_finished_layout);

        nfcAdapter = NfcAdapter.getDefaultAdapter(this);
        nfcReadWrite = new NfcWriter();
        writeModeEnabled = false;

        // Get view elements
        resultText = (TextView) findViewById(R.id.gameFinishedText);
        writeButton = (Button) findViewById(R.id.writeTagButton);
        // Bind events
        writeButton.setOnClickListener(this);

        // TESTING ELEMENTS
        result = new Random().nextInt(2);
        String resultMessage = (result == 0) ? "You Lost!" : "You Won!";
        resultText.setText(resultMessage);
    }

    @Override
    public void onPause() {
        super.onPause();
        disableWriteMode();
    }

    @Override
    public void onClick(View view) {

        // Write button click
        if(view.getId() == R.id.writeTagButton) {
            // Notify user of ability to write
            Toast toast = Toast.makeText(this.getApplicationContext(), "Write Enabled, Hold tag to write data",
                    Toast.LENGTH_SHORT);
            toast.show();
            enableWriteMode();
        }
    }

    @Override
    public void onNewIntent(Intent intent) {

        // Can we write to tag?
        if(writeModeEnabled) {
            writeModeEnabled = false;

            // write to newly scanned tag
            Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
            Toast toast = null;

            // Fetch stored player data
            playerDetails = this.getSharedPreferences("playerDetails", Context.MODE_PRIVATE);
            Map<String, ?> details = playerDetails.getAll();

            Player player = new Player();

            // No stored player data
            if(details.size() == 0) {
                if(result == 0) {
                    player.setWins((byte) 0);
                    player.setLosses((byte) 0);
                    player.incrementLosses();
                }
                else {
                    player.setWins((byte) 0);
                    player.setLosses((byte) 0);
                    player.incrementWins();
                }
                // Default colours
                player.setPaddleColours((byte) 127, (byte) 127, (byte) 127);
                // Store new player data into private app memory
                SharedPreferences.Editor edit = playerDetails.edit();
                edit.putString("wins", String.valueOf(player.getWins()));
                edit.putString("losses", String.valueOf(player.getLosses()));
                edit.putString("red", String.valueOf(player.getRed()));
                edit.putString("green", String.valueOf(player.getGreen()));
                edit.putString("blue", String.valueOf(player.getBlue()));
                edit.commit();
            }
            else {
                // Get stored player data
                player.setWins(Byte.valueOf(details.get("wins").toString()));
                player.setLosses(Byte.valueOf((details.get("losses").toString())));
                player.setPaddleColours(Byte.valueOf((details.get("red").toString())),
                        Byte.valueOf((details.get("green").toString())),
                        Byte.valueOf((details.get("blue").toString())));
            }

            // TESTING ELEMENT
            if(result == 0)
                player.incrementLosses();
            else
                player.incrementWins();

            if (nfcReadWrite.WriteTag(tag, player))
                toast = Toast.makeText(this.getApplicationContext(), "Tag Write Success", Toast.LENGTH_SHORT);
            else
                toast = Toast.makeText(this.getApplicationContext(), "Tag Write Failed", Toast.LENGTH_SHORT);

            toast.show();
        }
    }

    private void enableWriteMode() {

        writeModeEnabled = true;

        // set up a PendingIntent to open the app when a tag is scanned
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0,
                new Intent(this, getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);
        IntentFilter tagDetected = new IntentFilter(NfcAdapter.ACTION_TAG_DISCOVERED);
        IntentFilter[] filters = new IntentFilter[] { tagDetected };
        // Enable pendingIntent for later use
        nfcAdapter.enableForegroundDispatch(this, pendingIntent, filters, null);
    }


    private void disableWriteMode() {
        // Disable adapter listening
        nfcAdapter.disableForegroundDispatch(this);
    }
}
