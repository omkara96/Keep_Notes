package com.omkar.varma.keepprivate.activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.omkar.varma.keepprivate.R;

public class HelpFeedbackActivity extends AppCompatActivity {

    public static final int REQUEST_CODE_SENT_FEEDBACK = 7;
    public static final int REQUEST_CODE_SENT_PROBLEM = 8;

    EditText problemName, problemDescription, feedBackText;
    Button submitProblem, submitFeedback;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help_feedback);

        problemName = findViewById(R.id.inputProblem);
        problemDescription = findViewById(R.id.inputproblemDesc);
        feedBackText = findViewById(R.id.inputfeedbacktext);

        submitProblem = findViewById(R.id.problemSendProblem);
        submitFeedback = findViewById(R.id.problemSendFeedbackBtn);

        submitProblem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!validateFeilds(problemName) | !validateFeilds(problemDescription)){
                    return;
                }

                String subject = "Note APP Help Problem -> "+ problemName.getText().toString().trim();
                Intent mailIntent = new Intent(Intent.ACTION_SEND);
                mailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{"omkarvarma525@gmail.com"});
                mailIntent.putExtra(Intent.EXTRA_SUBJECT,subject);
                mailIntent.putExtra(Intent.EXTRA_TEXT,problemDescription.getText().toString().trim());
                mailIntent.setType("message/rfc822");
                startActivityForResult(Intent.createChooser(mailIntent,"Choose an Email client :"),REQUEST_CODE_SENT_PROBLEM);
            }
        });

        submitFeedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!validateFeilds(feedBackText)){
                    return;
                }

                String subject = "Note APP Feed Back -->";
                Intent mailIntent = new Intent(Intent.ACTION_SEND);
                mailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{"omkarvarma525@gmail.com"});
                mailIntent.putExtra(Intent.EXTRA_SUBJECT,subject);
                mailIntent.putExtra(Intent.EXTRA_TEXT,feedBackText.getText().toString().trim());
                mailIntent.setType("message/rfc822");
                startActivityForResult(Intent.createChooser(mailIntent,"Choose an Email client :"),REQUEST_CODE_SENT_FEEDBACK);


            }
        });

        findViewById(R.id.imageBackHelpFeedback).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HelpFeedbackActivity.super.onBackPressed();
            }
        });
    }


    public boolean validateFeilds(EditText editText){
        if(editText.getText().toString().trim().isEmpty()){
            editText.setError("Required Field can not be empty !");
            Toast.makeText(this, "Please Enter valid data into field.", Toast.LENGTH_SHORT).show();
            return false;
        }else {
            editText.setError(null);
            return true;
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == REQUEST_CODE_SENT_FEEDBACK){
                feedBackText.setText(null);
                problemDescription.setText(null);
                problemName.setText(null);
                Toast.makeText(this, "Sending Feedback !", Toast.LENGTH_LONG).show();
                super.onBackPressed();
        }else if(requestCode == REQUEST_CODE_SENT_PROBLEM){
            feedBackText.setText(null);
            problemDescription.setText(null);
            problemName.setText(null);
            Toast.makeText(this, "Sending Problem Query !", Toast.LENGTH_LONG).show();
            super.onBackPressed();
        }else {
            Toast.makeText(this, "Something went wrong please try again!", Toast.LENGTH_LONG).show();
        }
    }
}