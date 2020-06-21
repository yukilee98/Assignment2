package com.example.assignment2;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final int SPEAK_REQUEST = 10;  //must be unique

    TextView text_value;
    Button button_Voice;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        text_value = (TextView) findViewById(R.id.textView1);
        button_Voice = (Button) findViewById(R.id.buttonVoice);

        //Overwrite the onClick in the class
        button_Voice.setOnClickListener(MainActivity.this);

        PackageManager packageManager = this.getPackageManager();   //refers to the package manager
        List<ResolveInfo> informationList = packageManager.queryIntentActivities(new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH), 0);

        //make sure user device does support speech recognition
        if (informationList.size() > 0){
            Toast.makeText(MainActivity.this, "Your device supports speech recognition", Toast.LENGTH_LONG).show();
            speechListening();
        }else{
            Toast.makeText(MainActivity.this, "Your device does not support speech recognition", Toast.LENGTH_LONG).show();
        }


    }

    // exit button
    public void clickExit (View view){
        moveTaskToBack(true);
        android.os.Process.killProcess(android.os.Process.myPid());
        System.exit(1);


    }
    //allow user to use the speech recognition
    private void speechListening() {
        Intent speechIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        speechIntent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Communicate with me");
        speechIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);  // user can talk in any language
        speechIntent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 10);
        startActivityForResult(speechIntent, SPEAK_REQUEST);


    }
    //get data from the speech Intent
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //get the words that the user had said
        if(requestCode == SPEAK_REQUEST && resultCode == RESULT_OK) {

            ArrayList<String> speechWords = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS); // get the words that the user said and then assign the words into the array list
            float[] confidLevels = data.getFloatArrayExtra(RecognizerIntent.EXTRA_CONFIDENCE_SCORES); // get the precise-ness on the words

            //show the results on the screen
            int index = 0;
            for (String userWord : speechWords) {
                //if this is true then the word will show to the screen
                if (confidLevels != null && index < confidLevels.length) {

                    text_value.setText(userWord + " - " + confidLevels[index]);


                }
            }
        }



    }
    @Override
    public void onClick(View view){

        speechListening();
    }
}