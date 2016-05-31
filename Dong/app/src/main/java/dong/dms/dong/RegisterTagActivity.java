package dong.dms.dong;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

public class RegisterTagActivity extends Activity implements View.OnClickListener {

    private NfcAdapter nfcAdapter;
    private NfcWriter nfcReadWrite;
    private boolean writeModeEnabled;

    SeekBar redBar, greenBar, blueBar;
    TextView paddleColour;
    Button writeButton;
    byte red, green, blue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        // Inflate layout
        setContentView(R.layout.register_tag_layout);
        // Set up NFC properties
        nfcAdapter = NfcAdapter.getDefaultAdapter(this);
        nfcReadWrite = new NfcWriter();
        writeModeEnabled = false;

        // Get view elements
        redBar = (SeekBar) findViewById(R.id.paddleRedSeekBar);
        greenBar = (SeekBar) findViewById(R.id.paddleGreenSeekBar);
        blueBar = (SeekBar) findViewById(R.id.paddleBlueSeekBar);
        paddleColour = (TextView) findViewById(R.id.registerPaddleColour);
        writeButton = (Button) findViewById(R.id.writeButton);

        // Set listeners
        redBar.setOnSeekBarChangeListener(new ColourSeekBarListener());
        greenBar.setOnSeekBarChangeListener(new ColourSeekBarListener());
        blueBar.setOnSeekBarChangeListener(new ColourSeekBarListener());
        writeButton.setOnClickListener(this);

        // Get current progress of colour sliders
        red = (byte) redBar.getProgress();
        green = (byte) greenBar.getProgress();
        blue = (byte) blueBar.getProgress();
    }

    @Override
    public void onNewIntent(Intent intent) {

        // Can we write to NFC?
        if(writeModeEnabled) {
            writeModeEnabled = false;

            // Create player object
            Player player = new Player();
            player.setPaddleColours(red, green, blue);

            // write to newly scanned tag
            Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);

            Toast toast = null;

            // Write to tag
            if (nfcReadWrite.WriteTag(tag, player)) {

                // Store scanned player details in private application memory
                SharedPreferences playerDetails = this.getSharedPreferences("playerDetails", Context.MODE_PRIVATE);
                SharedPreferences.Editor edit = playerDetails.edit();
                edit.putInt("wins", player.getWins());
                edit.putInt("losses", player.getLosses());
                edit.putInt("red", player.getRed());
                edit.putInt("green", player.getGreen());
                edit.putInt("blue", player.getBlue());
                edit.commit();

                // Notify user
                toast = Toast.makeText(this.getApplicationContext(), "Tag Write Success", Toast.LENGTH_SHORT);
            }
            else
                // Notify user
                toast = Toast.makeText(this.getApplicationContext(), "Tag Write Failed", Toast.LENGTH_SHORT);

            toast.show();
            // Close activity
            finish();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if(nfcAdapter != null)
            disableWriteMode();
    }

    @Override
    public void onClick(View view) {

        // Write Button click
        if(view.getId() == R.id.writeButton) {
            if (nfcAdapter != null) {
                // Notify user of ability to write
                Toast toast = Toast.makeText(this.getApplicationContext(), "Write Enabled, Hold tag to write data",
                        Toast.LENGTH_SHORT);
                toast.show();
                enableWriteMode();
            }
            else {
                Toast.makeText(this.getApplicationContext(), "Device does not support NFC", Toast.LENGTH_SHORT).show();
            }
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

    private class ColourSeekBarListener implements SeekBar.OnSeekBarChangeListener {

        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            // Update colour variables for seek bar value change
            switch (seekBar.getId()) {
                case R.id.paddleRedSeekBar :
                    red = (byte) progress; break;
                case R.id.paddleGreenSeekBar :
                    green = (byte) progress; break;
                case R.id.paddleBlueSeekBar :
                    blue = (byte) progress; break;
            }

            paddleColour.setBackgroundColor(Color.rgb(red*2, green*2, blue*2));
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {
            // Do nothing
        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
            // Do nothing
        }
    }
}
