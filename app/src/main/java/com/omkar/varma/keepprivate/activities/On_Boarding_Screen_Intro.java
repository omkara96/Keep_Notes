package com.omkar.varma.keepprivate.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.omkar.varma.keepprivate.R;
import com.omkar.varma.keepprivate.onboarding_primary.OnBoardingAdapter;
import com.omkar.varma.keepprivate.onboarding_primary.OnboardingItem;

import java.util.ArrayList;
import java.util.List;

public class On_Boarding_Screen_Intro extends AppCompatActivity {

    private OnBoardingAdapter onBoardingAdapter;
    private LinearLayout layoutOnboardingIndicator;
    private  Button buttonOnboardingAction, skip;

    ViewPager2 onBoardingViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_on__boarding__screen__intro);

        layoutOnboardingIndicator = findViewById(R.id.layoutOnBoardingIndicator);
        buttonOnboardingAction = findViewById(R.id.buttonOnBoardingAction);
        skip =findViewById(R.id.onBoadSkip);
        setupOnBoardingAdapter();


        onBoardingViewPager = findViewById(R.id.onBoardingViewPager);
        onBoardingViewPager.setAdapter(onBoardingAdapter);

        setupOnboardingIndicator();
        setCurrentOnboardingIndicator(0);

        onBoardingViewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                setCurrentOnboardingIndicator(position);
            }
        });

        skip.setEnabled(true);
        skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(On_Boarding_Screen_Intro.this, MainActivity.class));
                   finish();
            }
        });

        buttonOnboardingAction.setEnabled(true);
        buttonOnboardingAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(onBoardingViewPager.getCurrentItem() + 1 < onBoardingAdapter.getItemCount()){
                    onBoardingViewPager.setCurrentItem(onBoardingViewPager.getCurrentItem()+1);
                }else {
                    startActivity(new Intent(On_Boarding_Screen_Intro.this, MainActivity.class));
                    finish();
                }
            }
        });

    }

    public void onBoardingButton(View view){

        Toast.makeText(On_Boarding_Screen_Intro.this, "Starting Intro", Toast.LENGTH_SHORT).show();

        if(onBoardingViewPager.getCurrentItem()  < onBoardingAdapter.getItemCount()){
            onBoardingViewPager.setCurrentItem(onBoardingViewPager.getCurrentItem()+1);
        }else {
            startActivity(new Intent(On_Boarding_Screen_Intro.this, MainActivity.class));
            finish();
        }

    }
    private void setupOnBoardingAdapter(){
        List<OnboardingItem> onboardingItems = new ArrayList<>();

        OnboardingItem screen_1 = new OnboardingItem();
        screen_1.setTitle("Welcome to notes!");
        screen_1.setDescription("- Keep your all notes at one place." +
                "\n-Easily manage your all notes and prioritise them by color code." +
                "\n-Add Images and URL's for better notes.");
        screen_1.setImage(R.drawable.note_on_boarding_screen_1);

        OnboardingItem screen_2 = new OnboardingItem();
        screen_2.setTitle("Add a new Note");
        screen_2.setDescription("Click + Add button to create note. Give title and Subtitle to save note, " +
                "you can edit your note at any time click on save button to save your note ");
        screen_2.setImage(R.drawable.note_on_board_screen_2);

        OnboardingItem screen_3 = new OnboardingItem();
        screen_3.setTitle("Accessing Quick Access Bar!");
        screen_3.setDescription("Quick Access bar allows directly creation of new note with insertion of Image and URL save time with quick access toolbar");
        screen_3.setImage(R.drawable.note_on_board_screen_3);

        OnboardingItem screen_4 = new OnboardingItem();
        screen_4.setTitle("Accessing Extra Features");
        screen_4.setDescription("Click on Miscellaneous button to get extended features." +
                "\nPrioritise your notes by adding colors to them." +
                "\nAdd Images and URL's for ease and powerful notes." +
                "\nalso you can see delete option to delete note if its in edit mode or already created.");
        screen_4.setImage(R.drawable.note_on_board_screen_4);

        OnboardingItem screen_5 = new OnboardingItem();
        screen_5.setTitle("Main Screen");
        screen_5.setDescription("This is main screen you see when app is launched." +
                "here you see your notes if they are saved for first time it is blank." +
                "\nUse search to find your notes quickly and click on note to view it and perform operations." +
                "\nClick on menu icon near search bar to access navigation drawer or swipe right--> to open it.");
        screen_5.setImage(R.drawable.note_on_board_screen_5);


        onboardingItems.add(screen_1);
        onboardingItems.add(screen_2);
        onboardingItems.add(screen_3);
        onboardingItems.add(screen_4);
        onboardingItems.add(screen_5);

        onBoardingAdapter = new OnBoardingAdapter(onboardingItems);

    }
    private  void setupOnboardingIndicator(){
        ImageView [] indicators = new ImageView[onBoardingAdapter.getItemCount()];
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(8,0,8,0);
        for(int i=0;i<indicators.length;i++){
            indicators[i] = new ImageView(getApplicationContext());
            indicators[i].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(),R.drawable.onboarding_indicator_inactive));

            indicators[i].setLayoutParams(layoutParams);
            layoutOnboardingIndicator.addView(indicators[i]);
        }
    }

    private void setCurrentOnboardingIndicator(int index){
        int childCount = layoutOnboardingIndicator.getChildCount();
        for(int i=0;i<childCount;i++){
            ImageView imageView = (ImageView) layoutOnboardingIndicator.getChildAt(i);
            if(i == index){
                imageView.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.onboarding_indicaor_active));
            }else {
                imageView.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.onboarding_indicator_inactive));
            }
        }

        if(index == onBoardingAdapter.getItemCount()-1){
            buttonOnboardingAction.setText("Start");

        }else {
            buttonOnboardingAction.setText("Next");

        }
    }

}