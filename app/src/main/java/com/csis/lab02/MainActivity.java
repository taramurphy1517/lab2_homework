package com.csis.lab02; //package we're in


//android imports
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

//PURE DATA IMPORTS

import org.puredata.android.io.AudioParameters;
import org.puredata.android.io.PdAudio;
import org.puredata.android.utils.PdUiDispatcher;
import org.puredata.core.PdBase;
import org.puredata.core.utils.IoUtils;

import java.io.File;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    private PdUiDispatcher dispatcher; //must declare this to use later, used to receive data from sendEvents

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);//Mandatory
        setContentView(R.layout.activity_main);//Mandatory

        //For declaring and initialising XML items, Always of form OBJECT_TYPE VARIABLE_NAME = (OBJECT_TYPE) findViewById(R.id.ID_SPECIFIED_IN_XML);

        Button send = (Button) findViewById(R.id.sendButton); //findViewById uses the ids you specified in the xml!

        //<---------PLACE DECLARATION AND INITIALISATION OF 2ND BUTTON UNDER HERE------------->


        final TextView freqText = (TextView) findViewById(R.id.sendText);//declared the editText field here pointing to id sendText

        Switch initSynthSwitch = (Switch) findViewById(R.id.onOffSwitch);//declared the switch here pointing to id onOffSwitch


        try { // try the code below, catch errors if things go wrong
            initPD(); //method is below to start PD
            loadPDPatch("synth.pd"); // This is the name of the patch in the zip
        } catch (IOException e) {
            e.printStackTrace(); // print error if init or load patch fails.
            finish(); // end program
        }

        //Check to see if switch value changes
        initSynthSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                float val = (isChecked) ?  1.0f : 0.0f; // value = (get value of isChecked, if true val = 1.0f, if false val = 0.0f)
                sendFloatPD("onOff", val); //send value to patch, receiveEvent names onOff

                //<------Turn on Volume-------------->
                //This will need its own listener if you provide xml widget to control
                sendFloatPD("amp",1.0f);
            }
        });

        //<------BUTTON CLICK LISTENER--------------->
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                sendFloatPD("freq", Float.parseFloat(freqText.getText().toString()));

            }
        });

        //<------PLACE 2ND BUTTON CLICK LISTENER UNDER HERE--------------->

    }

    @Override //If screen is resumed
    protected void onResume(){
        super.onResume();
        PdAudio.startAudio(this);
    }

    @Override//If we switch to other screen
    protected void onPause()
    {
        super.onPause();
        PdAudio.stopAudio();
    }

    //METHOD TO SEND FLOAT TO PUREDATA PATCH
    public void sendFloatPD(String receiver, Float value)//REQUIRES (RECEIVEEVENT NAME, FLOAT VALUE TO SEND)
    {
        PdBase.sendFloat(receiver, value); //send float to receiveEvent
    }

    //METHOD TO SEND BANG TO PUREDATA PATCH
    public void sendBangPD(String receiver)
    {

        PdBase.sendBang(receiver); //send bang to receiveEvent
    }


    //<---THIS METHOD LOADS SPECIFIED PATCH NAME----->
    private void loadPDPatch(String patchName) throws IOException
    {
        File dir = getFilesDir(); //Get current list of files in directory
        try {
            IoUtils.extractZipResource(getResources().openRawResource(R.raw.synth), dir, true); //extract the zip file in raw called synth
            File pdPatch = new File(dir, patchName); //Create file pointer to patch
            PdBase.openPatch(pdPatch.getAbsolutePath()); //open patch
        }catch (IOException e)
        {

        }
    }

    //<---THIS METHOD INITIALISES AUDIO SERVER----->
    private void initPD() throws IOException
    {
        int sampleRate = AudioParameters.suggestSampleRate(); //get sample rate from system
        PdAudio.initAudio(sampleRate,0,2,8,true); //initialise audio engine

        dispatcher = new PdUiDispatcher(); //create UI dispatcher
        PdBase.setReceiver(dispatcher); //set dispatcher to receive items from puredata patches
    }

}
