package com.csis.lab02;

import android.app.Activity;
import android.os.Bundle;
import android.widget.SeekBar;

/**
 * Created by conorsouness on 30/01/2017.
 */

public class SliderActivity extends Activity {
    private SeekBar freqSlider;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);


        //THIS SECTION FOR PART 2
        freqSlider = (SeekBar) findViewById(R.id.freqSlider);

        freqSlider.setOnSeekBarChangeListener(
            new SeekBar.OnSeekBarChangeListener()
            {
                float frequency = 0;
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    frequency = progress;
                    //now if you want to send the frequency to PD?....
                    //
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {

                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {

                }

            });

        //END OF SECTION FOR PART 2

        }
    }

