package dong.dms.dong;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

public class RegisterTagActivity extends Activity implements View.OnClickListener {

    SeekBar redBar,
            greenBar,
            blueBar;
    TextView paddleColour,
             playerName;
    Button writeButton;
    int red,
        green,
        blue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        // Inflate layout
        setContentView(R.layout.register_tag_layout);

        // Get view elements
        redBar = (SeekBar) findViewById(R.id.paddleRedSeekBar);
        greenBar = (SeekBar) findViewById(R.id.paddleGreenSeekBar);
        blueBar = (SeekBar) findViewById(R.id.paddleBlueSeekBar);
        paddleColour = (TextView) findViewById(R.id.registerPaddleColour);
        playerName = (TextView) findViewById(R.id.registerNameTextField);
        writeButton = (Button) findViewById(R.id.writeButton);

        // Set listeners
        redBar.setOnSeekBarChangeListener(new ColourSeekBarListener());
        greenBar.setOnSeekBarChangeListener(new ColourSeekBarListener());
        blueBar.setOnSeekBarChangeListener(new ColourSeekBarListener());

        // Get current progress of colour sliders
        red = redBar.getProgress();
        green = greenBar.getProgress();
        blue = blueBar.getProgress();
    }

    @Override
    public void onClick(View view) {

        if(view.getId() == R.id.writeButton) {
            // Create player object
            Player player = new Player((String) playerName.getText());
            player.setPaddleColours(red, green, blue);
            // TODO - NFC WRITE TO TAG
        }
    }

    private class ColourSeekBarListener implements SeekBar.OnSeekBarChangeListener {

        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            switch (seekBar.getId()) {
                case R.id.paddleRedSeekBar :
                    red = progress; break;
                case R.id.paddleGreenSeekBar :
                    green = progress; break;
                case R.id.paddleBlueSeekBar :
                    blue = progress; break;
            }

            paddleColour.setBackgroundColor(Color.rgb(red, green, blue));
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
