package com.omkar.varma.keepprivate.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import com.omkar.varma.keepprivate.R;

public class AboutUsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);

        findViewById(R.id.imageBackAboutUs).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AboutUsActivity.super.onBackPressed();
            }
        });

        findViewById(R.id.aboutEmail).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:omkarvarma525@gmail.com")));
            }
        });

        findViewById(R.id.followMeOnInstagram).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.instagram.com/omkarvarma96")));
            }
        });

        findViewById(R.id.followMeOnLinkedIn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.linkedin.com/in/omkara96")));
            }
        });

        findViewById(R.id.followMeOnGithub).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.github.com/omkara96")));
            }
        });
    }
}