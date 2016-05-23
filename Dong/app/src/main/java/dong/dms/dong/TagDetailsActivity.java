package dong.dms.dong;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.os.Parcelable;
import android.os.Bundle;
import android.widget.TextView;

public class TagDetailsActivity extends Activity {

    TextView winsText,
             lossesText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        // Inflate layout
        setContentView(R.layout.tag_details_layout);

        // Get view elements
        winsText = (TextView) findViewById(R.id.detailsWinsCountText);
        lossesText = (TextView) findViewById(R.id.detailsLossesCountText);

        Intent intent = getIntent();
        //Check mime type, get ndef message  from intent and display the message in text view
        if(intent.getType() != null && intent.getType().equals("application/aut.dong")) {
            Parcelable[] rawMsgs = getIntent().getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);
            NdefMessage msg = (NdefMessage) rawMsgs[0];
            NdefRecord record = msg.getRecords()[0];

            // Fetch player data from NFC tag
            Player player = (Player) NfcWriter.deserialize(record.getPayload());

            // Store scanned player details in private application memory
            SharedPreferences playerDetails = this.getSharedPreferences("playerDetails", Context.MODE_PRIVATE);
            SharedPreferences.Editor edit = playerDetails.edit();
            edit.putInt("wins", player.getWins());
            edit.putInt("losses", player.getLosses());
            edit.putInt("red", player.getRed());
            edit.putInt("green", player.getGreen());
            edit.putInt("blue", player.getBlue());
            // Save data
            edit.commit();

            // Set values on text elements
            winsText.setText(Integer.toString(player.getWins()));
            lossesText.setText(Integer.toString(player.getLosses()));
        }
    }
}
